(defproject langlab "0.1.0-SNAPSHOT"

  ; GENERAL OPTIONS

  :description "langlab library"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aot :all 

  ; DEPENDENCIES

  :dependencies [   
    [org.clojure/clojure "1.5.1"]
    [org.clojure/math.numeric-tower "0.0.2"]
    ;; language detection
    [com.cybozu.labs/langdetect "1.1-20120112"]         
    ;; stemmers family + useful analysis utilities
    [org.apache.lucene/lucene-analyzers-common "4.3.1"]
    ;; Polish multistemming
    [org.carrot2/morfologik-stemming "1.6.0"]
    [org.carrot2/morfologik-polish "1.6.0"]
    ;; ICU for improved parsing (BreakIterator)
    [com.ibm.icu/icu4j "51.1"]
    
    ;; Encoding detector
    [com.googlecode.juniversalchardet/juniversalchardet "1.0.3"]
    
    ;; OpenNLP Clojure wrappers
    [clojure-opennlp "0.3.1"]
    ]

  ; SOURCE DIRECTORY RECONFIGURATION

  :source-paths ["src" "src/main/clojure"]
  :java-source-paths ["src/main/java"] 
  :test-paths [ "src/test/clojure"]
  
  ; PLUGINS + CONFIGURATION

  :plugins [ [codox "0.6.4"]
             [lein-resource "0.3.1"] ]

  ;; codox configuration 

  :codox {
          :output-dir "target/apidoc"
          :sources [ "src/main/clojure"]
          ;; TODO Uncoment below before push to github
          ;; :src-dir-uri "http://github.com/lopusz/langlab/blob/master"
          ;; :src-linenum-anchor-prefix "L"
          }

  ;; resource-plugin configuration
  :hooks [ leiningen.resource ]

  :resource {
    :resource-paths [ "src-templates" ] 
    :target-path "src" 
    :extra-values { :must-enabled "false"} }
    
  ; PROFILES

  :profiles {
    :debug {
        :resource { :extra-values { :must-enabled "true" } }}
   }
  )

