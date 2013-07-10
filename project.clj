(defproject langlab-base "0.1.0-SNAPSHOT"
  :description "langlab-base library"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ; DEPENDENCIES

  :dependencies [[org.clojure/clojure "1.5.1"]]

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

