import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TSR Saltillo - Service Desk");
        
        // Iniciamos mandando la ventana a la clase LoginView
        primaryStage.setScene(LoginView.getEscenaLogin(primaryStage));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}   