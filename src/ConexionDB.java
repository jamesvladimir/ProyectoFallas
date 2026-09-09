import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConexionDB {
    
    
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Control_fallas;encrypt=true;trustServerCertificate=true;";
    
    private static final String USER = "user2_abd";
    private static final String PASSWORD = "user2_abd"; 

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a Control_fallas con el usuario: " + USER);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }
    
    public static void main(String[] args) {
        getConnection();
    }
}