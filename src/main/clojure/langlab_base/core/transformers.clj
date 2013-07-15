(ns langlab-base.core.transformers
  "Module contains utilities for transforming tokens."
  (:require [ clojure.string :refer (blank?) ]))

(defn trans-tokens-drop-whitespace [ tokens ]
  (filter #(not (blank? %))  tokens))

(comment
     (defn merge-tokens-with-space [ tokens ]
  (apply str (interpose " " tokens)))


(defn en-trans-tokens-drop-articles [ tokens ]
  (let [
        is-not-article? 
          (fn [s] (not (contains? #{ "a" "an" "the" } 
                                 (lower-case s)))) 
        ]
    (filter is-not-article? tokens)))

(defn trans-tokens-drop-punct   [ tokens ]
  (filter #(not (is-punct-token? %)) tokens))

(defn trans-tokens-lower [ tokens ]
  (map lower-case tokens))

(defn trans-tokens-drop-punct-lower [ tokens ]
  (map lower-case
   (trans-tokens-drop-punct tokens)))

(defn trans-tokens-merge-punct [ tokens ]
  (let [
        grouped-tokens
          (partition-by is-punct-token? tokens)
        merge-punct-tokens-f
          #(if (is-punct-token? (first %))
               [(apply str %)]
               %)
       ]
  (mapcat merge-punct-tokens-f grouped-tokens)))

(defn trans-tokens-split-punct [ tokens ]
  (let [
         trans-f 
           (fn [token]
             (if (is-punct-token? token)
               (map str (seq token))
               [token]))
        ]
    (mapcat trans-f tokens))))
