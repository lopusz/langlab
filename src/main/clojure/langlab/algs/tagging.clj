(ns langlab.algs.tagging
  "Module contains functionality related to tagging with dictionary tags.
   The implemented functionality can be divided into three main areas:

   - generating dictionaries and frequency dictionaries,
   - tagging itself,
   - tags algebra (union, intersection, difference, etc.).
  "
  (:refer-clojure :exclude [assert])
  (:require
     [ pjstadig.assertions :refer [assert]]
     [ langlab.core.parsers :refer [split-tokens-with-whitespace] ]
     [ langlab.core.transformers :refer [ merge-tokens-with-space ] ]
     [ langlab.core.ngrams :refer [gen-ngrams]])
  (import [ java.io Reader]))

; READING TAGS DICTIONARY FUNCTIONS

(defn- ensure-norm-f-defaults
  "Add default keys to the `env` parameter used by normalization function."
  [ env ]
  (merge { :trans-tokens-f identity
           :merge-tokens-f merge-tokens-with-space }
         env))

(defn- make-norm-f
  "Tag normalization function used internally by all tags algebra
   function (intersection/union/difference)."
  [ env ]
  (let [
         { :keys [ split-tokens-f stem-f trans-tokens-f merge-tokens-f] }
           env
      ]
    (fn [ s ]
      (->> s
        split-tokens-f
        trans-tokens-f
        (map stem-f)
        merge-tokens-f))))

(defn- make-norm-nostem-f
  "Tag normalization function used internally by all tags algebra
   function (intersection/union/difference)."
  [ env ]
  (let [
         { :keys [split-tokens-f trans-tokens-f merge-tokens-f] }
           env
      ]
    (fn [ s ]
      (->> s
        split-tokens-f
        trans-tokens-f
        merge-tokens-f))))

(defn- gen-dict-entry
  "On the basis of string `s`, generates one-element map
  `{ normalized-s normalized-entry-without-stemming }`."
  [s env]
  (let [
         norm-f (make-norm-f env)
         norm-nostem-f (make-norm-nostem-f env)
       ]
    (hash-map (norm-f s) (norm-nostem-f s))))

(defn gen-dict-from-seq
  "Generates dictionary from a given `seq`. The result is a map of the form
     {  normalized-entry normalized-entry-without-stemming }.
   Normalization function splits tokens, transforms them, stems,
   and finally merges. It is constructed according to the keys in `env`.

   The following keywords mapping to functions can be included in `env`:

   - `:split-tokens-f` - tokenizer function (mandatory),
   - `:stem-f` - stemming function (mandatory),
   - `:trans-tokens-f` - transforming function (default: identity),
   - `:merge-tokens-f` - merging tokens (default: merge-tokens-with-space)."

  [seq env]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (let [
        env*
          (ensure-norm-f-defaults env)

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
        norm-f (make-norm-f env)
        norm-nostem-f (make-norm-nostem-f env)
        ]
    (hash-map (norm-f s) { (norm-nostem-f s) 1 })))

(defn gen-fdict-from-seq
  "Generate frequency dictionary (`fdict`) from `seq`.
   The fdict is useful when there are many tokens that stem to the same entry
   in a given `seq`.
   The resulting fdict has the form:

     `{ normalized-entry { entry1 freq1 entry2 freq2 }, ...}.`

   All `entry1`, `entry2`, ... were normalized without stemming. When
   applying stemming they normalize to the same `normalized-entry`.
   The normalization function is constructed according to the keys in `env`.

   The following keywords mapping to functions can be included in `env`:

   - `:split-tokens-f` - tokenizer function (mandatory),
   - `:stem-f` - stemming function (mandatory),
   - `:trans-tokens-f` - transforming function (default: identity),
   - `:merge-tokens-f` - merging tokens (default: merge-tokens-with-space)."

  [ seq env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))
  (let [
        env* (ensure-norm-f-defaults env)
        merge-hists-f
          #(merge-with + %1 %2)
        fdict-entries-seq
          (map #(gen-fdict-entry % env*) seq)
       ]
    (reduce
      #(merge-with merge-hists-f  %1 %2)
      {}
      fdict-entries-seq)))

