import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GeneradorPDF {

    public static boolean exportarReporte(File destino, List<RegistroReportePDF> datos, LocalDate inicio, LocalDate fin, String alcance) {
        try {
            List<String> lineas = new ArrayList<>();
            lineas.add("==========================================================================");
            lineas.add("   TSR SALTILLO - SERVICE DESK | REPORTE DE MANTENIMIENTO Y FALLAS");
            lineas.add("==========================================================================");
            lineas.add("Periodo evaluado : " + inicio + " al " + fin);
            lineas.add("Alcance / Equipo : " + alcance);
            lineas.add("Total registros  : " + datos.size());
            lineas.add("--------------------------------------------------------------------------");
            lineas.add(String.format("%-4s | %-12s | %-11s | %-18s | %-14s | %-5s", 
                "ID", "EQUIPO", "TIPO", "CAUSA RAIZ", "REFACCION", "MIN"));
            lineas.add("--------------------------------------------------------------------------");

            int tiempoTotal = 0;
            for (RegistroReportePDF r : datos) {
                tiempoTotal += r.getTiempoMinutos();
                lineas.add(String.format("%-4d | %-12s | %-11s | %-18s | %-14s | %-5d",
                    r.getIdFalla(),
                    cortar(r.getEquipo(), 12),
                    cortar(r.getTipoFalla(), 11),
                    cortar(r.getCausaRaiz(), 18),
                    cortar(r.getRefaccionUsada(), 14),
                    r.getTiempoMinutos()
                ));
            }

            double mttr = datos.isEmpty() ? 0 : (double) tiempoTotal / datos.size();
            lineas.add("--------------------------------------------------------------------------");
            lineas.add(String.format("Tiempo Muerto Acumulado : %d minutos", tiempoTotal));
            lineas.add(String.format("MTTR del Periodo        : %.1f minutos", mttr));
            lineas.add("==========================================================================");

            escribirArchivoPDF(destino, lineas);
            return true;
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return false;
        }
    }

    private static String cortar(String txt, int max) {
        if (txt == null) return "-";
        String limpio = txt.replaceAll("[^\\x20-\\x7E]", "?");
        return limpio.length() > max ? limpio.substring(0, max - 2) + ".." : limpio;
    }

    private static void escribirArchivoPDF(File file, List<String> lineas) throws Exception {
        StringBuilder stream = new StringBuilder();
        stream.append("BT\n/F1 9 Tf\n35 750 Td\n13 TL\n");
        for (String linea : lineas) {
            String escapada = linea.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
            stream.append("(").append(escapada).append(") Tj\nT*\n");
        }
        stream.append("ET\n");
        byte[] streamBytes = stream.toString().getBytes(StandardCharsets.ISO_8859_1);

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();

        pdf.write("%PDF-1.4\n".getBytes(StandardCharsets.ISO_8859_1));

        // Obj 1: Catalog
        offsets.add(pdf.size());
        pdf.write("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        // Obj 2: Pages
        offsets.add(pdf.size());
        pdf.write("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        // Obj 3: Page
        offsets.add(pdf.size());
        pdf.write("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        // Obj 4: Content Stream
        offsets.add(pdf.size());
        pdf.write(("4 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n").getBytes(StandardCharsets.ISO_8859_1));
        pdf.write(streamBytes);
        pdf.write("\nendstream\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        // Obj 5: Font (Courier monoespaciada para alinear columnas de tabla)
        offsets.add(pdf.size());
        pdf.write("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        int xrefOffset = pdf.size();
        pdf.write(("xref\n0 " + (offsets.size() + 1) + "\n").getBytes(StandardCharsets.ISO_8859_1));
        pdf.write("0000000000 65535 f \n".getBytes(StandardCharsets.ISO_8859_1));
        for (int off : offsets) {
            pdf.write(String.format("%010d 00000 n \n", off).getBytes(StandardCharsets.ISO_8859_1));
        }
        pdf.write(("trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + xrefOffset + "\n%%EOF").getBytes(StandardCharsets.ISO_8859_1));

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(pdf.toByteArray());
        }
    }
}