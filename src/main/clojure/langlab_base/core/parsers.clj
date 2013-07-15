(ns langlab-base.core.parsers
  "Module contains tools for parsing text into sentences and words."
  (:import [ langlab.base.core.parsers
             BreakIteratorWrapper ICUBreakIteratorWrapper ])
  (:require [ clojure.string :refer (trim) ]
            [ langlab-base.core.transformers  
                :refer (trans-tokens-drop-whitespace) ]))

;  (:require 
;     [ opennlp nlp :refer (make-tokenizer make-sentence-detector)]))

(defn split-sentences-nosplit 
  "Returns one-element vector with `s` without splitting it into sentences."
  [s]
  [s])

;; BreakIterator BASED SPLITTERS

(defn lg-split-sentences-bi 
  "Split `s` into seq of sentences using standard BreakIterator class. 
   Sets language to `lang`.
   
   _Note_: It is not clear to me how the locale is used by BreakIterator."
  [ ^String lang ^String s ]
  (iterator-seq (BreakIteratorWrapper/getSentenceIterator s lang)))

(defn en-split-sentences-bi 
  "Convenience function alias for `lg-split-sentences-bi` for English."
  [ ^String s ]
  (lg-split-sentences-bi "en" s))

(defn lg-split-tokens-bi 
  "Split `s` into seq of words using standard BreakIterator class. 
   Sets language to `lang`.
   _Note_: It is not clear to me how the locale is used by BreakIterator."
  [ ^String lang ^String s ]
  (trans-tokens-drop-whitespace
    (iterator-seq (BreakIteratorWrapper/getWordIterator s lang))))

(defn en-split-tokens-bi 
  "Convenience function alias for `lg-split-tokens-bi` for English."
  [ ^String s ]
  (lg-split-tokens-bi "en" s))

;; ICU BreakIterator BASED SPLITTERS

(defn lg-split-sentences-icu-bi 
  "Split `s` into seq of sentences using ICU BreakIterator class. 
   Sets language to `lang`.
   
   _Note_: It is not clear to me how the locale is used by BreakIterator."

  [ ^String lang ^String s ]
  (map (iterator-seq (ICUBreakIteratorWrapper/getSentenceIterator s lang))))

(defn en-split-sentences-icu-bi 
  "Convenience function alias for `lg-split-sentences-icu-bi` for English."
  [ ^String s ]
  (lg-split-sentences-icu-bi "en" s))

(defn lg-split-tokens-icu-bi 
  "Split `s` into seq of words using ICU BreakIterator class. 
   Sets language to `lang`.
   _Note_: It is not clear to me how the locale is used by BreakIterator."
  [ ^String lang ^String s ]
  (trans-tokens-drop-whitespace
    (iterator-seq (ICUBreakIteratorWrapper/getWordIterator s lang))))

(defn en-split-tokens-icu-bi 
  "Convenience function alias for `lg-split-tokens-icu-bi` for English."
  [ ^String s ]
  (lg-split-tokens-bi "en" s))

;; ONLP BASED SPLITTER FACTORIES 

(comment

(defn make-split-tokens-onlp
  "Creates Open NLP token spliter using model from file `model-fname`."
  [ model-fname ]
  (make-tokenizer model-fname))

(defn make-split-sentences-onlp
  "Creates Open NLP sentence spliter using model from `model-fname`."
  [ model-fname ]
  (make-sentence-detector model-fname)))

(comment

(defn split-tokens-with-space [ s ]
  "Inverse of the merge-tokens-with-space."
  (split s #"\s+"))

(def  ^:private punct-chars "?!&-.,;'\"")

(def  ^:private punct-re
  (re-pattern (str "[" punct-chars "]*")))

(def  ^:private punct-split-re
  (re-pattern (str "[" punct-chars "]+|[^" punct-chars "]+")))

(defn is-punct-token? [ token ] 
  (not (nil? (re-matches punct-re token))))

(defn- split-punct-and-words 
  "Splits punctuation and words glued together.
   E.g., '!!!x,' ->  [ '!!!' 'x' ',']'"
  [ s ] 
  (re-seq punct-split-re s))

(defn split-tokens-regex [ s ] 
  "Splits string 's' into tokens (i.e. words and punctuation signs)."
  (mapcat
      split-punct-and-words
      (split s #"\s+"))))
