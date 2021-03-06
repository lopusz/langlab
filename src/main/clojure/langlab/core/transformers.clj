(ns langlab.core.transformers
  "Module contains utilities for transforming tokens."
  (:require [ clojure.string :refer (blank? split lower-case join) ]
            [ langlab.core.characters
               :refer (contains-whitespace-only? contains-punct-only?
                       contains-letters-or-digits-only?)
              ]))

(defn merge-tokens-with-space
  "Creates a string from `tokens` seq, by inserting space between them."
  [ tokens ]
  (join " " tokens))

(defn trans-drop-whitespace
  "From seq `tokens` removes all entries that contain only whitespace."
  [ tokens ]
  (filter #(not (contains-whitespace-only? %))  tokens))

(defn trans-merge-punct
  "In seq `tokens` merges those groups that contain only punctuation.

   `(trans-merge-punct [ \"Wow\" \"!\" \"!\" \"!\" ])`

   `[ \"Wow\" \"!!!\" ]`

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

(defn trans-split-punct
  "Split all punctuation tokens from `tokens` into separate characters.

   `(trans-split-punct [ \"Wow\" \"!!!\" ])`

   `[ \"Wow\" \"!\" \"!\" \"!\" ]`

   Inverse of `trans-split-punct`."
  [ tokens ]
  (let [
         trans-f
           (fn [token]
             (if (contains-punct-only? token)
               (map str (seq token))
               [token]))
        ]
    (mapcat trans-f tokens)))

(defn trans-drop-punct
  "Drops all items from `tokens` that contains only punctuation tokens."
  [ tokens ]
  (filter #(not (contains-punct-only? %)) tokens))

(defn trans-keep-letters-or-digits
  "Drops all items from `tokens` that contain other characters
   than letters or digits."
  [ tokens ]
  (filter #(contains-letters-or-digits-only? %) tokens))

(defn trans-lower-case
  "Lowercases all `tokens`."
  [ tokens ]
  (map lower-case tokens))

(defn trans-drop-punct-lower
  "Drops all punctuation tokens and lowercases all `tokens`."
  [ tokens ]
  (map lower-case
   (trans-drop-punct tokens)))

(defn trans-drop-set
  "Drop all elements of `tokens` that are included in the `drop-set`.
   To generate `drop-set` one of the functions returning stopwords or
   articles from module core.stopwords can be used."
  [ drop-set tokens ]
  (filter #(not (contains? drop-set %)) tokens))

(defn trans-drop-set-all-case
  "Drop all elements of `tokens` that are included in the `drop-set`.
   Ignore case.
   To generate `drop-set` one of the functions returning stopwords or
   articles from module `core.stopwords` can be used."
  [ drop-set tokens ]
  (filter #(not (contains? drop-set (lower-case %))) tokens))
