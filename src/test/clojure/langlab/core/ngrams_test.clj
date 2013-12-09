(ns langlab.core.ngrams-test
  (:require
    [ clojure.test :refer :all ]
    [ langlab.core.ngrams :refer (gen-ngrams gen-ngrams-range)]))

(deftest test-gen-ngrams
  (is (=
       (gen-ngrams [ ] 5)
       []))
  (is (=
       (gen-ngrams [ "Alice" "Bob" "Jane" "John" ] 2)
       [  [ "Alice" "Bob" ]  [ "Bob" "Jane" ] [ "Jane" "John" ] ]))

  (is (=
       (gen-ngrams [ "Alice" "Bob" "Jane" "John" ] 3)
       [  [ "Alice" "Bob" "Jane" ] [ "Bob" "Jane" "John" ] ]))

  (is (=
       (gen-ngrams [ "Alice" "Bob" ] 3)
       [ ])))

(deftest test-gen-ngrams-range
  (is (=
       (gen-ngrams-range [ ] 5 7)
       []))
  (is (=
       (gen-ngrams-range [ "Alice" "Bob" "Jane" "John" ] 2 4)
       [ [ "Alice" "Bob" ]  [ "Bob" "Jane"]  [ "Jane" "John" ]
         [ "Alice" "Bob" "Jane" ] [ "Bob" "Jane" "John" ]
         [ "Alice" "Bob" "Jane" "John" ] ]))
  (is (=
       (gen-ngrams-range [ "Alice" "Bob" "Jane" "John" ] 2 10)
       [ [ "Alice" "Bob" ]  [ "Bob" "Jane"]  [ "Jane" "John" ]
         [ "Alice" "Bob" "Jane" ] [ "Bob" "Jane" "John" ]
         [ "Alice" "Bob" "Jane" "John" ] ] ))
  (is (=
       (gen-ngrams-range [ "Alice" "Bob" "Jane" "John" ] 2 2)
       [ [ "Alice" "Bob" ]  [ "Bob" "Jane" ] [ "Jane" "John" ]  ]))

  (is (=
       (gen-ngrams-range [ "Alice" "Bob" "Jane" "John" ] 3 1)
       [ ] )))
