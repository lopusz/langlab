(ns langlab.core.stopwords
  "Module contains predefined sets of stopwords/articles for various languages
   and functions to operate on them.

   All constants are lowercase."

  (:require [ clojure.string :refer [lower-case]])
  (:import 
             [ org.apache.lucene.analysis.ar ArabicAnalyzer ]
             [ org.apache.lucene.analysis.hy ArmenianAnalyzer ] 
             [ org.apache.lucene.analysis.eu BasqueAnalyzer ] 
             [ org.apache.lucene.analysis.br BrazilianAnalyzer ] 
             [ org.apache.lucene.analysis.bg BulgarianAnalyzer ]
             [ org.apache.lucene.analysis.ca CatalanAnalyzer ]
             [ org.apache.lucene.analysis.cz CzechAnalyzer ]
             [ org.apache.lucene.analysis.da DanishAnalyzer ]
             [ org.apache.lucene.analysis.en EnglishAnalyzer ] 
             [ org.apache.lucene.analysis.fi FinnishAnalyzer ]
             [ org.apache.lucene.analysis.fr FrenchAnalyzer ]
             [ org.apache.lucene.analysis.gl GalicianAnalyzer ]
             [ org.apache.lucene.analysis.de GermanAnalyzer ]
             [ org.apache.lucene.analysis.el GreekAnalyzer ]
             [ org.apache.lucene.analysis.hi HindiAnalyzer ]
             [ org.apache.lucene.analysis.hu HungarianAnalyzer ]
             [ org.apache.lucene.analysis.id IndonesianAnalyzer ]
             [ org.apache.lucene.analysis.ga IrishAnalyzer ]
             [ org.apache.lucene.analysis.it ItalianAnalyzer ]
             [ org.apache.lucene.analysis.lv LatvianAnalyzer ]
             [ org.apache.lucene.analysis.no NorwegianAnalyzer ]
             [ org.apache.lucene.analysis.fa PersianAnalyzer ]
             [ org.apache.lucene.analysis.pt PortugueseAnalyzer ] 
             [ org.apache.lucene.analysis.ro RomanianAnalyzer ]
             [ org.apache.lucene.analysis.ru RussianAnalyzer ] 
             [ org.apache.lucene.analysis.es SpanishAnalyzer ]
             [ org.apache.lucene.analysis.sv SwedishAnalyzer ]
             [ org.apache.lucene.analysis.th ThaiAnalyzer ]
             [ org.apache.lucene.analysis.tr TurkishAnalyzer ]))

(defn trans-drop-set
  "Drop all elements of 'tokens' that are included in the 'drop-set'.
   For 'drop-set' one of the module constants representing stopwords or
   articles can be used."
  [ drop-set tokens ]
  (filter #(not (contains? drop-set %)) tokens))

(defn trans-drop-set-all-case
  "Drop all elements of 'tokens' that are included in the 'drop-set'.
   Ignore case.
   For 'drop-set' one of the module constants representing stopwords or
   articles can be used."
  [ drop-set tokens ]
  (filter #(not (contains? drop-set (lower-case %))) tokens))

(def en-articles
  "English articles."
  #{ "a" "the" "an"})

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

(defn- conv-char-array-set-to-str-set [ char-array-set]
  (into #{} (map #(String. %) char-array-set)))

(defn ar-get-sw-lucene 
  "Returns default Arabic stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (ArabicAnalyzer/getDefaultStopSet)))

(defn hy-get-sw-lucene 
  "Returns default Armenian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (ArmenianAnalyzer/getDefaultStopSet))) 

(defn eu-get-sw-lucene 
  "Returns default Basque stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (BasqueAnalyzer/getDefaultStopSet))) 

(defn br-get-sw-lucene 
  "Returns default Brazilian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (BrazilianAnalyzer/getDefaultStopSet))) 

(defn bg-get-sw-lucene 
  "Returns default bulgarianstop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (BulgarianAnalyzer/getDefaultStopSet)))

(defn ca-get-sw-lucene 
  "Returns default Catalan stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (CatalanAnalyzer/getDefaultStopSet)))

(defn cz-get-sw-lucene 
  "Returns default Czech stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (CzechAnalyzer/getDefaultStopSet)))

(defn da-get-sw-lucene 
  "Returns default Danish nstop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (DanishAnalyzer/getDefaultStopSet)))

(defn en-get-sw-lucene 
  "Returns default bulgarianstop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (EnglishAnalyzer/getDefaultStopSet)))

(defn fi-get-sw-lucene 
  "Returns default Finish stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (FinnishAnalyzer/getDefaultStopSet)))

(defn fr-get-sw-lucene 
  "Returns default French stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (FrenchAnalyzer/getDefaultStopSet)))

(defn gl-get-sw-lucene 
  "Returns default Galician stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (GalicianAnalyzer/getDefaultStopSet)))

(defn de-get-sw-lucene 
  "Returns default German stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (GermanAnalyzer/getDefaultStopSet)))

(defn el-get-sw-lucene 
  "Returns default Greek stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (GreekAnalyzer/getDefaultStopSet)))

(defn hi-get-sw-lucene 
  "Returns default Hindi stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (HindiAnalyzer/getDefaultStopSet)))

(defn hu-get-sw-lucene 
  "Returns default Hungarian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (HungarianAnalyzer/getDefaultStopSet)))

(defn id-get-sw-lucene 
  "Returns default Indonesian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (IndonesianAnalyzer/getDefaultStopSet)))

(defn ga-get-sw-lucene 
  "Returns default Irish stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (IrishAnalyzer/getDefaultStopSet)))

(defn it-get-sw-lucene 
  "Returns default Italian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (ItalianAnalyzer/getDefaultStopSet)))

(defn lv-get-sw-lucene 
  "Returns default Latvian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (LatvianAnalyzer/getDefaultStopSet)))

(defn no-get-sw-lucene 
  "Returns default Norwegian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (NorwegianAnalyzer/getDefaultStopSet)))

(defn fa-get-sw-lucene  
  "Returns default Persian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (PersianAnalyzer/getDefaultStopSet)))

(defn pt-get-sw-lucene 
  "Returns default Portuguese stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (PortugueseAnalyzer/getDefaultStopSet)))
 
(defn ro-get-sw-lucene 
  "Returns default romanian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (RomanianAnalyzer/getDefaultStopSet)))

(defn ru-get-sw-lucene 
  "Returns default Russian stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (RussianAnalyzer/getDefaultStopSet))) 

(defn es-get-sw-lucene 
  "Returns default Spanish stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (SpanishAnalyzer/getDefaultStopSet)))

(defn sv-get-sw-lucene 
  "Returns default Swedish stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (SwedishAnalyzer/getDefaultStopSet)))

(defn th-get-sw-lucene 
  "Returns default Thai stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (ThaiAnalyzer/getDefaultStopSet)))

(defn tr-get-sw-lucene 
  "Returns default Turkish stop word set used by Lucene."
  [] 
  (conv-char-array-set-to-str-set (TurkishAnalyzer/getDefaultStopSet)))
