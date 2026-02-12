package CONTROLADOR;

import MODELO.Ventas;
import MODELO.detalle_ventas;
import java.util.ArrayList;

/**
 * Patrón Strategy: Define la interfaz para diferentes estrategias de generación de reportes
 */
public interface ReporteStrategy {
    
    /**
     * Genera el contenido del reporte en el formato específico
     * @param ventas Lista de ventas a reportar
     * @param detalles Lista de detalles de ventas
     * @return String con el contenido formateado
     */
    String generarReporte(ArrayList<Ventas> ventas, ArrayList<detalle_ventas> detalles);
    
    /**
     * Retorna la extensión del archivo para este formato
     */
    String getExtension();
    
    /**
     * Retorna el nombre del formato
     */
    String getNombreFormato();
}
