(ns langlab.core.parsers
  "Module contains tools for parsing text into sentences and words."
  (:import 
     [ org.apache.lucene.util Version]
     [ org.apache.lucene.analysis.core SimpleAnalyzer]
     [ langlab.jcore.parsers
             BreakIteratorWrapper ICUBreakIteratorWrapper LuceneTools])
  (:require [ clojure.string :refer (trim) ]
            [ opennlp.nlp :refer (make-tokenizer make-sentence-detector) ] 
            [ langlab.core.transformers  
                :refer (trans-drop-whitespace) ]
            ))

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
  (trans-drop-whitespace
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
  (iterator-seq (ICUBreakIteratorWrapper/getSentenceIterator s lang)))

(defn en-split-sentences-icu-bi 
  "Convenience function alias for `lg-split-sentences-icu-bi` for English."
  [ ^String s ]
  (lg-split-sentences-icu-bi "en" s))

(defn lg-split-tokens-icu-bi 
  "Split `s` into seq of words using ICU BreakIterator class. 
   Sets language to `lang`.
   _Note_: It is not clear to me how the locale is used by BreakIterator."
  [ ^String lang ^String s ]
  (trans-drop-whitespace
    (iterator-seq (ICUBreakIteratorWrapper/getWordIterator s lang))))

(defn en-split-tokens-icu-bi 
  "Convenience function alias for `lg-split-tokens-icu-bi` for English."
  [ ^String s ]
  (lg-split-tokens-icu-bi "en" s))

;; LUCENE BASED PARSERS

(defn split-tokens-simple-lucene 
  "Splits `s` on whitespace and removes punctuation.
   Splitter based on Simple Analyzer from Lucene."
  [ ^String s ]
  (LuceneTools/splitTokensWithAnalyzer 
     (new SimpleAnalyzer Version/LUCENE_43) s))

;; ONLP BASED SPLITTER FACTORIES 

(defn make-split-tokens-onlp
  "Creates Open NLP token splitter using model from file `model-fname`."
  [ model-fname ]
  (make-tokenizer model-fname))

(defn make-split-sentences-onlp
  "Creates Open NLP sentence splitter using model from `model-fname`."
  [ model-fname ]
  (make-sentence-detector model-fname))
