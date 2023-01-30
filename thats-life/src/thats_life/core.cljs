(ns thats-life.core
  (:require [reagent.core :as core]
            [thats-life.logic :as logic]))

(defonce game-state
  (reagent/atom core/init-players :validator core/is-valid?))

;;The onbeforeunload event occurs when a document is about to be unloaded.
;;This event allows you to display a message in a confirmation dialog box to inform the user 
;;whether they wants to stay or leave the current page.
(aset js/window "onbeforeunload" 
      (constantly "Do you really want to leave the game unfinished? Then you must lose!"))