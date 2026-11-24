public class SolucionHistorica {
    private String equipo;
    private String tipoFalla;
    private String descripcionFalla;
    private String causaRaiz;
    private String refaccion;
    private int tiempoMinutos;

    public SolucionHistorica(String equipo, String tipoFalla, String descripcionFalla, String causaRaiz, String refaccion, int tiempoMinutos) {
        this.equipo = equipo;
        this.tipoFalla = tipoFalla;
        this.descripcionFalla = descripcionFalla;
        this.causaRaiz = causaRaiz;
        this.refaccion = refaccion;
        this.tiempoMinutos = tiempoMinutos;
    }

    public String getEquipo() { return equipo; }
    public String getTipoFalla() { return tipoFalla; }
    public String getDescripcionFalla() { return descripcionFalla; }
    public String getCausaRaiz() { return causaRaiz; }
    public String getRefaccion() { return refaccion; }
    public int getTiempoMinutos() { return tiempoMinutos; }
}