(ns langlab.core.stopwords-test
  (:require
    [ clojure.test :refer :all ]
    [ langlab.core.stopwords
       :refer (en-articles trans-drop-set trans-drop-set-all-case)]))

(deftest test-trans-drop-set
  (is (=
        (trans-drop-set en-articles [ ])
        [ ]))
  (is (=
        (trans-drop-set en-articles [ "it" "is" "amazing" ])
        [ "it" "is" "amazing" ]))
  (is (=
        (trans-drop-set en-articles [ "the" "a" "an" ])
        []))
  (is (=
        (trans-drop-set en-articles [ "a" "cloud" "in" "the" "sky"])
        [ "cloud" "in" "sky"])))

(deftest test-trans-drop-set-all-case
  (is (=
        (trans-drop-set-all-case en-articles [ ])
        [ ]))
  (is (=
        (trans-drop-set-all-case en-articles [ "it" "is" "amazing" ])
        [ "it" "is" "amazing" ]))
  (is (=
        (trans-drop-set-all-case en-articles [ "thE" "The" "a" "aN" ])
        []))
  (is (=
        (trans-drop-set-all-case en-articles
          [ "A" "cloud" "in" "tHe" "sky" "with" "aN" "apple"])
        [ "cloud" "in" "sky" "with" "apple"])))
