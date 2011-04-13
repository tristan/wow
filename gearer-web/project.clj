(defproject gearer "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
		 [compojure "0.6.2"]
		 [clj-http "0.1.1"]
		 [clj-zpt "0.0.1-SNAPSHOT"]]
  :dev-dependencies [[swank-clojure "1.2.0"]
		     [lein-ring "0.4.0"]]
  :ring {:handler gearer.core/main-routes}
  )