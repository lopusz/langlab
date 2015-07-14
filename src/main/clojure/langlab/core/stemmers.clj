(ns langlab.core.stemmers
   "Module contains stemming algorithms."
   (:require [ clojure.java.io :refer (resource input-stream)])
   (:import
    [org.tartarus.snowball SnowballProgram]
    [org.tartarus.snowball.ext
        ArmenianStemmer  BasqueStemmer CatalanStemmer DanishStemmer,
        DutchStemmer EnglishStemmer FinnishStemmer FrenchStemmer German2Stemmer
        GermanStemmer HungarianStemmer IrishStemmer ItalianStemmer KpStemmer
        LovinsStemmer NorwegianStemmer PorterStemmer PortugueseStemmer
        RomanianStemmer RussianStemmer SpanishStemmer SwedishStemmer
        TurkishStemmer EnglishStemmer PorterStemmer LovinsStemmer]
    [org.apache.lucene.analysis.stempel StempelStemmer]
    [org.apache.lucene.analysis.pl PolishAnalyzer]
    [org.egothor.stemmer Trie]
    [edu.washington.cs.knowitall.morpha MorphaStemmer]
    [clef_tools.stemmers PolishLight]))

(defn- stem-snowball [ ^SnowballProgram sb ^String word ]
  (let [stemmer (doto sb
    (.setCurrent word)
    .stem)]
    (.getCurrent stemmer)))

(defn ca-stem-snowball
  "Returns a stem of `word` generated by Snowball Catalan stemmer."
  [ ^String word ]
  (stem-snowball (new CatalanStemmer) word))

(defn da-stem-snowball
  "Returns a stem of `word` generated by Snowball Danish stemmer."
  [ ^String word ]
  (stem-snowball (new DanishStemmer) word))

(defn de-stem-snowball
  "Returns a stem of `word` generated by Snowball German stemmer."
  [ ^String word ]
  (stem-snowball (new GermanStemmer) word))

(defn de-2-stem-snowball
  "Returns a stem of `word` generated by Snowball German2 stemmer."
  [ ^String word ]
  (stem-snowball (new German2Stemmer) word))

(defn en-stem-snowball
  "Returns a stem of `word` generated by Snowball English stemmer (porter2)."
  [ ^String word ]
  (stem-snowball (new EnglishStemmer) word))

(defn en-porter-stem-snowball
  "Returns a stem of `word` generated by Snowball classic Porter stemmer."
  [ ^String word ]
  (stem-snowball (new PorterStemmer) word))

(defn en-lovins-stem-snowball
  "Returns a stem of `word` generated by Snowball Lovins Stemmer."
  [ ^String word ]
  (stem-snowball (new LovinsStemmer) word))

(defn es-stem-snowball
  "Returns a stem of `word` generated by Snowball Spanish stemmer."
  [ ^String word ]
  (stem-snowball (new SpanishStemmer) word))

(defn eu-stem-snowball
  "Returns a stem of `word` generated by Snowball Basque stemmer."
  [ ^String word ]
  (stem-snowball (new BasqueStemmer) word))

(defn fi-stem-snowball
  "Returns a stem of `word` generated by Snowball Finnish stemmer."
  [ ^String word ]
  (stem-snowball (new FinnishStemmer) word))

(defn fr-stem-snowball
  "Returns a stem of `word` generated by Snowball French stemmer."
  [ ^String word ]
  (stem-snowball (new FrenchStemmer) word))

(defn ga-stem-snowball
  "Returns a stem of `word` generated by Snowball Irish stemmer."
  [ ^String word ]
  (stem-snowball (new IrishStemmer) word))

(defn hu-stem-snowball
  "Returns a stem of `word` generated by Snowball Hungarian stemmer."
  [ ^String word ]
  (stem-snowball (new HungarianStemmer) word))

(defn hy-stem-snowball
  "Returns a stem of `word` generated by Snowball Armenian stemmer."
  [ ^String word ]
  (stem-snowball (new ArmenianStemmer) word))

(defn it-stem-snowball
  "Returns a stem of `word` generated by Snowball Italian stemmer."
  [ ^String word ]
  (stem-snowball (new ItalianStemmer) word))

(defn nl-stem-snowball
  "Returns a stem of `word` generated by Snowball Dutch stemmer."
  [ ^String word ]
  (stem-snowball (new DutchStemmer) word))

(defn nl-kp-stem-snowball
 "Returns a stem of `word` generated by Snowball Kraaij-Pohlmann Dutch stemmer."
  [ ^String word ]
  (stem-snowball (new KpStemmer) word))

(defn no-stem-snowball
  "Returns a stem of `word` generated by Snowball Norwegian stemmer."
  [ ^String word ]
  (stem-snowball (new NorwegianStemmer) word))

(defn pt-stem-snowball
  "Returns a stem of `word` generated by Snowball Portuguese stemmer."
  [ ^String word ]
  (stem-snowball (new PortugueseStemmer) word))

(defn ro-stem-snowball
  "Returns a stem of `word` generated by Snowball Romanian stemmer."
  [ ^String word ]
  (stem-snowball (new RomanianStemmer) word))

(defn ru-stem-snowball
  "Returns a stem of `word` generated by Snowball Russian stemmer."
  [ ^String word ]
  (stem-snowball (new RussianStemmer) word))

(defn sv-stem-snowball
  "Returns a stem of `word` generated by Snowball Swedish stemmer."
  [ ^String word ]
  (stem-snowball (new SwedishStemmer) word))

(defn tr-stem-snowball
  "Returns a stem of `word` generated by Snowball Turkish stemmer."
  [ ^String word ]
  (stem-snowball (new TurkishStemmer) word))

(def ^:private pl-stempel-trie
  (delay (StempelStemmer/load
           (.getResourceAsStream
             PolishAnalyzer PolishAnalyzer/DEFAULT_STEMMER_FILE))))

(defn ^:private pl-gen-stem-stempel [^String word]
  (.stem ^StempelStemmer (StempelStemmer. ^Trie @pl-stempel-trie) word))

(defn pl-stem-stempel
  "Returns a stem of `word` generated by Stempel Polish stemmer."
  [^String word]
    (let [
        stem (pl-gen-stem-stempel word)
        ]
    (if (= stem  nil)
      word
      (.toString ^StringBuilder stem))))

(defn en-morpha-stemmer
  "Returns a stem of `word` generated by morpha stemmer.

   See: http://www.sussex.ac.uk/Users/johnca/morph.html
  "
  [ ^String word ]
  (MorphaStemmer/stemToken word))

(defn pl-stem-clef-light
  "Returns a stem of `word` generated by PolishLight stemmer from clef-tools.

   See: http://members.unine.ch/jacques.savoy/clef/"

    [ ^String word]
    (PolishLight/stem word))
