(ns langlab.cmns.should)

(def should-enabled {{should-enabled}})

(defmacro should [ x ] ) 

(defmacro should-not [ x ] )

(defmacro should [ x ]
  (when should-enabled
    `(when-not ~x
       (throw (new AssertionError (str "Assert failed: " (pr-str '~x)))))))
  
(defmacro should-not [ x ]
    `(not (should ~x)))
