(ns langlab-base.core.transformers-test
  (:require 
    [ clojure.test :refer :all ]
    [ langlab-base.core.transformers :refer :all]))

(deftest merge-tokens-with-space-test
  (is (= (merge-tokens-with-space [])
         ""))
  (is (= (merge-tokens-with-space [ "token1" "token2"])
         "token1 token2")))

(deftest split-tokens-with-space-test
  (is (= (split-tokens-with-space "")
         []))
  (is (= (split-tokens-with-space " \t ")
         []))
  (is (= (split-tokens-with-space "token1 \t token2\t ")
         [ "token1" "token2"])))

(def ^:private whitespace-utf-test-data
  [ "A" "\u0009" "B" "\u000A" "C" "\u000B" "D" "\u000C" "E" "\u000D" 
    "F" "\u0020" "G"  "H"  "I" "\u1680" "J" "\u180E" 
    "K" "\u2000" "L" "\u2001" "M" "\u2002" "N" "\u2003" "O" "\u2004" 
    "P" "\u2005" "R" "\u2006" "S" "T" "\u2008" "U" "\u2009" 
    "W" "\u200A" "X" "\u2028" "Y" "\u2029" "Z"  "1" "\u205F" 
    "2" "\u3000" ])

;; The following UTF-8 are ignored by Character.isWhitespace 
;; See, e.g., http://stackoverflow.com/questions/1060570/why-is-non-breaking-space-not-a-whitespace-character-in-java
;; There is Character.isSpaceChar method which detects no-break space

(def ^:private hard-whitespace-utf-test-data
  [ "\u0085" "Next line"
    "\u00A0" "No-break space"
    "\u2007" "Figure space"
    "\u202F" "Narrow no-break space"])

(deftest trans-drop-whitespace-test 
  (is (= (trans-drop-whitespace []) []))
  (is (= (trans-drop-whitespace whitespace-utf-test-data ) 
         ["A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "R" 
          "S" "T" "U" "W" "X" "Y" "Z" "1" "2"]))
  (is (= (trans-drop-whitespace
            [ "\t " " " "token1" "¿¿" "token2" "…" ])
            [ "token1" "¿¿" "token2" "…"]))
  ; Hard spaces & co do not work as expected
  (is (not= (trans-drop-whitespace hard-whitespace-utf-test-data)
            [ "Next line" "No-break space" "Figure space" 
               "Narrow no-break space"] )))

(deftest trans-merge-punct-test
  (is (= (trans-merge-punct []) []))
  (is (= (trans-merge-punct [ "Easy" "sentence" "without" "punct" ])
      [ "Easy" "sentence" "without" "punct" ]))
  (is (= (trans-merge-punct 
        [ "Not easy" "sentence" "." "." "." "Contains" "punct" "!" "!" "!"])
        [ "Not easy" "sentence" "..." "Contains" "punct" "!!!" ]))
  (is (= (trans-merge-punct
           ["¿" "¿" "¿" "A" "qué" "debe" "atenerse" "el" "hombre" "sobre" "la" 
            "realidad" "?" "?" "?"])
         ["¿¿¿" "A" "qué" "debe" "atenerse" "el" "hombre" "sobre" "la" 
            "realidad" "???"])))

(deftest trans-split-punct-test
  (is (= (trans-split-punct []) []))
  (is (= (trans-split-punct [ "Easy" "sentence" "without" "punct" ])
         [ "Easy" "sentence" "without" "punct" ]))
  (is (= (trans-split-punct 
           [ "Not easy" "sentence" "..." "Contains" "punct" "!!!" ])
          [ "Not easy" "sentence" "." "." "." "Contains" "punct" "!" "!" "!"]))
  (is (= (trans-split-punct
            ["¿¿¿" "A" "qué" "debe" "atenerse" "el" "hombre" "sobre" "la" 
             "realidad" "???"])
         ["¿" "¿" "¿" "A" "qué" "debe" "atenerse" "el" "hombre" "sobre" "la" 
            "realidad" "?" "?" "?"])))

(deftest en-trans-drop-articles-test 
  (is (= (en-trans-drop-articles []) []))
  (is (= (en-trans-drop-articles 
            [ "The" "question" "of" "an" "article" "." 
              "THe" "question" "." "AN" "answer" "." 
              "A" "test" "of" "a" "test" "."])
        [ "question" "of" "article" "." "question" "." "answer" "." 
              "test" "of" "test" "."])))

(deftest trans-drop-punct-test
  (is (= (trans-drop-punct []) []))
  (is (= (trans-drop-punct ["." "." "."] ) []))
  (is (= (trans-drop-punct [ "¿¿¿" "A"  "qué" "debe" "atenerse" "el" "hombre" 
         "sobre" "la" "realidad" "???"    "—"  "Bynajmniej" "…" ])
         [ "A"  "qué" "debe" "atenerse" "el" "hombre" 
         "sobre" "la" "realidad"  "Bynajmniej" ])))

(deftest trans-lower-case-test
  (is (= (trans-lower-case []) []))
  (is (= (trans-lower-case [ "" "" ]) [ "" "" ]))
  (is (= (trans-lower-case [ "Ósemka" "Życie" "USA"]) 
         [ "ósemka" "życie" "usa"]))
  (is (= (trans-lower-case [ "ÀÂÇÉÈÊËÏÎÔÙÛÜŸ" "ßÄÖÜ" "ĄĆĘŁŃÓŻŹ" ])
         [ "àâçéèêëïîôùûüÿ" "ßäöü" "ąćęłńóżź"])))
