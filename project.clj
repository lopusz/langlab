(defproject langlab "1.3.0"

  ; GENERAL OPTIONS

  :description "langlab library"
  :url "http://github.com/lopusz/langlab"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.0.0"

  :aot :all
  :omit-source true

  :global-vars {*warn-on-reflection* true}

  ;; Options used by Java
  ;;; run with assertions enabled
  :jvm-opts ["-ea"]
  ;;; lint report on on java code (sometimes maybe useful)
  ;;; :javac-options [ "-Xlint"]

  ; DEPENDENCIES

  :dependencies [
    [org.clojure/clojure "1.6.0"]
    [org.clojure/math.numeric-tower "0.0.4"]

    ;; Runtime assertions
    [pjstadig/assertions "0.1.0"]

    ;; ICU - various tools used in parsers, lang+encoding detectors
    [com.ibm.icu/icu4j "53.1"]

    ;; language detectors
    [com.cybozu.labs/langdetect "1.1-20120112"]
    [org.apache.tika/tika-core "1.5"]

    ;; stemmers family + useful analysis utilities
    [org.apache.lucene/lucene-analyzers-common "5.2.1"]
    [org.apache.lucene/lucene-analyzers-stempel "5.2.1"]

    ;; Polish multistemmers
    [org.carrot2/morfologik-stemming "1.9.0"]
    [org.carrot2/morfologik-polish "1.9.0"]

    ;; Morpha stemmer
    [edu.washington.cs.knowitall/morpha-stemmer "1.0.5"]

    ;; encoding detector based on Mozilla algorithm
    [com.googlecode.juniversalchardet/juniversalchardet "1.0.3"]

    ;; fuzzy string similarity
    [org.apache.commons/commons-lang3 "3.3.2"]

    ;; OpenNLP Clojure wrappers
    [clojure-opennlp "0.3.2"]

    ;; Hunspell stemmer of Lucene
    [hunspell-stemmer "0.3.0"]

    ;; Additional language tools
    [clef-tools "0.2.0"]
    ]

  ; SOURCE DIRECTORY RECONFIGURATION

  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths [ "src/test/clojure"]

  ; PLUGINS + CONFIGURATION

  :plugins [
             [codox "0.8.12"]
             [lein-ancient "0.5.5"]
           ]

  ;; codox configuration

  :codox {
          :output-dir "target/apidoc"
          :sources [ "src/main/clojure"]
          :defaults {:doc/format :markdown}
          :src-dir-uri "http://github.com/lopusz/langlab/blob/master/"
          :src-linenum-anchor-prefix "L"
          }
)
