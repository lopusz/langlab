(ns langlab.core.ngrams-test
  (:require
    [ clojure.test :refer :all ]
    [ langlab.core.ngrams :refer (gen-ngrams)]))

(deftest test-gen-ngrams
  (is (=
       (gen-ngrams 5 [ ])
       []))
  (is (=
       (gen-ngrams 1 [ "Alice" "Bob" "Jane" "John" ])
       [ ["Alice"] ["Bob"] ["Jane"] ["John"] ] ))
  (is (=
       (gen-ngrams 2 [ "Alice" "Bob" "Jane" "John" ])
       [  [ "Alice" "Bob" ]  [ "Bob" "Jane" ] [ "Jane" "John" ] ]))
  (is (=
       (gen-ngrams 3 [ "Alice" "Bob" "Jane" "John" ])
       [  [ "Alice" "Bob" "Jane" ] [ "Bob" "Jane" "John" ] ]))
  (is (=
       (gen-ngrams 3 [ "Alice" "Bob" ])
       [ ]))
  (is (=
       (gen-ngrams 5 7 [ ])
       []))
  (is (=
       (gen-ngrams 2 4 [ "Alice" "Bob" "Jane" "John" ])
       [ [ "Alice" "Bob" ]  [ "Bob" "Jane"]  [ "Jane" "John" ]
         [ "Alice" "Bob" "Jane" ] [ "Bob" "Jane" "John" ]
         [ "Alice" "Bob" "Jane" "John" ] ]))
  (is (=
       (gen-ngrams 2 10 [ "Alice" "Bob" "Jane" "John" ])
       [ [ "Alice" "Bob" ]  [ "Bob" "Jane"]  [ "Jane" "John" ]
         [ "Alice" "Bob" "Jane" ] [ "Bob" "Jane" "John" ]
         [ "Alice" "Bob" "Jane" "John" ] ] ))
  (is (=
       (gen-ngrams 2 2 [ "Alice" "Bob" "Jane" "John" ])
       [ [ "Alice" "Bob" ]  [ "Bob" "Jane" ] [ "Jane" "John" ]  ]))

  (is (=
       (gen-ngrams 3 1 [ "Alice" "Bob" "Jane" "John" ])
       [ ] ))

  (is (=
       (gen-ngrams [ "Alice" "Bob" "Jane" "John" ])
         [
           [ "Alice" ] [ "Bob" ] [ "Jane"] [ "John" ]
           [ "Alice" "Bob" ] [ "Bob" "Jane"] [ "Jane" "John" ]
           [ "Alice" "Bob" "Jane" ] ["Bob" "Jane" "John"]
           [ "Alice" "Bob" "Jane" "John" ]])))
