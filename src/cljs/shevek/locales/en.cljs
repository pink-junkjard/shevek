(ns shevek.locales.en)

(def translations
  {:menu {:logout "Logout"}
   :home {:menu "Home"
          :title "Welcome!"
          :subtitle "What would you like to see today?"}
   :cubes {:title "Data Cubes"
           :menu "Cubes"
           :missing "There aren't any data cubes defined"
           :data-range "Available data range"}
   :dashboards {:title "Dashboards"
                :missing "There aren't any dashboards created"
                :saved "Dashboard '{1}' saved!"
                :deleted "Dashboard '{1}' deleted!"
                :report-count #(cond
                                 (zero? %) "No reports"
                                 (= 1 %) "1 report"
                                 :else "{1} reports")
                :no-reports "This dashboard has no reports"
                :updated-at "Last updated"}
   :reports {:title "Reports"
             :missing "There aren't any reports created"
             :name "Name"
             :description "Description"
             :dashboards "Pin in Dashboards"
             :updated-at "Last updated"
             :saved "Report '{1}' saved!"
             :deleted "Report '{1}' deleted!"
             :unauthorized "Oops! This report is no longer available."
             :export-as-csv "Export as CSV"}
   :viewer {:dimensions "Dimensions"
            :measures "Measures"
            :filters "Filters"
            :splits "Splits"
            :split-on "Split On"
            :rows "Rows"
            :columns "Columns"
            :pinboard "Pinboard"
            :limit "Limit"
            :sort-by "Sort By"
            :granularity "Granularity"
            :no-pinned "Drag dimensions here to pin them for quick access"
            :no-measures "Please select at least one measure"
            :no-results "No results were found that match the specified search criteria"
            :split-required "At least one split is required for the {1} visualization"
            :too-many-splits-for-chart "A maximum of two splits may be provided for chart visualization"
            :unauthorized "It seems that the {1} cube is no longer available. Please contact the administrator."
            :toggle-fullscreen "Toggle Fullscreen"}
   :viewer.period {:relative "Relative"
                   :specific "Specific"
                   :latest "Latest"
                   :latest-hour "Latest Hour"
                   :latest-day "Latest Day"
                   :latest-7days "Latest 7 Days"
                   :latest-30days "Latest 30 Days"
                   :latest-90days "Latest 90 Days"
                   :current "Current"
                   :current-day "Current Day"
                   :current-week "Current Week"
                   :current-month "Current Month"
                   :current-quarter "Current Quarter"
                   :current-year "Current Year"
                   :previous "Previous"
                   :previous-day "Previous Day"
                   :previous-week "Previous Week"
                   :previous-month "Previous Month"
                   :previous-quarter "Previous Quarter"
                   :previous-year "Previous Year"
                   :from "From"
                   :to "To"}
   :viewer.operator {:include "Include"
                     :exclude "Exclude"}
   :viewer.viztype {:totals "totals"
                    :table "table"
                    :bar-chart "bar chart"
                    :line-chart "line chart"
                    :pie-chart "pie chart"}
   :share {:title "Share"
           :copy-url "Copy URL"
           :copied "URL Copied!"}
   :raw-data {:menu "View raw data"
              :title "Raw Event Data"
              :showing "Showing the first {1} events matching: "
              :button "Raw Data"}
   :settings {:menu "Settings"
              :lang "Language"
              :update-now "Update Now"
              :auto-refresh "Auto Update"
              :auto-refresh-opts (fn [] [["Off" 0] ["Every 10 seconds" 10] ["Every 30 seconds" 30] ["Every minute" 60] ["Every 10 minutes" 600] ["Every 30 minutes" 1800]])
              :abbreviations "Number Abbreviations"
              :abbreviations-opts (fn [] [["Use default settings" "default"] ["Don't use abbreviations" "no"] ["Use abbreviations" "yes"]])}
   :admin {:menu "Manage Users"
           :title "Management"
           :subtitle "Configure the users who will be using the system and their permissions"
           :users "Users"}
   :users {:username "Username"
           :fullname "Full Name"
           :email "Email"
           :admin "Admin"
           :password "Password"
           :password-confirmation "Password Confirmation"
           :invalid-credentials "Invalid username or password"
           :session-expired "Session expired, please login again"
           :password-hint "Leave blank if you don't want to change it"
           :unauthorized "Sorry, you are not allow to access this page. Please contact the administrator for more information."
           :basic-info "Basic Information"
           :permissions "Permissions"}
   :permissions {:allowed-cubes "Allowed Cubes"
                 :admin-all-cubes "Admin users view everything"
                 :all-cubes "Can view all cubes"
                 :no-cubes "Can view no cubes"
                 :only-cubes-selected "Can view only the following cubes"
                 :all-measures "All measures will be visible"
                 :only-measures-selected "Only the following measures will be visible"
                 :select-measures "Please select the allowed measures"
                 :no-measures "None"
                 :add-filter "Add Filter"}
   :account {:title "Your Account"
             :subtitle "Edit your profile details here"
             :current-password "Current Password"
             :new-password "New Password"
             :saved "Your account has been saved"
             :invalid-current-password "is incorrect"}
   :date-formats {:second "yyyy-MM-dd HH:mm:ss"
                  :minute "MMM d, h:mma"
                  :hour "MMM d, yyyy ha"
                  :day "MMM d, yyyy"
                  :month "MMM yyyy"
                  :file "yyMMdd_HHmm"}
   :calendar {:days ["S" "M" "T" "W" "T" "F" "S"]
              :months ["January" "February" "March" "April" "May" "June" "July" "August" "September" "October" "November" "December"]
              :monthsShort ["Jan" "Feb" "Mar" "Apr" "May" "Jun" "Jul" "Aug" "Sep" "Oct" "Nov" "Dec"]
              :today "Today"
              :now "Now"
              :am "AM"
              :pm "PM"}
   :actions {:ok "Accept"
             :cancel "Cancel"
             :edit "Modify"
             :save "Save"
             :save-as "Save As"
             :new "Create"
             :delete "Delete"
             :close "Close"
             :select "Select"
             :hold-delete "You must click the button and hold for one second to confirm"
             :search "Search"}
   :validation {:required "can't be blank"
                :regex "doesn't match pattern"
                :email "is not a valid email address"
                :password "must have a combination of at least 7 letters and numbers (or symbols)"
                :confirmation "doesn't match the previous value"}
   :boolean {:true "Yes"
             :false "No"}
   :errors {:no-results "No results where found"
            :no-desc "No description"
            :bad-gateway "The system is not available right now. Please try again later."
            "Query timeout" "The query is taking longer than expected. Please try with a shorter period."}})
