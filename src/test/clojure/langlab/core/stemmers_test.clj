(ns langlab.core.stemmers-test
  (:require [clojure.test :refer :all]
            [langlab.core.stemmers :refer :all]))

(deftest pl-stem-clef-light-test
  (is (= (pl-stem-clef-light "panowie") "pan"))
  (is (= (pl-stem-clef-light "aktorowi") "aktor"))
  (is (= (pl-stem-clef-light "kotami") "kot"))
  (is (= (pl-stem-clef-light "kotach") "kot"))
  (is (= (pl-stem-clef-light "wodżie") "wod"))
  (is (= (pl-stem-clef-light "autobuśem") "autobus"))
  (is (= (pl-stem-clef-light "aktorom") "aktor"))
  (is (= (pl-stem-clef-light "kotow") "kot"))
  (is (= (pl-stem-clef-light "aktokze") "aktok"))
  (is (= (pl-stem-clef-light "aktorzy") "aktor"))
  (is (= (pl-stem-clef-light "kotami") "kot"))
  (is (= (pl-stem-clef-light "Rośjanin") "Rosjan")))

