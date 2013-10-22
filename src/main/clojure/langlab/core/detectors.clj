(ns langlab.core.detectors
  "Module contains language and encoding detection utilities.

   Language is represented with two-letter strings containing
   ISO 639-1 codes. 
   (http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes).

   Unfortunately, there is no standard for encoding string
   representation."

  (:import [ com.cybozu.labs.langdetect Language Detector DetectorFactory ]
           [ com.ibm.icu.text CharsetDetector ]
           [ org.apache.tika.language LanguageIdentifier ]
           [ langlab.jcore.detectors EncodDetectorTools ])
  (:require [ clojure.string :refer (upper-case)]))

;; The resources trick is from Chass Emerick. Thanks!
;; See http://code.google.com/p/language-detection/issues/detail?id=9

(set! *warn-on-reflection* true)

;; LANGUAGE DETECTORS

(defn get-lang-avail-cybozu 
  "Returns a set of language code strings recognized in Cybozu Labs library."
  []
    #{"af" "ar" "bg" "bn" "cs" "da" "de" "el" "en" "es" "et" "fa" "fi" "fr" "gu"
     "he" "hi" "hr" "hu" "id" "it" "ja" "kn" "ko" "lt" "lv" "mk" "ml" "mr" "ne"
     "nl" "no" "pa" "pl" "pt" "ro" "ru" "sk" "sl" "so" "sq" "sv" "sw" "ta" "te"
     "th" "tl" "tr" "uk" "ur" "vi" "zh-cn" "zh-tw"})

(defn- load-profile-cybozu [ ^java.util.List f ]
  (DetectorFactory/loadProfile f))
 
(def ^:private init-cybozu 
  (delay
   (->> (get-lang-avail-cybozu)
     (map (partial str "profiles/"))
     (map (comp slurp clojure.java.io/resource))
     load-profile-cybozu)))

(defn detect-lang-cybozu
  "Returns language code string for `s` using the Cybozu Labs library.
   The optional `env` parameter can contain the following optional keys:
   - `:alpha`   - alpha parameter of the Cybozu algorithm,
   - `:max-len` - maximum length of `s` to be taken for lang detection."
  ([ ^String s env ]
    (let [
        _ @init-cybozu
        detector (DetectorFactory/create)
        ]
        (do
          (when (:alpha env)
             (. detector setAlpha (:alpha env)))
          (when (:max-len env)
             (. detector setMaxTextLength (:max-len env)))
          (. detector append s)
          (. detector detect))))

  ([ ^String s ]
     (detect-lang-cybozu  s {})))

(defn detect-all-lang-prob-cybozu 
  "Returns multiple language code strings for `s` and their probabilities
   according to the Cybozu Labs library. The result is a map 
   { lang1 prob1, lang2 prob2, ... }.
   Optional `env` parameter can contain the following optional keys:
   - `:alpha`   - alpha parameter of the Cybozu algorithm,
   - `:max-len` - maximum length of `s` to be taken for lang detection."
  ([ ^String s env ]
     (let [
           _ @init-cybozu
           detector (DetectorFactory/create)
           _ 
             (do
               (when (:alpha env)
                 (. detector setAlpha (:alpha env)))
               (when (:max-len env)
                 (. detector setMaxTextLength (:max-len env)))
               (. detector append s))

           lang-object-list 
             (. detector getProbabilities)
           langs
             (map #(. ^Language % lang) lang-object-list)
           probs
             (map #(. ^Language % prob) lang-object-list)
        ]
       (zipmap langs probs)))

  ([ ^String s ]
     (detect-all-lang-prob-cybozu s {})))

(defn get-lang-avail-tika 
  "Returns a set of language code strings recognized by Tika."
  []
  (into #{} (LanguageIdentifier/getSupportedLanguages)))

(defn detect-lang-tika 
  "Returns language code string for `s` obtained using Apache Tika."
  [ ^String s ]
  (let [
         li (new LanguageIdentifier s)
        ]
    (. li getLanguage)))

(defn detect-lang-prob-tika 
  "Returns a map { lang prob } where `lang` is language code string for `s`.
   `prob` represents confidence in the detection.  
  Because library offers only a boolean function `isReasonablyCertain()`
  there are only values 0.0 (not certain) and (1.0) certain.

  WARNING
  The probability is **very conservative**. According to the apidocs for short 
  texts it always gives uncertain. Even on long English texts I could not 
  find any example for which it returns `certain` (Tika 1.4)."
  [ ^String s ]
    (let [
         li (new LanguageIdentifier s)
         lang (. li getLanguage)
         certain (. li isReasonablyCertain)
         prob (if certain 1.0 0.0)
        ]
    {lang prob}))

(defn get-lang-avail-icu 
  "Returns a set of language code strings recognized by ICU library.
   No function to get actual available language list is present in ICU4j.
   This was obtained by running grep in the com.ibm.icu sources:

   grep -h -o -e '\"[a-z][a-z]\"' Charset*.java  | sort | uniq"  
  []
  #{ "ar" "cs" "da" "de" "el" "en" "es" "fr" "he" "hu" "it" "ja" "ko"
     "nl" "no" "pl" "pt" "ro" "ru"  "sv" "tr" "zh"})

(defn detect-lang-icu [ ^String s ]
  (EncodDetectorTools/detectLangICU s))

;; ENCODING DETECTORS

(defn get-encod-avail-unichardet 
  "Returns a set of string identifiers for encodings available in 
   encoding detection library juniversalchardet."
  [] 
  (into #{} (EncodDetectorTools/getEncodAvailUnichardet)))
 
(defn detect-encod-unichardet 
  "Returns a string identifier of encoding for file `fname` as
   detected by juniversalchardet."
  [ ^String fname ]
  (EncodDetectorTools/detectEncodUnichardet fname))

(defn get-encod-avail-icu 
  "Returns a set of string identifiers for encodings available in 
   encoding detection tools of ICU4j."
  [] 
  (into #{} 
    (map upper-case (CharsetDetector/getAllDetectableCharsets))))

(defn detect-encod-icu 
  "Returns a string identifier of encoding for file `fname` as
   detected by ICU4j."
  [ ^String fname ]
  (EncodDetectorTools/detectEncodICU fname))

(defn detect-encod-prob-icu 
  "Returns a string identifier of encoding for file `fname` as
   detected by ICU4j together with the confidence in the detection
   (double from [0.0.1.0]). The result is a map { encod prob }."
  [ ^String fname ]
  (EncodDetectorTools/detectEncodProbICU fname))

(defn detect-all-encod-prob-icu 
  "Returns multiple string identifiers of encodings for file `fname` as
   detected by ICU4j.  For each encoding the confidence in the detection
   is provided (double from [0.0.1.0]).  The result is a map 
   { encod1 prob1, encod2 prob2, ... }."
  [ ^String fname ]
  (EncodDetectorTools/detectAllEncodProbICU fname))
