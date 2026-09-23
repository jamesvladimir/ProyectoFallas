import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    public static Scene getEscenaLogin(Stage stage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblLogo = new Label("I N I C I A R\nS E S I Ó N");
        lblLogo.setStyle(Estilos.TEXTO_BLANCO + " -fx-font-size: 18px; -fx-text-alignment: center;");
        
        Label lblBienvenido = new Label("Bienvenido");
        lblBienvenido.setStyle(Estilos.TITULO);
        
        VBox boxTextos = new VBox(5, lblLogo, new Label(""), lblBienvenido);
        boxTextos.setAlignment(Pos.CENTER_LEFT);

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtEmail.setStyle(Estilos.INPUT_BEIGE);
        txtEmail.setMaxWidth(300);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setStyle(Estilos.INPUT_BEIGE);
        txtPass.setMaxWidth(300);

        Hyperlink linkOlvido = new Hyperlink("¿Olvidaste tu contraseña?");
        linkOlvido.setStyle(Estilos.TEXTO_BLANCO);
        linkOlvido.setOnAction(e -> stage.setScene(getEscenaRecuperar(stage)));

        Button btnLogin = new Button("Iniciar sesión");
        btnLogin.setStyle(Estilos.BTN_MENTA);
        btnLogin.setMaxWidth(300);
        
        btnLogin.setOnAction(e -> {
            String usuario = txtEmail.getText().trim();
            String pass = txtPass.getText();

            // 1. Validar campos vacíos
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

            // 2. Validar contra la Base de Datos
            int resultado = ConexionDB.validarUsuarioDetallado(usuario, pass);
            
            switch (resultado) {
                case 0:
                    // Éxito: Limpiamos los campos y entramos al menú
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

        Hyperlink linkCrear = new Hyperlink("Crear cuenta");
        linkCrear.setStyle(Estilos.TEXTO_BLANCO + " -fx-font-size: 14px;");
        linkCrear.setOnAction(e -> stage.setScene(getEscenaRegistro(stage)));

        VBox boxInferior = new VBox(linkCrear);
        boxInferior.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(boxInferior, new Insets(50, 0, 0, 0));

        layout.getChildren().addAll(boxTextos, txtEmail, txtPass, linkOlvido, btnLogin, boxInferior);
        return new Scene(layout, 380, 650);
    }

    public static Scene getEscenaRegistro(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER); 
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblTitulo = new Label("Crear Cuenta");
        lblTitulo.setStyle(Estilos.TITULO);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Completo");
        txtNombre.setStyle(Estilos.INPUT_BEIGE);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de Usuario");
        txtUsuario.setStyle(Estilos.INPUT_BEIGE);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setStyle(Estilos.INPUT_BEIGE);

        Button btnRegistrar = new Button("Registrarse");
        btnRegistrar.setStyle(Estilos.BTN_MENTA);
        btnRegistrar.setMaxWidth(300);

        Hyperlink linkVolver = new Hyperlink("¿Ya tienes cuenta? Inicia sesión");
        linkVolver.setStyle("-fx-text-fill: #A7C7E7;"); 

        // Acción del botón Registrar
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String pass = txtPass.getText().trim();

            if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor llena todos los campos.");
                return;
            }

            // Validamos que el nombre de usuario no esté
            int checkUser = ConexionDB.validarUsuarioDetallado(usuario, pass);
            if (checkUser != 1) { 
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario ocupado", "Este nombre de usuario ya está registrado. Elige otro.");
                return;
            }

            // Guardamos en la base de datos
            if (ConexionDB.registrarUsuarioPublico(nombre, usuario, pass)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Cuenta Creada", "Tu cuenta se ha creado con éxito. Ahora puedes iniciar sesión.");
                stage.setScene(getEscenaLogin(stage));
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema de conexión. Inténtalo más tarde.");
            }
        });

        // Acción del link para volver atrás sin registrarse
        linkVolver.setOnAction(e -> stage.setScene(getEscenaLogin(stage)));

        layout.getChildren().addAll(lblTitulo, txtNombre, txtUsuario, txtPass, btnRegistrar, linkVolver);
        return new Scene(layout, 380, 650);
    }

    public static Scene getEscenaRecuperar(Stage stage) {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblTitulo = new Label("Recuperar Pass");
        lblTitulo.setStyle(Estilos.TITULO);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingresa tu Email");
        txtUsuario.setStyle(Estilos.INPUT_BEIGE);

        Button btnRecuperar = new Button("Enviar Código");
        btnRecuperar.setStyle(Estilos.BTN_MENTA);
        btnRecuperar.setMaxWidth(300);

        Button btnVolver = new Button("Volver al Login");
        btnVolver.setStyle(Estilos.BTN_PELIGRO);
        btnVolver.setOnAction(e -> stage.setScene(getEscenaLogin(stage)));

        layout.getChildren().addAll(lblTitulo, txtUsuario, btnRecuperar, btnVolver);
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