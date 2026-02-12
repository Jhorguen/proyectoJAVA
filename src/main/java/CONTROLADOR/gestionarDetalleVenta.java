package CONTROLADOR;

import MODELO.detalle_ventas;
import java.util.ArrayList;

public interface gestionarDetalleVenta {
    
    void guardar(detalle_ventas dv);
    void actualizar(detalle_ventas dv, int id);
    void eliminar(int id);
    
    ArrayList<detalle_ventas> listar();
    ArrayList<detalle_ventas> listarPorVenta(int idVenta);
    detalle_ventas buscar(int id);
}
