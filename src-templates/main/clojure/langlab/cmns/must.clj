(ns langlab.cmns.must)

(def must-enabled {{must-enabled}})

(defmacro must [ x ]
  (when must-enabled
    `(when-not ~x
       (throw (new AssertionError (str "Assert failed: " (pr-str '~x)))))))
  
(defmacro must-not [ x ]
    `(not (should ~x)))
