;; Copyright Rawley Fowler & Contributors.
;; https://github.com/rawleyfowler/sluj
(ns sluj.core
  (:require [sluj.charmap :as c]
            [clojure.string :as string]
            [clojure.walk :as w]))

(defn- replace-characters
  "Replaces any characters in a string that are mapped in the given charmap"
  [s charmap]
  (string/replace s (re-pattern (string/join "|" (keys charmap))) charmap))

(defn sluj
  "'Slugifies' a given string s, to make it usable in URI's. Specifically UTF-16 strings"
  ;; Credits to u/MartinPuda on Reddit for reminding me I can destructure
  [s & {:keys [locale separator casing]
        :or {locale "none" separator "-" casing "lower"}
        :as opts}]
  (let [spec-opts [:locale :remove :separator :casing]
        casing-fn (if (= (keyword casing) :lower)
                    string/lower-case
                    string/upper-case)
        charmap (merge c/charmap
                       (w/stringify-keys (apply dissoc (get opts :charmap opts) spec-opts))
                       ((keyword locale) c/locales))]
    (-> s
        (string/replace #"[:/?#\[\]@!$&'()*+,;=]" "") ;; Remove any reserved RFC 3986 characters
        (replace-characters charmap) ;; Replace the characters with the charmap characters
        (->>
         (re-seq #"\w+") ;; Separate the words into a list
         (string/join separator)) ;; Join the words back with the separator
        casing-fn))) ;; Apply the specified casing
