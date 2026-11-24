public class RegistroReportePDF {
    private int idFalla;
    private String equipo;
    private String tipoFalla;
    private String causaRaiz;
    private String refaccionUsada;
    private int tiempoMinutos;
    private String fechaReparacion;

    public RegistroReportePDF(int idFalla, String equipo, String tipoFalla, String causaRaiz, String refaccionUsada, int tiempoMinutos, String fechaReparacion) {
        this.idFalla = idFalla;
        this.equipo = equipo;
        this.tipoFalla = tipoFalla;
        this.causaRaiz = causaRaiz;
        this.refaccionUsada = refaccionUsada;
        this.tiempoMinutos = tiempoMinutos;
        this.fechaReparacion = fechaReparacion;
    }

    public int getIdFalla() { return idFalla; }
    public String getEquipo() { return equipo; }
    public String getTipoFalla() { return tipoFalla; }
    public String getCausaRaiz() { return causaRaiz; }
    public String getRefaccionUsada() { return refaccionUsada; }
    public int getTiempoMinutos() { return tiempoMinutos; }
    public String getFechaReparacion() { return fechaReparacion; }
}