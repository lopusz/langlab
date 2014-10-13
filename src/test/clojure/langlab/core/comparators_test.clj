(ns langlab.core.comparators-test
  (:require [clojure.test :refer :all]
            [langlab.cmns.tests :refer (is-eq-dict) ]
            [langlab.core.comparators :refer :all]))


(deftest calc-common-prefix-length-test
  (is (= (calc-common-prefix-length "" "") 0))
  (is (= (calc-common-prefix-length "" "empty") 0))
  (is (= (calc-common-prefix-length "empty" "") 0))
  (is (= (calc-common-prefix-length "morze" "może") 2))
  (is (= (calc-common-prefix-length "może" "morze") 2))
  (is (= (calc-common-prefix-length "word" "another word") 0))
  (is (= (calc-common-prefix-length "another word" "word") 0))
  (is (= (calc-common-prefix-length "ąęść" "ąęść") 4))
  (is (= (calc-common-prefix-length "ąęść" "ąęśćabc") 4))
  (is (= (calc-common-prefix-length "ąęśćabc" "ąęść") 4))
  (is (= (calc-common-prefix-length "ąęśćabc" "ąęśćcbc") 4))
  (is (= (calc-common-prefix-length "ąęśćcbc" "ąęśćabc") 4))
  (is (= (calc-common-prefix-length "a" "a") 1)))
