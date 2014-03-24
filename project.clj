(defproject langlab "1.0.0-SNAPSHOT"

  ; GENERAL OPTIONS

  :description "langlab library"
  :url "http://github.com/lopusz/langlab"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aot :all 
  :omit-source true

  ;; :global-vars {*warn-on-reflection* true}

  ;; Options used by Java 
  ;;; run with assertions enabled
  :jvm-opts ["-ea"]
  ;;; lint report on on java code (sometimes maybe useful)
  ;;; :javac-options [ "-Xlint"]
 
  ; DEPENDENCIES

  :dependencies [   
    [org.clojure/clojure "1.5.1"]
    [org.clojure/math.numeric-tower "0.0.2"]
 
    ;; Runtime assertions
    [pjstadig/assertions "0.1.0"]

    ;; ICU - various tools used in parsers, lang+encoding detectors
    [com.ibm.icu/icu4j "51.1"]
 
    ;; language detectors
    [com.cybozu.labs/langdetect "1.1-20120112"]         
    [org.apache.tika/tika-core "1.4"] 

    ;; stemmers family + useful analysis utilities
    [org.apache.lucene/lucene-analyzers-common "4.5.1"]
    [org.apache.lucene/lucene-analyzers-stempel "4.5.1"]
    
    ;; Polish multistemmers
    [org.carrot2/morfologik-stemming "1.6.0"]
    [org.carrot2/morfologik-polish "1.6.0"]

    ;; Morpha stemmer
    [edu.washington.cs.knowitall/morpha-stemmer "1.0.5"]

    ;; encoding detector based on mozilla algorithm
    [com.googlecode.juniversalchardet/juniversalchardet "1.0.3"]
    
    ;; OpenNLP Clojure wrappers
    [clojure-opennlp "0.3.1"]
    ]
  
  ; SOURCE DIRECTORY RECONFIGURATION

  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"] 
  :test-paths [ "src/test/clojure"]
  
  ; PLUGINS + CONFIGURATION

  :plugins [ [codox "0.6.6"] ]

  ;; codox configuration 

  :codox {
          :output-dir "target/apidoc"
          :sources [ "src/main/clojure"]
          :src-dir-uri "http://github.com/lopusz/langlab/blob/master/"
          :src-linenum-anchor-prefix "L"
          }
  )
