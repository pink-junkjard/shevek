(ns shevek.locales.en)

(def translations
  {:sessions {:logout "Log out"}
   :home {:menu "Home"
          :title "Welcome!"
          :subtitle "What would you like to analyze today?"}
   :cubes {:title "Cubes"
           :subtitle "Available data cubes"
           :name  "Name"
           :description  "Description"
           :missing "There are no cubes defined."
           :data-range "Available data range"}
   :dashboards {:title "Dashboards"
                :subtitle "Manage your dashboards"
                :search-hint "Filter by name or description"
                :missing "You have no dashboards yet."
                :saved "Dashboard saved!"
                :deleted "Dashboard '{1}' deleted!"
                :updated-at "Last updated"
                :new "New Dashboard"
                :new-panel "Add Panel"
                :name "Name"
                :description "Description"}
   :dashboard {:edit-panel "Edit Panel"
               :fullscreen-panel "Toggle Fullscreen"
               :remove-panel "Remove Panel"
               :select-cube "Select a cube for the report"}
   :reports {:title "Reports"
             :subtitle "Manage your reports"
             :recent "Recent Reports"
             :missing "You haven't created any reports yet. Click on a cube to design a new one and then save it."
             :name "Name"
             :description "Description"
             :dashboards "Pin in Dashboards"
             :updated-at "Last updated"
             :saved "Report saved!"
             :deleted "Report '{1}' deleted!"
             :unauthorized "Oops! This report is no longer available."
             :download-csv "Download as CSV"
             :new "New Report"}
   :designer {:dimensions "Dimensions"
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
              :chart-with-second-split-on-rows "The second split must be on the columns to be able to generate the chart"
              :unauthorized "It seems that the {1} cube is no longer available. Please contact the administrator."
              :maximize "Maximize results panel"
              :minimize "Minimize results panel"
              :grand-total "Grand Total"
              :go-back "Go back to dashboard"}
   :designer.period {:relative "Relative"
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
   :designer.operator {:include "Include"
                       :exclude "Exclude"}
   :designer.viztype {:totals "totals"
                      :table "table"
                      :bar-chart "bar chart"
                      :line-chart "line chart"
                      :pie-chart "pie chart"}
   :share {:title "Share"
           :label "Link to share"
           :copy "Copy"
           :copied "Link copied!"
           :report-hint "This is a point-in-time snapshot of your report. Any changes made afterward, will not be reflected on shared report."}
   :raw-data {:title "Raw Event Data"
              :showing "Showing the first {1} events matching: "
              :button "Raw Data"}
   :configuration {:menu "Application Configuration, Manage Users"
                   :title "Configuration"
                   :subtitle "Application level settings"
                   :users "Users"}
   :profile {:menu "User preferences, Change Password"
             :preferences "Preferences"
             :password "Change Password"}
   :preferences {:lang "Language"
                 :abbreviations "Number Abbreviations"
                 :abbreviations-opts (fn [] [["Use default settings" "default"] ["Don't use abbreviations" "no"] ["Use abbreviations" "yes"]])
                 :saved "Preferences saved!"
                 :refresh-every "Refresh every"
                 :refresh-every-opts (fn [] {0 "Never"
                                             10 "10s"
                                             30 "30s"
                                             60 "1m"
                                             600 "10m"
                                             1800 "30m"})
                 :refresh-now "Refresh Now"}
   :account {:current-password "Current Password"
             :new-password "New Password"
             :saved "Your account has been saved"
             :invalid-current-password "is incorrect"}
   :admin {:menu "Manage Users"
           :title "Management"
           :subtitle "Configure the users who will be using the system and their permissions"
           :users "Users"}
   :users {:username "Username"
           :fullname "Name"
           :email "Email"
           :admin "Admin"
           :password "Password"
           :password-confirmation "Password Confirmation"
           :invalid-credentials "Invalid username or password"
           :session-expired "Session expired, please login again"
           :password-hint "Leave blank if you don't want to change it"
           :unauthorized "Sorry, you are not allow to access this page. Please contact the administrator for more information."
           :basic-info "Basic Information"
           :permissions "Permissions"
           :search-hint "Filter by username or fullname"
           :deleted "User deleted!"}
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
              :pm "PM"
              :dayNames ["Sunday" "Monday" "Tuesday" "Wednesday" "Thursday" "Friday" "Saturday"]} ; Not used by the calendar but for :format "dow" dimensions
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
             :search "Search"
             :header "Actions"
             :confirm "Confirm"
             :manage "Manage"
             :refresh "Refresh"}
   :validation {:required "can't be blank"
                :regex "doesn't match pattern"
                :email "is not a valid email address"
                :password "must have a combination of at least 7 letters and numbers (or symbols)"
                :confirmation "doesn't match the previous value"}
   :boolean {:true "Yes"
             :false "No"}
   :errors {:no-results "No results where found."
            :no-desc "No description"
            :bad-gateway "The system is not available right now. Please try again later."
            :unexpected "We're sorry but something went wrong. We've been notified about this issue and we'll take a look at it shortly."
            :page-not-found "The requested page was not found. Make sure there aren't any errors in the web address."
            "Query timeout" "The query is taking longer than expected. Please try with a shorter period."}})
