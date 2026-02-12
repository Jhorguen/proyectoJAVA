package CONTROLADOR;

import MODELO.Ventas;
import MODELO.Cliente;
import MODELO.detalle_ventas;
import MODELO.Celular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class gestionarVentaImpl implements gestionarVenta {
    
    Conexion c = new Conexion();
    gestionarCelularImpl gestionarCel = new gestionarCelularImpl();
    gestionarDetalleVentaImpl gestionarDetalle = new gestionarDetalleVentaImpl();
    
    private static final double IVA = 0.19; // 19% de IVA
    
    @Override
    public void guardar(Ventas v) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("insert into ventas(id_cliente, fecha, total) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, v.getId_cliente().getId());
            ps.setString(2, v.getFecha().toString());
            ps.setDouble(3, v.getTotal());
            ps.executeUpdate();
            System.out.println("VENTA REGISTRADA CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void actualizar(Ventas v, int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE ventas SET id_cliente=?, fecha=?, total=? WHERE id=?");
            ps.setInt(1, v.getId_cliente().getId());
            ps.setString(2, v.getFecha().toString());
            ps.setDouble(3, v.getTotal());
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("VENTA ACTUALIZADA CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("delete from ventas where id=?");
            ps.setInt(1, id);
            int op = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la venta?", null, JOptionPane.YES_NO_OPTION);
            if (op == 0) {
                ps.executeUpdate();
                System.out.println("VENTA ELIMINADA CON EXITO!");
            } else {
                System.out.println("Operacion cancelada");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Ventas> listar() {
        ArrayList<Ventas> ventas = new ArrayList<>();
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from ventas");
            while (rs.next()) {
                Ventas v = new Ventas();
                v.setId(rs.getInt(1));
                v.setId_cliente(obtenerCliente(rs.getInt(2)));
                v.setFecha(LocalDate.parse(rs.getString(3)));
                v.setTotal(rs.getDouble(4));
                ventas.add(v);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ventas;
    }

    @Override
    public Ventas buscar(int id) {
        Ventas v = new Ventas();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from ventas where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                v.setId(rs.getInt(1));
                v.setId_cliente(obtenerCliente(rs.getInt(2)));
                v.setFecha(LocalDate.parse(rs.getString(3)));
                v.setTotal(rs.getDouble(4));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return v;
    }
    
    private Cliente obtenerCliente(int idCliente) {
        Cliente cl = new Cliente();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from cliente where id=?");
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cl.setId(rs.getInt(1));
                cl.setNombre(rs.getString(2));
                cl.setIdentificacion(rs.getString(3));
                cl.setCorreo(rs.getString(4));
                cl.setTelefono(rs.getInt(5));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cl;
    }
    

    public int registrarVentaCompleta(int idCliente, ArrayList<detalles_temp> detalles) {
        double subtotal = 0;
        double iva = 0;
        double total = 0;
        int idVenta = -1;
        
        // Calcular subtotal
        for (detalles_temp d : detalles) {
            Celular cel = gestionarCel.buscar(d.idCelular);
            double precioUnitario = cel.getPrecio();
            double montoDetalle = precioUnitario * d.cantidad;
            subtotal += montoDetalle;
        }
        
        iva = subtotal * IVA;
        total = subtotal + iva;
        
        try (Connection con = c.conexion()) {
            // Insertar venta
            Cliente cliente = obtenerCliente(idCliente);
            Ventas venta = new Ventas();
            venta.setId_cliente(cliente);
            venta.setFecha(LocalDate.now());
            venta.setTotal(total);
            
            PreparedStatement psVenta = con.prepareStatement("insert into ventas(id_cliente, fecha, total) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, idCliente);
            psVenta.setString(2, LocalDate.now().toString());
            psVenta.setDouble(3, total);
            psVenta.executeUpdate();
            
            // sacar id de venta nueva
            ResultSet rsVenta = psVenta.getGeneratedKeys();
            if (rsVenta.next()) {
                idVenta = rsVenta.getInt(1);
                venta.setId(idVenta);
                
                // detales
                for (detalles_temp d : detalles) {
                    Celular cel = gestionarCel.buscar(d.idCelular);
                    double montoDetalle = cel.getPrecio() * d.cantidad;
                    PreparedStatement psDetalle = con.prepareStatement("insert into detalle_ventas(id_venta, id_celular, cantidad, subtotal) values (?, ?, ?, ?)");
                    psDetalle.setInt(1, idVenta);
                    psDetalle.setInt(2, d.idCelular);
                    psDetalle.setInt(3, d.cantidad);
                    psDetalle.setDouble(4, montoDetalle);
                    psDetalle.executeUpdate();
                    
                    // Actualizar stok
                    gestionarCel.actualizarStock(d.idCelular, d.cantidad);
                }
                
                System.out.println("===== VENTA REGISTRADA EXITOSAMENTE =====");
                System.out.println("ID Venta: " + idVenta);
                System.out.println("Subtotal: $" + String.format("%.2f", subtotal));
                System.out.println("IVA (19%): $" + String.format("%.2f", iva));
                System.out.println("TOTAL: $" + String.format("%.2f", total));
                System.out.println("==========================================");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return idVenta;
    }
    
    public double[] calcularTotalConIVA(double precioUnitario, int cantidad) {
        double subtotal = precioUnitario * cantidad;
        double iva = subtotal * IVA;
        double total = subtotal + iva;
        
        return new double[]{subtotal, iva, total};
    }

    public static class detalles_temp {
        public int idCelular;
        public int cantidad;
        
        public detalles_temp(int idCelular, int cantidad) {
            this.idCelular = idCelular;
            this.cantidad = cantidad;
        }
    }
}
