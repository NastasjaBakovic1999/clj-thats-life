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

(deftest remove-once-test
  (is (= (logic/remove-once (partial = 0) [0 0 0 1 1 1 2 2 2]) '(0 0 1 1 1 2 2 2)))
  (is (= (logic/remove-once (partial = 1) [0 0 0 1 1 1 2 2 2]) '(0 0 0 1 1 2 2 2)))
  (is (= (logic/remove-once (partial = 2) [0 0 0 1 1 1 2 2 2]) '(0 0 0 1 1 1 2 2)))
  (is (= (logic/remove-once (partial = [1]) [[] [] [] [1] [] [] [] []
                                           [-1] [-1] [-1] [-1] [-1] [-1]
                                           [] [] [] [] [] [] [] []
                                           [] [] [] [] [] [] [] [] [] []])
         '([] [] [] [] [] [] [] 
           [-1] [-1] [-1] [-1] [-1] [-1] 
           [] [] [] [] [] [] [] [] [] 
           [] [] [] [] [] [] [] [] [])))
  (is (= (logic/remove-once (partial = [1]) [[] [] [] [1] [] [1] [] []
                                             [-1] [-1] [-1] [-1] [-1] [-1]
                                             [] [] [] [] [] [] [] []
                                             [] [] [] [] [] [1] [] [] [] []])
         '([] [] [] [] [1] [] [] 
           [-1] [-1] [-1] [-1] [-1] [-1] 
           [] [] [] [] [] [] [] [] [] []
           [] [] [] [1] [] [] [] []))) 
  (is (= (logic/remove-once (partial = [1]) [[] [1] [] [1] [2] [] [] []
                                               [-1] [-1] [-1] [-1] [-1] [-1]
                                               [] [] [] [2] [3] [3] [] []
                                               [] [] [] [] [] [] [] [] [] []])
           '([] [] [1] [2] [] [] [] 
             [-1] [-1] [-1] [-1] [-1] [-1] 
             [] [] [] [2] [3] [3] [] [] [] 
             [] [] [] [] [] [] [] [] [])))
  )

(deftest trim-test
    (is (= (logic/trim {:start-pawns [0 0 0 1 1 1 2 2 2]
                        :path [-1 -2 -3 -4 -5 -6 -7 -8
                               0 0 0 0 0 0
                               1 2 3 4 5 6 7 8
                               -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
                        :dice 4
                        :ids [0 1 2 3 4 5 6 7 8
                              9 10 11 12 13 14
                              15 16 17 18 19 20 21 22
                              23 24 25 26 27 28 29 30 31 32]
                        :pawns [[] [] [] [] [] [] [] []
                                [-1] [-1] [-1] [-1] [-1] [-1]
                                [] [] [] [] [] [] [] []
                                [] [] [] [] [] [] [] [] [] []]
                        :collect [[] [] []]
                        :up 2}) 
           {:start-pawns [0 0 0 1 1 1 2 2 2]
            :path [-1 -2 -3 -4 -5 -6 -7 -8 0 0 0 0 0 0 1 2 3 4 5 6 7 8 -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
            :dice 4, 
            :ids [0 1 2 3 4 5 6 7 8
                  9 10 11 12 13 14
                  15 16 17 18 19 20 21 22 
                  23 24 25 26 27 28 29 30 31 32]
            :pawns [[] [] [] [] [] [] [] [] 
                    [-1] [-1] [-1] [-1] [-1] [-1]
                    [] [] [] [] [] [] [] [] [] [] 
                    [] [] [] [] [] [] [] []] 
            :collect [[] [] []]
            :up 2}))
  (is (= (logic/trim {:start-pawns []
                      :path [-1 -2 -3 -4 -5 -6 -7 -8
                             0 0 0 0 0 0
                             1 2 3 4 5 6 7 8
                             -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
                      :dice 4
                      :ids [0 1 2 3 4 5 6 7 8
                            9 10 11 12 13 14
                            15 16 17 18 19 20 21 22
                            23 24 25 26 27 28 29 30 31 32]
                      :pawns [[] [] [] [] [] [] [] []
                              [-1] [-1] [-1] [-1] [-1] [-1]
                              [] [] [] [] [] [] [] []
                              [] [] [] [] [] [] [] [] [] []]
                      :collect [[] [] []]
                      :up 2})
         {:start-pawns []
          :path []
          :dice 4
          :ids [32]
          :pawns []
          :collect [[] [] []]
          :up 2}))
    (is (= (logic/trim {:start-pawns []
                        :path [-1 -2 -3 -4 -5 -6 -7 -8
                               0 0 0 0 0 0
                               1 2 3 4 5 6 7 8
                               -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
                        :dice 4
                        :ids [0 1 2 3 4 5 6 7 8
                              9 10 11 12 13 14
                              15 16 17 18 19 20 21 22
                              23 24 25 26 27 28 29 30 31 32]
                        :pawns [[] [] [1] [] [] [] [] []
                                [-1] [-1] [-1] [-1] [-1] [-1]
                                [] [] [] [] [] [] [] []
                                [] [] [] [] [] [] [] [] [] []]
                        :collect [[] [] []]
                        :up 2})
           {:start-pawns []
            :path [-3 -4 -5 -6 -7 -8
                   0 0 0 0 0 0 
                   1 2 3 4 5 6 7 8
                   -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
            :dice 4
            :ids [2 3 4 5 6 7 8 
                  9 10 11 12 13 14
                  15 16 17 18 19 20 21 22
                  23 24 25 26 27 28 29 30 31 32]
            :pawns [[1] [] [] [] [] [] 
                    [-1] [-1] [-1] [-1] [-1] [-1]
                    [] [] [] [] [] [] [] [] 
                    [] [] [] [] [] [] [] [] [] []]
            :collect [[] [] []]
            :up 2}))
   (is (= (logic/trim {:start-pawns []
                       :path [-1 -2 -3 -4 -5 -6 -7 -8
                              0 0 0 0 0 0
                              1 2 3 4 5 6 7 8
                              -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
                       :dice 4
                       :ids [0 1 2 3 4 5 6 7 8
                             9 10 11 12 13 14
                             15 16 17 18 19 20 21 22
                             23 24 25 26 27 28 29 30 31 32]
                       :pawns [[] [] [] [] [2] [1] [] []
                               [-1] [-1] [-1] [-1] [-1] [-1]
                               [] [] [] [] [] [2] [] []
                               [] [] [1] [] [] [3] [] [] [] []]
                       :collect [[] [] []]
                       :up 2})
          {:start-pawns []
           :path [-5 -6 -7 -8 
                  0 0 0 0 0 0
                  1 2 3 4 5 6 7 8 
                  -1 -2 -3 -4 -5 -6 -7 -8 -9 -10]
           :dice 4
           :ids [4 5 6 7 8 
                 9 10 11 12 13 14
                 15 16 17 18 19 20 21 22
                 23 24 25 26 27 28 29 30 31 32]
           :pawns [[2] [1] [] []
                   [-1] [-1] [-1] [-1] [-1] [-1]
                   [] [] [] [] [] [2] [] []
                   [] [] [1] [] [] [3] [] [] [] []]
           :collect [[] [] []]
           :up 2}))
  )


