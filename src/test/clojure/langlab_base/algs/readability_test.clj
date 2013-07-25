(ns langlab-base.algs.readability-test
  (:require
    [ clojure.test :refer :all ]
    [ langlab-base.cmns.tests :refer (is-eq-dict float-eq) ] 
    [ langlab-base.core.parsers 
        :refer (en-split-tokens-bi en-split-sentences-bi) ]
    [ langlab-base.algs.readability :refer :all ]))

(def ^:private  test-data-count-words 
  { 
     "John, you should try..." 4
     (str "Meaningless! Meaningless! Utterly meaningless! " 
     "Everything is meaningless... "
     "What do people gain from all their labors?") 15
     "" 0 })

(def test-count-words
  (is-eq-dict 
     #(count-words % {:split-tokens-f en-split-tokens-bi})
     test-data-count-words))
 
(def ^:private  test-data-count-sentences 
  { 
     (str "Meaningless! Meaningless! Utterly meaningless! " 
     "Everything is meaningless... "
     "What do people gain from all their labors?") 5
     ""  0
     " " 0 })

(deftest test-count-sentences
  (is-eq-dict 
     #(count-sentences % {:split-sentences-f en-split-sentences-bi})
     test-data-count-sentences))

(def ^:private  test-data-count-letters-in-tokens
  {
   [ "Dog" "eats" "dog" ] 10
   [ "Empty" ] 5
   [ ]  0
   })

(deftest test-count-letters-in-tokens
  (is-eq-dict
     count-letters-in-tokens 
     test-data-count-letters-in-tokens))

(def ^:private  test-data-calc-text-stats
  { 
     (str 
        "Meaningless! Meaningless! Utterly meaningless! " 
        "Everything is meaningless... "
        "What do people gain from all their difficult labors?")  
        { :n-letters 106 :n-words 16 :n-hard-words 6 :n-sentences 5} })

(deftest test-calc-stats 
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        ]
    (is-eq-dict
      #(calc-text-stats % env) 
      test-data-calc-text-stats)))

(def ^:private  test-data-calc-indicies
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

(deftest test-gunning-fog-index
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        data 
          (zipmap test-data-calc-indicies  [ 16.28 19.26 ])
        ]
    (is-eq-dict
      #(calc-gunning-fog-index (calc-text-stats % env))
         data
         (partial float-eq 0.01))))

(deftest test-coleman-liau-index
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        data  (zipmap test-data-calc-indicies  [ 13.90 14.53 ])
        ]
    (is-eq-dict
      #(calc-coleman-liau-index (calc-text-stats % env))
        data
        (partial float-eq 0.01))))

(deftest test-automated-readability-index
  (let [
        env {
             :split-tokens-f en-split-tokens-bi
             :split-sentences-f en-split-sentences-bi
              }
        data  
          (zipmap (drop 1 test-data-calc-indicies)  (drop 1 [ 11.37 15.76 ]))
        ]
    (is-eq-dict
      #(calc-automated-readability-index (calc-text-stats % env))
        data
        (partial float-eq 0.01))))
