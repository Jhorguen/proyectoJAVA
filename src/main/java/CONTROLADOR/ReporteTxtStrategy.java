package CONTROLADOR;

import MODELO.Ventas;
import MODELO.detalle_ventas;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Estrategia para generar reportes en formato TXT
 */
public class ReporteTxtStrategy implements ReporteStrategy {

    @Override
    public String generarReporte(ArrayList<Ventas> ventas, ArrayList<detalle_ventas> detalles) {
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
        sb.append("Reporte generado: ").append(java.time.LocalDateTime.now()).append("\n");
        sb.append("═".repeat(80)).append("\n");
        
        return sb.toString();
    }

    @Override
    public String getExtension() {
        return ".txt";
    }

    @Override
    public String getNombreFormato() {
        return "TXT";
    }
}
