public class CausaRaiz {
    private int id;
    private String nombre;

    public CausaRaiz(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public int getId() { return id; }
    @Override
    public String toString() { return nombre; }
}