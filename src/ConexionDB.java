import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.HashMap;

public class ConexionDB {
    
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Control_Fallas;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "user2_abd";
    private static final String PASSWORD = "user2_abd"; 
    
    // Variable global para almacenar el rol del usuario conectado
    public static int idRolActual = 0;

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error al conectar BD: " + e.getMessage());
        }
        return conexion;
    }
    
    public static int validarUsuarioDetallado(String nombreUsuario, String contrasena) {
        String sql = "SELECT contrasena_hash, estatus_activo, id_rol FROM usuario WHERE nombre_usuario = ? OR correo_electronico = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String bdPass = rs.getString("contrasena_hash");
                boolean activo = rs.getBoolean("estatus_activo");
                if (!activo) return 3;
                
                if (bdPass.equals(contrasena)) {
                    idRolActual = rs.getInt("id_rol"); // Guardamos el rol en sesión
                    return 0; 
                } else {
                    return 2; 
                }
            } else {
                return 1; 
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    public static boolean existeMaquinaria(String numSerie) {
        String sql = "SELECT num_serie FROM maquinaria WHERE num_serie = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            return pstmt.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public static List<TipoMaquinaria> obtenerTiposMaquinaria() {
        List<TipoMaquinaria> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_maquina, nombre_tipo FROM tipo_maquinaria";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new TipoMaquinaria(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {}
        return lista;
    }

    public static boolean registrarMaquinaria(String numSerie, String nombreEquipo, int idTipoMaquina) {
        String sql = "INSERT INTO maquinaria (num_serie, nombre_equipo, id_tipo_maquina, id_marca, id_ubicacion, id_estado) VALUES (?, ?, ?, 1, 1, 1)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            pstmt.setString(2, nombreEquipo);
            pstmt.setInt(3, idTipoMaquina);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static List<TipoFalla> obtenerTiposFalla() {
        List<TipoFalla> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_falla, nombre FROM tipo_falla";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new TipoFalla(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {}
        return lista;
    }

    public static boolean registrarFalla(String numSerie, int idTipoFalla, String descripcion) {
        String sql = "INSERT INTO reportar_falla (num_serie, id_tipo_falla, descripcion) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            pstmt.setInt(2, idTipoFalla);
            pstmt.setString(3, descripcion);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static List<String> obtenerFallasPendientes() {
        List<String> fallas = new ArrayList<>();
        String sql = "SELECT f.id_falla, f.num_serie FROM reportar_falla f LEFT JOIN actualizar_bitacora b ON f.id_falla = b.id_falla WHERE b.id_falla IS NULL";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) fallas.add(rs.getInt("id_falla") + " - Máquina: " + rs.getString("num_serie"));
        } catch (SQLException e) {}
        return fallas;
    }

    // --- MÉTODOS NUEVOS BLOQUE 2 ---

    public static List<Departamento> obtenerDepartamentos() {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT id_departamento, nombre_departamento FROM departamento";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new Departamento(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {}
        return lista;
    }

    public static List<CausaRaiz> obtenerCausasRaiz() {
        List<CausaRaiz> lista = new ArrayList<>();
        String sql = "SELECT id_causa, nombre FROM causa_raiz";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new CausaRaiz(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {}
        return lista;
    }

    public static List<Refaccion> obtenerRefacciones() {
        List<Refaccion> lista = new ArrayList<>();
        String sql = "SELECT id_refaccion, nombre FROM refacciones";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new Refaccion(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {}
        return lista;
    }

    public static List<MaquinariaCatalogo> obtenerMaquinasActivas() {
        List<MaquinariaCatalogo> lista = new ArrayList<>();
        String sql = "SELECT num_serie, nombre_equipo FROM maquinaria WHERE id_estado = 1";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new MaquinariaCatalogo(rs.getString(1), rs.getString(2)));
        } catch (SQLException e) {}
        return lista;
    }

    // Actualizado Bloque 2: Incluye inserción en tablas de detalle (transaccional)
    public static boolean registrarBitacoraDetallada(int idFalla, int tiempo, int idCausa, int idRefaccion, int cantidad) {
        String sqlBitacora = "INSERT INTO actualizar_bitacora (id_falla, tiempo_reparacion_minutos, id_causa) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_bitacora_refaccion (id_bitacora, id_refaccion, cantidad_utilizada) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Iniciamos transacción
            try (PreparedStatement pstmt = conn.prepareStatement(sqlBitacora, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, idFalla);
                pstmt.setInt(2, tiempo);
                pstmt.setInt(3, idCausa);
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idBitacora = rs.getInt(1);
                    try (PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle)) {
                        pstDetalle.setInt(1, idBitacora);
                        pstDetalle.setInt(2, idRefaccion);
                        pstDetalle.setInt(3, cantidad);
                        pstDetalle.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) { return false; }
    }

    public static boolean registrarMantenimiento(String numSerie, int umbral, String tipoUmbral, int idRefaccion, int cantidad) {
        String sqlMant = "INSERT INTO mantenimiento_preventivo (num_serie, umbral_uso, tipo_umbral) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_mantenimiento_pieza (id_mantenimiento, id_refaccion, cantidad) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMant, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, numSerie);
                pstmt.setInt(2, umbral);
                pstmt.setString(3, tipoUmbral);
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idMant = rs.getInt(1);
                    try (PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle)) {
                        pstDetalle.setInt(1, idMant);
                        pstDetalle.setInt(2, idRefaccion);
                        pstDetalle.setInt(3, cantidad);
                        pstDetalle.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) { return false; }
    }

    public static double[] obtenerMetricasDashboard() {
        // [0] = MTTR (Promedio de minutos), [1] = MTBF simulado
        double[] metricas = new double[]{0.0, 0.0};
        String sql = "SELECT AVG(tiempo_reparacion_minutos) as mttr, COUNT(id_falla) as total_fallas FROM actualizar_bitacora";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                metricas[0] = rs.getDouble("mttr");
                int totalFallas = rs.getInt("total_fallas");
                // Cálculo simple de MTBF (Simulando mes de 43200 minutos - tiempo muerto) / fallas
                if (totalFallas > 0) {
                    metricas[1] = (43200.0 - (totalFallas * metricas[0])) / totalFallas;
                }
            }
        } catch (SQLException e) {}
        return metricas;
    }

    // Actualizado Bloque 2: Se agregó correo y especialidad (id_rol 3 = Técnico)
    public static boolean registrarTecnico(String nombre, String usuario, String pass, String correo, int idDepto, String especialidad) {
        String sql = "INSERT INTO usuario (nombre_completo, nombre_usuario, contrasena_hash, correo_electronico, id_rol, id_departamento, especialidad_tecnica) VALUES (?, ?, ?, ?, 3, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, usuario);
            pstmt.setString(3, pass);
            pstmt.setString(4, correo);
            pstmt.setInt(5, idDepto);
            pstmt.setString(6, especialidad);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // --- MÉTODOS PREVIOS RESTANTES ---
    public static Map<String, Integer> obtenerFallasPorTipo() {
        Map<String, Integer> datos = new HashMap<>();
        String sql = "SELECT t.nombre, COUNT(r.id_falla) as total FROM reportar_falla r INNER JOIN tipo_falla t ON r.id_tipo_falla = t.id_tipo_falla GROUP BY t.nombre";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) datos.put(rs.getString("nombre"), rs.getInt("total"));
        } catch (SQLException e) {}
        return datos;
    }

    public static List<ModeloReporte> obtenerHistorialFallas() {
        List<ModeloReporte> lista = new ArrayList<>();
        String sql = "SELECT r.id_falla, r.num_serie, t.nombre AS tipo_falla, CASE WHEN b.id_falla IS NULL THEN 'Pendiente' ELSE 'Atendida' END AS estado FROM reportar_falla r INNER JOIN tipo_falla t ON r.id_tipo_falla = t.id_tipo_falla LEFT JOIN actualizar_bitacora b ON r.id_falla = b.id_falla ORDER BY r.fecha_reporte DESC";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(new ModeloReporte(rs.getInt("id_falla"), rs.getString("num_serie"), rs.getString("tipo_falla"), rs.getString("estado")));
        } catch (SQLException e) {}
        return lista;
    }

    public static boolean registrarUsuarioPublico(String nombreCompleto, String usuario, String contrasena) {
        String sql = "INSERT INTO usuario (nombre_completo, nombre_usuario, contrasena_hash, id_rol, id_departamento, estatus_activo) VALUES (?, ?, ?, 4, 3, 1)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, usuario);
            pstmt.setString(3, contrasena);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean modificarMaquinaria(String numSerie, String nuevoNombre) {
        String sql = "UPDATE maquinaria SET nombre_equipo = ? WHERE num_serie = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoNombre);
            pstmt.setString(2, numSerie);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean eliminarMaquinaria(String numSerie) {
        String sql = "UPDATE maquinaria SET id_estado = 2 WHERE num_serie = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numSerie);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
    