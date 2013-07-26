(ns langlab.algs.readability
  "Module contains functions for computing readability indices."
  (:require
     [ langlab.cmns.must :refer (must) ]
     [ langlab.core.characters
         :refer (count-latin-vowel-groups-without-final en-count-chars-bi) ]
     [ langlab.core.transformers
         :refer (trans-drop-punct trans-drop-whitespace) ]))

(defn count-words
  "Counts the number of words in `s` based on the provided `env`.
   The `env` supports keys:
   - `:split-tokens-f` (mandatory)
   - `:trans-drop-punct-f` (defaults to `trans-drop-punct`)"

  [ ^String s env ]

  ;; Prerequisites
  (must  (contains? env :split-tokens-f))

  (let [
        env-default
          { :trans-drop-punct-f trans-drop-punct }

        env*
          (merge env-default env)

        { :keys (trans-drop-punct-f split-tokens-f) }
          env*
        ]
    (-> s
        split-tokens-f
        trans-drop-punct-f
        count)))

(defn count-sentences
  "Counts the number of sentences in `s` based on the provided `env`.
   The `env` supports key `:split-sentences-f` (mandatory)."

  [ ^String s env ]

  ;; Prerequisites
  (must (contains? env :split-sentences-f))

  (let [
        { :keys (split-sentences-f) } env
        ]
  (-> s
      split-sentences-f
      trans-drop-whitespace
      count)))

(defn calc-text-stats
  "Calculates statistics of text `s`.
   The `env` supports the following keys:
   - `:count-chars-f` (defaults to `en-count-chars-bi`)
   - `:is-hard-word-f` (defaults to count-latin-vowel-groups-without-final>2)
   - `:split-tokens-f`  (mandatory)
   - `:split-sentences-f` (mandatory)
   - `:trans-drop-punct-f` (defaults to `trans-drop-punct`)

   Result contains a map with  the following fields:
   :n-chars  (total number of letters in words)
   :n-words (number of words)
   :n-hard-words  (number of hard words according to is-hard-word-f)
   :n-sentences (number of sentences)"

  [ ^String s env ]

  ;; Prerequisites
  (must (contains? env :split-sentences-f))
  (must (contains? env :split-tokens-f))

  (let [
        env-default
          { :trans-drop-punct-f  trans-drop-punct
            :count-chars-f en-count-chars-bi
            :is-hard-word-f
              #(< 2 (count-latin-vowel-groups-without-final %)) }
        env*
          (merge env-default env)

        { :keys [ count-chars-f
                  is-hard-word-f
                  split-sentences-f
                  split-tokens-f
                  trans-drop-punct-f ] }
          env*

        n-sentences
          (count-sentences s env)

        words
          (-> s
            split-tokens-f
            trans-drop-punct-f)

        n-words
          (count words)

        n-chars
          (reduce +
            (map count-chars-f words))

        hard-words
          (filter is-hard-word-f words)

        n-hard-words
          (count hard-words)
        ]
    { :n-chars n-chars, :n-words n-words,
      :n-hard-words n-hard-words, :n-sentences n-sentences } ))

(defn calc-gunning-fog-index

  "Calculates Gunning Fog Readability Index based on the given `stats`.
   Stats should include fields `:n-words`, `:n-hard-words`,
   `:n-sentences`."

  [ stats ]

  ;; Prerequisites
  (must (contains? stats :n-hard-words))
  (must (contains? stats :n-sentences))
  (must (contains? stats :n-words))

  (let [
         { :keys [ n-words n-hard-words n-sentences ] } stats
        ]
    (float
      (* 0.4
        (+
          (/ n-words n-sentences)
          (* 100.0
            (/ n-hard-words n-words)))))))

(defn calc-coleman-liau-index
  "Calculates Coleman-Liau Readability Index based on the given `stats`.
   Stats should include fields `:n-words`, `:n-hard-words`,
   `:n-sentences`, `n-chars`."

  [ stats ]

  ;; Prerequisites
  (must (contains? stats :n-chars))
  (must (contains? stats :n-hard-words))
  (must (contains? stats :n-sentences))
  (must (contains? stats :n-words))

  (let [
         { :keys [ n-chars n-words n-hard-words n-sentences ] } stats
        ]
     (+
       -15.8
       (*
         5.88
         (float (/ n-chars n-words)))
       (*
         -29.59
         (float (/ n-sentences n-words))))))

(defn calc-automated-readability-index
  "Calculates Automated Readability Index based on the given `stats`.
   Stats should include fields `:n-words`, `:n-hard-words`,
   `:n-sentences`, `n-chars`."

  [ stats ]

  ;; Prerequisites
  (must (contains? stats :n-chars))
  (must (contains? stats :n-hard-words))
  (must (contains? stats :n-sentences))
  (must (contains? stats :n-words))

  (let [
         { :keys [ n-chars n-words n-hard-words n-sentences ] } stats
       ]
    (+
      -21.43
       (*
         4.71
         (float (/ n-chars n-words)))
       (*
         0.50
         (float (/ n-words n-sentences))))))
