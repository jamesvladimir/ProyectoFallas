public class ModeloReporte {
    private int id;
    private String maquina;
    private String tipo;
    private String estado;

    public ModeloReporte(int id, String maquina, String tipo, String estado) {
        this.id = id;
        this.maquina = maquina;
        this.tipo = tipo;
        this.estado = estado;
    }

    // JavaFX necesita estos métodos exactamente con esta nomenclatura
    public int getId() { return id; }
    public String getMaquina() { return maquina; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }
}