package CONTROLADOR;

import MODELO.Ventas;
import java.util.ArrayList;

public interface gestionarVenta {
    
    void guardar(Ventas v);
    void actualizar(Ventas v, int id);
    void eliminar(int id);
    
    ArrayList<Ventas> listar();
    Ventas buscar(int id);
}
