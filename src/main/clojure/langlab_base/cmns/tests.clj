(ns langlab-base.cmns.tests
  "Module includes test utilities."
  (:require
    [ clojure.test :refer (is) ]
    [ clojure.math.numeric-tower :refer (abs)] ))

(defn float-eq 
  "Return true if absolute value of a difference between `x` and `y` is 
   lower than parameter `tol`."
  [ tol x y ]
  (let [
          diff (float (- x y))
        ]
  (> tol (float  (abs diff)))))

(defn run-dict-test 
  "Runs test of function `f` based on map `arg-fval-map`
     { data1 result1 data2 result2 ... }
   There is an option of including customized equality operator  `eq-f`,
   useful, e.g., for floats."
  ([ f arg-fval-map ]
    (let [
          args (keys arg-fval-map)
          fvals (vals arg-fval-map)
          fvals* (map f args)
          seq-is= 
            (fn [seq1 seq2] 
               (map 
                 #(is (= %1 %2) (str %1 " not equal " %2)) 
                 seq1 seq2))
         ]
      (every? identity (seq-is= fvals fvals*))))

  ([ f arg-fval-map eq-f ]
     (let [
           args (keys arg-fval-map)
           fvals (vals arg-fval-map)
           fvals* (map f args)
           seq-is= 
             (fn [seq1 seq2] 
               (map 
                 #(is (eq-f %1 %2) (str  %1 " not equal " %2)) 
                 seq1 seq2))
         ]
      (every? identity (seq-is= fvals fvals*)))))
