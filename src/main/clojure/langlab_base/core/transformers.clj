(ns langlab-base.core.transformers
  "Module contains utilities for transforming tokens."
  (:require [ clojure.string :refer (blank? split) ]
            [ langlab-base.core.characters :refer :all]))

(defn merge-tokens-with-space
  "Creates a string from `tokens` seq, by inserting space between them.
   Inverse of `split-tokens-with-space`."
  [ tokens ]
  (apply str (interpose " " tokens)))

(defn split-tokens-with-space 
  "Splits `s` into tokens on whitespace (using regexp \\s+). 
   Inverse of `merge-tokens-with-space`."
  [ s ]
  (split s #"\s+"))

(defn trans-drop-whitespace 
  "From seq `tokens` removes all entries that contain only whitespace."
  [ tokens ]
  (filter #(not (contains-whitespace-only? %))  tokens))

(defn trans-merge-punct 
  "In seq `tokens` merges those groups that contain only punctuation.
   E.g.  [ \"Wow\" \"!\" \"!\" \"!\" ] -> [ \"Wow\" \"!!!\" ].
   Inverse of `trans-split-punct`."
  [ tokens ]
  (let [
        grouped-tokens
          (partition-by contains-punct-only? tokens)
        merge-punct-tokens-f
          #(if (contains-punct-only? (first %))
               [(apply str %)]
               %)
       ]
  (mapcat merge-punct-tokens-f grouped-tokens)))

(defn trans-split-punct [ tokens ]
  (let [
         trans-f 
           (fn [token]
             (if (contains-punct-only? token)
               (map str (seq token))
               [token]))
        ]
    (mapcat trans-f tokens)))

(defn en-trans-drop-articles [ tokens ]
  (let [
        is-not-article? 
          (fn [s] (not (contains? #{ "a" "an" "the" } 
                                 (lower-case s)))) 
        ]
    (filter is-not-article? tokens)))

(defn trans-drop-punct   [ tokens ]
  (filter #(not (is-punct-token? %)) tokens))

(defn trans-lower-case [ tokens ]
  (map lower-case tokens))

(defn trans-drop-punct-lower [ tokens ]
  (map lower-case
   (trans-drop-punct tokens))) )
