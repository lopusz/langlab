(ns langlab.core.stemmers-test
  (:require [clojure.test :refer :all]
            [langlab.core.stemmers :refer :all]))

(deftest pl-stem-clef-light-test
  (is (= (pl-stem-light-clef "panowie") "pan"))
  (is (= (pl-stem-light-clef "aktorowi") "aktor"))
  (is (= (pl-stem-light-clef "kotami") "kot"))
  (is (= (pl-stem-light-clef "kotach") "kot"))
  (is (= (pl-stem-light-clef "wodżie") "wod"))
  (is (= (pl-stem-light-clef "autobuśem") "autobus"))
  (is (= (pl-stem-light-clef "aktorom") "aktor"))
  (is (= (pl-stem-light-clef "kotow") "kot"))
  (is (= (pl-stem-light-clef "aktokze") "aktok"))
  (is (= (pl-stem-light-clef "aktorzy") "aktor"))
  (is (= (pl-stem-light-clef "kotami") "kot"))
  (is (= (pl-stem-light-clef "Rośjanin") "Rosjan")))

