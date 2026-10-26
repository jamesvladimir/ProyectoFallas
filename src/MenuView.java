import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView {

    public static Scene getEscenaMenu(Stage stage) {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20, 30, 20, 30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        // Barra Superior
        Label lblTitulo = new Label("S E L E C C I Ó N");
        lblTitulo.setStyle(Estilos.TEXTO_BLANCO + " -fx-font-size: 18px; -fx-letter-spacing: 3px;");
        
        Button btnCerrar = new Button("Cerrar\nsesion");
        btnCerrar.setStyle(Estilos.BTN_PELIGRO + " -fx-text-alignment: center;");
        btnCerrar.setOnAction(e -> {
            ConexionDB.idRolActual = 0; // Limpiamos la sesión al salir
            stage.setScene(LoginView.getEscenaLogin(stage));
        });

        HBox topBar = new HBox(50, lblTitulo, btnCerrar);
        topBar.setAlignment(Pos.CENTER);
       
        // Menú desplegable de Maquinaria
        MenuButton mbMaquinaria = new MenuButton("Gestión\nMaquinaria");
        mbMaquinaria.setStyle(Estilos.BTN_MENTA);
        mbMaquinaria.setPrefSize(280, 65);

        mbMaquinaria.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            Node label = mbMaquinaria.lookup(".label");
            if (label != null) {
                label.setStyle("-fx-alignment: center; -fx-text-alignment: center;");
            }
        });

        MenuItem itemAgregar = new MenuItem("Alta de Maquinaria");
        MenuItem itemModificar = new MenuItem("Modificar Maquinaria");
        MenuItem itemEliminar = new MenuItem("Baja de Maquinaria");
        MenuItem itemMantenimiento = new MenuItem("Programar Mantenimiento"); // Nuevo Bloque 2

        mbMaquinaria.getItems().addAll(itemAgregar, itemModificar, itemEliminar, itemMantenimiento);

        itemAgregar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getIngresoMaquinaria(stage)));
        itemModificar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getModificarMaquinaria(stage)));
        itemEliminar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getEliminarMaquinaria(stage)));
        itemMantenimiento.setOnAction(e -> stage.setScene(VistasCasosDeUso.getProgramarMantenimiento(stage)));

        // Botones principales
        Button btnFalla = crearBotonMenu("Reportar Falla");
        btnFalla.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportarFalla(stage)));

        Button btnBitacora = crearBotonMenu("Bitacora\nDigital");
        btnBitacora.setOnAction(e -> stage.setScene(VistasCasosDeUso.getBitacora(stage)));

        Button btnTecnico = crearBotonMenu("Registrar\nTecnico");
        btnTecnico.setOnAction(e -> {
            // SEGURIDAD: Solo permite el acceso si el rol es 1 (Administrador)
            if (ConexionDB.idRolActual == 1) {
                stage.setScene(VistasCasosDeUso.getRegistrarTecnico(stage));
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Acceso Denegado");
                alerta.setHeaderText(null);
                alerta.setContentText("Acceso restringido. Solo los administradores pueden registrar nuevo personal técnico.");
                alerta.showAndWait();
            }
        });

        Button btnDashboard = crearBotonMenu("Dashboard de Eficiencia");
        btnDashboard.setOnAction(e -> stage.setScene(VistasCasosDeUso.getDashboard(stage)));

        Button btnReportes = crearBotonMenu("Reportes");
        btnReportes.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportes(stage)));

        layout.getChildren().addAll(topBar, btnFalla, mbMaquinaria, btnBitacora, btnTecnico, btnDashboard, btnReportes);
        return new Scene(layout, 380, 650);
    }

    private static Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(Estilos.BTN_MENTA);
        btn.setPrefSize(280, 65);
        return btn;
    }
}