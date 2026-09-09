import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtSerie = new TextField();
        txtSerie.setPromptText("Escanear Código QR o Serie");
        txtSerie.setStyle(Estilos.INPUT_BEIGE);

        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Descripción de la falla...");
        txtDesc.setStyle(Estilos.INPUT_BEIGE);
        txtDesc.setPrefRowCount(4);

        Button btnEnviar = new Button("Enviar Reporte");
        btnEnviar.setStyle(Estilos.BTN_MENTA);
        btnEnviar.setPrefWidth(Double.MAX_VALUE);

        layout.getChildren().addAll(crearTopBar(stage, "Reportar Falla"), txtSerie, txtDesc, btnEnviar);
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

        Button btnGuardar = new Button("Registrar Equipo");
        btnGuardar.setStyle(Estilos.BTN_MENTA);
        btnGuardar.setPrefWidth(Double.MAX_VALUE);

        layout.getChildren().addAll(crearTopBar(stage, "Alta de Maquinaria"), txtSerie, txtNombre, btnGuardar);
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

        TextField txtTiempo = new TextField();
        txtTiempo.setPromptText("Tiempo Invertido (min)");
        txtTiempo.setStyle(Estilos.INPUT_BEIGE);

        Button btnFinalizar = new Button("Actualizar Bitácora");
        btnFinalizar.setStyle(Estilos.BTN_MENTA);
        btnFinalizar.setPrefWidth(Double.MAX_VALUE);

        layout.getChildren().addAll(crearTopBar(stage, "Bitácora Digital"), cbFallas, txtTiempo, btnFinalizar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getRegistrarTecnico(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del Técnico");
        txtNombre.setStyle(Estilos.INPUT_BEIGE);

        TextField txtEspecialidad = new TextField();
        txtEspecialidad.setPromptText("Especialidad (Ej. Eléctrico)");
        txtEspecialidad.setStyle(Estilos.INPUT_BEIGE);

        Button btnRegistrar = new Button("Registrar Personal");
        btnRegistrar.setStyle(Estilos.BTN_MENTA);
        btnRegistrar.setPrefWidth(Double.MAX_VALUE);

        layout.getChildren().addAll(crearTopBar(stage, "Registro Técnicos"), txtNombre, txtEspecialidad, btnRegistrar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getDashboard(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblPlaceholder = new Label("Área de Gráficas (MTTR / MTBF)");
        lblPlaceholder.setStyle(Estilos.TEXTO_BLANCO + " -fx-border-color: white; -fx-padding: 40px; -fx-alignment: center;");
        lblPlaceholder.setPrefWidth(Double.MAX_VALUE);

        Button btnExportar = new Button("Exportar a Excel");
        btnExportar.setStyle(Estilos.BTN_MENTA);
        btnExportar.setPrefWidth(Double.MAX_VALUE);

        layout.getChildren().addAll(crearTopBar(stage, "Dashboard"), lblPlaceholder, btnExportar);
        return new Scene(layout, 380, 650);
    }

    public static Scene getReportes(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        ComboBox<String> cbBitacoras = new ComboBox<>();
        cbBitacoras.setPromptText("Seleccione Bitácora Cerrada");
        cbBitacoras.setStyle(Estilos.INPUT_BEIGE);
        cbBitacoras.setPrefWidth(Double.MAX_VALUE);

        Button btnPDF = new Button("Generar PDF Salida");
        btnPDF.setStyle(Estilos.BTN_MENTA);
        btnPDF.setPrefWidth(Double.MAX_VALUE);

        layout.getChildren().addAll(crearTopBar(stage, "Reportes Salida"), cbBitacoras, btnPDF);
        return new Scene(layout, 380, 650);
    }
}
