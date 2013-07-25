(ns langlab-base.core.characters
  "Module includes string utilities operating on characters.

   This includes, e.g., diacritics removal,  vowel groups detection, etc."
  (:import [ langlab.base.core.characters CharacterTools StringTools ]))

(defn remove-diacritics
  "Remove diacritical marks from the string `s`, E.g., 'żółw' is transformed
   to 'zolw'."
  [ ^String s ]
  (StringTools/removeDiacritics s))

(defn lg-count-chars-bi [ ^String lang ^String s ]
  (StringTools/countCharactersBreakIterator s lang))

(defn en-count-chars-bi
  "Counts number of characters in `s` using Break Iterator.
   Uses English locale."
  [ ^String s ]
  (StringTools/countCharactersBreakIterator s "en"))

(defn lg-count-chars-icu-bi
  "Counts number of characters in `s` using Break Iterator.
   Uses locale corresponding to `lang`."
  [ ^String lang ^String s ]
  (StringTools/countCharactersICUBreakIterator s lang))

(defn en-count-chars-icu-bi
  "Counts number of characters in `s` using ICU Break Iterator.
   Uses English locale."
  [ ^String s ]
  (StringTools/countCharactersICUBreakIterator s "en"))

(defn count-latin-vowel-groups
  "Counts groups of latin vowels in the string `s`, e.g. for 'employee'
   it should return 2."
  [ ^String s ]
  (-> s
      (StringTools/removeDiacritics)
      (StringTools/countLatinVowelGroups)))

(defn count-latin-vowel-groups-without-final
  "Counts groups of latin vowels in string `s` without the group ending
   the word, e.g. for 'employee' it should return 1."
  [ ^String s ]
  (-> s
      (StringTools/removeDiacritics)
      (StringTools/countLatinVowelGroupsWithoutFinal)))

(defn contains-whitespace?
  "Checks if `s` contains whitespace according to `Character.isWhitespace(cp)`.
   Some intuitivelly whitespace characters from Unicode are excluded
   (e.g., hard spaces). See tests."
  [ ^String s ]
  (StringTools/containsWhitespace s))

(defn contains-whitespace-only?
  "Checks if `s` contains *only* whitespace according to
   `Character.isWhitespace(cp)`.
   Be warned that some intuitivelly whitespace characters from Unicode are
   excluded (e.g., hard spaces). See tests."
  [ ^String s ]
  (StringTools/containsWhitespaceOnly s))

(defn contains-punct?
  "Checks if `s` contains punctuation according to `Character.getType(cp)`
   equal to *_PUNCTUATION classes."
  [ ^String s ]
  (StringTools/containsPunct s))

(defn contains-punct-only?
  "Checks if `s` contains *only* punctuation according to
   `Character.getType(cp)` equal to *_PUNCTUATION classes."
  [ ^String s ]
  (StringTools/containsPunctOnly s))

(defn contains-non-bmp?
  "Checks if `s` contains non-bmp characters according to
   `!Character.isBmpCodePoint(cp)`."
  [ ^String s ]
  (StringTools/containsNonBmp s))

(defn remove-non-bmp
  "Removes all non-bmp codepoints from `s`."
  [ ^String s ]
  (StringTools/removeNonBmp s))

(defn remove-bmp
  "Removes all bmp codepoints from `s`."
  [ ^String s ]
  (StringTools/removeBmp s))
