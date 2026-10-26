public class MaquinariaCatalogo {
    private String numSerie;
    private String nombre;

    public MaquinariaCatalogo(String numSerie, String nombre) {
        this.numSerie = numSerie;
        this.nombre = nombre;
    }
    public String getNumSerie() { return numSerie; }
    @Override
    public String toString() { return numSerie + " - " + nombre; }
}