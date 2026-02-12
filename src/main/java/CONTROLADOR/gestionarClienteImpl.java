package CONTROLADOR;

import MODELO.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class gestionarClienteImpl implements gestionarCliente {
    
    Conexion c = new Conexion();
    
    @Override
    public void guardar(Cliente cl) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("insert into clientes(nombre, identificacion, correo, telefono) values (?, ?, ?, ?)");
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getIdentificacion());
            ps.setString(3, cl.getCorreo());
            ps.setInt(4, cl.getTelefono());
            ps.executeUpdate();
            System.out.println("CLIENTE REGISTRADO CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void actualizar(Cliente cl, int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE clientes SET nombre=?, identificacion=?, correo=?, telefono=? WHERE id=?");
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getIdentificacion());
            ps.setString(3, cl.getCorreo());
            ps.setInt(4, cl.getTelefono());
            ps.setInt(5, id);
            ps.executeUpdate();
            System.out.println("CLIENTE ACTUALIZAO CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("delete from clientes where id=?");
            ps.setInt(1, id);
            int op = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el cliente?", null, JOptionPane.YES_NO_OPTION);
            if (op == 0) {
                ps.executeUpdate();
                System.out.println("CLIENTE ELIMINADO CON EXITO!");
            } else {
                System.out.println("Operacion cancelada");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Cliente> listar() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from clientes");
            while (rs.next()) {
                clientes.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clientes;
    }

    @Override
    public Cliente buscar(int id) {
        Cliente cl = new Cliente();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from clientes where id=?");
            ps.setInt(1, id);
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
}
