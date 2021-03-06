(ns shevek.acceptance.profile-test
  (:require [clojure.test :refer [deftest use-fixtures is]]
            [shevek.acceptance.test-helper :refer [wrap-acceptance-tests it login click click-text visit fill has-css? has-text? click-tid select clear-preferences]]
            [etaoin.keys :as k]))

(use-fixtures :once wrap-acceptance-tests)

(use-fixtures :once {:after clear-preferences})

(deftest ^:acceptance profile-tests
  (it "changing the app language"
    (login)
    (click-tid "sidebar-profile")
    (select {:data-tid "lang"} "Español")
    (click-text "Save")
    (is (has-css? "#notification" :text "Preferences saved!"))
    (is (has-text? "Preferencias")))

  (it "the current password mast match"
    (login {:username "max" :fullname "Max" :password "secret999"})
    (click-tid "sidebar-profile")
    (is (has-css? ".page-title" :text "Max"))
    (click-text "Change Password")
    (fill {:name "current-password"} "nop" k/enter)
    (is (has-css? ".red.label" :text "is incorrect"))
    (is (has-css? ".page-title" :text "Max")))

  (it "changing the fullname"
    (login {:username "max" :fullname "Max" :password "secret999"})
    (click-tid "sidebar-profile")
    (click-text "Change Password")
    (fill {:name "fullname"} (k/with-shift k/home) k/delete "Mex")
    (fill {:name "current-password"} "secret999" k/enter)
    (is (has-css? "#notification" :text "Your account has been saved"))
    (is (has-css? ".page-title" :text "Mex"))))
