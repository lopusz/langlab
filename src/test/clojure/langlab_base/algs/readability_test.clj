(ns langlab-base.algs.readability-test
  (:require
    [ clojure.test :refer :all ]
    [ langlab-base.cmns.tests :refer (is-eq-dict float-eq) ]
    [ langlab-base.core.parsers
        :refer (en-split-tokens-bi en-split-sentences-bi) ]
    [ langlab-base.algs.readability :refer :all ]))

(def ^:private count-words-test-data
  {
     "John, you should try..." 4
     (str "Meaningless! Meaningless! Utterly meaningless! "
     "Everything is meaningless... "
     "What do people gain from all their labors?") 15
     "" 0 })

(deftest count-words-test
  (is-eq-dict
     #(count-words % {:split-tokens-f en-split-tokens-bi})
     count-words-test-data))

(def ^:private count-sentences-test-data
  {
     (str "Meaningless! Meaningless! Utterly meaningless! "
     "Everything is meaningless... "
     "What do people gain from all their labors?") 5
     ""  0
     " " 0 })

(deftest count-sentences-test
  (is-eq-dict
     #(count-sentences % {:split-sentences-f en-split-sentences-bi})
     count-sentences-test-data))

(def ^:private calc-text-stats-test-data
  {
     (str
        "Meaningless! Meaningless! Utterly meaningless! "
        "Everything is meaningless... "
        "What do people gain from all their difficult labors?")
        { :n-chars 106 :n-words 16 :n-hard-words 6 :n-sentences 5} })

(deftest calc-stats-test
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        ]
    (is-eq-dict
      #(calc-text-stats % env)
      calc-text-stats-test-data)))

(def ^:private calc-indicies-test-data
  [ (str
        "Meaningless! Meaningless! Utterly meaningless! "
        "Everything is meaningless... "
        "What do people gain from all their difficult labors?")
     (str
    "Existing computer programs that measure readability are based largely "
    "upon subroutines which estimate number of syllables, usually by counting "
    "vowels. The shortcoming in estimating syllables is that it necessitates "
    "keypunching the prose into the computer. There is no need to estimate "
    "syllables since word length in letters is a better predictor of "
    "readability than word length in syllables. Therefore, a new readability "
    "formula was computed that has for its predictors letters per 100 words "
    "and sentences per 100 words. Both predictors can be counted by an optical "
    "scanning device, and thus the formula makes it economically feasible for "
    "an organization such as the US Office of Education to calibrate the "
    "readability of all textbooks for the public school system.")])

(deftest calc-gunning-fog-index-test
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        data
          (zipmap calc-indicies-test-data  [ 16.28 19.26 ])
        ]
    (is-eq-dict
      #(calc-gunning-fog-index (calc-text-stats % env))
         data
         (partial float-eq 0.01))))

(deftest calc-coleman-liau-index-test
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        data  (zipmap calc-indicies-test-data  [ 13.90 14.53 ])
        ]
    (is-eq-dict
      #(calc-coleman-liau-index (calc-text-stats % env))
        data
        (partial float-eq 0.01))))

(deftest calc-automated-readability-index-test
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        data
          (zipmap (drop 1 calc-indicies-test-data)  (drop 1 [ 11.37 15.76 ]))
        ]
    (is-eq-dict
      #(calc-automated-readability-index (calc-text-stats % env))
        data
        (partial float-eq 0.01))))
