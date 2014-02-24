(ns langlab.algs.tagging
  "Module contains functionality related to tagging with dictionary tags.
   The implemented functionality can be divided into three main areas:
   * generating dictionaries and frequency dictionaries,
   * tagging itself,
   * tagg algebra (union/intersection/difference) etc.
  "
  (:refer-clojure :exclude [assert])
  (:require
     [ pjstadig.assertions :refer [assert]]
     [ langlab.core.transformers
       :refer [merge-tokens-with-space split-tokens-with-space] ]
     [ langlab.core.ngrams :refer [gen-ngrams]]
     [ langlab.core.parsers :refer [split-sentences-nosplit]]))

(defn- gen-dict-entry [s env]
  (let [
        { :keys
         [split-tokens-f transform-tokens-f stem-f merge-tokens-f] }
          env
        norm-f
         #(->> %
            split-tokens-f
            transform-tokens-f
            (map stem-f)
            merge-tokens-f)
        ]
  (hash-map (norm-f s) s)))

(defn gen-dict-from-seq [seq env]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (let [
        env-default
          { :transform-tokens-f identity
            :merge-tokens-f merge-tokens-with-space }
        env*
          (merge env-default env)

        dict-entries-seq
          (map
            #(gen-dict-entry % env*)
            seq)
        ]
  (reduce
     merge
     {}
     dict-entries-seq)))

(defn- gen-fdict-entry [s env]
  (let [
        { :keys
         [split-tokens-f transform-tokens-f stem-f merge-tokens-f] }
          env
        norm-f
         #(->> %
            split-tokens-f
            transform-tokens-f
            (map stem-f)
            merge-tokens-f)
        norm-f-nostem
          #(->> %
            split-tokens-f
            transform-tokens-f
            merge-tokens-f)
        ]
    (hash-map (norm-f s) { (norm-f-nostem s) 1})))

(defn gen-fdict-from-seq [ seq env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (let [
        env-default
          { :transform-tokens-f identity
            :merge-tokens-f merge-tokens-with-space }
        env*
          (merge env-default env)
        merge-hists-f
          #(merge-with + %1 %2)
        fdict-entries-seq
          (map #(gen-fdict-entry % env*) seq)
       ]
    (reduce
      #(merge-with merge-hists-f  %1 %2)
      {}
      fdict-entries-seq)))

(defn get-min-tokens-in-dict
  [ dict & { :keys [ split-tokens-f ]
             :or { split-tokens-f split-tokens-with-space} } ]

  (if (empty? dict)
    nil
    (->> dict
         (keys)
         (map (comp count split-tokens-f))
         (apply min))))

(defn get-max-tokens-in-dict
  [ dict  & { :keys [ split-tokens-f ]
              :or { split-tokens-f split-tokens-with-space} } ]

  (if (empty? dict)
    nil
    (->> dict
         (keys)
         (map (comp count split-tokens-f))
         (apply max))))

(comment
  ;; Decouple from reader types
  (defn gen-dict-from-file
    [ fname gen-dict-env ]
    (with-open [ r (make-gzip-or-normal-reader fname)]
      (gen-dict-from-seq (line-seq r) gen-dict-env))))

(defn tag-str [ s env ]
  (assert (contains? env :stem-f))
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :dict))

  (let [
        env-default
          { :merge-tokens-f merge-tokens-with-space
            :trans-tokens-f identity }
        env* (merge env-default env)
        { :keys [ stem-f split-tokens-f trans-tokens-f merge-tokens-f dict ] }
          env*
        nmin
          (if (contains? env :dict-min-tokens)
            (env :dict-min-tokens)
            (get-min-tokens-in-dict dict))
        nmax
          (if (contains? env :dict-min-tokens)
            (env :dict-max-tokens)
            (get-max-tokens-in-dict dict))
        gen-all-ngrams #(gen-ngrams nmin nmax %)
        ]
        (->> s
             (split-tokens-f)
             (trans-tokens-f)
             (map stem-f)
             (gen-all-ngrams)
             (map merge-tokens-f)
             (filter #(contains? dict %))
             (map #(dict %))
             (frequencies))))

(defn tag-str-with-sentences [ s env ]
  (let [
        env-default
          { :split-sentences-f split-sentences-nosplit }
        env*
          (merge env-default env)
        split-sentences-f (:split-sentences-f env*)
        tag
          #(tag-str % env)
        sentence-tags
          (->> s
               split-sentences-f
               (map tag))
        ]
    (reduce (partial merge-with +) {} sentence-tags)))

(defn- make-norm-f [ env ]
  (let [
      { :keys [ split-tokens-f stem-f trans-tokens-f merge-tokens-f] }
       env
      ]
    (fn [ s ]
      (->> s
        (split-tokens-f)
        (trans-tokens-f)
        (map stem-f)
        (merge-tokens-f)))))

(defn calc-tags-intersection [ tags1 tags2 env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (assert (contains? env :trans-tokens-f))
  (assert (contains? env :merge-tokens-f))
  (let [
        norm-f (make-norm-f env)
        tags2-norm-keys (into #{} (map norm-f (keys tags2)))
        is-in-tags2? #(contains? tags2-norm-keys (norm-f %))
        common-keys (filter is-in-tags2? (keys tags1))
        ]
    (select-keys tags1 common-keys)))

(defn calc-tags-union [ tags1 tags2 env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (assert (contains? env :trans-tokens-f))
  (assert (contains? env :merge-tokens-f))
  (let [
        norm-f (make-norm-f env)
        tags1-norm-keys (into #{} (map norm-f (keys tags1)))
        is-not-in-tags1? #(not (contains? tags1-norm-keys (norm-f %)))
        tags2-key-no-duplicates
          (filter is-not-in-tags1? (keys tags2))
        tags2-no-duplicates
          (select-keys tags2 tags2-key-no-duplicates)
       ]
    (merge tags1 tags2-no-duplicates)))

(defn calc-tags-difference [ tags1 tags2 env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (assert (contains? env :trans-tokens-f))
  (assert (contains? env :merge-tokens-f))
  (let [
        norm-f (make-norm-f env)
        tags2-norm-keys (into #{} (map norm-f (keys tags2)))
        is-not-in-tags2? #(not (contains? tags2-norm-keys (norm-f %)))
        difference-keys  (filter is-not-in-tags2? (keys tags1))
       ]
    (zipmap
       difference-keys
       (map #(tags1 %) difference-keys))))
