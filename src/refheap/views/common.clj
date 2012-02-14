(ns refheap.views.common
  (:use [clavatar.core :only [gravatar]])
  (:require [noir.session :as session]
            [stencil.core :as stencil]
            [hiccup.core :as hiccup]
            [hiccup.page-helpers :as ph]
            [refheap.models.paste :as paste]))

(defn avatar [email size]
  (ph/image (gravatar email :size size)))

(defn logged-in [username]
  (stencil/render-file
    "refheap/views/templates/loggedin"
    {:user (when-let [user (or username
                               (and (bound? #'session/*noir-session*)
                                    (:username (session/get :user))))]
             {:username user})}))

(defn layout [body]
  (stencil/render-file
    "refheap/views/templates/common"
    {:user (logged-in nil)
     :content (hiccup/html body)}))

(def header nil)

(defn page-buttons [base n per page]
  [:div.centered
   (when-not (= 1 page)
     [:a#newer.pagebutton {:href (str base "?page=" (dec page))} "newer"])
   (when-not (or (zero? n) (= page (paste/count-pages n per)))
     [:a.pagebutton {:href (str base "?page=" (inc page))} "older"])])
