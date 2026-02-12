package CONTROLADOR;

/**
 * Patrón Factory
 */
public class ReporteFactory {
    

    public static ReporteStrategy crearReporte(String tipo) {
        switch (tipo.toUpperCase()) {
            case "TXT":
                return new ReporteTxtStrategy();
            case "CSV":
                return new ReporteCsvStrategy();
            default:
                throw new IllegalArgumentException("Formato de reporte no soportado: " + tipo);
        }
    }
    

    public static String[] getFormatosSoportados() {
        return new String[]{"TXT", "CSV"};
    }
}
