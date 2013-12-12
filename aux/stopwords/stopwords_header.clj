(ns langlab.core.stopwords
  "Module contains predefined sets of stopwords/articles for various languages 
   and functions to operate on them.

   All constants are lowercase."
   
   (:require [ clojure.string :refer [lower-case]]))

(defn trans-drop-sw [ sw  tokens ]
  (filter #(not (contains? sw %)) tokens))

(defn trans-drop-sw-all-case [ sw  tokens ]
  (filter #(not (contains? sw (lower-case %))) tokens))

(def en-articles
  "English articles."
  #{ "a" "the" "en"})

(def de-articles
  "German articles."
  #{ "der" "das" "die" "den" "dem" "des" 
     "ein" "eine" "einer" "einem" "einen" "eines"
     "kein" "keine" "keiner" "keinem" "keinen" "keines"})

(def nl-articles
  "Dutch articles."
  #{ "de" "het" "een" "geen" })

(def fr-articles
  "French articles."
  #{ "le" "la" "l'" "les" "un" "une" "des"})

(def it-articles
  "Italian articles."
  #{ "il" "lo" "i" "gli" "la" "le" "un" "uno" "una"})

(def es-articles
  "Spanish articles."
  #{ "el" "la" "los" "las" "un" "una" "unos" "unas"})

(def pt-articles
  "Portuguese articles."
  #{ "o" "a" "os" "as" "um" "uns" "uma" "umas"})