(defn gen-dict-from-reader
  "Generates dictionary from a given reader `r`, passing `env` to
   the `gen-dict-from-seq`."
  [ ^Reader r env ]
    (with-open [ r* r ]
      (gen-dict-from-seq (line-seq r*) env)))

(defn gen-fdict-from-reader
  "Generates frequency dictionary from a given reader `r`. passing `env` to
   the `gen-fdict-from-seq`."
  [ ^Reader r env ]
    (with-open [ r* r ]
      (gen-fdict-from-seq (line-seq r*) env)))

(defn- select-most-freq-shortest-entry [ m ]
  (let [
          m-keys (keys m)
          m-vals (vals m)
          max-len
            (apply max
              (map #(.length ^String %) m-keys))
          sort-f
            #(- (* (m %) (inc max-len)) (.length ^String %))
        ]
  (apply max-key sort-f m-keys)))

(defn conv-fdict-to-dict
  "Converts frequency dictionary `fdict` of the form

     `{ normalized-entry { entry1 freq1 entry2 freq2 }, ... },`

   to an ordinary dictionary by selecting most frequent `entry1`, `entry2`, ...
   From the entries having the same frequencies the shortest is selected."
  [ fdict ]
  (zipmap
    (keys fdict)
    (map select-most-freq-shortest-entry (vals fdict))))

(defn- get-min-tokens-in-dict
  "Calculates the smallest number of tokens contained in all keys
   of given `dict`."
  [ dict & { :keys [ split-tokens-f ]
             :or { split-tokens-f split-tokens-with-whitespace} } ]

  (if (empty? dict)
    nil
    (->> dict
         (keys)
         (map (comp count split-tokens-f))
         (apply min))))

(defn- get-max-tokens-in-dict
  "Calculates the smallest number of tokens contained in all keys
   of given `dict`. The optional key is :split-tokens-f"
  [ dict  & { :keys [ split-tokens-f ]
              :or { split-tokens-f split-tokens-with-whitespace} } ]

  (if (empty? dict)
    nil
    (->> dict
         (keys)
         (map (comp count split-tokens-f))
         (apply max))))

; TAGGING FUNCTIONS

(defn- make-tag-single-str-f
  "Creates a function that tags single string with dictionary `dict`
   based on functions contained in the `env`."
  [ dict env ]
  (let [
        env-default
           { :merge-tokens-f merge-tokens-with-space
             :trans-tokens-f identity }
        env*
           (merge env-default env)
        nmin (get-min-tokens-in-dict dict)
        nmax (get-max-tokens-in-dict dict)
        gen-all-ngrams #(gen-ngrams nmin nmax %)
        { :keys [ stem-f split-tokens-f trans-tokens-f merge-tokens-f ] }
           env*
       ]
    (fn [s]
      (->> s
           split-tokens-f
           trans-tokens-f
           (map stem-f)
           gen-all-ngrams
           (map merge-tokens-f)
           (filter #(contains? dict %))
           (map #(dict %))
           frequencies))))

(defn make-tag-f
  "Creates a function that tags string with dictionary `dict` based on
   functions contained in the `env`. The result is a map `{ tag freq }`.

   The following keywords mapping to functions can be included in `env`:

   - `:split-tokens-f` - tokenizer function (mandatory),
   - `:stem-f` - stemming function (mandatory),
   - `:trans-tokens-f` - transforming function (default: identity),
   - `:merge-tokens-f` - merging tokens (default: merge-tokens-with-space),
   - `:split-sentences-f` - parses string into sentences (optional).

    If `:split-sentences-f` is given, sentences are parsed separately and tag
    maps from all sentences are merged."
  [ dict env ]
  (assert (map? dict))
  (assert (contains? env :stem-f))
  (assert (contains? env :split-tokens-f))

  (if (contains? env :split-sentences-f)
    (let [
            split-sentences-f
              (:split-sentences-f env)
            tag-single-str-f
              (make-tag-single-str-f dict env)
          ]
      (fn [s]
         (let [
                sentences (split-sentences-f s)
                sentences-tags (map tag-single-str-f sentences)
               ]
           (reduce (partial merge-with +) {} sentences-tags))))
    (make-tag-single-str-f dict env)))

; TAGS ALGEBRA FUNCTIONS (UNION/DIFFERENCE/INTERSECTION)

(defn calc-tags-intersection
  "Returns all the `{ tag freq }` pairs from `tags1` map, where
   normalized `tag` is among normalized tags from `tags2`.
   Normalization function splits tokens, transforms them, stems
   and finally merges. The normalization function is constructed
   according to the keys in `env`.

   The following keywords mapping to functions can be included in `env`:

   - `:split-tokens-f` - tokenizer function (mandatory),
   - `:stem-f` - stemming function (mandatory),
   - `:trans-tokens-f` - transforming function (default: identity),
   - `:merge-tokens-f` - merging tokens (default: merge-tokens-with-space)."
  [ tags1 tags2 env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))

  (let [
        norm-f (make-norm-f (ensure-norm-f-defaults env))
        tags2-norm-keys (into #{} (map norm-f (keys tags2)))
        is-in-tags2? #(contains? tags2-norm-keys (norm-f %))
        common-keys (filter is-in-tags2? (keys tags1))
        ]
    (select-keys tags1 common-keys)))

(defn calc-tags-union
  "Returns all the `{ tag freq }` pairs from `tags1` and those
   pairs from `tags2` where normalized tag is not included already from `tags1`.
   Normalization function splits tokens, transforms them, stems
   and finally merges. It is constructed according to the keys in `env`.

   The following keywords mapping to functions can be included in `env`:

   - `:split-tokens-f` - tokenizer function (mandatory),
   - `:stem-f` - stemming function (mandatory),
   - `:trans-tokens-f` - transforming function (default: identity),
   - `:merge-tokens-f` - merging tokens (default: merge-tokens-with-space)."

  [ tags1 tags2 env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))

  (let [
        norm-f (make-norm-f (ensure-norm-f-defaults env))
        tags1-norm-keys (into #{} (map norm-f (keys tags1)))
        is-not-in-tags1? #(not (contains? tags1-norm-keys (norm-f %)))
        tags2-key-no-duplicates
          (filter is-not-in-tags1? (keys tags2))
        tags2-no-duplicates
          (select-keys tags2 tags2-key-no-duplicates)
       ]
    (merge tags1 tags2-no-duplicates)))

(defn calc-tags-difference
  "Returns all the `{ tag freq }` pairs from `tags1` map, where
   normalized `tag` is not among normalized tags from `tags2`.
   Normalization function splits tokens, transforms them, stems
   and finally merges. It is constructed according to the keys in `env`.

   The following keywords mapping to functions can be included in `env`:

   - `:split-tokens-f` - tokenizer function (mandatory),
   - `:stem-f` - stemming function (mandatory),
   - `:trans-tokens-f` - transforming function (default: identity),
   - `:merge-tokens-f` - merging tokens (default: merge-tokens-with-space)."

  [ tags1 tags2 env ]
  (assert (contains? env :split-tokens-f))
  (assert (contains? env :stem-f))

  (let [
        norm-f (make-norm-f (ensure-norm-f-defaults env))
        tags2-norm-keys (into #{} (map norm-f (keys tags2)))
        is-not-in-tags2? #(not (contains? tags2-norm-keys (norm-f %)))
        difference-keys  (filter is-not-in-tags2? (keys tags1))
       ]
    (zipmap
       difference-keys
       (map #(tags1 %) difference-keys))))
