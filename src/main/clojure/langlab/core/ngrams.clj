(ns langlab.core.ngrams
  "Module contains n-gram generation function.")

(defn gen-ngrams
  "Function generates n-grams from a given 'tokens' sequence.

   The following invocations are possible:

   - `(gen-n-grams n tokens)` - generates all n-grams
   - `(gen-n-grams n m tokens)` - generates all n-grams,(n+1)-grams,...,(m)-grams
   - `(gen-ngrams tokens)` - generates 1 .. (count tokens) n-grams
  "
  ([ tokens ] (gen-ngrams 1 (count tokens) tokens))
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

(comment

(defn gen-ngrams*
  "Function generates n-grams from a given 'tokens' sequence.

   Alternative slightly slower version (?).

   The following invocations are possible:

   - `(gen-ngrams n tokens)` - generates all n-grams
   - `(gen-ngrams n m tokens)` - generates all n-grams,(n+1)-grams,...,(m)-grams
   - `(gen-ngrams tokens)` - generates 1 .. (count tokens) n-grams
  "
  ([ tokens ]
     (gen-ngrams 1 (count tokens) tokens))

  ([ n tokens ]
    (partition n 1 tokens))

  ([ n m tokens ]
    (mapcat
      #(gen-ngrams % tokens)
      (range n (inc m))))))
