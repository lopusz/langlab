(ns langlab.core.comparators
  "Module contains various tools for fuzzy strings comparison."
  (:import [ org.apache.commons.lang3 StringUtils ]
           [ langlab.jcore.comparators ComparatorTools]))

(defn calc-levenshtein-dist-aclang
   "Calculates the Levenshtein distance between `s1` and `s2`.
    It uses Appache Commons Lang3 (aclang)."
   [ ^String s1 ^String s2 ]
   (StringUtils/getLevenshteinDistance s1 s2))

(defn calc-trunc-levenshtein-dist-aclang
   "Calculates the Levenshtein distance between `s1` and `s2`,
    only if it is lower or equal to `truncation`.
    If the Levenshtein distance is greater `false` is returned.
    It uses Appache Commons Lang3 (aclang)."
  [ ^String s1 ^String s2 ^Integer truncation]
  (let [
         dist (StringUtils/getLevenshteinDistance s1 s2 truncation)
       ]
    (if (= dist -1)
      false
      dist)))

(defn calc-jaro-winkler-dist-aclang
  "Computes the Jaro-Winkler distance between `s1` and `s2`

   http://en.wikipedia.org/wiki/Jaro-Winkler_distance

   It uses Appache Commons Lang3 (aclang)."
  [ ^String s1 ^String s2]
   (StringUtils/getJaroWinklerDistance s1 s2))

(defn calc-common-prefix-length [ ^String s1 ^String s2 ]
  (ComparatorTools/calcCommonPrefixLength s1 s2))

; Add here metaphone and soundex comparisons form commons-codec
; http://en.wikipedia.org/wiki/Soundex
; http://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Soundex.html
; http://en.wikipedia.org/wiki/Metaphone
; http://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Metaphone.html
