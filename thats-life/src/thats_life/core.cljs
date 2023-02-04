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

(defn pawns-render [pawns up from robot]
  (let [guards-move (get (set pawns) up)]
    [:ol (map 
       (fn [n] 
          (let [mobile (and (not bot) (or (= n up) (and guards-move (core/guard? n))))]
             [(symbol (str "li" (when mobile ".mobile")))
                 (when mobile
                    {:on-click
                       (fn[e]
                          (swap! game-state #(core/move % from n)))})
                 [pawn-render n]]))
       pawns)]))

(defn player-pawn-render [n players]
  (let [player-name (nth players n)]
    [:div.player
     (render-pawn n)
     [:div.player-name player-name]]))

(defn dice-render [dice-num]
  (when dice-num [:dice.img {:src (str "images/dice-" dice-num ".svg")}]))

(defn card-render [what value antitoxin]
  (let [classes (filter some? [what (when antitoxin "anti") (logic/card-kind value)])]
    [(symbol (str "div.card." (clojure.string/join "." classes)))
     [:card.img {:src (str "images/" classes ".svg")}]
     [:value.div (or (when value (if antitoxin (* -1 value) value)))]]))

(defn game-render []
  (let [state @game-state]
    [:div 
     [:h1 "That's life"]]))

;;The onbeforeunload event occurs when a document is about to be unloaded.
(aset js/window "onbeforeunload" 
      (constantly "Do you really want to leave the game unfinished? Then you must lose!"))

(reagent/render [game-render]
                (js/document.querySelector "#game"))