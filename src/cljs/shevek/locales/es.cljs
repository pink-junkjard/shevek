(ns shevek.locales.es)

(def translations
  {:sessions {:logout "Salir"}
   :home {:menu "Inicio"
          :title "Bienvenido!"
          :subtitle "Qué te gustaría analizar hoy?"}
   :cubes {:title "Cubos"
           :subtitle "Cubos de datos disponibles"
           :name  "Nombre"
           :description  "Descripción"
           :missing "No hay cubos definidos."
           :data-range "Rango de datos disponibles"}
   :dashboards {:title "Dashboards"
                :subtitle "Administrar dashboards"
                :search-hint "Filtrar por nombre o descripción"
                :missing "No has creado ningún dashboard todavía."
                :deleted "Dashboard '{1}' eliminado correctamente"
                :report-count #(cond
                                 (zero? %) "Sin reportes"
                                 (= 1 %) "1 reporte"
                                 :else "{1} reportes")
                :updated-at "Última Actualización"
                :new "Nuevo Dashboard"
                :name "Nombre"
                :description "Descripción"}
   :dashboard {:add-panel "Agregar Panel"
               :edit-panel "Modificar Panel"
               :fullscreen-panel "Maximizar/Minimizar"
               :remove-panel "Quitar Panel"
               :select-cube "Seleccioná un cubo para el reporte"
               :saved "Dashboard guardado correctamente"
               :share-disabled "<b>Compartir deshabilitado</b><br/>Debe guardar el dashboard antes de poder compartirlo."
               :share-hint "Este link es una referencia de sólo lectura a su dashboard. Cualquier cambio posterior será visible por los usuarios que posean en el link."
               :import "Importar Dashboard"
               :import-as-link "Importar como Link"
               :import-as-link-desc "El dashboard se guardará como un link al original. Cualquier cambio que el propietario realice sobre el original se verá reflejado en tu copia, pero no podrás realizar modificaciones."
               :import-as-copy "Importar como Copia"
               :import-as-copy-desc "Genera una copia completa del dashboard, lo cual te permitirá hacerle modificaciones. No recibirás actualizaciones posteriores realizadas sobre dashboard original."
               :import-name "Podés darle otro nombre aquí"
               :imported "Dashboard importado!"}
   :reports {:title "Reportes"
             :subtitle "Administrar reportes"
             :recent "Reportes Recientes"
             :missing "No has creado ningún reporte todavía. Seleccioná un cubo para crear uno y luego guardalo para que aparezca aquí."
             :name "Nombre"
             :description "Descripción"
             :updated-at "Última actualización"
             :saved "Reporte guardado correctamente"
             :deleted "Reporte '{1}' eliminado correctamente"
             :unauthorized "Oops! Este reporte ya no está disponible."
             :download-csv "Descargar en formato CSV"
             :new "Nuevo Reporte"
             :share-hint "Este link es una copia de su reporte. Cualquier cambio posterior no se verá reflejado en el reporte compartido."}
   :designer {:dimensions "Dimensiones"
              :measures "Métricas"
              :filters "Filtros"
              :split-on "Colocar En"
              :rows "Filas"
              :columns "Columnas"
              :limit "Límite"
              :sort-by "Ordenar Por"
              :granularity "Granularidad"
              :no-pinned "Arrastre dimensiones aquí para acceso rápido"
              :no-measures "Por favor seleccione al menos una métrica"
              :no-results "No se encontraron resultados que coincidan con los criterios especificados"
              :split-required "Se necesita al menos una dimensión en el split ver los datos en forma de {1}"
              :too-many-splits-for-chart "Para visualización de gráficos debe haber como máximo dos splits"
              :chart-with-second-split-on-rows "Debe colocar el segundo split en las columnas para poder generar el gráfico"
              :unauthorized "Parece que el cubo {1} ya no está disponible. Por favor, contactá al administrador para más información."
              :maximize "Maximizar panel de resultados"
              :minimize "Minimizar panel de resultados"
              :grand-total "Total General"
              :go-back "Volver al dashboard"}
   :designer.period {:relative "Relativo"
                     :specific "Específico"
                     :latest "Ultimo"
                     :latest-hour "Ultima Hora"
                     :latest-day "Ultimo Día"
                     :latest-7days "Ultimos 7 Días"
                     :latest-30days "Ultimos 30 Días"
                     :latest-90days "Ultimos 90 Días"
                     :current "Actual"
                     :current-day "Día de Hoy"
                     :current-week "Esta Semana"
                     :current-month "Este Mes"
                     :current-quarter "Este Trimestre"
                     :current-year "Este Año"
                     :previous "Previo"
                     :previous-day "Día de Ayer"
                     :previous-week "Semana Pasada"
                     :previous-month "Mes Pasado"
                     :previous-quarter "Trimestre Pasado"
                     :previous-year "Año Pasado"
                     :from "Desde"
                     :to "Hasta"}
   :designer.operator {:include "Incluir"
                       :exclude "Excluir"}
   :designer.viztype {:totals "totales"
                      :table "tabla"
                      :bar-chart "barras"
                      :line-chart "línea"
                      :pie-chart "torta"}
   :share {:title "Compartir"
           :label "Link para compartir"
           :copy "Copiar"
           :copied "Link copiado!"}
   :raw-data {:menu "Ver datos desagregados"
              :title "Datos Desagregados"
              :showing "Primeros {1} eventos según filtro: "
              :button "Datos Desagregados"}
   :configuration {:menu "Configuración de la Aplicación, Gestionar Usuarios"
                   :title "Configuración"
                   :subtitle "Gestionar usuarios"
                   :users "Usuarios"}
   :profile {:menu "Preferencias del Usuario, Cambiar Password"
             :preferences "Preferencias"
             :password "Cambiar Password"}
   :preferences {:lang "Lenguaje"
                 :abbreviations "Abreviaturas de Números"
                 :abbreviations-opts (fn [] [["User formato por defecto" "default"] ["No abreviar nada" "no"] ["Abreviar todo" "yes"]])
                 :saved "Preferencias guardadas!"
                 :refresh-every "Refrescar cada"
                 :refresh-every-opts (fn [] {0 "Nunca"
                                             10 "10s"
                                             30 "30s"
                                             60 "1m"
                                             600 "10m"
                                             1800 "30m"})
                 :refresh-now "Actualizar Ahora"}
   :account {:current-password "Password Actual"
             :new-password "Nuevo Password"
             :saved "Tu cuenta se grabó correctamente"
             :invalid-current-password "es incorrecto"}
   :admin {:menu "Configurar Usuarios"
           :title "Administración"
           :subtitle "Configure los usuarios que podrán acceder al sistema y sus permisos"
           :users "Usuarios"}
   :users {:username "Usuario"
           :fullname "Nombre"
           :email "Email"
           :password-confirmation "Confirmación de Password"
           :invalid-credentials "Usuario y/o password incorrecto"
           :session-expired "Sesión expirada, por favor ingrese nuevamente"
           :password-hint "Dejar en blanco para no cambiarlo"
           :unauthorized "No tenés acceso a esta página. Por favor, contactá al administrador para más información."
           :basic-info "Información Básica"
           :permissions "Permisos"
           :search-hint "Filtrar por usuario o nombre"
           :deleted "Usuario eliminado!"}
   :permissions {:allowed-cubes "Cubes Visibles"
                 :admin-all-cubes "Administradores pueden ver todo"
                 :all-cubes "Puede ver todos los cubos"
                 :no-cubes "No puede ver ningún cubo"
                 :only-cubes-selected "Puede ver sólo los siguientes cubos"
                 :all-measures "Todas las métricas serán visibles"
                 :only-measures-selected "Sólo las siguientes métricas serán visibles"
                 :select-measures "Seleccioná las métricas permitidas"
                 :no-measures "Ninguna"
                 :add-filter "Agregar Filtro"}
   :date-formats {:second "dd/MM/yyyy HH:mm:ss"
                  :minute "dd/MM HH:mm"
                  :hour "dd/MM/yy H 'hs'"
                  :day "dd/MM/yy"
                  :month "MM/yy"}
   :calendar {:days ["D" "L" "M" "M" "J" "V" "S"]
              :months ["Enero" "Febrero" "Marzo" "Abril" "Mayo" "Junio" "Julio" "Agosto" "Septiembre" "Octubre" "Noviembre" "Diciembre"]
              :monthsShort ["Ene" "Feb" "Mar" "Abr" "May" "Jun" "Jul" "Ago" "Sep" "Oct" "Nov" "Dic"]
              :today "Hoy"
              :now "Ahora"
              :dayNames ["Domingo" "Lunes" "Martes" "Miércoles" "Jueves" "Viernes" "Sábado"]}
   :actions {:ok "Aceptar"
             :cancel "Cancelar"
             :edit "Editar"
             :save "Guardar"
             :save-as "Guardar Como"
             :new "Crear"
             :delete "Eliminar"
             :close "Cerrar"
             :select "Seleccionar"
             :hold-delete "Clickee el botón y mantenga presionado por un segundo para confirmar"
             :search "Buscar"
             :confirm "Confirmar"
             :manage "Gestionar"
             :refresh "Refrescar"
             :import "Importar"
             :double-click-edit "Double click para editar"}
   :validation {:required "este campo es obligatorio"
                :email "no es una dirección válida"
                :password "debería tener al menos 7 letras y números (o símbolos)"
                :confirmation "no coincide con el valor anterior"}
   :boolean {:true "Si"
             :false "No"}
   :errors {:no-results "No se encontraron resultados."
            :no-desc "Sin descripción"
            :bad-gateway "El sistema no está disponible en este momento. Por favor, intente nuevamente más tarde."
            :unexpected "Lo sentimos pero algo salió mal. Hemos sido notificados y lo estaremos resolviendo a la brevedad."
            :page-not-found "La página solicitada no existe. Tal vez hay un error en la dirección, o quizás la página haya sido movida."
            "Query timeout" "La consulta está demorando demasiado. Por favor, intente con un período más corto."}})
