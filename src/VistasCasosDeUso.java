import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import java.util.Map;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VistasCasosDeUso {

    private static HBox crearTopBar(Stage stage, String tituloVista) {
        Button btnHome = new Button("🏠");
        btnHome.setStyle(Estilos.BTN_HOME);
        btnHome.setOnAction(e -> stage.setScene(MenuView.getEscenaMenu(stage)));

        Label lblTitulo = new Label(tituloVista);
        lblTitulo.setStyle(Estilos.TITULO + " -fx-font-size: 18px;");

        HBox topBar = new HBox(20, btnHome, lblTitulo);
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }

    // --- MÉTODO AUXILIAR PARA AUTOCOMPLETADO DE NÚMERO DE SERIE ---
    private static void configurarAutocompletadoSerie(TextField txtSerie) {
        ContextMenu contextMenu = new ContextMenu();
        
        txtSerie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                contextMenu.hide();
                return;
            }
            
            // Obtenemos las máquinas activas de la BD
            List<MaquinariaCatalogo> maquinas = ConexionDB.obtenerMaquinasActivas();
            ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
            
            String filtro = newValue.toLowerCase();
            for (MaquinariaCatalogo maq : maquinas) {
                // Filtramos si coincide con el número de serie o el nombre
                if (maq.getNumSerie().toLowerCase().contains(filtro) || maq.toString().toLowerCase().contains(filtro)) {
                    MenuItem item = new MenuItem(maq.toString());
                    item.setOnAction(e -> {
                        txtSerie.setText(maq.getNumSerie()); // Rellena con el número de serie exacto
                        contextMenu.hide();
                    });
                    menuItems.add(item);
                }
            }
            
            if (!menuItems.isEmpty()) {
                contextMenu.getItems().setAll(menuItems);
                if (!contextMenu.isShowing()) {
                    contextMenu.show(txtSerie, Side.BOTTOM, 0, 0);
                }
            } else {
                contextMenu.hide();
            }
        });
        
        // Ocultar el menú si el campo pierde el foco
        txtSerie.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                contextMenu.hide();
            }
        });
    }

    public static Scene getReportarFalla(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtSerie = new TextField();
        txtSerie.setPromptText("Número de Serie");
        txtSerie.setStyle(Estilos.INPUT_BEIGE);
        txtSerie.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 15 ? c : null));
        
        // Aplicamos el autocompletado de sugerencias
        configurarAutocompletadoSerie(txtSerie);

        ComboBox<TipoFalla> cbTipoFalla = new ComboBox<>();
        cbTipoFalla.setPromptText("Seleccionar Tipo de Falla");
        cbTipoFalla.setStyle(Estilos.INPUT_BEIGE);
        cbTipoFalla.setPrefWidth(Double.MAX_VALUE);
        cbTipoFalla.getItems().addAll(ConexionDB.obtenerTiposFalla());

        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Descripción de la falla...");
        txtDesc.setStyle(Estilos.INPUT_BEIGE);
        txtDesc.setPrefRowCount(4);

        Button btnEnviar = new Button("Enviar Reporte");
        btnEnviar.setStyle(Estilos.BTN_MENTA);
        btnEnviar.setPrefWidth(Double.MAX_VALUE);
        
        btnEnviar.setOnAction(e -> {
            String serie = txtSerie.getText().trim();
            TipoFalla tipoSeleccionado = cbTipoFalla.getValue();
            String desc = txtDesc.getText().trim();

            if (serie.isEmpty() || tipoSeleccionado == null || desc.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Debes ingresar la serie, seleccionar el tipo de falla y escribir una descripción.");
                return;
            }
            if (!ConexionDB.existeMaquinaria(serie)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Serie no encontrada", "El número de serie '" + serie + "' no existe en el sistema.");
                return;
            }
            if (ConexionDB.registrarFalla(serie, tipoSeleccionado.getId(), desc)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El reporte se ha guardado correctamente.");
                txtSerie.clear(); cbTipoFalla.setValue(null); txtDesc.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema al guardar en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Reportar Falla"), txtSerie, cbTipoFalla, txtDesc, btnEnviar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getIngresoMaquinaria(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtSerie = new TextField();
        txtSerie.setPromptText("Número de Serie");
        txtSerie.setStyle(Estilos.INPUT_BEIGE);
        txtSerie.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 15 ? c : null));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del Equipo");
        txtNombre.setStyle(Estilos.INPUT_BEIGE);
        txtNombre.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 15 ? c : null));

        ComboBox<TipoMaquinaria> cbTipoMaquinaria = new ComboBox<>();
        cbTipoMaquinaria.setPromptText("Seleccionar Tipo de Maquinaria");
        cbTipoMaquinaria.setStyle(Estilos.INPUT_BEIGE);
        cbTipoMaquinaria.setPrefWidth(Double.MAX_VALUE);
        cbTipoMaquinaria.getItems().addAll(ConexionDB.obtenerTiposMaquinaria());

        Button btnGuardar = new Button("Registrar Equipo");
        btnGuardar.setStyle(Estilos.BTN_MENTA);
        btnGuardar.setPrefWidth(Double.MAX_VALUE);
        
        btnGuardar.setOnAction(e -> {
            String serie = txtSerie.getText().trim();
            String nombre = txtNombre.getText().trim();
            TipoMaquinaria tipoSeleccionado = cbTipoMaquinaria.getValue();

            if (serie.isEmpty() || nombre.isEmpty() || tipoSeleccionado == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Completa todos los campos y selecciona un tipo de maquinaria.");
                return;
            }
            if (ConexionDB.existeMaquinaria(serie)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Equipo duplicado", "Ya existe una máquina registrada con la serie '" + serie + "'.");
                return;
            }
            if (ConexionDB.registrarMaquinaria(serie, nombre, tipoSeleccionado.getId())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La maquinaria se ha registrado correctamente.");
                txtSerie.clear(); txtNombre.clear(); cbTipoMaquinaria.setValue(null);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema al guardar en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Alta de Maquinaria"), txtSerie, txtNombre, cbTipoMaquinaria, btnGuardar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getBitacora(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        ComboBox<String> cbFallas = new ComboBox<>();
        cbFallas.setPromptText("Falla Pendiente");
        cbFallas.setStyle(Estilos.INPUT_BEIGE);
        cbFallas.setPrefWidth(Double.MAX_VALUE);
        cbFallas.getItems().addAll(ConexionDB.obtenerFallasPendientes());

        TextField txtTiempo = new TextField();
        txtTiempo.setPromptText("Tiempo Invertido (máx. 999 min)");
        txtTiempo.setStyle(Estilos.INPUT_BEIGE);
        txtTiempo.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            return (newText.matches("\\d*") && newText.length() <= 3) ? change : null;
        }));

        ComboBox<CausaRaiz> cbCausa = new ComboBox<>();
        cbCausa.setPromptText("Causa Raíz");
        cbCausa.setStyle(Estilos.INPUT_BEIGE);
        cbCausa.setPrefWidth(Double.MAX_VALUE);
        cbCausa.getItems().addAll(ConexionDB.obtenerCausasRaiz());

        ComboBox<Refaccion> cbRefaccion = new ComboBox<>();
        cbRefaccion.setPromptText("Refacción Utilizada");
        cbRefaccion.setStyle(Estilos.INPUT_BEIGE);
        cbRefaccion.setPrefWidth(Double.MAX_VALUE);
        cbRefaccion.getItems().addAll(ConexionDB.obtenerRefacciones());

        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad de Piezas (Ej: 2)");
        txtCantidad.setStyle(Estilos.INPUT_BEIGE);
        txtCantidad.setTextFormatter(new TextFormatter<String>(c -> (c.getControlNewText().matches("\\d*")) ? c : null));

        Button btnFinalizar = new Button("Actualizar Bitácora");
        btnFinalizar.setStyle(Estilos.BTN_MENTA);
        btnFinalizar.setPrefWidth(Double.MAX_VALUE);

        btnFinalizar.setOnAction(e -> {
            String seleccion = cbFallas.getValue();
            String tiempoStr = txtTiempo.getText().trim();
            CausaRaiz causa = cbCausa.getValue();
            Refaccion refaccion = cbRefaccion.getValue();
            String cantStr = txtCantidad.getText().trim();

            if (seleccion == null || tiempoStr.isEmpty() || causa == null || refaccion == null || cantStr.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor llena todos los campos de la bitácora.");
                return;
            }
            int tiempo, cant;
            try {
                tiempo = Integer.parseInt(tiempoStr);
                cant = Integer.parseInt(cantStr);
                if (tiempo <= 0 || cant <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Dato inválido", "El tiempo y la cantidad deben ser números enteros mayores a 0.");
                return;
            }

            int idFalla = Integer.parseInt(seleccion.split(" -")[0]);

            if (ConexionDB.registrarBitacoraDetallada(idFalla, tiempo, causa.getId(), refaccion.getId(), cant)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La bitácora ha sido actualizada.");
                cbFallas.getItems().remove(seleccion); 
                cbFallas.setValue(null); txtTiempo.clear(); cbCausa.setValue(null); cbRefaccion.setValue(null); txtCantidad.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema al guardar en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Bitácora Digital"), cbFallas, txtTiempo, cbCausa, cbRefaccion, txtCantidad, btnFinalizar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getRegistrarTecnico(Stage stage) {
        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20, 30, 20, 30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Completo");
        txtNombre.setStyle(Estilos.INPUT_BEIGE);

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo Electrónico");
        txtCorreo.setStyle(Estilos.INPUT_BEIGE);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario para Login");
        txtUsuario.setStyle(Estilos.INPUT_BEIGE);
        txtUsuario.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 30 ? c : null));

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setStyle(Estilos.INPUT_BEIGE);
        txtPass.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 10 ? c : null));

        ComboBox<Departamento> cbArea = new ComboBox<>();
        cbArea.setPromptText("Área Asignada (Depto)");
        cbArea.setStyle(Estilos.INPUT_BEIGE);
        cbArea.setPrefWidth(Double.MAX_VALUE);
        cbArea.getItems().addAll(ConexionDB.obtenerDepartamentos());

        TextField txtEspecialidad = new TextField();
        txtEspecialidad.setPromptText("Especialidad (Ej: PLC, Motores)");
        txtEspecialidad.setStyle(Estilos.INPUT_BEIGE);

        Button btnRegistrar = new Button("Registrar Personal");
        btnRegistrar.setStyle(Estilos.BTN_MENTA);
        btnRegistrar.setPrefWidth(Double.MAX_VALUE);

        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String pass = txtPass.getText().trim();
            Departamento depto = cbArea.getValue();
            String esp = txtEspecialidad.getText().trim();

            if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || pass.isEmpty() || depto == null || esp.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Llena todos los campos para registrar al técnico.");
                return;
            }

            int checkUser = ConexionDB.validarUsuarioDetallado(usuario, pass);
            if (checkUser != 1) { 
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario ocupado", "Este nombre de usuario o correo ya está registrado.");
                return;
            }

            if (ConexionDB.registrarTecnico(nombre, usuario, pass, correo, depto.getId(), esp)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Técnico agregado correctamente.");
                txtNombre.clear(); txtCorreo.clear(); txtUsuario.clear(); txtPass.clear(); cbArea.setValue(null); txtEspecialidad.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el técnico en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Registro Técnicos"), txtNombre, txtCorreo, txtUsuario, txtPass, cbArea, txtEspecialidad, btnRegistrar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getProgramarMantenimiento(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        ComboBox<MaquinariaCatalogo> cbMaquinas = new ComboBox<>();
        cbMaquinas.setPromptText("Seleccionar Maquinaria");
        cbMaquinas.setStyle(Estilos.INPUT_BEIGE);
        cbMaquinas.setPrefWidth(Double.MAX_VALUE);
        cbMaquinas.getItems().addAll(ConexionDB.obtenerMaquinasActivas());

        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.setPromptText("Tipo de Umbral");
        cbTipo.setStyle(Estilos.INPUT_BEIGE);
        cbTipo.setPrefWidth(Double.MAX_VALUE);
        cbTipo.getItems().addAll("Horas", "Ciclos");

        TextField txtUmbral = new TextField();
        txtUmbral.setPromptText("Límite Uso (Ej: 5000)");
        txtUmbral.setStyle(Estilos.INPUT_BEIGE);
        txtUmbral.setTextFormatter(new TextFormatter<String>(c -> (c.getControlNewText().matches("\\d*")) ? c : null));

        ComboBox<Refaccion> cbPieza = new ComboBox<>();
        cbPieza.setPromptText("Pieza a reemplazar");
        cbPieza.setStyle(Estilos.INPUT_BEIGE);
        cbPieza.setPrefWidth(Double.MAX_VALUE);
        cbPieza.getItems().addAll(ConexionDB.obtenerRefacciones());

        TextField txtCant = new TextField();
        txtCant.setPromptText("Cantidad");
        txtCant.setStyle(Estilos.INPUT_BEIGE);
        txtCant.setTextFormatter(new TextFormatter<String>(c -> (c.getControlNewText().matches("\\d*")) ? c : null));

        Button btnGuardar = new Button("Guardar Programación");
        btnGuardar.setStyle(Estilos.BTN_MENTA);
        btnGuardar.setPrefWidth(Double.MAX_VALUE);

        btnGuardar.setOnAction(e -> {
            MaquinariaCatalogo maq = cbMaquinas.getValue();
            String tipoU = cbTipo.getValue();
            String uStr = txtUmbral.getText().trim();
            Refaccion pieza = cbPieza.getValue();
            String cStr = txtCant.getText().trim();

            if (maq == null || tipoU == null || uStr.isEmpty() || pieza == null || cStr.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Completar campos", "Debe llenar todos los parámetros para programar el mantenimiento.");
                return;
            }

            if (ConexionDB.registrarMantenimiento(maq.getNumSerie(), Integer.parseInt(uStr), tipoU, pieza.getId(), Integer.parseInt(cStr))) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Mantenimiento programado exitosamente.");
                cbMaquinas.setValue(null); cbTipo.setValue(null); txtUmbral.clear(); cbPieza.setValue(null); txtCant.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema con la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Mantenimiento"), cbMaquinas, cbTipo, txtUmbral, cbPieza, txtCant, btnGuardar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getDashboard(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        HBox boxMetricas = new HBox(15);
        boxMetricas.setAlignment(Pos.CENTER);
        
        double[] metricas = ConexionDB.obtenerMetricasDashboard();
        
        Label lblMTTR = new Label(String.format("MTTR:\n%.1f min", metricas[0]));
        lblMTTR.setStyle("-fx-background-color: #f3efe6; -fx-text-fill: #1a4449; -fx-padding: 10px; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-text-alignment: center;");
        
        Label lblMTBF = new Label(String.format("MTBF:\n%.1f min", metricas[1]));
        lblMTBF.setStyle("-fx-background-color: #aecfc0; -fx-text-fill: #1a4449; -fx-padding: 10px; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-text-alignment: center;");
        
        boxMetricas.getChildren().addAll(lblMTTR, lblMTBF);

        PieChart graficaFallas = new PieChart();
        graficaFallas.setLegendVisible(true);
        Map<String, Integer> datosBD = ConexionDB.obtenerFallasPorTipo();
        
        if (datosBD.isEmpty()) {
            graficaFallas.setTitle("Datos insuficientes para generar métricas");
        } else {
            for (Map.Entry<String, Integer> entry : datosBD.entrySet()) {
                graficaFallas.getData().add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
            }
        }

        layout.getChildren().addAll(crearTopBar(stage, "Dashboard"), boxMetricas, graficaFallas);
        return new Scene(layout, 380, 650);
    }

    public static Scene getReportes(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblTitulo = new Label("Historial de Fallas");
        lblTitulo.setStyle(Estilos.TITULO);

        TableView<ModeloReporte> tabla = new TableView<>();
        
        TableColumn<ModeloReporte, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(40);
        
        TableColumn<ModeloReporte, String> colMaquina = new TableColumn<>("Máquina");
        colMaquina.setCellValueFactory(new PropertyValueFactory<>("maquina"));
        colMaquina.setPrefWidth(100);
        
        TableColumn<ModeloReporte, String> colTipo = new TableColumn<>("Tipo Falla");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(100);
        
        TableColumn<ModeloReporte, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(80);

        tabla.getColumns().addAll(colId, colMaquina, colTipo, colEstado);
        ObservableList<ModeloReporte> datosBD = FXCollections.observableArrayList(ConexionDB.obtenerHistorialFallas());
        tabla.setItems(datosBD);

        layout.getChildren().addAll(crearTopBar(stage, "Reportes"), lblTitulo, tabla);
        return new Scene(layout, 380, 650);
    }

    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static Scene getModificarMaquinaria(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtSerie = new TextField();
        txtSerie.setPromptText("Número de Serie (existente)");
        txtSerie.setStyle(Estilos.INPUT_BEIGE);
        txtSerie.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 15 ? c : null));
        
        // Aplicamos autocompletado en Modificar Maquinaria
        configurarAutocompletadoSerie(txtSerie);

        TextField txtNuevoNombre = new TextField();
        txtNuevoNombre.setPromptText("Nuevo Nombre del Equipo");
        txtNuevoNombre.setStyle(Estilos.INPUT_BEIGE);
        txtNuevoNombre.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 15 ? c : null));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setStyle(Estilos.BTN_MENTA);
        btnGuardar.setPrefWidth(Double.MAX_VALUE);

        btnGuardar.setOnAction(e -> {
            String serie = txtSerie.getText().trim();
            String nuevoNombre = txtNuevoNombre.getText().trim();
            if (serie.isEmpty() || nuevoNombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Completa todos los campos para modificar el equipo.");
                return;
            }
            if (!ConexionDB.existeMaquinaria(serie)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Serie no encontrada", "La serie '" + serie + "' no existe en el sistema.");
                return;
            }
            if (ConexionDB.modificarMaquinaria(serie, nuevoNombre)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La maquinaria se ha actualizado correctamente.");
                txtSerie.clear(); txtNuevoNombre.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Problema al actualizar en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Modificar Maquinaria"), txtSerie, txtNuevoNombre, btnGuardar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getEliminarMaquinaria(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtSerie = new TextField();
        txtSerie.setPromptText("Número de Serie a dar de baja");
        txtSerie.setStyle(Estilos.INPUT_BEIGE);
        txtSerie.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= 15 ? c : null));
        
        // Aplicamos autocompletado en Baja de Maquinaria
        configurarAutocompletadoSerie(txtSerie);

        Button btnEliminar = new Button("Dar de Baja");
        btnEliminar.setStyle(Estilos.BTN_PELIGRO);
        btnEliminar.setPrefWidth(Double.MAX_VALUE);

        btnEliminar.setOnAction(e -> {
            String serie = txtSerie.getText().trim();
            if (serie.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "Ingresa el número de serie de la maquinaria.");
                return;
            }
            if (!ConexionDB.existeMaquinaria(serie)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Serie no encontrada", "La serie '" + serie + "' no existe en el sistema.");
                return;
            }
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Baja");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de dar de baja la serie: " + serie + "?");
            java.util.Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                if (ConexionDB.eliminarMaquinaria(serie)) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Maquinaria dada de baja (Inactivo).");
                    txtSerie.clear();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la base de datos.");
                }
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Baja de Maquinaria"), txtSerie, btnEliminar);
        return new Scene(layout, 380, 650);
    }
}