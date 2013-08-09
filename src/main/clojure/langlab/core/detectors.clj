(ns langlab.core.detectors
  "Module contains language detection utilities."
  (:import [ com.cybozu.labs.langdetect Detector DetectorFactory ]
           [ langlab.jcore.detectors EncodDetectorTools ]))

;; The resources trick is from Chass Emerick. Thanks!
;; See http://code.google.com/p/language-detection/issues/detail?id=9

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

(defn detect-lang-prob-cybozu 
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
     (detect-lang-prob-cybozu s {})))

(defn detect-lang-tika []
  )

(defn get-encod-avail-unichardet [] 
  (EncodDetectorTools/getEncodAvailUnichardet))
 
(defn detect-encod-unichardet [ ^String fname ]
  (EncodDetectorTools/detectEncodUnichardet fname))

(defn detect-encod-icu [ ^String fname ]
  )