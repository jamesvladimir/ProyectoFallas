public class TipoMaquinaria {
    private int id;
    private String nombre;

    public TipoMaquinaria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre; // Esto es lo que se desplegará visualmente en el ComboBox
    }
}