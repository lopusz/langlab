(ns langlab.core.ngrams
  "Module contains n-gram generation functions.")

(defn gen-ngrams [ tokens n ]
  (let [
        tokens-len (count tokens)
        tokens-rests (take (+ (- tokens-len n) 1)
                         (iterate rest tokens))
        ngrams-seq (for [ c tokens-rests ] (take n c))
        ]
    ngrams-seq))

(defn gen-ngrams-range [ tokens nmin nmax ]
  (let [
        tokens-len (count tokens)
        nmax* (min tokens-len nmax)
        n-range (range nmin (+ nmax* 1))
        gen-coll-ngrams (fn [n] (gen-ngrams tokens n))
        ]
  (mapcat gen-coll-ngrams n-range)))
