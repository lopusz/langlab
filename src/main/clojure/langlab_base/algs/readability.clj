(ns langlab-base.algs.readability
  "Module includes utilities and functions computing readability indices."
  (:require 
     [ langlab-base.cmns.should :refer (should) ]
     [ langlab-base.core.characters 
         :refer (count-latin-vowel-groups-without-final) ]
     [ langlab-base.core.transformers 
         :refer (trans-drop-punct trans-drop-whitespace) ]))

(defn count-words
  "Counts the numer of words in `s` based on the provided `env`. 
   The `env` supports keys:
   - `:split-tokens-f` (mandatory) 
   - `:trans-drop-punct-f` (defaulted to `trans-drop-punct`)"

  [ ^String s env ]

  ;; Prerequisites
  (should (every? 
            #(contains? env %)
            [ :split-tokens-f ]))

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
  "Counts the numer of sentences in `s` based on the provided `env`. 
   The `env` supports key `:split-sentences-f` (mandatory)."

  [ ^String s env ]

  ;; Prerequisites
  (should (every? 
            #(contains? env %)
            [ :split-sentences-f ]))

  (let [
        { :keys (split-sentences-f) } env
        ]
  (-> s
      split-sentences-f
      trans-drop-whitespace
      count)))

(defn count-letters-in-tokens [ tokens ]
  (reduce +
    (map #(. % length) tokens)))
  

(defn calc-text-stats 
  "Calculates statistics of text `s`. 
   The `env` supports the following keys:
   - `:split-tokens-f`  (mandatory)
   - `:trans-drop-punct-f` (defaulted to `trans-drop-punct`
   - `:is-hard-word-f` (defaulted to count-latin-vowel-groups-without-final>2)
   - `:split-sentences-f` (mandatory)

   Result contains a map with  the following fields:
   :n-words (number of words)
   :n-letters  (total number of letters in words)
   :n-hard-words  (number of hard words according to is-hard-word-f)
   :n-sentences (number of sentences)"

  [ ^String s env ]

  ;; Prerequisites
  (should (every? 
            #(contains? env %)
            [ :split-tokens-f 
              :split-sentences-f ]))
  
  (let [
        env-default 
          { :trans-drop-punct-f  trans-drop-punct 
            :is-hard-word-f 
              #(< 2 (count-latin-vowel-groups-without-final %)) }
        env*
          (merge env-default env)

        { :keys [ split-tokens-f
                  trans-drop-punct-f 
                  is-hard-word-f
                  split-sentences-f ] } 
          env*

        n-sentences 
          (count-sentences s env)

        words 
          (-> s
            split-tokens-f
            trans-drop-punct-f)
          
        n-words
          (count words)

        n-letters
          (count-letters-in-tokens words)
 
        hard-words  
          (filter is-hard-word-f words)
        
        n-hard-words
          (count hard-words)
        ]
    { :n-letters n-letters,
      :n-words n-words, :n-hard-words n-hard-words,
      :n-sentences n-sentences } ))

(defn calc-gunning-fog-index 

  "Calculates Gunning Fog Readability Index based on the given `stats`.
   Stats should include fields `:n-words`, `:n-hard-words`, 
   `:n-sentences`."

  [ stats ]

  ;; Prerequisites
  (should (every? 
            #(contains? stats %)
            [ :n-words :n-hard-words :n-sentences ]))
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
   `:n-sentences`, `n-letters`."

  [ stats ]

  ;; Prerequisites
  (should (every? 
            #(contains? stats %)
            [ :n-words :n-hard-words :n-sentences :n-letters]))

  (let [
         { :keys [ n-letters n-words n-hard-words n-sentences ] } stats
        ]
     (+
       -15.8 
       (*
         5.88
         (float (/ n-letters n-words)))
       (*
         -29.59
         (float (/ n-sentences n-words))))))

(defn calc-automated-readability-index 
  "Calculates Automated Readability Index based on the given `stats`.
   Stats should include fields `:n-words`, `:n-hard-words`, 
   `:n-sentences`, `n-letters`."

  [ stats ]

  ;; Prerequisites
  (should (every? 
            #(contains? stats %)
            [ :n-words :n-hard-words :n-sentences :n-letters]))

  (let [
         { :keys [ n-letters n-words n-hard-words n-sentences ] } stats
       ]
    (+
      -21.43 
       (*
         4.71
         (float (/ n-letters n-words)))
       (*
         0.50
         (float (/ n-words n-sentences))))))
