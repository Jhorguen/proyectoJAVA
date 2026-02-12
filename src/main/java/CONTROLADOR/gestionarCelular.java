package CONTROLADOR;

import MODELO.Celular;
import java.util.ArrayList;

public interface gestionarCelular {
    
    void guardar(Celular cel);
    void actualizar(Celular cel, int id);
    void eliminar(int id);
    
    ArrayList<Celular> listar();
    Celular buscar(int id);
    void actualizarStock(int id, int cantidad);
}
