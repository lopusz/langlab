(ns langlab-base.core.characters
  "Module integrates utilities involving characters (e.g., diacritics removal,
   vowel groups detection, etc.)"
  (:import [ langlab.base.core.characters CharacterTools StringTools ]))

(defn remove-diacritics 
  "Remove diacritical marks from the string `s`, E.g., 'żółw' is transformed
   to 'zolw'."
  [ ^String s ]
  (StringTools/removeDiacritics s))

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
