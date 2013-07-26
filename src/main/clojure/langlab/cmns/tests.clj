(ns langlab.cmns.tests
  "Module contains test utilities."
  (:require
    [ clojure.test :refer (is) ]
    [ clojure.math.numeric-tower :refer (abs)] 
    [ langlab.cmns.colls :refer (spprint-noeol) ]))

(defn float-eq 
  "Return true if absolute value of a difference between `x` and `y` is 
   lower than parameter `tol`."
  [ tol x y ]
  (let [
          diff (float (- x y))
        ]
  (> tol (float  (abs diff)))))

(defn is-eq-dict
  "Runs test of function `f` based on map `arg-fval-map`
     { data1 result1 data2 result2 ... }
   There is an option of including customized equality operator  `eq-f`,
   useful, e.g., for floats."
  ([ f arg-fval-map ]
     (is-eq-dict f arg-fval-map #(= %1 %2)))

  ([ f arg-fval-map eq-f ]
     (let [
           args (keys arg-fval-map)
           fvals (vals arg-fval-map)
           fvals* (map f args)
           seq-is= 
             (fn [seq1 seq2] 
               (doall (map 
                 #(is (eq-f %1 %2) (str  (spprint-noeol %1) " not equal " 
                                         (spprint-noeol %2))) 
                 seq1 seq2)))
         ]
      (seq-is= fvals fvals*))))

(defn is-NOT-eq-dict
  "Runs anti-test of function `f` based on map `arg-fval-map`
     { data1 result1 data2 result2 ... }
   The results should NOT be equal to those provided in map.
   This is useful to document known deficiencies."
   [ f arg-fval-map ]
     (is-eq-dict f arg-fval-map #(not= %1 %2)))
