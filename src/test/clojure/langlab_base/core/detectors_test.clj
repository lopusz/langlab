(ns langlab-base.core.detectors-test
  (:require 
    [ clojure.test :refer :all ]
    [ langlab-base.core.detectors :refer :all]
    [ langlab-base.cmns.coll :refer (keys-sorted-by-val-desc)]))

(def test-data-pl-en
  (str "Żółw nad żółtą rzeką szedł." 
       "A test or examination is an assessment intended to " 
       "measure a test-taker's knowledge."))
  
(deftest detect-lang-cybozu-test 
  (is (=
          "pl"
          (detect-lang-cybozu  test-data-pl-en  {:max-len 20} )))
  (is (=
          "en"
          (detect-lang-cybozu  test-data-pl-en))))

;; Warning! It seems that 'detect-lang-prob-cybozu' is non-deterministic
;; (i.e., it uses some kind of random number generator), so be careful when
;; performing tests.

(deftest detect-lang-prob-cybozu-test
  (is (=
          "pl"
          (->>
            (detect-lang-prob-cybozu  test-data-pl-en  {:max-len 20 } )
            (keys-sorted-by-val-desc)
            (first))))
  (is (=
          "en"
          (->>
            (detect-lang-prob-cybozu  test-data-pl-en )
            (keys-sorted-by-val-desc)
            (first)))))
