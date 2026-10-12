public class TipoFalla {
    private int id;
    private String nombre;

    public TipoFalla(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre; // Se despliega en el ComboBox
    }
}