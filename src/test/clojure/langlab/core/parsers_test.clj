(ns langlab.core.parsers-test
  (:require 
    [ clojure.test :refer :all ]
    [ langlab.cmns.tests :refer (is-eq-dict is-NOT-eq-dict)]
    [ langlab.core.transformers :refer (trans-merge-punct) ]
    [ langlab.core.parsers :refer :all]))

(def ^:private basic-split-tokens-test-data 
   { "John, you should try!"  [ "John" "," "you" "should" "try" "!"] })

(def ^:private spec-chars-split-tokens-test-data 
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

(def ^:private multi-punct-split-tokens-test-data 
   {"This; however; is false --- nothing is tested..."
       [ "This" ";" "however" ";"
          "is" "false" "---" "nothing" "is" "tested" "..."]
     "She loves you!!! Yeah?!? Yeah! Yeah!!!"
       [ "She" "loves" "you" "!!!"  "Yeah" "?!?"  "Yeah" "!" "Yeah" "!!!"] })

(defn- test-split-tokens-bi [ split-f ]
  (is-eq-dict 
    (comp
      trans-merge-punct
      split-f) 
     basic-split-tokens-test-data)

  (is-eq-dict 
    (comp 
       trans-merge-punct
       split-f)
    spec-chars-split-tokens-test-data)

  (is-eq-dict 
    (comp 
       trans-merge-punct
       split-f)
    multi-punct-split-tokens-test-data))

(deftest en-split-tokens-bi-test 
  (test-split-tokens-bi en-split-tokens-bi))

(deftest en-split-tokens-icu-bi-test 
  (test-split-tokens-bi en-split-tokens-icu-bi))

(deftest en-split-tokens-onlp
  (let [
         split-f
          (make-split-tokens-onlp 
            "src/test/clojure/langlab/core/data/en-token.bin")
        ]
  (is-eq-dict 
    (comp
      trans-merge-punct
      split-f) 
     basic-split-tokens-test-data)

  (is-NOT-eq-dict 
    (comp 
       trans-merge-punct
       split-f)
    spec-chars-split-tokens-test-data)

  (is-eq-dict 
    (comp 
       trans-merge-punct
       split-f)
    multi-punct-split-tokens-test-data)))

(def ^:private basic-split-sentences-test-data
  { (str 
   "Dash it all! I thought to myself, they can't trade without using some "
   "kind of craft on that lot of fresh water--steamboats! Why shouldn't I "
   "try to get charge of one? I went on along Fleet Street, but could not "
   "shake off the idea. The snake had charmed me.")
   [ "Dash it all!"
     (str "I thought to myself, they can't trade without using some kind "
          "of craft on that lot of fresh water--steamboats!")
     "Why shouldn't I try to get charge of one?"
     "I went on along Fleet Street, but could not shake off the idea."
     "The snake had charmed me."]})

(def ^:private abbrev-split-sentences-test-data
  { "I was born in the U.S.A. and other places. Anarchy is in the U.K. Bye!"
    [ "I was born in the U.S.A. and other places."  
       "Anarchy is in the U.K." "Bye!"] })

(def ^:private multi-punct-split-sentences-test-data
   { "Home... Is this a final place??? True. Nothing is set!"
     [ "Home..." "Is this a final place???" "True." "Nothing is set!" ] })

(def ^:private easy-spec-chars-split-sentences-test-data
   {  (str "¡¡¡A qué debe atenerse el hombre sobre la realidad!!! "
            "¿A qué debe atenerse el hombre sobre la realidad?")
     [ "¡¡¡A qué debe atenerse el hombre sobre la realidad!!!"
       "¿A qué debe atenerse el hombre sobre la realidad?"] })

(def ^:private hard-spec-chars-split-sentences-test-data
  {  
   "Home… Is this a — final — place??? True. Nothing is set!"
   [ "Home…" "Is this a — final — place???" "True." "Nothing is set!" ]})

(def ^:private quote-split-sentences-test-data
  { (str "\"And this also,\" said Marlow suddenly, " 
         "\"has been one of the dark places of the earth.\"")
    [""] })

(defn test-split-sentences-bi [ split-f ]
  (is-eq-dict split-f basic-split-sentences-test-data)
  (is-eq-dict split-f multi-punct-split-sentences-test-data)
  (is-eq-dict split-f easy-spec-chars-split-sentences-test-data)
  (is-eq-dict split-f abbrev-split-sentences-test-data)
  ;; Unicode "…" are not recognized
  (is-NOT-eq-dict split-f hard-spec-chars-split-sentences-test-data))

(deftest en-split-sentences-bi-test
  (test-split-sentences-bi en-split-sentences-bi))

(deftest en-split-sentences-icu-bi-test
  (test-split-sentences-bi en-split-sentences-icu-bi))

(deftest en-split-sentences-onlp-test
  (let [
        split-f
          (make-split-sentences-onlp 
            "src/test/clojure/langlab/core/data/en-sent.bin")
       ]
  (is-eq-dict split-f basic-split-sentences-test-data)
  ;; Default ONLP models seem to fail  on most fancy tests :(
  (is-NOT-eq-dict split-f multi-punct-split-sentences-test-data)
  (is-NOT-eq-dict split-f easy-spec-chars-split-sentences-test-data)
  (is-NOT-eq-dict split-f abbrev-split-sentences-test-data)
  (is-NOT-eq-dict split-f hard-spec-chars-split-sentences-test-data)))



