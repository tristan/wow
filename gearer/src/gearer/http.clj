(ns gearer.http
  (:import (org.apache.http HttpResponse))
  (:import (org.apache.http.impl.client DefaultHttpClient))
  (:import (org.apache.http.client.methods HttpGet))
  (:require
   [clj-http.client :as c]
   [clojure.contrib.json :as json]
   [clojure.contrib.str-utils :as re]))

(defn get-url 
  ([url] (get-url url {}))
  ([url ops]
     (try 
      (let [response (c/get url ops)]
	(response :body))
      (catch java.lang.Exception e
	(println url (.getMessage e))
	nil))))

(defn get-xml [url]
  (let [response (c/get url)]
    (let [xmlstream (java.io.ByteArrayInputStream. (.getBytes (response :body)))
	  xmlmap (clojure.xml/parse xmlstream)]
      xmlmap)))

(defn get-json [url]
  (json/read-json (or (get-url url) "{}")))

(defn put-json [url j]
  (let [r (c/put url {:body (json/json-str j)})]
    (try
     (json/read-json (r :body))
     (catch Exception e
       {}))))

(defn post-json [url j]
  (let [r (c/post url {:body (json/json-str j)
		       :content-type :json})]
    (try
     (json/read-json (r :body))
     (catch Exception e
       {}))))

(defn get-file [url]
  (get-url url {:as :byte-array}))

(defn process-stream [url callback]
  (let [httpclient (DefaultHttpClient.)
	httpget (HttpGet. url)
	response (. httpclient execute httpget)
	entity (. response getEntity)]
    (with-open [content (clojure.java.io/reader (. entity getContent))]
      (loop []
	(try
	 (let [line (.readLine content)]
	   (if (not (nil? line))
	     (do
	       (when (not (= line ""))
		 (callback (json/read-json line)))
	       (recur))
	     (println "connection dropped")))
	 (catch java.lang.Exception e
	   (println "error")
	   (. e printStackTrace)))))))