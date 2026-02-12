package CONTROLADOR;

import MODELO.Ventas;
import MODELO.detalle_ventas;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generador de reportes de ventas en archivo TXT
 * Usa try-with-resources para manejo automático de recursos
 * Principio SOLID: Single Responsibility Principle
 */
public class ReporteVentasFile {
    
    private gestionarVentaImpl gestionarVen = new gestionarVentaImpl();
    private gestionarDetalleVentaImpl gestionarDetalle = new gestionarDetalleVentaImpl();
    private ConfiguracionApp config = ConfiguracionApp.getInstance();
    
    /**
     * Genera un reporte de ventas en formato TXT
     * @return Path del archivo generado
     * @throws IOException si hay error al escribir el archivo
     */
    public String generarReporte() throws IOException {
        // Obtener data
        ArrayList<Ventas> ventas = gestionarVen.listar();
        ArrayList<detalle_ventas> detalles = gestionarDetalle.listar();
        
        // Generar contenido del reporte
        String contenido = generarContenidoReporte(ventas, detalles);
        
        // Crear directorio si no existe
        Files.createDirectories(Paths.get(config.getRutaReportes()));
        
        // Generar nombre del archivo con timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String nombreArchivo = "reporte_ventas_" + timestamp + ".txt";
        String rutaCompleta = config.getRutaReportes() + nombreArchivo;
        
        // Guardar archivo usando try-with-resources
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta))) {
            writer.write(contenido);
            writer.flush();
            System.out.println("✓ Reporte generado exitosamente en: " + rutaCompleta);
        } catch (IOException e) {
            System.err.println("✗ Error al generar reporte: " + e.getMessage());
            throw e;
        }
        
        return rutaCompleta;
    }
    
    /**
     * Genera el contenido formateado del reporte
     */
    private String generarContenidoReporte(ArrayList<Ventas> ventas, ArrayList<detalle_ventas> detalles) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═".repeat(80)).append("\n");
        sb.append("                      REPORTE DE VENTAS - TECNO STORE\n");
        sb.append("═".repeat(80)).append("\n\n");
        
        // Estadísticas generales
        sb.append("ESTADÍSTICAS GENERALES\n");
        sb.append("-".repeat(80)).append("\n");
        
        double totalVentas = ventas.stream().mapToDouble(Ventas::getTotal).sum();
        double promedioVentas = ventas.stream().mapToDouble(Ventas::getTotal).average().orElse(0.0);
        double ventaMax = ventas.stream().mapToDouble(Ventas::getTotal).max().orElse(0.0);
        double ventaMin = ventas.stream().mapToDouble(Ventas::getTotal).filter(v -> v > 0).min().orElse(0.0);
        
        sb.append(String.format("Total de Ventas: %d\n", ventas.size()));
        sb.append(String.format("Monto Total: $%.2f\n", totalVentas));
        sb.append(String.format("Promedio por Venta: $%.2f\n", promedioVentas));
        sb.append(String.format("Venta Máxima: $%.2f\n", ventaMax));
        sb.append(String.format("Venta Mínima: $%.2f\n\n", ventaMin));
        
        // Ventas por mes
        sb.append("VENTAS POR MES\n");
        sb.append("-".repeat(80)).append("\n");
        sb.append(String.format("%-20s %-20s %-20s\n", "Mes", "Cantidad", "Total"));
        sb.append("-".repeat(80)).append("\n");
        
        Map<YearMonth, Long> ventasPorMes = ventas.stream()
                .collect(Collectors.groupingBy(
                    v -> YearMonth.from(v.getFecha()),
                    LinkedHashMap::new,
                    Collectors.counting()
                ));
        
        Map<YearMonth, Double> montosPorMes = ventas.stream()
                .collect(Collectors.groupingBy(
                    v -> YearMonth.from(v.getFecha()),
                    LinkedHashMap::new,
                    Collectors.summingDouble(Ventas::getTotal)
                ));
        
        ventasPorMes.forEach((mes, cantidad) -> 
            sb.append(String.format("%-20s %-20d $%-19.2f\n", 
                mes.toString(), 
                cantidad, 
                montosPorMes.get(mes)))
        );
        
        sb.append("\n");
        
        // Detalle de todas las ventas
        sb.append("DETALLE DE VENTAS\n");
        sb.append("-".repeat(80)).append("\n");
        sb.append(String.format("%-5s %-12s %-25s %-15s %-12s\n", "ID", "Fecha", "Cliente", "Monto", "Detalles"));
        sb.append("-".repeat(80)).append("\n");
        
        for (Ventas v : ventas) {
            long cantDetalles = detalles.stream().filter(d -> d.getId_venta().getId() == v.getId()).count();
            sb.append(String.format("%-5d %-12s %-25s $%-14.2f %-12d artículos\n",
                v.getId(),
                v.getFecha().toString(),
                v.getId_cliente().getNombre(),
                v.getTotal(),
                cantDetalles));
        }
        
        sb.append("\n");
        sb.append("═".repeat(80)).append("\n");
        sb.append("Reporte generado: ").append(LocalDateTime.now()).append("\n");
        sb.append("═".repeat(80)).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Lee y retorna el contenido de un archivo de reporte
     * @param rutaArchivo Path del archivo
     * @return Contenido del archivo
     * @throws IOException si hay error al leer
     */
    public String leerReporte(String rutaArchivo) throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            System.err.println("Error al leer reporte: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Lista todos los reportes generados
     */
    public void listarReportes() {
        File carpeta = new File(config.getRutaReportes());
        
        if (!carpeta.exists()) {
            System.out.println("La carpeta de reportes no existe aún.");
            return;
        }
        
        File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("reporte_") && name.endsWith(".txt"));
        
        if (archivos == null || archivos.length == 0) {
            System.out.println("No hay reportes generados.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("REPORTES DISPONIBLES");
        System.out.println("=".repeat(80));
        System.out.printf("%-50s %-15s %-10s\n", "Archivo", "Tamaño", "Fecha");
        System.out.println("-".repeat(80));
        
        for (File archivo : archivos) {
            long tamaño = archivo.length();
            long fecha = archivo.lastModified();
            String fechaFormato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(fecha);
            
            System.out.printf("%-50s %-15s %-10s\n", 
                archivo.getName(), 
                String.format("%.2f KB", tamaño / 1024.0),
                fechaFormato);
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
    
    /**
     * Elimina un archivo de reporte
     * @param nombreArchivo Nombre del archivo a eliminar
     */
    public void eliminarReporte(String nombreArchivo) {
        String rutaCompleta = config.getRutaReportes() + nombreArchivo;
        File archivo = new File(rutaCompleta);
        
        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println("✓ Reporte eliminado: " + nombreArchivo);
            } else {
                System.out.println("✗ No se pudo eliminar el reporte.");
            }
        } else {
            System.out.println("✗ El archivo no existe.");
        }
    }
}
