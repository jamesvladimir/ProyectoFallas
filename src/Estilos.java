public class Estilos {
    // Degradado Azul Marino Profundo (Midnight Navy)
    public static final String FONDO_PRINCIPAL = 
        "-fx-background-color: linear-gradient(to bottom, #162E54 0%, #0A1526 100%);";
    
    // Botón Principal Beige Arena (conserva el nombre BTN_MENTA para compatibilidad total)
    public static final String BTN_MENTA = 
        "-fx-background-color: #E6DCCD; " +
        "-fx-text-fill: #0F1E36; " +
        "-fx-font-size: 14px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 14px; " +
        "-fx-cursor: hand; " +
        "-fx-alignment: center; " +
        "-fx-text-alignment: center; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 6, 0, 0, 3);";

    public static final String BTN_MENTA_HOVER = 
        "-fx-background-color: #D5C6B0; " +
        "-fx-text-fill: #0F1E36; " +
        "-fx-font-size: 14px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 14px; " +
        "-fx-cursor: hand; " +
        "-fx-alignment: center; " +
        "-fx-text-alignment: center;";

    // Estilo específico para los botones del Menú Principal (icono a la izquierda + texto)
    public static final String BTN_MENU_ITEM = 
        "-fx-background-color: #E6DCCD; " +
        "-fx-text-fill: #0F1E36; " +
        "-fx-font-size: 14px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 14px; " +
        "-fx-padding: 0 18 0 18; " +
        "-fx-cursor: hand; " +
        "-fx-alignment: CENTER_LEFT; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 6, 0, 0, 3);";

    public static final String BTN_MENU_ITEM_HOVER = 
        "-fx-background-color: #D5C6B0; " +
        "-fx-text-fill: #0F1E36; " +
        "-fx-font-size: 14px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 14px; " +
        "-fx-padding: 0 18 0 18; " +
        "-fx-cursor: hand; " +
        "-fx-alignment: CENTER_LEFT;";

    // Botón Cerrar Sesión / Peligro en Terracota / Coral
    public static final String BTN_PELIGRO = 
        "-fx-background-color: #C85A48; " +
        "-fx-text-fill: white; " +
        "-fx-font-weight: bold; " +
        "-fx-font-size: 11px; " +
        "-fx-background-radius: 10px; " +
        "-fx-padding: 6 12 6 12; " +
        "-fx-cursor: hand; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 4, 0, 0, 2);";

    public static final String BTN_HOME = 
        "-fx-background-color: rgba(230, 220, 205, 0.12); " +
        "-fx-text-fill: #E6DCCD; " +
        "-fx-font-size: 18px; " +
        "-fx-cursor: hand; " +
        "-fx-border-color: #E6DCCD; " +
        "-fx-border-radius: 10px; " +
        "-fx-background-radius: 10px;";

    // Inputs estándar (para VistasCasosDeUso) en Marfil Crema
    public static final String INPUT_BEIGE = 
        "-fx-background-color: #F5F2EB; " +
        "-fx-text-fill: #0F1E36; " +
        "-fx-prompt-text-fill: #64748B; " +
        "-fx-background-radius: 12px; " +
        "-fx-padding: 11px 14px; " +
        "-fx-font-size: 13px; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 4, 0, 0, 2);";

    // Contenedor HBox para inputs con icono integrado (Login / Registro)
    public static final String CAJA_INPUT_ICONO = 
        "-fx-background-color: #F5F2EB; " +
        "-fx-background-radius: 14px; " +
        "-fx-padding: 2 14 2 14; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.22), 5, 0, 0, 2);";

    public static final String INPUT_TRANSPARENTE = 
        "-fx-background-color: transparent; " +
        "-fx-text-fill: #0F1E36; " +
        "-fx-prompt-text-fill: #64748B; " +
        "-fx-font-size: 13px;";

    public static final String TEXTO_BLANCO = "-fx-text-fill: #E2E8F0;";
    
    public static final String TITULO = 
        "-fx-text-fill: #FFFFFF; " +
        "-fx-font-size: 24px; " +
        "-fx-font-weight: bold; " +
        "-fx-letter-spacing: 1px;";
}