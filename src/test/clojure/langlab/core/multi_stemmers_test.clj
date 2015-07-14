(ns langlab.core.multi-stemmers-test
  (:require [clojure.test :refer :all]
            [langlab.core.multi-stemmers :refer :all]))

(deftest select-longest-word-test
  (is (=
        (select-longest-word [ ])
        nil))
  (is (=
      (select-longest-word [ "ala" "ola" "ula"])
      "ula"))
  (is (=
    (select-longest-word [ "ala" "eleonora" "ola"])
    "eleonora")))

(deftest select-shortest-word-test
  (is (=
        (select-shortest-word [ ])
        nil))
  (is (=
        (select-shortest-word [ "ala" "ola" "ula"])
        "ula"))
  (is (=
        (select-shortest-word [ "ala" "mi" "ola"])
        "mi")))

(deftest merge-multiple-words-test
  (is (=
        (merge-multiple-words [ ] "|")
        nil))
  (is (=
        (merge-multiple-words [ "ala" "ula" "ola"] "|")
        "ala|ola|ula"))
  (is (=
        (merge-multiple-words [ "a3" "a2" "a1"] " and ")
        "a1 and a2 and a3")))
