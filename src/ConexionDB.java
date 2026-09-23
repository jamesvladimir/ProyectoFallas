import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.HashMap;

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
    
    public static int validarUsuarioDetallado(String nombreUsuario, String contrasena) {
        String sql = "SELECT contrasena_hash, estatus_activo FROM usuario WHERE nombre_usuario = ?";
        
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String bdPass = rs.getString("contrasena_hash");
                boolean activo = rs.getBoolean("estatus_activo");
                
                if (!activo) {
                    return 3; // El usuario existe pero está dado de baja
                }
                
                if (bdPass.equals(contrasena)) {
                    return 0; // Todo es correcto
                } else {
                    return 2; // El usuario existe, pero la contraseña no coincide
                }
            } else {
                return 1; // No se encontró ningún registro con ese nombre de usuario
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return -1;
        }
    }

    // 1. Verifica si una máquina ya está registrada
    public static boolean existeMaquinaria(String numSerie) {
        String sql = "SELECT num_serie FROM maquinaria WHERE num_serie = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Retorna true si encontró el número de serie
        } catch (SQLException e) {
            System.err.println("Error al buscar maquinaria: " + e.getMessage());
            return false;
        }
    }

    // 2. Inserta una nueva máquina en la BD
    public static boolean registrarMaquinaria(String numSerie, String nombreEquipo) {
        
        String sql = "INSERT INTO maquinaria (num_serie, nombre_equipo, id_tipo_maquina, id_marca, id_ubicacion, id_estado) VALUES (?, ?, 1, 1, 1, 1)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            pstmt.setString(2, nombreEquipo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar maquinaria: " + e.getMessage());
            return false;
        }
    }

    // 3. Inserta un nuevo reporte de falla
    public static boolean registrarFalla(String numSerie, String descripcion) {
       
        String sql = "INSERT INTO reportar_falla (num_serie, id_tipo_falla, descripcion) VALUES (?, 1, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            pstmt.setString(2, descripcion);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar falla: " + e.getMessage());
            return false;
        }
    }
    // 4. Obtiene la lista de fallas que NO han sido atendidas 
    public static List<String> obtenerFallasPendientes() {
        List<String> fallas = new ArrayList<>();
        // Un LEFT JOIN para buscar fallas que no existan 
        String sql = "SELECT f.id_falla, f.num_serie FROM reportar_falla f " +
                     "LEFT JOIN actualizar_bitacora b ON f.id_falla = b.id_falla " +
                     "WHERE b.id_falla IS NULL";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                fallas.add(rs.getInt("id_falla") + " - Máquina: " + rs.getString("num_serie"));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar fallas pendientes: " + e.getMessage());
        }
        return fallas;
    }

    // 5. Guarda la atención de la falla en la bitácora
    public static boolean registrarBitacora(int idFalla, int tiempoReparacion) {
        // Para este ejemplo, asignaremos id_causa = 1 (Desgaste natural) por defecto
        String sql = "INSERT INTO actualizar_bitacora (id_falla, tiempo_reparacion_minutos, id_causa) VALUES (?, ?, 1)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFalla);
            pstmt.setInt(2, tiempoReparacion);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar bitácora: " + e.getMessage());
            return false;
        }
    }

    // 6. Crea un nuevo usuario Técnico
    public static boolean registrarTecnico(String nombreCompleto, String usuario, String contrasena) {
        String sql = "INSERT INTO usuario (nombre_completo, nombre_usuario, contrasena_hash, id_rol, id_departamento, estatus_activo) VALUES (?, ?, ?, 3, 2, 1)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, usuario);
            pstmt.setString(3, contrasena);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar técnico: " + e.getMessage());
            return false;
        }
    }
    // 7. Obtiene el conteo de fallas agrupadas por tipo para el Dashboard
    public static Map<String, Integer> obtenerFallasPorTipo() {
        Map<String, Integer> datos = new HashMap<>();
        // JOIN para obtener el nombre de la falla y contar cuántas veces aparece
        String sql = "SELECT t.nombre, COUNT(r.id_falla) as total " +
                     "FROM reportar_falla r " +
                     "INNER JOIN tipo_falla t ON r.id_tipo_falla = t.id_tipo_falla " +
                     "GROUP BY t.nombre";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.put(rs.getString("nombre"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar datos del dashboard: " + e.getMessage());
        }
        return datos;
    }
    // 8. Obtiene el historial de todas las fallas para la tabla de reportes
    public static List<ModeloReporte> obtenerHistorialFallas() {
        List<ModeloReporte> lista = new ArrayList<>();
        
        // El LEFT JOIN verifica si la falla existe en la bitácora 
        String sql = "SELECT r.id_falla, r.num_serie, t.nombre AS tipo_falla, " +
                     "CASE WHEN b.id_falla IS NULL THEN 'Pendiente' ELSE 'Atendida' END AS estado " +
                     "FROM reportar_falla r " +
                     "INNER JOIN tipo_falla t ON r.id_tipo_falla = t.id_tipo_falla " +
                     "LEFT JOIN actualizar_bitacora b ON r.id_falla = b.id_falla " +
                     "ORDER BY r.fecha_reporte DESC";
        
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new ModeloReporte(
                    rs.getInt("id_falla"),
                    rs.getString("num_serie"),
                    rs.getString("tipo_falla"),
                    rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar historial de reportes: " + e.getMessage());
        }
        return lista;
    }
    // 9. Registra un usuario nuevo desde la pantalla de Login
    public static boolean registrarUsuarioPublico(String nombreCompleto, String usuario, String contrasena) {
        // id_rol = 4 (Operador de Planta), id_departamento = 3 (Línea de Producción)
        String sql = "INSERT INTO usuario (nombre_completo, nombre_usuario, contrasena_hash, id_rol, id_departamento, estatus_activo) VALUES (?, ?, ?, 4, 3, 1)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, usuario);
            pstmt.setString(3, contrasena);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario público: " + e.getMessage());
            return false;
        }
    }
}

    