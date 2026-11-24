import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    // Carga el logo desde la raíz del proyecto con mayor presencia visual
    private static ImageView crearLogo(double ancho) {
        ImageView logoView = new ImageView();
        try {
            Image logoImg = new Image("file:logo.png");
            logoView.setImage(logoImg);
            logoView.setFitWidth(ancho);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("No se pudo cargar logo.png: " + e.getMessage());
        }
        return logoView;
    }

    // Helper para envolver TextField con un ícono a la izquierda como en el mockup
    private static HBox crearInputConIcono(String icono, TextField campo) {
        HBox contenedor = new HBox(10);
        contenedor.setAlignment(Pos.CENTER_LEFT);
        contenedor.setMaxWidth(300);
        contenedor.setPrefHeight(46);
        contenedor.setStyle(Estilos.CAJA_INPUT_ICONO);

        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-text-fill: #64748B; -fx-font-size: 15px;");

        campo.setStyle(Estilos.INPUT_TRANSPARENTE);
        HBox.setHgrow(campo, Priority.ALWAYS);

        contenedor.getChildren().addAll(lblIcono, campo);
        return contenedor;
    }

    public static Scene getEscenaLogin(Stage stage) {
        VBox layout = new VBox(14);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25, 35, 25, 35));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        // 1. Título superior espaciado
        Label lblTitulo = new Label("I N I C I A R   S E S I Ó N");
        lblTitulo.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13px; -fx-font-weight: bold;");

        // 2. Logo grande centrado
        ImageView logoView = crearLogo(115);
        VBox.setMargin(logoView, new Insets(4, 0, 2, 0));

        // 3. Bienvenido
        Label lblBienvenido = new Label("Bienvenido");
        lblBienvenido.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 26px; -fx-font-weight: bold;");
        VBox.setMargin(lblBienvenido, new Insets(0, 0, 8, 0));

        // 4. Campo Usuario con icono
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Usuario");
        txtEmail.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 50 ? change : null));
        HBox boxUsuario = crearInputConIcono("👤", txtEmail);

        // 5. Campo Contraseña con icono
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 10 ? change : null));
        HBox boxPass = crearInputConIcono("🔒", txtPass);

        // 6. Enlace recuperar contraseña
        Hyperlink linkOlvido = new Hyperlink("¿Olvidaste tu contraseña?");
        linkOlvido.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px; -fx-border-color: transparent;");
        linkOlvido.setOnAction(e -> stage.setScene(getEscenaRecuperar(stage)));

        // 7. Botón Principal Beige
        Button btnLogin = new Button("Iniciar sesión");
        btnLogin.setStyle(Estilos.BTN_MENTA);
        btnLogin.setMaxWidth(300);
        btnLogin.setPrefHeight(46);
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle(Estilos.BTN_MENTA_HOVER));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle(Estilos.BTN_MENTA));
        
        btnLogin.setOnAction(e -> {
            String usuario = txtEmail.getText().trim();
            String pass = txtPass.getText();

            if (usuario.isEmpty() && pass.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Debes ingresar tu usuario y tu contraseña.");
                return; 
            }
            if (usuario.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Falta Usuario", "Por favor, ingresa tu nombre de usuario o email.");
                return;
            }
            if (pass.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Falta Contraseña", "Por favor, ingresa tu contraseña.");
                return;
            }

            int resultado = ConexionDB.validarUsuarioDetallado(usuario, pass);
            
            switch (resultado) {
                case 0:
                    txtEmail.clear();
                    txtPass.clear();
                    stage.setScene(MenuView.getEscenaMenu(stage));
                    break;
                case 1:
                    mostrarAlerta(Alert.AlertType.ERROR, "Usuario incorrecto", "El usuario ingresado no existe en nuestra base de datos.");
                    break;
                case 2:
                    mostrarAlerta(Alert.AlertType.ERROR, "Contraseña incorrecta", "La contraseña es incorrecta. Inténtalo nuevamente.");
                    break;
                case 3:
                    mostrarAlerta(Alert.AlertType.ERROR, "Acceso denegado", "Este usuario se encuentra inactivo o dado de baja.");
                    break;
                default:
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de sistema", "No se pudo conectar a la base de datos.");
                    break;
            }
        });

        // 8. Crear cuenta
        Hyperlink linkCrear = new Hyperlink("Crear cuenta");
        linkCrear.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13px; -fx-border-color: transparent;");
        linkCrear.setOnAction(e -> stage.setScene(getEscenaRegistro(stage)));

        VBox boxInferior = new VBox(linkCrear);
        boxInferior.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(boxInferior, new Insets(8, 0, 0, 0));

        layout.getChildren().addAll(lblTitulo, logoView, lblBienvenido, boxUsuario, boxPass, linkOlvido, btnLogin, boxInferior);
        return new Scene(layout, 380, 650);
    }

    public static Scene getEscenaRegistro(Stage stage) {
        VBox layout = new VBox(14);
        layout.setPadding(new Insets(25, 35, 25, 35));
        layout.setAlignment(Pos.CENTER); 
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        ImageView logoView = crearLogo(85);

        Label lblTitulo = new Label("Crear Cuenta");
        lblTitulo.setStyle(Estilos.TITULO + " -fx-font-size: 22px;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Completo");
        txtNombre.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 40 ? change : null));
        HBox boxNombre = crearInputConIcono("🪪", txtNombre);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de Usuario");
        txtUsuario.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 30 ? change : null));
        HBox boxUsuario = crearInputConIcono("👤", txtUsuario);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 10 ? change : null));
        HBox boxPass = crearInputConIcono("🔒", txtPass);

        Button btnRegistrar = new Button("Registrarse");
        btnRegistrar.setStyle(Estilos.BTN_MENTA);
        btnRegistrar.setMaxWidth(300);
        btnRegistrar.setPrefHeight(45);
        btnRegistrar.setOnMouseEntered(e -> btnRegistrar.setStyle(Estilos.BTN_MENTA_HOVER));
        btnRegistrar.setOnMouseExited(e -> btnRegistrar.setStyle(Estilos.BTN_MENTA));

        Hyperlink linkVolver = new Hyperlink("¿Ya tienes cuenta? Inicia sesión");
        linkVolver.setStyle("-fx-text-fill: #E6DCCD; -fx-font-size: 12px; -fx-border-color: transparent;"); 

        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String pass = txtPass.getText().trim();

            if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor llena todos los campos.");
                return;
            }

            int checkUser = ConexionDB.validarUsuarioDetallado(usuario, pass);
            if (checkUser != 1) { 
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario ocupado", "Este nombre de usuario ya está registrado. Elige otro.");
                return;
            }

            if (ConexionDB.registrarUsuarioPublico(nombre, usuario, pass)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Cuenta Creada", "Tu cuenta se ha creado con éxito. Ahora puedes iniciar sesión.");
                stage.setScene(getEscenaLogin(stage));
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema de conexión. Inténtalo más tarde.");
            }
        });

        linkVolver.setOnAction(e -> stage.setScene(getEscenaLogin(stage)));

        layout.getChildren().addAll(logoView, lblTitulo, boxNombre, boxUsuario, boxPass, btnRegistrar, linkVolver);
        return new Scene(layout, 380, 650);
    }

    public static Scene getEscenaRecuperar(Stage stage) {
        VBox layout = new VBox(16);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(35));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        ImageView logoView = crearLogo(85);

        Label lblTitulo = new Label("Recuperar Acceso");
        lblTitulo.setStyle(Estilos.TITULO + " -fx-font-size: 22px;");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingresa tu Email o Usuario");
        txtUsuario.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 30 ? change : null));
        HBox boxUsuario = crearInputConIcono("✉️", txtUsuario);

        Button btnRecuperar = new Button("Enviar Código");
        btnRecuperar.setStyle(Estilos.BTN_MENTA);
        btnRecuperar.setMaxWidth(300);
        btnRecuperar.setPrefHeight(45);
        btnRecuperar.setOnMouseEntered(e -> btnRecuperar.setStyle(Estilos.BTN_MENTA_HOVER));
        btnRecuperar.setOnMouseExited(e -> btnRecuperar.setStyle(Estilos.BTN_MENTA));

        Button btnVolver = new Button("Volver al Login");
        btnVolver.setStyle(Estilos.BTN_PELIGRO);
        btnVolver.setMaxWidth(300);
        btnVolver.setPrefHeight(38);
        btnVolver.setOnAction(e -> stage.setScene(getEscenaLogin(stage)));

        layout.getChildren().addAll(logoView, lblTitulo, boxUsuario, btnRecuperar, btnVolver);
        return new Scene(layout, 380, 650);
    }
    
    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}