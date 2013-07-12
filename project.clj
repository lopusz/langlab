(defproject langlab-base "0.1.0-SNAPSHOT"

  ; GENERAL OPTIONS

  :description "langlab-base library"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ; DEPENDENCIES

  :dependencies [   
    [org.clojure/clojure "1.5.1"]
    ;; language detection
    [com.cybozu.labs/langdetect "1.1-20120112"]         
    ;; stemmers family + useful analysis utilities
    [org.apache.lucene/lucene-analyzers-common "4.3.1"]
    ;; Polish multistemming
    [org.carrot2/morfologik-stemming "1.6.0"]
    [org.carrot2/morfologik-polish "1.6.0"]
    ]

  ; SOURCE DIRECTORY RECONFIGURATION

  :source-paths ["src" "src/main/clojure"]
  :java-source-paths ["src/main/java"] 
  :test-paths [ "src/test/clojure"]
  
  ; PLUGINS + CONFIGURATION

  :plugins [[codox "0.6.4"]]

  :codox {
          :output-dir "target/apidoc"
          :sources [ "src/main/clojure"]
          ;; TODO Uncoment below before push to github
          ;; :src-dir-uri "http://github.com/lopusz/langlab-base/blob/master"
          ;; :src-linenum-anchor-prefix "L"
          }  
  )

