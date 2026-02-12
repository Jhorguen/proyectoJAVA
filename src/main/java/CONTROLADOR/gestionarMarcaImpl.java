package CONTROLADOR;

import MODELO.Marca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class gestionarMarcaImpl implements gestionarMarca{
    
    Conexion c = new Conexion();
    
    @Override
    public void guardar(Marca m) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("insert into marca(nombre) values (?)");
            ps.setString(1, m.getNombre());
            ps.executeUpdate();
            System.out.println("REGISTRO EXITOSO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void actualizar(Marca m, int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE marca SET nombre=? WHERE id=?");
            ps.setString(1, m.getNombre());
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("ACTUALIZACION REALIZADA CON EXITO!");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("delete from marca where id=?");
            ps.setInt(1, id);
            int op = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la marca?", null, JOptionPane.YES_NO_OPTION);
            if (op == 0) {
                ps.executeUpdate();
                System.out.println("ELIMINADO CON EXITO!");
            } else {
                System.out.println("Operacion cancelada");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Marca> listar() {
        ArrayList<Marca> marcas = new ArrayList<>();
        try (Connection con = c.conexion()) {
            //creo el statement para que quede listo cuando quiera escribir en sql
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from marca");
            while (rs.next()) {
                marcas.add(new Marca(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return marcas;

    }

    @Override
    public Marca buscar(int id) {
        Marca mar = new Marca();
        try (Connection con = c.conexion()) {
            PreparedStatement ps = con.prepareStatement("select * from marca where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mar.setId(rs.getInt(1));
                mar.setNombre(rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return mar;
    }
}
