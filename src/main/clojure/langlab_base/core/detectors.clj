(ns langlab-base.core.detectors
  "Module includes language detection utilities."
  (:import [ com.cybozu.labs.langdetect Detector DetectorFactory ]))

;; The resources trick is from Chass Emerick. Thanks!
;; See http://code.google.com/p/language-detection/issues/detail?id=9

(def ^:private init-cybozu 
  (delay
   (->> 
    #{"af" "ar" "bg" "bn" "cs" "da" "de" "el" "en" "es" "et" "fa" "fi" "fr" "gu"
     "he" "hi" "hr" "hu" "id" "it" "ja" "kn" "ko" "lt" "lv" "mk" "ml" "mr" "ne"
     "nl" "no" "pa" "pl" "pt" "ro" "ru" "sk" "sl" "so" "sq" "sv" "sw" "ta" "te"
     "th" "tl" "tr" "uk" "ur" "vi" "zh-cn" "zh-tw"}
     (map (partial str "profiles/"))
     (map (comp slurp clojure.java.io/resource))
     com.cybozu.labs.langdetect.DetectorFactory/loadProfile)))

(defn detect-lang-cybozu
  "Detects language of string `s` using the cybozu labs library.
   The optional `env` parameter can contain the following optional keys:
   `:alpha`   - alpha parameter of the cybozu alogirthm,
   `:max-len` - maximum length of `s` to be taken for lang detection."
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
   cybozu labs library. The result is a map { lang_id prob }.
   The optional `env` parameter can contain the following optional keys:
   `:alpha`   - alpha parameter of the cybozu alogirthm,
   `:max-len` - maximum length of `s` to be taken for lang detection."
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
