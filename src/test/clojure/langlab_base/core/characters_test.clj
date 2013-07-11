(ns langlab-base.core.characters-test
  (:require [clojure.test :refer :all]
            [langlab-base.core.characters :refer :all]))

(deftest remove-diacritics-test
  (is (= (remove-diacritics "") ""))
  (is (= (remove-diacritics "żółw") "zolw"))
  (is (= (remove-diacritics "ąćęłńóżźĄĆĘŁŃÓŻŹ") "acelnozzACELNOZZ"))
  (is (= (remove-diacritics "äöüßÄÖÜ") "aoußAOU"))
  (is (= (remove-diacritics "àâçéèêëïîôùûüÿÀÂÇÉÈÊËÏÎÔÙÛÜŸ") 
         "aaceeeeiiouuuyAACEEEEIIOUUUY")))

(deftest count-latin-vowel-groups-test
  (is (= (count-latin-vowel-groups "") 0))
  (is (= (count-latin-vowel-groups "a") 1))
  (is (= (count-latin-vowel-groups "believe") 3))
  (is (= (count-latin-vowel-groups "größer") 2))
  (is (= (count-latin-vowel-groups "personae") 3))
  (is (= (count-latin-vowel-groups "algorithm") 3)))

(deftest count-latin-vowel-groups-without-final-test
  (is (= (count-latin-vowel-groups-without-final "") 0))
  (is (= (count-latin-vowel-groups-without-final "a") 0))
  (is (= (count-latin-vowel-groups-without-final "believe") 2))
  (is (= (count-latin-vowel-groups-without-final "größer") 2))
  (is (= (count-latin-vowel-groups-without-final "personae") 2))
  (is (= (count-latin-vowel-groups-without-final "algorithm") 3)))
