import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView {

    public static Scene getEscenaMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(22, 30, 20, 30));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        // Barra Superior (S E L E C C I Ó N + Cerrar sesión)
        Label lblTitulo = new Label("S E L E C C I Ó N");
        lblTitulo.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnCerrar = new Button("Cerrar\nsesión");
        btnCerrar.setStyle(Estilos.BTN_PELIGRO + " -fx-text-alignment: center;");
        btnCerrar.setOnAction(e -> {
            ConexionDB.idRolActual = 0;
            stage.setScene(LoginView.getEscenaLogin(stage));
        });

        HBox topBar = new HBox(10, lblTitulo, spacer, btnCerrar);
        topBar.setAlignment(Pos.CENTER);
        topBar.setMaxWidth(310);
        VBox.setMargin(topBar, new Insets(0, 0, 6, 0));

        String estiloMenuItem = "-fx-font-size: 13px; -fx-padding: 8 15 8 15; -fx-text-fill: #0F1E36;";

        // 1. Reportar Falla
        Button btnFalla = crearBotonMenuConIcono("⚠️", "Reportar Falla");
        btnFalla.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportarFalla(stage)));

        // 2. Menú desplegable de Gestión Maquinaria
        MenuButton mbMaquinaria = crearMenuButtonConIcono("⚙️", "Gestión Maquinaria");

        MenuItem itemAgregar = new MenuItem("➕   Alta de Maquinaria");
        MenuItem itemModificar = new MenuItem("✏️   Modificar Maquinaria");
        MenuItem itemEliminar = new MenuItem("📉   Baja de Maquinaria");
        MenuItem itemMantenimiento = new MenuItem("🔧   Programar Mantenimiento");

        itemAgregar.setStyle(estiloMenuItem);
        itemModificar.setStyle(estiloMenuItem);
        itemEliminar.setStyle(estiloMenuItem);
        itemMantenimiento.setStyle(estiloMenuItem);

        mbMaquinaria.getItems().addAll(itemAgregar, itemModificar, itemEliminar, itemMantenimiento);

        itemAgregar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getIngresoMaquinaria(stage)));
        itemModificar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getModificarMaquinaria(stage)));
        itemEliminar.setOnAction(e -> stage.setScene(VistasCasosDeUso.getEliminarMaquinaria(stage)));
        itemMantenimiento.setOnAction(e -> stage.setScene(VistasCasosDeUso.getProgramarMantenimiento(stage)));

        // 3. Bitácora Digital
        Button btnBitacora = crearBotonMenuConIcono("📓", "Bitácora Digital");
        btnBitacora.setOnAction(e -> stage.setScene(VistasCasosDeUso.getBitacora(stage)));

        // 4. Registrar Técnico (Solo Administradores - idRolActual == 1)
        Button btnTecnico = crearBotonMenuConIcono("👷", "Registrar Técnico");
        btnTecnico.setOnAction(e -> {
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

        // 5. Dashboard de Eficiencia
        Button btnDashboard = crearBotonMenuConIcono("📊", "Dashboard de Eficiencia");
        btnDashboard.setOnAction(e -> stage.setScene(VistasCasosDeUso.getDashboard(stage)));

        // 6. Historial de Soluciones (Bloque 3)
        Button btnSoluciones = crearBotonMenuConIcono("💡", "Historial de Soluciones");
        btnSoluciones.setOnAction(e -> stage.setScene(VistasCasosDeUso.getHistorialSoluciones(stage)));

        // 7. Menú desplegable de Reportes (Tabla + Exportar PDF Bloque 3)
        MenuButton mbReportes = crearMenuButtonConIcono("📄", "Reportes");

        MenuItem itemTablaReportes = new MenuItem("📋   Historial de Fallas (Tabla)");
        MenuItem itemReportePDF = new MenuItem("📑   Generar Reporte en PDF");

        itemTablaReportes.setStyle(estiloMenuItem);
        itemReportePDF.setStyle(estiloMenuItem);

        mbReportes.getItems().addAll(itemTablaReportes, itemReportePDF);

        itemTablaReportes.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportes(stage)));
        itemReportePDF.setOnAction(e -> stage.setScene(VistasCasosDeUso.getReportePDF(stage)));

        layout.getChildren().addAll(
            topBar,
            btnFalla,
            mbMaquinaria,
            btnBitacora,
            btnTecnico,
            btnDashboard,
            btnSoluciones,
            mbReportes
        );

        return new Scene(layout, 380, 650);
    }

    private static Button crearBotonMenuConIcono(String icono, String texto) {
        Button btn = new Button();
        btn.setStyle(Estilos.BTN_MENU_ITEM);
        btn.setPrefSize(310, 52);
        btn.setMaxWidth(310);

        HBox contenido = new HBox(14);
        contenido.setAlignment(Pos.CENTER_LEFT);
        
        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 19px; -fx-text-fill: #0F1E36;");
        
        Label lblTexto = new Label(texto);
        lblTexto.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0F1E36;");
        
        contenido.getChildren().addAll(lblIcono, lblTexto);
        btn.setGraphic(contenido);

        btn.setOnMouseEntered(e -> btn.setStyle(Estilos.BTN_MENU_ITEM_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(Estilos.BTN_MENU_ITEM));

        return btn;
    }

    private static MenuButton crearMenuButtonConIcono(String icono, String texto) {
        MenuButton mb = new MenuButton();
        mb.setStyle(Estilos.BTN_MENU_ITEM);
        mb.setPrefSize(310, 52);
        mb.setMaxWidth(310);

        HBox contenido = new HBox(14);
        contenido.setAlignment(Pos.CENTER_LEFT);
        
        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 19px; -fx-text-fill: #0F1E36;");
        
        Label lblTexto = new Label(texto);
        lblTexto.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0F1E36;");
        
        contenido.getChildren().addAll(lblIcono, lblTexto);
        mb.setGraphic(contenido);

        mb.setOnMouseEntered(e -> mb.setStyle(Estilos.BTN_MENU_ITEM_HOVER));
        mb.setOnMouseExited(e -> mb.setStyle(Estilos.BTN_MENU_ITEM));

        return mb;
    }
}