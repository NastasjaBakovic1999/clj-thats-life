(ns thats-life.core
  (:require [reagent.core :as core]
            [thats-life.logic :as logic]))

(defonce game-state
  (reagent/atom core/init-players :validator core/is-valid?))

(def pawn-imgs [
                "images/guard.svg"
                "images/pawn-black.svg"
                "images/pawn-blue.svg"
                "images/pawn-green.svg"
                "images/pawn-orange.svg"
                "images/pawn-red.svg"
                "images/pawn-yellow.svg" ])

(defn pawn-render [n]
  [:pawn.img {:src (get pawn-imgs (inc n))}])

(defn game-render []
  (let [state @game-state]
    [:div 
     [:h1 "That's life"]]))

;;The onbeforeunload event occurs when a document is about to be unloaded.
(aset js/window "onbeforeunload" 
      (constantly "Do you really want to leave the game unfinished? Then you must lose!"))

(reagent/render [game-render]
                (js/document.querySelector "#game"))