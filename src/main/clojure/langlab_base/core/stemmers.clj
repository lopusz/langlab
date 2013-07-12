(ns langlab-base.core.stemmers
  "Stemming utilities gathered from many libraries."
   (:import
    [ org.tartarus.snowball SnowballProgram ]
    [ org.tartarus.snowball.ext 
        ArmenianStemmer  BasqueStemmer CatalanStemmer DanishStemmer, 
        DutchStemmer EnglishStemmer FinnishStemmer FrenchStemmer German2Stemmer
        GermanStemmer HungarianStemmer IrishStemmer ItalianStemmer KpStemmer
        LovinsStemmer NorwegianStemmer PorterStemmer PortugueseStemmer 
        RomanianStemmer RussianStemmer SpanishStemmer SwedishStemmer 
        TurkishStemmer EnglishStemmer PorterStemmer LovinsStemmer]))

(defn- stem-snowball [ ^SnowballProgram sb ^String word ]
  (let [stemmer (doto sb
    (.setCurrent word)
    .stem)]
    (.getCurrent stemmer)))

(defn ca-stem-snowball
  "Snowball Catalan stemmer."
  [ ^String word ]
  (stem-snowball (new CatalanStemmer) word))

(defn da-stem-snowball
  "Snowball Danish stemmer."
  [ ^String word ]
  (stem-snowball (new DanishStemmer) word))

(defn de-stem-snowball
  "Snowball German stemmer."
  [ ^String word ]
  (stem-snowball (new GermanStemmer) word))

(defn de-2-stem-snowball
  "Snowball German2 stemmer."
  [ ^String word ]
  (stem-snowball (new German2Stemmer) word))

(defn en-stem-snowball
  "Snowball English stemmer (porter2)."
  [ ^String word ]
  (stem-snowball (new EnglishStemmer) word))

(defn en-porter-stem-snowball 
  "Snowball classic Porter stemmer."
  [ ^String word ]
  (stem-snowball (new PorterStemmer) word))

(defn en-lovins-stem-snowball [ ^String word ]
  (stem-snowball (new LovinsStemmer) word))

(defn es-stem-snowball
  "Snowball Spanish stemmer."
  [ ^String word ]
  (stem-snowball (new SpanishStemmer) word))

(defn eu-stem-snowball
  "Snowball Basque stemmer."
  [ ^String word ]
  (stem-snowball (new BasqueStemmer) word))

(defn fi-stem-snowball
  "Snowball Finnish stemmer."
  [ ^String word ]
  (stem-snowball (new FinnishStemmer) word))

(defn fr-stem-snowball
  "Snowball French stemmer."
  [ ^String word ]
  (stem-snowball (new FrenchStemmer) word))

(defn ga-stem-snowball
  "Snowball Irish stemmer."
  [ ^String word ]
  (stem-snowball (new IrishStemmer) word))

(defn hu-stem-snowball
  "Snowball Hungarian stemmer."
  [ ^String word ]
  (stem-snowball (new HungarianStemmer) word))

(defn hy-stem-snowball
  "Snowball Armenian stemmer."
  [ ^String word ]
  (stem-snowball (new ArmenianStemmer) word))

(defn it-stem-snowball
  "Snowball Italian stemmer."
  [ ^String word ]
  (stem-snowball (new ItalianStemmer) word))

(defn nl-stem-snowball
  "Snowball Dutch stemmer."
  [ ^String word ]
  (stem-snowball (new DutchStemmer) word))

(defn nl-kp-stem-snowball
  "Snowball Kraaij-Pohlmann Dutch stemmer."
  [ ^String word ]
  (stem-snowball (new KpStemmer) word))

(defn no-stem-snowball
  "Snowball Norwegian stemmer."
  [ ^String word ]
  (stem-snowball (new NorwegianStemmer) word))

(defn pt-stem-snowball
  "Snowball Portuguese stemmer."
  [ ^String word ]
  (stem-snowball (new PortugueseStemmer) word))

(defn ro-stem-snowball
  "Snowball Romanian stemmer."
  [ ^String word ]
  (stem-snowball (new RomanianStemmer) word))

(defn ru-stem-snowball
  "Snowball Russian stemmer."
  [ ^String word ]
  (stem-snowball (new RussianStemmer) word))

(defn sv-stem-snowball
  "Snowball Sweedish stemmer."
  [ ^String word ]
  (stem-snowball (new SwedishStemmer) word))

(defn tr-stem-snowball
  "Snowball Turkish stemmer."
  [ ^String word ]
  (stem-snowball (new TurkishStemmer) word))
