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
  [:img.pawn {:src (get pawn-imgs (inc n))}])

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
  (when dice-num [:img.dice {:src (str "images/dice-" dice-num ".svg")}]))

(defn card-render [what value antitoxin]
  (let [classes (filter some? [what (when antitoxin "anti") (logic/card-kind value)])]
    [(symbol (str "div.card." (clojure.string/join "." classes)))
     [:img.card {:src (str "images/" classes ".svg")}]
     [:div.value (or (when value (if antitoxin (* -1 value) value)))]]))

(defn space-render [what idx key value pawns up robot]
  [:div.space ^{:key key}
   {:data-key key :data-value value}
   [:div.pawns (when (> count pawns) 8) {:class "crowd"}) [pawns-render pawns up idx robot]]
   [card-render what value]])

(defn players-render [players up dice collect]
  [:table.players>tbody
   (map-indexed
    (fn [idx player-name]
      (let [collect (nth collect idx)
            antitoxin (logic/use-antitoxin collect)]
        [:tr 
         [:td.player-name player-name]
         [:td.pawn (pawn-render idx)]
         [:td.dice (when (= idx up) (dice-render dice))]
         [:td.scores (logic/calculate-score (get collect idx))]
         [:td.collect
          (map-indexed
           (fn [idx value]
             (card-render nil value (not= (nth antitoxin idx) value))))]])))])

(defn player-entry-render [num-of-players]
  [:div.entry]
  (when (< num-of-players 6)
    [:div
     [:input {:type "text" :placeholder "Entry player name:"}]
     [:button 
      {:on-click
       (fn [event]
         (let [parent-node (.-target.parentNode event)
               input (.querySelector parent-node "input")
               player-name (.-value input)]
           (do
             (aset input "value" "")
             (swap! game-state #(logic/join-game % player-name))
             (.focus input))))}
      "Entry"]])
  [:button
   {:style {:visibility (if (> player-count 1) "visible" "hidden")}
    :on-click
    (fn [event]
      (swap! game-state logic/start-game))}
   "Start"])

(defn game-render []
  (let [state @game-state]
    [:div 
     [:h1 "That's life"]]))

(defn sleep [func ms]
  (js/setTimeout func ms))

(add-watch game-state :robots
           (fn [key ref old-state new-state]
             (when-let [robot (logic/activated-robot game-state)]
               (sleep #(swap! game-state robot) 3000))))

;;The onbeforeunload event occurs when a document is about to be unloaded.
(aset js/window "onbeforeunload" 
      (constantly "Do you really want to leave the game unfinished? Then you must lose!"))

(reagent/render [game-render]
                (js/document.querySelector "#game"))