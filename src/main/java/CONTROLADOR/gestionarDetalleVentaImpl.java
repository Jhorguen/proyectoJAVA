package CONTROLADOR;

import MODELO.detalle_ventas;
import MODELO.Ventas;
import MODELO.Celular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class gestionarDetalleVentaImpl implements gestionarDetalleVenta {
    
    Conexion c = new Conexion();
    gestionarCelularImpl gestionarCel = new gestionarCelularImpl();
    
    @Override
    public void guardar(detalle_ventas dv) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("insert into detalle_ventas(id_venta, id_celular, cantidad, subtotal) values (?, ?, ?, ?)");
            ps.setInt(1, dv.getId_venta().getId());
            ps.setInt(2, dv.getId_celular().getId());
            ps.setInt(3, dv.getCantidad());
            ps.setDouble(4, dv.getSubtotal());
            ps.executeUpdate();
            System.out.println("DETALLE DE VENTA REGISTRADO CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void actualizar(detalle_ventas dv, int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE detalle_ventas SET id_venta=?, id_celular=?, cantidad=?, subtotal=? WHERE id=?");
            ps.setInt(1, dv.getId_venta().getId());
            ps.setInt(2, dv.getId_celular().getId());
            ps.setInt(3, dv.getCantidad());
            ps.setDouble(4, dv.getSubtotal());
            ps.setInt(5, id);
            ps.executeUpdate();
            System.out.println("DETALLE DE VENTA ACTUALIZADO CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("delete from detalle_ventas where id=?");
            ps.setInt(1, id);
            int op = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el detalle?", null, JOptionPane.YES_NO_OPTION);
            if (op == 0) {
                ps.executeUpdate();
                System.out.println("DETALLE ELIMINADO CON EXITO!");
            } else {
                System.out.println("Operacion cancelada");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<detalle_ventas> listar() {
        ArrayList<detalle_ventas> detalles = new ArrayList<>();
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from detalle_ventas");
            while (rs.next()) {
                detalle_ventas dv = new detalle_ventas();
                dv.setId(rs.getInt(1));
                dv.setId_venta(obtenerVenta(rs.getInt(2)));
                dv.setId_celular(gestionarCel.buscar(rs.getInt(3)));
                dv.setCantidad(rs.getInt(4));
                dv.setSubtotal(rs.getDouble(5));
                detalles.add(dv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return detalles;
    }

    @Override
    public ArrayList<detalle_ventas> listarPorVenta(int idVenta) {
        ArrayList<detalle_ventas> detalles = new ArrayList<>();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from detalle_ventas where id_venta=?");
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                detalle_ventas dv = new detalle_ventas();
                dv.setId(rs.getInt(1));
                dv.setId_venta(obtenerVenta(rs.getInt(2)));
                dv.setId_celular(gestionarCel.buscar(rs.getInt(3)));
                dv.setCantidad(rs.getInt(4));
                dv.setSubtotal(rs.getDouble(5));
                detalles.add(dv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return detalles;
    }

    @Override
    public detalle_ventas buscar(int id) {
        detalle_ventas dv = new detalle_ventas();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from detalle_ventas where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dv.setId(rs.getInt(1));
                dv.setId_venta(obtenerVenta(rs.getInt(2)));
                dv.setId_celular(gestionarCel.buscar(rs.getInt(3)));
                dv.setCantidad(rs.getInt(4));
                dv.setSubtotal(rs.getDouble(5));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dv;
    }
    
    private Ventas obtenerVenta(int idVenta) {
        Ventas v = new Ventas();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from ventas where id=?");
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                v.setId(rs.getInt(1));
                v.setFecha(LocalDate.parse(rs.getString(3)));
                v.setTotal(rs.getDouble(4));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return v;
    }
}
