(ns sluj.core
  (:require [sluj.charmap :as c]
            [clojure.string :as string]
            [clojure.walk :as w]))

;; How about instead we setify the map characters, and then apply replacements to any non ascii chars. This will be way more performant.
(defn- replace-characters
  "Replaces any characters in a string that are mapped in the given charmap"
  [s charmap]
  (let [character-set (set s)
        characters (filter #(contains? charmap (str %)) character-set)]
    (string/replace s (re-pattern (string/join "|" characters)) charmap)))

(defn- extract-words
  "Extracts words from the string"
  [s]
  (re-seq #"\w+" s))

(defn sluj
  "'Slugifies' a given string s, to make it usable in URI's. Specifically UTF-16 strings."
  [s & {:as opts}]
  (let [spec-opts '(:locale
                     :remove
                     :separator
                     :casing)
        locale (get opts :locale "en")
        casing (if (get opts :casing :lower)
                 string/lower-case
                 string/upper-case)
        separator (get opts :separator "-") ;; Legal separators: Any ASCII char that we don't replace, down below.
        charmap (merge c/charmap 
                       (w/stringify-keys (apply dissoc (get opts :charmap opts) spec-opts)))]
    (-> s
        (string/replace #"[:/?#\[\]@!$&'()*+,;=]" "") ;; Remove any reserved RFC 3986 characters
        (replace-characters charmap) ;; Replace the characters with the charmap characters
        ;;(extract-words) ;; Extract the words to a list of words
        ;;(string/join separator) ;; Replace spaces with the separator
        ;;casing
        ))) ;; Apply the specified casing
