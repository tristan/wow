(defproject gearer "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
;		 [http.async.client "0.2.1"]
;		 [clj-http "0.1.1"]
; CLJ-HTTP specific
     [org.apache.httpcomponents/httpclient "4.0.3"]
     [commons-codec "1.4"]
     [commons-io "1.4"]
     ]
  :dev-dependencies [[swank-clojure "1.2.0"]
		     ; CLJ-HTTP specific
		     [ring/ring-jetty-adapter "0.3.5"]
		     [ring/ring-devel "0.3.5"]])