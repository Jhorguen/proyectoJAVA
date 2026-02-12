package VISTA;

import CONTROLADOR.ReporteVentasFile;
import CONTROLADOR.ConfiguracionApp;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class menuGenerarReportes extends JFrame {
    
    private ReporteVentasFile generador = new ReporteVentasFile();
    private ConfiguracionApp config = ConfiguracionApp.getInstance();
    
    private JTextArea areaResultado;
    
    public menuGenerarReportes() {
        setTitle("GENERAR REPORTE DE VENTAS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de opciones
        JPanel panelOpciones = new JPanel(new GridBagLayout());
        panelOpciones.setBorder(BorderFactory.createTitledBorder("OPCIONES"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        JButton btnGenerar = new JButton(" GENERAR REPORTE TXT");
        btnGenerar.setBackground(new Color(34, 139, 34));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerar.setPreferredSize(new Dimension(400, 50));
        btnGenerar.addActionListener(e -> generarReporte());
        panelOpciones.add(btnGenerar, gbc);
        
        gbc.gridy = 1;
        JButton btnListar = new JButton(" LISTAR REPORTES");
        btnListar.setBackground(new Color(70, 130, 180));
        btnListar.setForeground(Color.WHITE);
        btnListar.setFont(new Font("Arial", Font.BOLD, 12));
        btnListar.addActionListener(e -> listarReportes());
        panelOpciones.add(btnListar, gbc);
        
        gbc.gridy = 2;
        JButton btnAbrir = new JButton(" ABRIR CARPETA DE REPORTES");
        btnAbrir.setBackground(new Color(184, 134, 11));
        btnAbrir.setForeground(Color.WHITE);
        btnAbrir.setFont(new Font("Arial", Font.BOLD, 12));
        btnAbrir.addActionListener(e -> abrirCarpeta());
        panelOpciones.add(btnAbrir, gbc);
        
        panelPrincipal.add(panelOpciones, BorderLayout.NORTH);
        
        // Panel de resultado
        JPanel panelResultado = new JPanel(new BorderLayout(5, 5));
        panelResultado.setBorder(BorderFactory.createTitledBorder("RESULTADO"));
        
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Courier New", Font.PLAIN, 11));
        areaResultado.setBackground(new Color(240, 240, 240));
        areaResultado.setText("Los reportes se guardarán en:\n" + config.getRutaReportes() + "\n\nSeleccione una opción...");
        
        JScrollPane scroll = new JScrollPane(areaResultado);
        panelResultado.add(scroll, BorderLayout.CENTER);
        
        panelPrincipal.add(panelResultado, BorderLayout.CENTER);
        
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("INFORMACIÓN"));
        JLabel lblInfo = new JLabel("Empresa: " + config.getNombreEmpresa() + " | IVA: " + (config.getIvaDefault() * 100) + "%");
        panelInfo.add(lblInfo);
        
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void generarReporte() {
        try {
            areaResultado.setText("Generando reporte...\n\n");
            String ruta = generador.generarReporte();
            areaResultado.setText("✓ REPORTE GENERADO EXITOSAMENTE\n\n");
            areaResultado.append("Ubicación: " + ruta + "\n\n");
            areaResultado.append("El archivo contiene:\n");
            areaResultado.append("  • Estadísticas generales\n");
            areaResultado.append("  • Ventas por mes\n");
            areaResultado.append("  • Detalle de todas las ventas\n");
            
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            areaResultado.setText("✗ ERROR al generar reporte:\n" + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listarReportes() {
        areaResultado.setText("REPORTES DISPONIBLES:\n");
        areaResultado.append("-".repeat(70) + "\n\n");
        
        java.io.File carpeta = new java.io.File(config.getRutaReportes());
        if (!carpeta.exists()) {
            areaResultado.append("La carpeta de reportes no existe.\n");
            return;
        }
        
        java.io.File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("reporte_") && name.endsWith(".txt"));
        
        if (archivos == null || archivos.length == 0) {
            areaResultado.append("No hay reportes generados.\n");
            return;
        }
        
        for (java.io.File archivo : archivos) {
            long tamaño = archivo.length();
            long fecha = archivo.lastModified();
            String fechaFormato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
            
            areaResultado.append(String.format("📄 %s (%.2f KB) - %s\n", 
                archivo.getName(), 
                tamaño / 1024.0,
                fechaFormato));
        }
    }
    
    private void abrirCarpeta() {
        try {
            String ruta = config.getRutaReportes();
            java.io.File carpeta = new java.io.File(ruta);
            
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("explorer \"" + new java.io.File(ruta).getAbsolutePath() + "\"");
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", ruta});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", ruta});
            }
            
            areaResultado.setText("✓ Abriendo carpeta: " + ruta);
        } catch (Exception e) {
            areaResultado.setText("✗ Error al abrir carpeta: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return "GENERAR REPORTE TXT";
    }
}
