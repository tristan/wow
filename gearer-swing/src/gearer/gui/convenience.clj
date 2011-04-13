(ns gearer.gui.convenience)

(defmacro with-action [component event & body]
  `(. ~component addActionListener
      (proxy [java.awt.event.ActionListener] []
        (actionPerformed [#^java.awt.event.ActionEvent ~event] ~@body))))