import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MenuView {

    public static Scene getEscenaMenu(Stage stage) {
        // Reducimos el espacio entre elementos de 25 a 15 para distribuir mejor el espacio vertical
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20, 30, 20, 30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        // Barra Superior: Título y Botón Cerrar Sesión
        Label lblTitulo = new Label("S E L E C C I Ó N");
        lblTitulo.setStyle(Estilos.TEXTO_BLANCO + " -fx-font-size: 18px; -fx-letter-spacing: 3px;");
        
        Button btnCerrar = new Button("Cerrar\nsesion");
        btnCerrar.setStyle(Estilos.BTN_PELIGRO + " -fx-text-alignment: center;");
        btnCerrar.setOnAction(e -> stage.setScene(LoginView.getEscenaLogin(stage)));

        HBox topBar = new HBox(50, lblTitulo, btnCerrar);
        topBar.setAlignment(Pos.CENTER);

       
        // Menú desplegable de Maquinaria
        MenuButton mbMaquinaria = new MenuButton("Gestión\nMaquinaria");
        mbMaquinaria.setStyle(Estilos.BTN_MENTA);
        mbMaquinaria.setPrefSize(280, 65);

        // Forzar el centrado del texto interno del MenuButton
        mbMaquinaria.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            Node label = mbMaquinaria.lookup(".label");
            if (label != null) {
                label.setStyle("-fx-alignment: center; -fx-text-alignment: center;");
            }
        });

        MenuItem itemAgregar = new MenuItem("Alta de Maquinaria");
        MenuItem itemModificar = new MenuItem("Modificar Maquinaria");
        MenuItem itemEliminar = new MenuItem("Baja de Maquinaria");

        mbMaquinaria.getItems().addAll(itemAgregar, itemModificar, itemEliminar);

        itemAgregar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getIngresoMaquinaria(stage)));
        itemModificar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getModificarMaquinaria(stage)));
        itemEliminar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getEliminarMaquinaria(stage)));

        // Botones del menú con texto centrado y altura generosa de 65px
        Button btnFalla = crearBotonMenu("Reportar Falla");
        btnFalla.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportarFalla(stage)));

        Button btnBitacora = crearBotonMenu("Bitacora\nDigital");
        btnBitacora.setOnAction(e -> stage.setScene(VistasCasosDeUso.getBitacora(stage)));

        Button btnTecnico = crearBotonMenu("Registrar\nTecnico");
        btnTecnico.setOnAction(e -> stage.setScene(VistasCasosDeUso.getRegistrarTecnico(stage)));

        Button btnDashboard = crearBotonMenu("Dashboard");
        btnDashboard.setOnAction(e -> stage.setScene(VistasCasosDeUso.getDashboard(stage)));

        Button btnReportes = crearBotonMenu("Reportes");
        btnReportes.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportes(stage)));

        layout.getChildren().addAll(topBar, btnFalla, mbMaquinaria, btnBitacora, btnTecnico, btnDashboard, btnReportes);
        return new Scene(layout, 380, 650);
    }

    private static Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(Estilos.BTN_MENTA);
        // Ancho de 280 y alto de 65 para dar suficiente espacio a textos de 1 o 2 líneas
        btn.setPrefSize(280, 65);
        return btn;
    }
}