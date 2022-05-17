(ns sluj.core-test
  (:use clojure.test 
        sluj.core))

(deftest basic
  (testing "Sluggify a given plain text string"
    (is (= (sluj "My name is mud") "my-name-is-mud"))
    (is (= (sluj "My name is definitley mud!") "my-name-is-definitley-mud")))
  (testing "Sluggify a given string with exotic UTF characters"
    (is (= (sluj "пo pomegranates") "po-pomegranates"))
    (is (= (sluj "маленький подъезд") "malenkij-poduezd"))))

(deftest opts
  (testing "Sluggify a given plain text string with custom separator"
    (is (= (sluj "Super awesome clojure code" :separator "_") "super_awesome_clojure_code"))
    (is (= (sluj "Massive crab" :separator "~") "massive~crab")))
  (testing "Sluggify a given string with a custom UTF-18 mapping"
    (is (= (sluj "I 🧡 Clojure" :🧡 "orange-heart") "i-orange-heart-clojure"))
    (is (= (sluj "Super awesome sentence!" :e "eee") "supeeer-aweeesomeee-seeenteeenceee"))
    (testing "Sluggify with a charmap map"
      (is (= (sluj "🐮 goes moo" :charmap {:🐮 "cow"}) "cow-goes-moo"))
      (is (= (sluj "🐮 loves 🦊" :charmap {:🦊 "fox" :🐮 "cow"}) "cow-loves-fox"))))
  (testing "Sluggify with a given locale"
    (is (= (sluj "маленький подъезд" :locale "bg") "malenykiy-podaezd")))
  (testing "Changing case"
    (is (= (sluj "make me uppercase" :casing "upper") "MAKE-ME-UPPERCASE"))
    ;; This is the default
    (is (= (sluj "make me lowercase" :casing "lower") "make-me-lowercase"))))
