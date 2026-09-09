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
            // Lógica de conexión a BD aquí. Si es correcto:
            stage.setScene(MenuView.getEscenaMenu(stage));
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
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(Estilos.FONDO_PRINCIPAL);

        Label lblTitulo = new Label("Crear Cuenta");
        lblTitulo.setStyle(Estilos.TITULO);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nuevo Usuario");
        txtUsuario.setStyle(Estilos.INPUT_BEIGE);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Nueva Contraseña");
        txtPass.setStyle(Estilos.INPUT_BEIGE);

        Button btnRegistrar = new Button("Registrarse");
        btnRegistrar.setStyle(Estilos.BTN_MENTA);
        btnRegistrar.setMaxWidth(300);

        Button btnVolver = new Button("Volver al Login");
        btnVolver.setStyle(Estilos.BTN_PELIGRO);
        btnVolver.setOnAction(e -> stage.setScene(getEscenaLogin(stage)));

        layout.getChildren().addAll(lblTitulo, txtUsuario, txtPass, btnRegistrar, btnVolver);
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
}
