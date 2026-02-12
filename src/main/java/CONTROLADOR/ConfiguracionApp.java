package CONTROLADOR;

/**
 * Patrón Singleton: Proporciona una única instancia de configuración global
 * Thread-safe usando sincronización
 * Principio SOLID: Single Responsibility Principle
 */
public class ConfiguracionApp {
    
    private static ConfiguracionApp instancia;
    
    private String rutaReportes;
    private String nombreEmpresa;
    private double ivaDefault;
    private boolean crearBackup;
    
    /**
     * Constructor privado para evitar instanciación directa
     */
    private ConfiguracionApp() {
        this.rutaReportes = "./reportes/";
        this.nombreEmpresa = "TECNO STORE";
        this.ivaDefault = 0.19;
        this.crearBackup = true;
    }
    
    /**
     * Obtiene la instancia única de configuración (Thread-safe)
     */
    public static synchronized ConfiguracionApp getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionApp();
        }
        return instancia;
    }
    
    // Getters y Setters
    public String getRutaReportes() {
        return rutaReportes;
    }
    
    public void setRutaReportes(String rutaReportes) {
        this.rutaReportes = rutaReportes;
    }
    
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    
    public double getIvaDefault() {
        return ivaDefault;
    }
    
    public void setIvaDefault(double ivaDefault) {
        this.ivaDefault = ivaDefault;
    }
    
    public boolean isCrearBackup() {
        return crearBackup;
    }
    
    public void setCrearBackup(boolean crearBackup) {
        this.crearBackup = crearBackup;
    }
}
