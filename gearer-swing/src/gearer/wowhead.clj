(ns gearer.wowhead
  (:require
   [clj-http.client :as c]
   [gearer.xml :as x]
   [clojure.contrib.string :as string]
   [clojure.contrib.str-utils :as re]))

(defn get-xml [url]
  (println url)
  (let [response (c/get url)
	reader (java.io.StringReader. (response :body))
	reader (if (re-find #"text/html"
			    (get-in response [:headers "content-type"]))
		 (let [w (java.io.StringWriter.)
		       t (doto (org.w3c.tidy.Tidy.)
			   (.setXHTML true)
			   (.parse reader w))]
		   (println "-" (.toString w) "-")
		   (java.io.StringReader. (.toString w)))
		 reader)
	is (org.xml.sax.InputSource. reader)
	]
    (. is setEncoding "UTF-8")
    (clojure.xml/parse is)))

(defn enchant [item-id]
  ; get xml
  (let [item-xml (get-xml (str "http://www.wowhead.com/item=" item-id "&xml"))
	name (last
	      (re/re-split #" - "
			   (or (x/select-node-content
				item-xml
				"wowhead/item/name") "")))
	spell-use (x/select-node-content
		   item-xml
		   "wowhead/item/jsonUse")
	spell-id (get-in (x/select-node
			  item-xml
			  "wowhead/item/createdBy/spell") [:attrs :id])
	effect-id (if (nil? spell-id)
		    nil
		    (let [spell-html (get-xml
				      (str "http://www.wowhead.com/spell="
					   spell-id))
			  ;effect (x/select-node
			;	  spell-html
			;	  "html/body/div/div/div/div/div/div/table/tbody/tr[5]/td")]
			  ](println spell-html)
		      ))]))
      
  ; find spell
  ; get spell html
  ; find effect
