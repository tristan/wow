(ns gearer.utils
  (:import
   (javax.swing ImageIcon
		JFrame)
   (java.awt Image)
   (java.awt.image CropImageFilter
		   FilteredImageSource)))

(defn crop-image [#^ImageIcon image x y w h]
  (ImageIcon.
   (.createImage
    (JFrame.)
    (FilteredImageSource.
     (.. image getImage getSource)
     (CropImageFilter. x y w h)))))

(defn get-resource [#^String name]
  (.getResource (.getContextClassLoader (Thread/currentThread)) name))