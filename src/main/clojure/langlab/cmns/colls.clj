(ns langlab.cmns.colls
  "Module contains various collection utilities.")

;; This does not follow the rule of verb in function name.
;; This is analogy for function keys.

(defn keys-sorted-by-val-asc 
  "Returns keys of map `m` sorted according to values in ascending order."
  [ m ]
  (sort-by m #(compare %1 %2) (keys m)))

(defn keys-sorted-by-val-desc 
  "Returns keys of map `m` sorted according to values in descending order."
  [ m ]
  (sort-by m #(compare %2 %1) (keys m)))

(defn keys-sorted-asc
  "Returns keys of map `m` sorted in ascending order."
  [ m ]
  (sort (keys m)))

(defn keys-sorted-desc 
  "Returns keys of map `m` sorted in descending order."
  [ m ]
  (sort #(compare %2 %1) (keys m)))
