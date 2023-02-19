(ns thatslife.logic_test
  (:require [cljs.test :refer-macros [deftest is are testing run-tests]]
            [thatslife.logic :as logic]))

(deftest is-valid?-test
  (is (= (logic/is-valid? {:players []}) true))
  (is (= (logic/is-valid? {:players ["Nastasja" "Stefan" "Robot-Zika"]}) true))
  (is (= (logic/is-valid? {:players [] :pawns []}) true))
  (is (= (logic/is-valid? {:pawns []}) false))
  (is (= (logic/is-valid? nil) false))
  (is (= (logic/is-valid? [:players []]) false)) 
  (is (= (logic/is-valid? '(:players [])) false)))

(deftest initial-pawns-test
  (is (= (logic/initial-pawns 2) 3))
  (is (= (logic/initial-pawns 3) 3))
  (is (= (logic/initial-pawns 4) 3))
  (is (= (logic/initial-pawns 5) 2))
  (is (= (logic/initial-pawns 6) 2)))

(deftest setup-pawns-test
  (is (= (logic/setup-pawns [-1 -2 -3 -4 -5 -6 -7 -8
                             0 0 0 0 0 0
                             1 2 3 4 5 6 7 8
                             -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]) 
         [[] [] [] [] [] [] [] [] 
          [-1] [-1] [-1] [-1] [-1] [-1] [-1] [-1] 
          [] [] [] [] [] [] [] [] 
          [] [] [] [] [] [] [] []]))
  (is (= (logic/setup-pawns [-1 -2 -3 -4 -5 -6 -7 -8
                             0 0 0 0 0 0
                             1 2 3 4 5 6 7 8])
        [[] [] [] [] [] [] [] [] 
         [-1] [-1] [-1] [-1] [-1] [-1] [-1] [-1] 
         [] [] [] [] [] []]))
  (is (= (logic/setup-pawns nil)
         [])))

(deftest join-game-test
  (is (= (logic/join-game {:players []} "Nastasja") {:players ["Nastasja"]}))
  (is (= (logic/join-game {:players ["Nastasja" "Stefan"]} "Robot-Zika") {:players ["Nastasja" "Stefan" "Robot-Zika"]}))
  (is (= (logic/join-game {:players ["Nastasja" "Stefan"]} nil) {:players ["Nastasja" "Stefan" nil]})) 
  (is (= (logic/join-game {:players ["Nastasja" "Stefan"] :pawns [0 0 0 1 1 1 2 2 2]} "Sima") {:players ["Nastasja" "Stefan" "Sima"] :pawns [0 0 0 1 1 1 2 2 2]})))

(deftest calculate-score-test 
  (is (= (logic/calculate-score []) 0)) 
  (is (= (logic/calculate-score [1 2 3]) 6))
  (is (= (logic/calculate-score [-7 -8]) -15))
  (is (= (logic/calculate-score [1 2 -7 -8 3 0 0]) 21))
  (is (= (logic/calculate-score [1 2 -7 -8 3 0 0 0 0]) 21))
  (is (= (logic/calculate-score [1 2 -7 -8 3 0]) 7))
  (is (= (logic/calculate-score [1 2 -7 -8 3]) -9))
  (is (= (logic/calculate-score [1 2 3 0 0]) 6))
  (is (= (logic/calculate-score [-7 -8 0 0]) 15))
  (is (= (logic/calculate-score [-7 -8 0]) 1))
  (is (= (logic/calculate-score [0 -7 -8]) 1))
  (is (= (logic/calculate-score [-7 0 -8]) 1))
  (is (= (logic/calculate-score [1 2 -7 -8 3 0 0 -9]) 16)))