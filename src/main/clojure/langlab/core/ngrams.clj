(ns langlab.core.ngrams
  "Module contains n-gram generation function.")

(defn gen-ngrams
  "Function generates n-grams from a given 'tokens' sequence.

   Two invocations are possible:
   (gen-n-grams n tokens) <- generates all n-grams
   (gen-n-grams n m tokens) <- generates all n-grams,(n+1)-grams,...,(m)-grams
  "
  ([ n tokens ]
    (let [
          tokens-len (count tokens)
          tokens-rests (take (+ (- tokens-len n) 1)
                         (iterate rest tokens))
          ngrams-seq (for [ c tokens-rests ] (take n c))
         ]
      ngrams-seq))
  ([ n m tokens ]
    (let [
          tokens-len (count tokens)
          m* (min tokens-len m)
          n-range (range n (+ m* 1))
          gen-coll-ngrams #(gen-ngrams % tokens)
         ]
      (mapcat gen-coll-ngrams n-range))))
