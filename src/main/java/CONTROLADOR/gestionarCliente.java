package CONTROLADOR;

import MODELO.Cliente;
import java.util.ArrayList;

public interface gestionarCliente {
    
    void guardar(Cliente c);
    void actualizar(Cliente c, int id);
    void eliminar(int id);
    
    ArrayList<Cliente> listar();
    Cliente buscar(int id);
}
