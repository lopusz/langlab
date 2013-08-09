(ns langlab.core.detectors
  "Module contains language detection utilities."
  (:import [ com.cybozu.labs.langdetect Detector DetectorFactory ]
           [ com.ibm.icu.text CharsetDetector ]
           [ org.apache.tika.language LanguageIdentifier ]
           [ langlab.jcore.detectors EncodDetectorTools ])
  (:require [ clojure.string :refer (upper-case)]))

;; The resources trick is from Chass Emerick. Thanks!
;; See http://code.google.com/p/language-detection/issues/detail?id=9

;; LANGUAGE DETECTORS

(defn get-lang-avail-cybozu 
  "Returns the set of available languages in Cybozu Labs library."
  []
    #{"af" "ar" "bg" "bn" "cs" "da" "de" "el" "en" "es" "et" "fa" "fi" "fr" "gu"
     "he" "hi" "hr" "hu" "id" "it" "ja" "kn" "ko" "lt" "lv" "mk" "ml" "mr" "ne"
     "nl" "no" "pa" "pl" "pt" "ro" "ru" "sk" "sl" "so" "sq" "sv" "sw" "ta" "te"
     "th" "tl" "tr" "uk" "ur" "vi" "zh-cn" "zh-tw"})

(def ^:private init-cybozu 
  (delay
   (->> (get-lang-avail-cybozu)
     (map (partial str "profiles/"))
     (map (comp slurp clojure.java.io/resource))
     com.cybozu.labs.langdetect.DetectorFactory/loadProfile)))

(defn detect-lang-cybozu
  "Detects language of string `s` using the Cybozu Labs library.
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
  "Returns the most probable languages of string `s` according to the
   Cybozu Labs library. The result is a map { lang_id prob }.
   The optional `env` parameter can contain the following optional keys:
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
             (map #(. % lang) lang-object-list)
           probs
             (map #(. % prob) lang-object-list)
        ]
       (zipmap langs probs)))

  ([ ^String s ]
     (detect-all-lang-prob-cybozu s {})))

(defn get-lang-avail-tika []
  (into #{} (LanguageIdentifier/getSupportedLanguages)))

(defn detect-lang-tika [ ^String s ]
  (let [
         li (new LanguageIdentifier s)
        ]
    (. li getLanguage)))

(defn get-lang-avail-icu 
  "No function to get actual available language list is present in ICU lib.
   This was obtained by running grep in the com.ibm.icu sources:

   grep -h -o -e "\"[a-z][a-z]\"" Charset*.java  | sort | uniq 
  "  
  []
  #{ "ar" "cs" "da" "de" "el" "en" "es" "fr" "he" "hu" "it" "ja" "ko"
     "nl" "no" "pl" "pt" "ro" "ru"  "sv" "tr" "zh"})


(defn detect-lang-icu [ ^String s ]
  (EncodDetectorTools/detectLangICU s))

;; ENCODING DETECTORS

(defn get-encod-avail-unichardet [] 
  (into #{} (EncodDetectorTools/getEncodAvailUnichardet)))
 
(defn detect-encod-unichardet [ ^String fname ]
  (EncodDetectorTools/detectEncodUnichardet fname))

(defn get-encod-avail-icu [] 
  (into #{} 
    (map upper-case (CharsetDetector/getAllDetectableCharsets))))

(defn detect-encod-icu [ ^String fname ]
  (EncodDetectorTools/detectEncodICU fname))

(defn detect-encod-prob-icu [ ^String fname ]
  (EncodDetectorTools/detectEncodProbICU fname))

(defn detect-all-encod-prob-icu [ ^String fname ]
  (EncodDetectorTools/detectAllEncodProbICU fname))
