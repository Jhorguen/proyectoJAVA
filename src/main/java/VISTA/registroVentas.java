package VISTA;

import CONTROLADOR.gestionarClienteImpl;
import CONTROLADOR.gestionarCelularImpl;
import CONTROLADOR.gestionarVentaImpl;
import MODELO.Cliente;
import MODELO.Celular;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class registroVentas extends JFrame {
    
    private JComboBox<Cliente> comboClientes;
    private JComboBox<Celular> comboCelulares;
    private JSpinner spinnerCantidad;
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;
    
    private gestionarClienteImpl gestionarCli = new gestionarClienteImpl();
    private gestionarCelularImpl gestionarCel = new gestionarCelularImpl();
    private gestionarVentaImpl gestionarVen = new gestionarVentaImpl();
    
    private ArrayList<gestionarVentaImpl.detalles_temp> detallesVenta = new ArrayList<>();
    
    public registroVentas() {
        setTitle("REGISTRO DE VENTAS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        inicializarComponentes();
        cargarClientes();
        cargarCelulares();
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior para selección
        JPanel panelSeleccion = new JPanel(new GridBagLayout());
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("DATOS DE VENTA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Cliente
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSeleccion.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        comboClientes = new JComboBox<>();
        panelSeleccion.add(comboClientes, gbc);
        
        // Celular
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelSeleccion.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 1;
        comboCelulares = new JComboBox<>();
        comboCelulares.addActionListener(e -> actualizarPrecio());
        panelSeleccion.add(comboCelulares, gbc);
        
        // Cantidad
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelSeleccion.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panelSeleccion.add(spinnerCantidad, gbc);
        
        // Botón Agregar
        gbc.gridx = 2;
        gbc.gridy = 2;
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarDetalle());
        panelSeleccion.add(btnAgregar, gbc);
        
        panelPrincipal.add(panelSeleccion, BorderLayout.NORTH);
        
        // Tabla de detalles
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder("DETALLES DE VENTA"));
        
        modeloTabla = new DefaultTableModel(new String[]{"ID Celular", "Modelo", "Precio", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scroll = new JScrollPane(tablaDetalles);
        panelTabla.add(scroll, BorderLayout.CENTER);
        
        // Panel de botones para tabla
        JPanel panelBotonesTabla = new JPanel();
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.addActionListener(e -> eliminarDetalle());
        panelBotonesTabla.add(btnEliminar);
        panelTabla.add(panelBotonesTabla, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        
        // Panel de totales
        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBorder(BorderFactory.createTitledBorder("TOTALES"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20);
        
        Font fontTotales = new Font("Arial", Font.BOLD, 14);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelTotales.add(new JLabel("Subtotal: "), gbc);
        gbc.gridx = 1;
        lblSubtotal = new JLabel("$0.00");
        lblSubtotal.setFont(fontTotales);
        panelTotales.add(lblSubtotal, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelTotales.add(new JLabel("IVA (19%): "), gbc);
        gbc.gridx = 1;
        lblIVA = new JLabel("$0.00");
        lblIVA.setFont(fontTotales);
        panelTotales.add(lblIVA, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelTotales.add(new JLabel("TOTAL: "), gbc);
        gbc.gridx = 1;
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(fontTotales);
        lblTotal.setForeground(new Color(0, 128, 0));
        panelTotales.add(lblTotal, gbc);
        
        panelPrincipal.add(panelTotales, BorderLayout.SOUTH);
        
        // Panel de botones finales
        JPanel panelBotones = new JPanel();
        JButton btnRegistrar = new JButton("REGISTRAR VENTA");
        btnRegistrar.setBackground(new Color(0, 128, 0));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.addActionListener(e -> registrarVenta());
        panelBotones.add(btnRegistrar);
        
        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void cargarClientes() {
        ArrayList<Cliente> clientes = gestionarCli.listar();
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }
    
    private void cargarCelulares() {
        ArrayList<Celular> celulares = gestionarCel.listar();
        for (Celular c : celulares) {
            comboCelulares.addItem(c);
        }
    }
    
    private void actualizarPrecio() {
        if (comboCelulares.getSelectedItem() != null) {
            Celular cel = (Celular) comboCelulares.getSelectedItem();
            // Aquí se puede mostrar el precio del celular si se desea
        }
    }
    
    private void agregarDetalle() {
        if (comboClientes.getSelectedItem() == null || comboCelulares.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione cliente y celular", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Celular cel = (Celular) comboCelulares.getSelectedItem();
        int cantidad = (int) spinnerCantidad.getValue();
        
        // Validar stock
        if (cel.getStock() < cantidad) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + cel.getStock(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Agregar a detalles
        detallesVenta.add(new gestionarVentaImpl.detalles_temp(cel.getId(), cantidad));
        
        double subtotal = cel.getPrecio() * cantidad;
        
        modeloTabla.addRow(new Object[]{
            cel.getId(),
            cel.getModelo(),
            "$" + String.format("%.2f", cel.getPrecio()),
            cantidad,
            "$" + String.format("%.2f", subtotal)
        });
        
        actualizarTotales();
    }
    
    private void eliminarDetalle() {
        int fila = tablaDetalles.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        detallesVenta.remove(fila);
        modeloTabla.removeRow(fila);
        actualizarTotales();
    }
    
    private void actualizarTotales() {
        double subtotal = 0;
        
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String precio = modeloTabla.getValueAt(i, 2).toString().replace("$", "");
            int cantidad = (int) modeloTabla.getValueAt(i, 3);
            subtotal += Double.parseDouble(precio) * cantidad;
        }
        
        double iva = subtotal * 0.19;
        double total = subtotal + iva;
        
        lblSubtotal.setText("$" + String.format("%.2f", subtotal));
        lblIVA.setText("$" + String.format("%.2f", iva));
        lblTotal.setText("$" + String.format("%.2f", total));
    }
    
    private void registrarVenta() {
        if (detallesVenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un detalle", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        int idVenta = gestionarVen.registrarVentaCompleta(cliente.getId(), detallesVenta);
        
        if (idVenta > 0) {
            JOptionPane.showMessageDialog(this, "Venta #" + idVenta + " registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        detallesVenta.clear();
        modeloTabla.setRowCount(0);
        spinnerCantidad.setValue(1);
        actualizarTotales();
    }
    
    @Override
    public String toString() {
        return "REGISTRO DE VENTAS";
    }
}
