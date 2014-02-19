(ns langlab.core.ngrams
  "Module contains n-gram generation function.")

(defn gen-ngrams
  "Function generates n-grams from a given 'tokens' sequence.

   Following invocations are possible:
   (gen-ngrams n tokens) <- generates all n-grams
   (gen-ngrams n m tokens) <- generates all n-grams,(n+1)-grams,...,(m)-grams
   (gen-ngrams tokens) <- generates 1 .. (count tokens) n-grams
  "
  ([ tokens ]
     (gen-ngrams 1 (count tokens) tokens))

  ([ n tokens ]
    (partition n 1 tokens))

  ([ n m tokens ]
    (mapcat
      #(gen-ngrams % tokens)
      (range n (inc m)))))
