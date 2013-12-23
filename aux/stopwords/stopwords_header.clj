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

(defn en-get-articles
  "Returns English article set."
  []
  #{ "a" "the" "an"})

(defn de-get-articles
  "Returns German article set."
  []
  #{ "der" "das" "die" "den" "dem" "des"
     "ein" "eine" "einer" "einem" "einen" "eines"
     "kein" "keine" "keiner" "keinem" "keinen" "keines"})

(defn nl-get-articles
  "Returns Dutch article set."
  []
  #{ "de" "het" "een" "geen" })

(defn fr-get-articles
  "Returns French article set."
  []
  #{ "le" "la" "l'" "les" "un" "une" "des"})

(defn it-get-articles
  "Returns Italian article set."
  []
  #{ "il" "lo" "i" "gli" "la" "le" "un" "uno" "una"})

(defn es-get-articles
  "Returns Spanish article set."
  []
  #{ "el" "la" "los" "las" "un" "una" "unos" "unas"})

(defn pt-get-articles
  "Return Portuguese article set."
  []
  #{ "o" "a" "os" "as" "um" "uns" "uma" "umas"})

(defn- conv-char-array-set-to-str-set [ char-array-set ]
  (into #{} (map #(String. %) char-array-set)))

(defn ar-get-stopwords-lucene
  "Returns default Arabic stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (ArabicAnalyzer/getDefaultStopSet)))

(defn hy-get-stopwords-lucene
  "Returns default Armenian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (ArmenianAnalyzer/getDefaultStopSet)))

(defn eu-get-stopwords-lucene
  "Returns default Basque stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (BasqueAnalyzer/getDefaultStopSet)))

(defn br-get-stopwords-lucene
  "Returns default Brazilian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (BrazilianAnalyzer/getDefaultStopSet)))

(defn bg-get-stopwords-lucene
  "Returns default Bulgarian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (BulgarianAnalyzer/getDefaultStopSet)))

(defn ca-get-stopwords-lucene
  "Returns default Catalan stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (CatalanAnalyzer/getDefaultStopSet)))

(defn cz-get-stopwords-lucene
  "Returns default Czech stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (CzechAnalyzer/getDefaultStopSet)))

(defn da-get-stopwords-lucene
  "Returns default Danish stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (DanishAnalyzer/getDefaultStopSet)))

(defn en-get-stopwords-lucene
  "Returns default Bulgarian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (EnglishAnalyzer/getDefaultStopSet)))

(defn fi-get-stopwords-lucene
  "Returns default Finish stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (FinnishAnalyzer/getDefaultStopSet)))

(defn fr-get-stopwords-lucene
  "Returns default French stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (FrenchAnalyzer/getDefaultStopSet)))

(defn gl-get-stopwords-lucene
  "Returns default Galician stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (GalicianAnalyzer/getDefaultStopSet)))

(defn de-get-stopwords-lucene
  "Returns default German stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (GermanAnalyzer/getDefaultStopSet)))

(defn el-get-stopwords-lucene
  "Returns default Greek stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (GreekAnalyzer/getDefaultStopSet)))

(defn hi-get-stopwords-lucene
  "Returns default Hindi stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (HindiAnalyzer/getDefaultStopSet)))

(defn hu-get-stopwords-lucene
  "Returns default Hungarian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (HungarianAnalyzer/getDefaultStopSet)))

(defn id-get-stopwords-lucene
  "Returns default Indonesian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (IndonesianAnalyzer/getDefaultStopSet)))

(defn ga-get-stopwords-lucene
  "Returns default Irish stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (IrishAnalyzer/getDefaultStopSet)))

(defn it-get-stopwords-lucene
  "Returns default Italian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (ItalianAnalyzer/getDefaultStopSet)))

(defn lv-get-stopwords-lucene
  "Returns default Latvian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (LatvianAnalyzer/getDefaultStopSet)))

(defn no-get-stopwords-lucene
  "Returns default Norwegian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (NorwegianAnalyzer/getDefaultStopSet)))

(defn fa-get-stopwords-lucene
  "Returns default Persian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (PersianAnalyzer/getDefaultStopSet)))

(defn pt-get-stopwords-lucene
  "Returns default Portuguese stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (PortugueseAnalyzer/getDefaultStopSet)))

(defn ro-get-stopwords-lucene
  "Returns default Romanian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (RomanianAnalyzer/getDefaultStopSet)))

(defn ru-get-stopwords-lucene
  "Returns default Russian stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (RussianAnalyzer/getDefaultStopSet)))

(defn es-get-stopwords-lucene
  "Returns default Spanish stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (SpanishAnalyzer/getDefaultStopSet)))

(defn sv-get-stopwords-lucene
  "Returns default Swedish stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (SwedishAnalyzer/getDefaultStopSet)))

(defn th-get-stopwords-lucene
  "Returns default Thai stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (ThaiAnalyzer/getDefaultStopSet)))

(defn tr-get-stopwords-lucene
  "Returns default Turkish stopword set used by Lucene."
  []
  (conv-char-array-set-to-str-set (TurkishAnalyzer/getDefaultStopSet)))
