import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import java.util.Map;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VistasCasosDeUso {

    // Método utilitario para crear la barra superior con el botón de "Casita"
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

 public static Scene getReportarFalla(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtSerie = new TextField();
        txtSerie.setPromptText("Número de Serie");
        txtSerie.setStyle(Estilos.INPUT_BEIGE);

        // ComboBox para seleccionar el tipo de falla cargado desde la BD
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

            // 1. Validar campos vacíos incluyendo el ComboBox
            if (serie.isEmpty() || tipoSeleccionado == null || desc.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Debes ingresar la serie, seleccionar el tipo de falla y escribir una descripción.");
                return;
            }

            // 2. Verificar que la máquina exista en la BD
            if (!ConexionDB.existeMaquinaria(serie)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Serie no encontrada", "El número de serie '" + serie + "' no existe en el sistema.");
                return;
            }

            // 3. Insertar la falla enviando el ID del tipo seleccionado
            if (ConexionDB.registrarFalla(serie, tipoSeleccionado.getId(), desc)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El reporte se ha guardado correctamente.");
                txtSerie.clear();
                cbTipoFalla.setValue(null);
                txtDesc.clear();
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

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del Equipo");
        txtNombre.setStyle(Estilos.INPUT_BEIGE);

        // ComboBox para desplegar los tipos de maquinaria de la BD
        ComboBox<TipoMaquinaria> cbTipoMaquinaria = new ComboBox<>();
        cbTipoMaquinaria.setPromptText("Seleccionar Tipo de Maquinaria");
        cbTipoMaquinaria.setStyle(Estilos.INPUT_BEIGE);
        cbTipoMaquinaria.setPrefWidth(Double.MAX_VALUE);
        
        // Llenamos el ComboBox con los datos reales de SQL Server
        cbTipoMaquinaria.getItems().addAll(ConexionDB.obtenerTiposMaquinaria());

        Button btnGuardar = new Button("Registrar Equipo");
        btnGuardar.setStyle(Estilos.BTN_MENTA);
        btnGuardar.setPrefWidth(Double.MAX_VALUE);
        
        btnGuardar.setOnAction(e -> {
            String serie = txtSerie.getText().trim();
            String nombre = txtNombre.getText().trim();
            TipoMaquinaria tipoSeleccionado = cbTipoMaquinaria.getValue();

            // 1. Validar campos vacíos incluyendo el ComboBox
            if (serie.isEmpty() || nombre.isEmpty() || tipoSeleccionado == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Completa todos los campos y selecciona un tipo de maquinaria.");
                return;
            }

            // 2. Validar que la máquina NO exista ya
            if (ConexionDB.existeMaquinaria(serie)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Equipo duplicado", "Ya existe una máquina registrada con el número de serie '" + serie + "'.");
                return;
            }

            // 3. Insertar la nueva máquina pasando el ID del tipo seleccionado
            if (ConexionDB.registrarMaquinaria(serie, nombre, tipoSeleccionado.getId())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La maquinaria se ha registrado correctamente.");
                txtSerie.clear();
                txtNombre.clear();
                cbTipoMaquinaria.setValue(null);
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
        cbFallas.setPromptText("Seleccionar Falla Pendiente");
        cbFallas.setStyle(Estilos.INPUT_BEIGE);
        cbFallas.setPrefWidth(Double.MAX_VALUE);
        // Llenar el ComboBox con datos reales de la BD
        cbFallas.getItems().addAll(ConexionDB.obtenerFallasPendientes());

        TextField txtTiempo = new TextField();
        txtTiempo.setPromptText("Tiempo Invertido (solo números)");
        txtTiempo.setStyle(Estilos.INPUT_BEIGE);

        Button btnFinalizar = new Button("Actualizar Bitácora");
        btnFinalizar.setStyle(Estilos.BTN_MENTA);
        btnFinalizar.setPrefWidth(Double.MAX_VALUE);

        btnFinalizar.setOnAction(e -> {
            String seleccion = cbFallas.getValue();
            String tiempoStr = txtTiempo.getText().trim();

            if (seleccion == null || tiempoStr.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, selecciona una falla e ingresa el tiempo invertido.");
                return;
            }

            int tiempo;
            try {
                // Validamos que de verdad haya escrito un número
                tiempo = Integer.parseInt(tiempoStr);
                if (tiempo <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Dato inválido", "El tiempo debe ser un número entero mayor a 0 (ej. 45).");
                return;
            }

            // Extraemos el ID de la falla de la cadena seleccionada (ej: "1 - Máquina: EXT-001" -> 1)
            int idFalla = Integer.parseInt(seleccion.split(" -")[0]);

            if (ConexionDB.registrarBitacora(idFalla, tiempo)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La bitácora ha sido actualizada.");
                cbFallas.getItems().remove(seleccion); // Quitamos la falla de la lista porque ya se atendió
                cbFallas.setValue(null);
                txtTiempo.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema al guardar en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Bitácora Digital"), cbFallas, txtTiempo, btnFinalizar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getRegistrarTecnico(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Completo");
        txtNombre.setStyle(Estilos.INPUT_BEIGE);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario para Login");
        txtUsuario.setStyle(Estilos.INPUT_BEIGE);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setStyle(Estilos.INPUT_BEIGE);

        Button btnRegistrar = new Button("Registrar Personal");
        btnRegistrar.setStyle(Estilos.BTN_MENTA);
        btnRegistrar.setPrefWidth(Double.MAX_VALUE);

        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String pass = txtPass.getText().trim();

            if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Llena todos los campos para registrar al técnico.");
                return;
            }

            // Reutilizamos el método que creaste antes en el Login para validar si el usuario existe
            int checkUser = ConexionDB.validarUsuarioDetallado(usuario, pass);
            if (checkUser != 1) { // Si no retorna 1, significa que el usuario SÍ existe en la BD
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario ocupado", "Este nombre de usuario ya está registrado.");
                return;
            }

            if (ConexionDB.registrarTecnico(nombre, usuario, pass)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Técnico agregado correctamente. Ya puede iniciar sesión.");
                txtNombre.clear();
                txtUsuario.clear();
                txtPass.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el técnico en la base de datos.");
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Registro Técnicos"), txtNombre, txtUsuario, txtPass, btnRegistrar);
        return new Scene(layout, 380, 650);
    }

  public static Scene getDashboard(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblTitulo = new Label("Resumen de Fallas");
        lblTitulo.setStyle(Estilos.TITULO);

        PieChart graficaFallas = new PieChart();
        graficaFallas.setLegendVisible(true);
        
        // Llamamos a la BD y llenamos la gráfica
        Map<String, Integer> datosBD = ConexionDB.obtenerFallasPorTipo();
        
        if (datosBD.isEmpty()) {
            graficaFallas.setTitle("No hay fallas reportadas");
        } else {
            for (Map.Entry<String, Integer> entry : datosBD.entrySet()) {
                // Formato: "Mecánica (3)"
                String etiqueta = entry.getKey() + " (" + entry.getValue() + ")";
                graficaFallas.getData().add(new PieChart.Data(etiqueta, entry.getValue()));
            }
        }

        layout.getChildren().addAll(crearTopBar(stage, "Dashboard"), lblTitulo, graficaFallas);
        return new Scene(layout, 380, 650);
    }

  public static Scene getReportes(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblTitulo = new Label("Historial de Fallas");
        lblTitulo.setStyle(Estilos.TITULO);

        // 1. Crear la tabla
        TableView<ModeloReporte> tabla = new TableView<>();
        
        // 2. Definir las columnas y conectarlas con la clase ModeloReporte
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

        // 3. Agregar las columnas a la tabla
        tabla.getColumns().addAll(colId, colMaquina, colTipo, colEstado);
        
        // 4. Llenar la tabla con los datos de SQL Server
        ObservableList<ModeloReporte> datosBD = FXCollections.observableArrayList(ConexionDB.obtenerHistorialFallas());
        tabla.setItems(datosBD);

        layout.getChildren().addAll(crearTopBar(stage, "Reportes"), lblTitulo, tabla);
        return new Scene(layout, 380, 650);
    }

    // Método pa alertas
    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
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

        TextField txtNuevoNombre = new TextField();
        txtNuevoNombre.setPromptText("Nuevo Nombre del Equipo");
        txtNuevoNombre.setStyle(Estilos.INPUT_BEIGE);

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
                mostrarAlerta(Alert.AlertType.ERROR, "Serie no encontrada", "El número de serie '" + serie + "' no existe en el sistema.");
                return;
            }

            if (ConexionDB.modificarMaquinaria(serie, nuevoNombre)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La maquinaria se ha actualizado correctamente.");
                txtSerie.clear();
                txtNuevoNombre.clear();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema al actualizar en la base de datos.");
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
                mostrarAlerta(Alert.AlertType.ERROR, "Serie no encontrada", "El número de serie '" + serie + "' no existe en el sistema.");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Baja");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de dar de baja la maquinaria con serie: " + serie + "?");

            java.util.Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                if (ConexionDB.eliminarMaquinaria(serie)) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La maquinaria fue dada de baja (Estatus: Inactivo).");
                    txtSerie.clear();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el estatus de la maquinaria en la base de datos.");
                }
            }
        });

        layout.getChildren().addAll(crearTopBar(stage, "Baja de Maquinaria"), txtSerie, btnEliminar);
        return new Scene(layout, 380, 650);
    }
}
