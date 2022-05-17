(ns sluj.core-test
  (:use clojure.test 
        sluj.core))

(deftest basic
  (testing "Sluggify a given plain text string."
    (is (= (sluj "My name is mud") "my-name-is-mud"))
    (is (= (sluj "My name is definitley mud!") "my-name-is-definitley-mud"))))

(deftest opts
  (testing "Sluggify a given plain text string with custom separator"
    (is (= (sluj "Super awesome clojure code" :separator "_") "super_awesome_clojure_code"))
    (is (= (sluj "Massive crab" :separator "~") "massive~crab"))))
