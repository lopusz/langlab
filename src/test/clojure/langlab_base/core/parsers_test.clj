(ns langlab-base.core.parsers-test
  (:require 
    [ clojure.test :refer :all ]
    [ langlab-base.cmns.tests :refer (run-dict-test)]
    [ langlab-base.core.transformers :refer (trans-merge-punct) ]
    [ langlab-base.core.parsers :refer :all]))

(def quotation-sentence-test-data
  "\"And this also,\" said Marlow suddenly," 
  "\"has been one of the dark places of the earth.\"")

(def basic-tokenize-test-data 
   { "John, you should try!"  [ "John" "," "you" "should" "try" "!"] })

(def spec-chars-tokenize-test-data 
   {  "¡Que pase un buen día!"
      [ "¡" "Que" "pase" "un" "buen" "día" "!" ]
      "¿A qué debe atenerse el hombre sobre la realidad?"
      [  "¿" "A"  "qué" "debe" "atenerse" "el" "hombre" 
          "sobre" "la" "realidad" "?" ]
      "¿¿¿A qué debe atenerse el hombre sobre la realidad???"
      [  "¿¿¿" "A"  "qué" "debe" "atenerse" "el" "hombre" 
         "sobre" "la" "realidad" "???" ] 
      "— Bynajmniej… Byłbym wielce szczęśliwy… Tak dawno… — bąkał Judym."
      [ "—"  "Bynajmniej" "…" "Byłbym" "wielce" "szczęśliwy" "…" 
        "Tak" "dawno" "…—" "bąkał" "Judym" "." ] 
      "Jeżeli tak będzie, to dlatego, że z tych szewców wiedzie swój „rodowód…”"
      [ "Jeżeli" "tak" "będzie" "," "to" "dlatego" "," "że" "z" "tych" 
        "szewców" "wiedzie" "swój" "„" "rodowód" "…”" ]})

(def multi-punct-tokenize-test-data 
   {"This; however; is false --- nothing is tested..."
       [ "This" ";" "however" ";"
          "is" "false" "---" "nothing" "is" "tested" "..."]
     "She loves you!!! Yeah?!? Yeah! Yeah!!!"
       [ "She" "loves" "you" "!!!"  "Yeah" "?!?"  "Yeah" "!" "Yeah" "!!!"] })

(defn test-split-tokens [ split-f ]
  (run-dict-test 
    (comp
      trans-merge-punct
      split-f) 
     basic-tokenize-test-data)

  (run-dict-test 
    (comp 
       trans-merge-punct
       split-f)
    spec-chars-tokenize-test-data)

  (run-dict-test 
    (comp 
       trans-merge-punct
       split-f)
    multi-punct-tokenize-test-data))

(deftest en-split-tokens-bi-test 
  (test-split-tokens en-split-tokens-bi))

(deftest en-split-tokens-bi-test 
  (test-split-tokens en-split-tokens-icu-bi))



