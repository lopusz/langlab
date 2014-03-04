(ns langlab.algs.tagging-test
  (:require
    [ clojure.test :refer :all ]
    [ langlab.core.stemmers :refer [en-stem-snowball]]
    [ langlab.core.transformers :refer
       [split-tokens-with-space merge-tokens-with-space trans-drop-punct-lower]]
    [ langlab.core.parsers :refer [lg-split-tokens-bi lg-split-sentences-bi]]
    [ langlab.core.stopwords :refer [trans-drop-set  en-get-articles]]
    [ langlab.algs.tagging :refer :all]))

; TEST READING TAGS DICTIONARY FUNCTIONS

(def ^:private env-gen-dict
 { :split-tokens-f split-tokens-with-space
   :trans-tokens-f trans-drop-punct-lower
   :stem-f en-stem-snowball
   :merge-tokens merge-tokens-with-space})

(deftest test-gen-dict-from-seq
  (is (=
        (gen-dict-from-seq [ ] env-gen-dict)
        { }))
  (is (=
        (gen-dict-from-seq [ "cars" "floors" ] env-gen-dict)
        { "car" "cars" "floor" "floors"}))
  (is (=
        (gen-dict-from-seq [ "big cars" "first floors"] env-gen-dict)
        { "big car" "big cars" "first floor" "first floors"})))

(deftest test-fdict-from-seq
  (is (=
        (gen-fdict-from-seq [ ] env-gen-dict)
        { }))
  (is (=
        (gen-fdict-from-seq
          [ "awesome cars" "first floors" "awesome  car" "first  floor" ]
          env-gen-dict)
        { "awesom car" { "awesome cars" 1 "awesome car" 1 }
          "first floor" { "first floors" 1 "first floor" 1 }})))

(deftest test-get-min-tokens-in-dict
  (let [
        test-dict {
                   "big car" "big cars"
                   "first floor" "first floors"
                   "rise and fall" "rise and fall"
                   "darkness" "dark"
                   "really nice and long film" "really nice and long film" }
        ]

    (is (=
         (get-min-tokens-in-dict test-dict)
         1))
    (is (=
         (get-min-tokens-in-dict (assoc test-dict " " " "))
         0))

    (is (=
         (get-min-tokens-in-dict { })
         nil))))

(deftest test-get-max-tokens-in-dict
  (let [
        test-dict {
                   "big car" "big cars"
                   "first floor" "first floors"
                   "rise and fall" "rise and fall"
                   "darkness" "dark"
                   "really nice and long film" "really nice and long film" }
        ]
    (is (=
         (get-max-tokens-in-dict test-dict)
         5))
    (is (=
         (get-max-tokens-in-dict {})
         nil))))

; TEST TAGGING FUNCTIONS

(deftest test-make-tag-f
  (let [
        s "Old school method for generative intelligence was reviewed.
           The general outcome is that old school methods works great."
        env  { :stem-f en-stem-snowball
               :split-tokens-f #(lg-split-tokens-bi "en" %)
               :trans-tokens-f trans-drop-punct-lower }
        dict (gen-dict-from-seq
                 [ "old school" "generative intelligence" "old school method" ]
                 env)
        tag-f (make-tag-f dict env)
        ]
    (is (=
         (tag-f "")
         {}))

    (is (=
         (tag-f "Empty, empty world.")
         {}))
    (is (=
         (tag-f s)
         { "generative intelligence" 1
           "old school" 2
           "old school method" 2 } ))))

(deftest test-make-tag-f-with-sentences
  (let [
        s
          "He was devoted to the old school.
           His approach belonged to the old school.
           Method of describing generative intelligence, more general than
           this one, was proposed recently."
        env-no-sentences-split
          { :stem-f en-stem-snowball
            :split-tokens-f #(lg-split-tokens-bi "en" %)
            :trans-tokens-f trans-drop-punct-lower }
        env-with-sentences-split
          (assoc env-no-sentences-split
            :split-sentences-f #(lg-split-sentences-bi "en" %))
        dict
          (gen-dict-from-seq
            [ "old school" "generative intelligence" "old school method" ]
            env-no-sentences-split)
        tag-no-sentences-split-f
          (make-tag-f dict env-no-sentences-split)
        tag-with-sentences-split-f
          (make-tag-f dict env-with-sentences-split)
        ]
    (is (=
          (tag-no-sentences-split-f s)
          { "generative intelligence" 1
            "old school" 2
            "old school method" 1 } ))
    (is (=
          (tag-with-sentences-split-f "")
          { }))
    (is (=
          (tag-with-sentences-split-f "Empty, empty world.")
          { } ))
    (is (=
          (tag-with-sentences-split-f s)
          { "generative intelligence" 1
            "old school" 2 }))))

; TEST TAGS ALGEBRA FUNCTIONS (UNION/DIFFERENCE/INTERSECTION)

(def ^:private env-tag-algebra
  { :split-tokens-f split-tokens-with-space
    :stem-f en-stem-snowball
    :trans-tokens-f
      (comp
        trans-drop-punct-lower
        #(trans-drop-set (en-get-articles) %))
    :merge-tokens-f merge-tokens-with-space })

(deftest test-calc-tag-intersection
  (let [
        tags1
            {
              "day"        2
              "old car"    7
              "black dog"  3
              "flying pig" 2
              "night"      1
            }
         tags2
             {
               "old cars"   3
               "white dog"  4
               "nightly"    3
             }
        ]
    (is (=
           { "old car" 7  "night" 1 }
           (calc-tags-intersection tags1 tags2 env-tag-algebra)))
    (is (=
           { }
           (calc-tags-intersection { } { } env-tag-algebra)))
    (is (=
           { }
           (calc-tags-intersection { } tags1 env-tag-algebra)))
    (is (=
           { }
           (calc-tags-intersection tags1 { } env-tag-algebra)))))

(deftest test-calc-tag-union
  (let [
        tags1
            {
              "day"        2
              "old car"    7
              "black dog"  3
              "flying pig" 2
            }
         tags2
             {
               "old cars"   3
               "white dog"  4
               "night"      1
             }
        ]
    (is (=
           { }
           (calc-tags-union { } { } env-tag-algebra)))
    (is (=
           tags1
           (calc-tags-union { } tags1 env-tag-algebra)))
    (is (=
           tags1
           (calc-tags-union tags1 { } env-tag-algebra)))
    (is (=
           { "day" 2 "black dog" 3 "old car" 7
             "flying pig" 2  "white dog" 4 "night" 1 }
           (calc-tags-union tags1 tags2 env-tag-algebra)))))

(deftest test-calc-tag-difference
  (let [
        tags1
            {
              "day"        2
              "old car"    7
              "black dog"  3
              "flying pig" 2
            }
         tags2
             {
               "old cars"   3
               "white dog"  4
               "night"      1
             }
        ]
    (is (=
           { }
           (calc-tags-difference { } { } env-tag-algebra)))
    (is (=
           tags1
           (calc-tags-difference tags1 { } env-tag-algebra)))
    (is (=
           { }
           (calc-tags-difference { } tags1 env-tag-algebra)))
    (is (=
           { "day" 2 "black dog" 3 "flying pig" 2 }
           (calc-tags-difference tags1 tags2 env-tag-algebra)))))
