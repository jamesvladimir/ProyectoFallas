import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView {

    public static Scene getEscenaMenu(Stage stage) {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        // Barra Superior: Título y Botón Cerrar Sesión
        Label lblTitulo = new Label("S E L E C C I Ó N");
        lblTitulo.setStyle(Estilos.TEXTO_BLANCO + " -fx-font-size: 18px; -fx-letter-spacing: 3px;");
        
        Button btnCerrar = new Button("Cerrar\nsesion");
        btnCerrar.setStyle(Estilos.BTN_PELIGRO + " -fx-text-alignment: center;");
        btnCerrar.setOnAction(e -> stage.setScene(LoginView.getEscenaLogin(stage)));

        HBox topBar = new HBox(70, lblTitulo, btnCerrar);
        topBar.setAlignment(Pos.CENTER);

        // Botones del menú
        Button btnFalla = crearBotonMenu("ReportarFalla");
        btnFalla.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportarFalla(stage)));

        Button btnMaquinaria = crearBotonMenu("Ingreso\nMaquinaria");
        btnMaquinaria.setOnAction(e -> stage.setScene(VistasCasosDeUso.getIngresoMaquinaria(stage)));

        Button btnBitacora = crearBotonMenu("Bitacora\nDigital");
        btnBitacora.setOnAction(e -> stage.setScene(VistasCasosDeUso.getBitacora(stage)));

        Button btnTecnico = crearBotonMenu("Registrar\nTecnico");
        btnTecnico.setOnAction(e -> stage.setScene(VistasCasosDeUso.getRegistrarTecnico(stage)));

        Button btnDashboard = crearBotonMenu("Dashboard");
        btnDashboard.setOnAction(e -> stage.setScene(VistasCasosDeUso.getDashboard(stage)));

        Button btnReportes = crearBotonMenu("Reportes");
        btnReportes.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportes(stage)));

        layout.getChildren().addAll(topBar, btnFalla, btnMaquinaria, btnBitacora, btnTecnico, btnDashboard, btnReportes);
        return new Scene(layout, 380, 650);
    }

    private static Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(Estilos.BTN_MENTA + " -fx-text-alignment: center; -fx-font-size: 18px;");
        btn.setPrefSize(280, 60);
        return btn;
    }
}