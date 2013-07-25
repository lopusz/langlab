(ns langlab-base.core.characters-test
  (:require [clojure.test :refer :all]
            [langlab-base.cmns.tests :refer (is-eq-dict) ]
            [langlab-base.core.characters :refer :all]))

(deftest remove-diacritics-test
  (is (= (remove-diacritics "") ""))
  (is (= (remove-diacritics "żółw") "zolw"))
  (is (= (remove-diacritics "ąćęłńóżźĄĆĘŁŃÓŻŹ") "acelnozzACELNOZZ"))
  (is (= (remove-diacritics "äöüßÄÖÜ") "aoußAOU"))
  (is (= (remove-diacritics "àâçéèêëïîôùûüÿÀÂÇÉÈÊËÏÎÔÙÛÜŸ") 
         "aaceeeeiiouuuyAACEEEEIIOUUUY")))

(def ^:private count-chars-test-data
  { "To be or not to be?" 19
    "Thîs lóo̰ks we̐ird."  17 
    "queer chars …¿„”— wow!" 22 
     " \uD840\uDC00 " 3})

(deftest lg-count-chars-bi-test 
  (is-eq-dict #(lg-count-chars-bi "en" %) count-chars-test-data))

(deftest en-count-chars-bi-test 
  (is-eq-dict en-count-chars-bi count-chars-test-data))

(deftest lg-count-chars-icu-bi-test
  (is-eq-dict #(lg-count-chars-icu-bi "en" %) count-chars-test-data))

(deftest en-count-chars-icu-bi-test 
  (is-eq-dict en-count-chars-icu-bi count-chars-test-data))

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

(deftest contains-punct-test
  (is (= (contains-punct? "") false))
  (is (= (contains-punct? "test,test") true))
  (is (= (contains-punct? ",;!") true))
  (is (= (contains-punct? "a—") true))
  (is (= (contains-punct? "¿xyz") true)))

(deftest contains-punct-only-test
  (is (= (contains-punct-only? "") false))
  (is (= (contains-punct-only? ",;!") true))
  (is (= (contains-punct-only? ",;!") true))
  (is (= (contains-punct-only? "…¿„”—") true))
  (is (= (contains-punct-only? "…¿a„”—") false))
  (is (= (contains-punct-only? "z…¿„”—") false))
  (is (= (contains-punct-only? ",;!b") false)))

(deftest contains-whitespace-test
  (is (= (contains-whitespace? "") false))
  (is (= (contains-whitespace? "z…¿„”—") false))
  (is (= (contains-whitespace? "z…¿\t„”—") true))
  (is (= (contains-whitespace? "z…¿ \n„”—") true)))

(deftest contains-whitespace-only-test
  (is (= (contains-whitespace-only? "") false))
  (is (= (contains-whitespace-only? "\ta\n") false))
  (is (= (contains-whitespace-only? "\t \t\n") true)))

(deftest contains-non-bmp-test
  (is (= (contains-non-bmp? "") false))
  (is (= (contains-non-bmp? "queer chars …¿„”— wow!") false))
  (is (= (contains-non-bmp? "Thîs lóo̰ks we̐ird") false))
  (is (= (contains-non-bmp?
          "We use surogate pair for non-bmp \uD840\uDC00, right?")
         true)))

(deftest remove-non-bmp-test
  (is (= (remove-non-bmp "") ""))
  (is (= (remove-non-bmp "queer chars …¿„”— wow!") "queer chars …¿„”— wow!"))
  (is (= (remove-non-bmp "Thîs lóo̰ks we̐ird") "Thîs lóo̰ks we̐ird"))
  (is (= (remove-non-bmp
           "We use surogate pair for non-bmp \uD840\uDC00, right?")
         "We use surogate pair for non-bmp , right?")))

(deftest remove-bmp-test
  (is (= (remove-bmp "") ""))
  (is (= (remove-bmp "queer chars …¿„”— wow!") ""))
  (is (= (remove-bmp "Thîs lóo̰ks we̐ird") ""))
  (is (= (remove-bmp
           "We use surogate pair for non-bmp \uD840\uDC00, right?")
         "\uD840\uDC00")))
