(ns gearer.core
  (:use compojure.core, ring.adapter.jetty)
  (:require [compojure.route :as route]
	    [gearer.views :as views]))

(defroutes main-routes
  (GET "/" [] (views/index))
  (GET "/:domain/:realm/:name/" [domain realm name]
       (views/character domain realm name))
  (route/resources "/" {:root "web"})
  (route/not-found "<h1>Page not found</h1>"))