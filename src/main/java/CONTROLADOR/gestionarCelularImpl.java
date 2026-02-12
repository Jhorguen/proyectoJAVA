package CONTROLADOR;

import MODELO.Celular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class gestionarCelularImpl implements gestionarCelular {
    
    Conexion c = new Conexion();
    
    @Override
    public void guardar(Celular cel) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("insert into celular(marca, modelo, precio, stock, sistemOp, gama) values (?, ?, ?, ?, ?, ?)");
            ps.setString(1, cel.getMarca());
            ps.setString(2, cel.getModelo());
            ps.setDouble(3, cel.getPrecio());
            ps.setInt(4, cel.getStock());
            ps.setString(5, cel.getSistemOp());
            ps.setString(6, cel.getGama());
            ps.executeUpdate();
            System.out.println("CELULAR REGISTRADO CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void actualizar(Celular cel, int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE celular SET marca=?, modelo=?, precio=?, stock=?, sistemOp=?, gama=? WHERE id=?");
            ps.setString(1, cel.getMarca());
            ps.setString(2, cel.getModelo());
            ps.setDouble(3, cel.getPrecio());
            ps.setInt(4, cel.getStock());
            ps.setString(5, cel.getSistemOp());
            ps.setString(6, cel.getGama());
            ps.setInt(7, id);
            ps.executeUpdate();
            System.out.println("CELULAR ACTUALIZADO CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("delete from celular where id=?");
            ps.setInt(1, id);
            int op = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el celular?", null, JOptionPane.YES_NO_OPTION);
            if (op == 0) {
                ps.executeUpdate();
                System.out.println("CELULAR ELIMINADO CON EXITO!");
            } else {
                System.out.println("Operacion cancelada");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Celular> listar() {
        ArrayList<Celular> celulares = new ArrayList<>();
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from celular");
            while (rs.next()) {
                celulares.add(new Celular(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return celulares;
    }

    @Override
    public Celular buscar(int id) {
        Celular cel = new Celular();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from celular where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cel.setId(rs.getInt(1));
                cel.setMarca(rs.getString(2));
                cel.setModelo(rs.getString(3));
                cel.setPrecio(rs.getDouble(4));
                cel.setStock(rs.getInt(5));
                cel.setSistemOp(rs.getString(6));
                cel.setGama(rs.getString(7));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cel;
    }

    @Override
    public void actualizarStock(int id, int cantidad) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE celular SET stock = stock - ? WHERE id=?");
            ps.setInt(1, cantidad);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("STOCK ACTUALIZADO!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
