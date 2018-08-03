(ns shevek.locales.es)

(def translations
  {:menu {:logout "Salir"}
   :home {:menu "Inicio"
          :title "Bienvenido!"
          :subtitle "Qué le gustaría analizar hoy?"}
   :cubes {:title "Cubos de Datos"
           :menu "Cubos"
           :missing "No hay cubos definidos"
           :data-range "Rango de datos disponibles"}
   :dashboards {:title "Dashboards"
                :missing "No se ha creado ningún dashboard todavía"
                :saved "Dashboard '{1}' guardado correctamente"
                :deleted "Dashboard '{1}' eliminado correctamente"
                :report-count #(cond
                                 (zero? %) "Sin reportes"
                                 (= 1 %) "1 reporte"
                                 :else "{1} reportes")
                :no-reports "Este dashboard no tiene reportes"
                :updated-at "Última actualización"}
   :reports {:title "Reportes"
             :missing "No se ha guardado ningún reporte todavía"
             :name "Nombre"
             :description "Descripción"
             :dashboards "Mostrar en Dashboards"
             :updated-at "Última actualización"
             :saved "Reporte '{1}' guardado correctamente"
             :deleted "Reporte '{1}' eliminado correctamente"
             :unauthorized "Oops! Este reporte ya no está disponible."}
   :viewer {:dimensions "Dimensiones"
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
            :unauthorized "Parece que el cubo {1} ya no está disponible. Por favor, contacte al administrador."
            :toggle-fullscreen "Mostrar en Pantalla Completa"}
   :viewer.period {:relative "Relativo"
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
   :viewer.operator {:include "Incluir"
                     :exclude "Excluir"}
   :viewer.viztype {:totals "totales"
                    :table "tabla"
                    :bar-chart "barras"
                    :line-chart "línea"
                    :pie-chart "torta"}
   :share {:title "Compartir"
           :copy-url "Copiar URL"
           :copied "URL Copiado!"}
   :raw-data {:menu "Ver datos desagregados"
              :title "Datos Desagregados"
              :showing "Primeros {1} eventos según filtro: "
              :button "Datos Desagregados"}
   :settings {:menu "Preferencias"
              :lang "Lenguaje"
              :update-now "Actualizar Ahora"
              :auto-refresh "Refrescar Automáticamente"
              :auto-refresh-opts (fn [] [["Nunca" 0] ["Cada 10 segundos" 10] ["Cada 30 segundos" 30] ["Cada 1 minuto" 60] ["Cada 10 minutos" 600] ["Cada 30 minutos" 1800]])
              :abbreviations "Abreviaturas de Números"
              :abbreviations-opts (fn [] [["User formato por defecto" "default"] ["No abreviar nada" "no"] ["Abreviar todo" "yes"]])}
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
           :unauthorized "Ud. no tiene acceso a esta página. Por favor, contacte al administrador para más información."
           :basic-info "Información Básica"
           :permissions "Permisos"}
   :permissions {:allowed-cubes "Cubes Visibles"
                 :admin-all-cubes "Administradores pueden ver todo"
                 :all-cubes "Puede ver todos los cubos"
                 :no-cubes "No puede ver ningún cubo"
                 :only-cubes-selected "Puede ver sólo los siguientes cubos"
                 :all-measures "Todas las métricas serán visibles"
                 :only-measures-selected "Sólo las siguientes métricas serán visibles"
                 :select-measures "Por favor seleccion las métricas permitidas"
                 :no-measures "Ninguna"
                 :add-filter "Agregar Filtro"}
   :account {:title "Tu Cuenta"
             :subtitle "Aquí puede cambiar los detalles de su perfil"
             :current-password "Password Actual"
             :new-password "Nuevo Password"
             :saved "Tu cuenta se grabó correctamente"
             :invalid-current-password "es incorrecto"}
   :date-formats {:second "dd/MM/yyyy HH:mm:ss"
                  :minute "dd/MM HH:mm"
                  :hour "dd/MM/yy H 'hs'"
                  :day "dd/MM/yy"
                  :month "MM/yy"}
   :calendar {:days ["D" "L" "M" "M" "J" "V" "S"]
              :months ["Enero" "Febrero" "Marzo" "Abril" "Mayo" "Junio" "Julio" "Agosto" "Septiembre" "Octubre" "Noviembre" "Diciembre"]
              :monthsShort ["Ene" "Feb" "Mar" "Abr" "May" "Jun" "Jul" "Ago" "Sep" "Oct" "Nov" "Dic"]
              :today "Hoy"
              :now "Ahora"}
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
             :search "Buscar"}
   :validation {:required "este campo es obligatorio"
                :email "no es una dirección válida"
                :password "debería tener al menos 7 letras y números (o símbolos)"
                :confirmation "no coincide con el valor anterior"}
   :boolean {:true "Si"
             :false "No"}
   :errors {:no-results "No se encontraron resultados"
            :no-desc "Sin descripción"
            :bad-gateway "El sistema no está disponible en este momento. Por favor, intente nuevamente más tarde."
            "Query timeout" "La consulta está demorando demasiado. Por favor, intente con un período más corto."}})