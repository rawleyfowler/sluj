# Sluj
Sluj is a very small library for converting strings of UTF-16 text to slugs. A slug is a piece of text that is URL safe. This is particularly useful for blogs!

This library adheres to the following:
<ul>
    <li>
        <a href="ttps://datatracker.ietf.org/doc/html/rfc3986">RFC 3986</a>
    </li>
    <li>
        Dependency-less
    </li>
</ul>

Shout out to [Trott/slug](https://github.com/Trott/slug) for inspiring me to make this over a rainy weekend.

## Contributing
I will absolutely be accepting any contributions to this project. I am particularly looking for contributions to the locale settings of the project, as there is no way I could create a locale for every alphabet myself.

## Examples
```clojure
;; Simple examples
(sluj "Hey it's me!") ;; => hey-its-me
(sluj "25 ways to pet a cat") ;; => 25-ways-to-pet-a-cat

;; UTF-16 examples
(sluj "I ♥ Unicode!") ;; => i-unicode
(sluj "I ♥ Unicode!" :♥ "love") ;; => i-love-unicode

(sluj "Turn this ship 200°!") ;; => turn-this-ship-200
(sluj "Turn this ship 200°!" :° "degrees") ;; => turn-this-ship-200-degrees

;; Non english characters
;; These were made with google translate, sorry in advanced.
;; Default locale is Russian for Cyrillics
(sluj "маленький подъезд") ;; => malenkij-poduezd

;; Change the locale if you wish
(sluj "маленький подъезд" :locale "bg") ;; => malenykiy-podaezd

;; Change sparator
(sluj "hi mom" :separator "%") ;; => hi%mom

;; Remove based on regexp
(sluj "hi mom part 2" :remove #"\d") ;; hi-mom-part

;; Pass the charmap as a argument
(sluj "I have weird characters ☢ ♥" :charmap {:♥ "heart" :☢ :radiate}) ;; => i-have-weird-characters-radiate-heart
;; Alternatively
(sluj "I have weird characters ♥ ☢" :♥ "heart" :☢ "radiate") ;; => i-have-weird-characters-radiate-heart
```
## License
This project is licensed via the MIT license. Please read through the `LICENSE` file at the root of the project for more information.
