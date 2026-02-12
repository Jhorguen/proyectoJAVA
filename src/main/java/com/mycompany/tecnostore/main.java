package com.mycompany.tecnostore;

import CONTROLADOR.*;
import MODELO.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    private static gestionarMarcaImpl gestionarMarc = new gestionarMarcaImpl();
    private static gestionarClienteImpl gestionarCli = new gestionarClienteImpl();
    private static gestionarCelularImpl gestionarCel = new gestionarCelularImpl();
    private static gestionarVentaImpl gestionarVen = new gestionarVentaImpl();
    private static ReporteVentasFile reportesFile = new ReporteVentasFile();
    private static Reportes reportes = new Reportes();
    
    public static void main(String[] args) {
        limpiarConsola();
        menuPrincipal();
    }
    
    public static void menuPrincipal() {
        boolean ejecutando = true;
        while (ejecutando) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║          🏪 TECNO STORE - SISTEMA DE GESTIÓN           ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("█ GESTIÓN:");
            System.out.println("  1. Gestionar Marcas");
            System.out.println("  2. Gestionar Clientes");
            System.out.println("  3. Gestionar Celulares");
            System.out.println();
            System.out.println("█ VENTAS:");
            System.out.println("  4. Registrar Nueva Venta");
            System.out.println();
            System.out.println("█ REPORTES:");
            System.out.println("  5. Generar Reportes");
            System.out.println();
            System.out.println("  0. Salir del Sistema");
            System.out.println();
            System.out.print("Seleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        menuMarcas();
                        break;
                    case 2:
                        menuClientes();
                        break;
                    case 3:
                        menuCelulares();
                        break;
                    case 4:
                        menuRegistroVentas();
                        break;
                    case 5:
                        menuReportes();
                        break;
                    case 0:
                        System.out.print("\n¿Desea salir del sistema? (s/n): ");
                        String respuesta = scanner.nextLine().toLowerCase();
                        if (respuesta.equals("s")) {
                            ejecutando = false;
                            System.out.println("\n✓ Gracias por usar TECNO STORE. ¡Hasta luego!");
                        }
                        break;
                    default:
                        System.out.println("\n✗ Opción no válida");
                        pausa();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n✗ Ingrese un número válido");
                pausa();
            }
        }
    }
    
    // ==================== MENÚ DE MARCAS ====================
    public static void menuMarcas() {
        boolean retorno = true;
        while (retorno) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║             GESTIÓN DE MARCAS                          ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  1. Crear Marca");
            System.out.println("  2. Listar Marcas");
            System.out.println("  3. Buscar Marca");
            System.out.println("  4. Actualizar Marca");
            System.out.println("  5. Eliminar Marca");
            System.out.println("  0. Volver al Menú Principal");
            System.out.println();
            System.out.print("Seleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        crearMarca();
                        break;
                    case 2:
                        listarMarcas();
                        break;
                    case 3:
                        buscarMarca();
                        break;
                    case 4:
                        actualizarMarca();
                        break;
                    case 5:
                        eliminarMarca();
                        break;
                    case 0:
                        retorno = false;
                        break;
                    default:
                        System.out.println("\n✗ Opción no válida");
                        pausa();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n✗ Ingrese un número válido");
                pausa();
            }
        }
    }
    
    private static void crearMarca() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    CREAR NUEVA MARCA");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.print("Nombre de marca: ");
        String nombre = scanner.nextLine();
        
        if (nombre.isEmpty()) {
            System.out.println("\n✗ El nombre no puede estar vacío");
            pausa();
            return;
        }
        
        Marca m = new Marca(0, nombre);
        gestionarMarc.guardar(m);
        System.out.println("\n✓ Marca creada exitosamente");
        pausa();
    }
    
    private static void listarMarcas() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    MARCAS REGISTRADAS");
        System.out.println("═══════════════════════════════════════════════════════");
        
        ArrayList<Marca> marcas = gestionarMarc.listar();
        
        if (marcas.isEmpty()) {
            System.out.println("\n✗ No hay marcas registradas");
        } else {
            System.out.printf("%-6s | %-40s\n", "ID", "NOMBRE");
            System.out.println("────────────────────────────────────────────────────");
            for (Marca m : marcas) {
                System.out.printf("%-6d | %-40s\n", m.getId(), m.getNombre());
            }
        }
        pausa();
    }
    
    private static void buscarMarca() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    BUSCAR MARCA");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.print("Nombre a buscar: ");
        String nombre = scanner.nextLine();
        
        ArrayList<Marca> marcas = gestionarMarc.buscar(nombre);
        
        if (marcas.isEmpty()) {
            System.out.println("\n✗ No se encontraron marcas");
        } else {
            System.out.printf("%-6s | %-40s\n", "ID", "NOMBRE");
            System.out.println("────────────────────────────────────────────────────");
            for (Marca m : marcas) {
                System.out.printf("%-6d | %-40s\n", m.getId(), m.getNombre());
            }
        }
        pausa();
    }
    
    private static void actualizarMarca() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    ACTUALIZAR MARCA");
        System.out.println("═══════════════════════════════════════════════════════");
        
        listarMarcas();
        
        System.out.print("\nIngrese ID de marca a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Nuevo nombre: ");
            String nuevoNombre = scanner.nextLine();
            
            Marca m = new Marca(id, nuevoNombre);
            gestionarMarc.actualizar(m, id);
            System.out.println("\n✓ Marca actualizada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ ID debe ser un número");
        }
        pausa();
    }
    
    private static void eliminarMarca() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    ELIMINAR MARCA");
        System.out.println("═══════════════════════════════════════════════════════");
        
        listarMarcas();
        
        System.out.print("\nIngrese ID de marca a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            gestionarMarc.eliminar(id);
            System.out.println("\n✓ Marca eliminada exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ ID debe ser un número");
        }
        pausa();
    }
    
    // ==================== MENÚ DE CLIENTES ====================
    public static void menuClientes() {
        boolean retorno = true;
        while (retorno) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║             GESTIÓN DE CLIENTES                        ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  1. Crear Cliente");
            System.out.println("  2. Listar Clientes");
            System.out.println("  3. Buscar Cliente");
            System.out.println("  4. Actualizar Cliente");
            System.out.println("  5. Eliminar Cliente");
            System.out.println("  0. Volver al Menú Principal");
            System.out.println();
            System.out.print("Seleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        crearCliente();
                        break;
                    case 2:
                        listarClientes();
                        break;
                    case 3:
                        buscarCliente();
                        break;
                    case 4:
                        actualizarCliente();
                        break;
                    case 5:
                        eliminarCliente();
                        break;
                    case 0:
                        retorno = false;
                        break;
                    default:
                        System.out.println("\n✗ Opción no válida");
                        pausa();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n✗ Ingrese un número válido");
                pausa();
            }
        }
    }
    
    private static void crearCliente() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    CREAR NUEVO CLIENTE");
        System.out.println("═══════════════════════════════════════════════════════");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Identificación: ");
        String identificacion = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Teléfono: ");
        
        try {
            int telefono = Integer.parseInt(scanner.nextLine());
            Cliente c = new Cliente(0, nombre, identificacion, correo, telefono);
            gestionarCli.guardar(c);
            System.out.println("\n✓ Cliente creado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Teléfono debe ser un número");
        }
        pausa();
    }
    
    private static void listarClientes() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    CLIENTES REGISTRADOS");
        System.out.println("═══════════════════════════════════════════════════════");
        
        ArrayList<Cliente> clientes = gestionarCli.listar();
        
        if (clientes.isEmpty()) {
            System.out.println("\n✗ No hay clientes registrados");
        } else {
            System.out.printf("%-6s | %-20s | %-15s | %-25s | %-10s\n", "ID", "NOMBRE", "IDENTIFICACIÓN", "CORREO", "TELÉFONO");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────");
            for (Cliente c : clientes) {
                System.out.printf("%-6d | %-20s | %-15s | %-25s | %-10d\n", c.getId(), c.getNombre(), c.getIdentificacion(), c.getCorreo(), c.getTelefono());
            }
        }
        pausa();
    }
    
    private static void buscarCliente() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    BUSCAR CLIENTE");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.print("Nombre a buscar: ");
        String nombre = scanner.nextLine();
        
        ArrayList<Cliente> clientes = gestionarCli.buscar(nombre);
        
        if (clientes.isEmpty()) {
            System.out.println("\n✗ No se encontraron clientes");
        } else {
            System.out.printf("%-6s | %-20s | %-15s | %-25s | %-10s\n", "ID", "NOMBRE", "IDENTIFICACIÓN", "CORREO", "TELÉFONO");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────");
            for (Cliente c : clientes) {
                System.out.printf("%-6d | %-20s | %-15s | %-25s | %-10d\n", c.getId(), c.getNombre(), c.getIdentificacion(), c.getCorreo(), c.getTelefono());
            }
        }
        pausa();
    }
    
    private static void actualizarCliente() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    ACTUALIZAR CLIENTE");
        System.out.println("═══════════════════════════════════════════════════════");
        
        listarClientes();
        
        System.out.print("\nIngrese ID de cliente a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva identificación: ");
            String identificacion = scanner.nextLine();
            System.out.print("Nuevo correo: ");
            String correo = scanner.nextLine();
            System.out.print("Nuevo teléfono: ");
            int telefono = Integer.parseInt(scanner.nextLine());
            
            Cliente c = new Cliente(id, nombre, identificacion, correo, telefono);
            gestionarCli.actualizar(c, id);
            System.out.println("\n✓ Cliente actualizado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Datos inválidos");
        }
        pausa();
    }
    
    private static void eliminarCliente() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    ELIMINAR CLIENTE");
        System.out.println("═══════════════════════════════════════════════════════");
        
        listarClientes();
        
        System.out.print("\nIngrese ID de cliente a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            gestionarCli.eliminar(id);
            System.out.println("\n✓ Cliente eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ ID debe ser un número");
        }
        pausa();
    }
    
    // ==================== MENÚ DE CELULARES ====================
    public static void menuCelulares() {
        boolean retorno = true;
        while (retorno) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║             GESTIÓN DE CELULARES                      ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  1. Crear Celular");
            System.out.println("  2. Listar Celulares");
            System.out.println("  3. Buscar Celular");
            System.out.println("  4. Actualizar Celular");
            System.out.println("  5. Eliminar Celular");
            System.out.println("  0. Volver al Menú Principal");
            System.out.println();
            System.out.print("Seleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        crearCelular();
                        break;
                    case 2:
                        listarCelulares();
                        break;
                    case 3:
                        buscarCelular();
                        break;
                    case 4:
                        actualizarCelular();
                        break;
                    case 5:
                        eliminarCelular();
                        break;
                    case 0:
                        retorno = false;
                        break;
                    default:
                        System.out.println("\n✗ Opción no válida");
                        pausa();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n✗ Ingrese un número válido");
                pausa();
            }
        }
    }
    
    private static void crearCelular() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    CREAR NUEVO CELULAR");
        System.out.println("═══════════════════════════════════════════════════════");
        
        ArrayList<Marca> marcas = gestionarMarc.listar();
        if (marcas.isEmpty()) {
            System.out.println("\n✗ Debe crear marcas primero");
            pausa();
            return;
        }
        
        System.out.println("\nMarcas disponibles:");
        for (int i = 0; i < marcas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + marcas.get(i).getNombre());
        }
        
        System.out.print("\nSeleccione marca (número): ");
        try {
            int marcaIdx = Integer.parseInt(scanner.nextLine()) - 1;
            if (marcaIdx < 0 || marcaIdx >= marcas.size()) {
                System.out.println("\n✗ Marca no válida");
                pausa();
                return;
            }
            
            String marca = marcas.get(marcaIdx).getNombre();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();
            System.out.print("Precio: $");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            System.out.print("Sistema Operativo: ");
            String sistemaOp = scanner.nextLine();
            System.out.print("Gama (Gama Baja/Media/Alta): ");
            String gama = scanner.nextLine();
            
            Celular c = new Celular(0, marca, modelo, precio, stock, sistemaOp, gama);
            gestionarCel.guardar(c);
            System.out.println("\n✓ Celular creado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Datos inválidos");
        }
        pausa();
    }
    
    private static void listarCelulares() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    CELULARES REGISTRADOS");
        System.out.println("═══════════════════════════════════════════════════════");
        
        ArrayList<Celular> celulares = gestionarCel.listar();
        
        if (celulares.isEmpty()) {
            System.out.println("\n✗ No hay celulares registrados");
        } else {
            System.out.printf("%-6s | %-15s | %-20s | %-10s | %-8s | %-10s | %-10s\n", "ID", "MARCA", "MODELO", "PRECIO", "STOCK", "S.O", "GAMA");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────");
            for (Celular c : celulares) {
                System.out.printf("%-6d | %-15s | %-20s | $%-9.2f | %-8d | %-10s | %-10s\n", c.getId(), c.getMarca(), c.getModelo(), c.getPrecio(), c.getStock(), c.getSistemOp(), c.getGama());
            }
        }
        pausa();
    }
    
    private static void buscarCelular() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    BUSCAR CELULAR");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.print("Modelo a buscar: ");
        String modelo = scanner.nextLine();
        
        ArrayList<Celular> celulares = gestionarCel.buscar(modelo);
        
        if (celulares.isEmpty()) {
            System.out.println("\n✗ No se encontraron celulares");
        } else {
            System.out.printf("%-6s | %-15s | %-20s | %-10s | %-8s | %-10s | %-10s\n", "ID", "MARCA", "MODELO", "PRECIO", "STOCK", "S.O", "GAMA");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────");
            for (Celular c : celulares) {
                System.out.printf("%-6d | %-15s | %-20s | $%-9.2f | %-8d | %-10s | %-10s\n", c.getId(), c.getMarca(), c.getModelo(), c.getPrecio(), c.getStock(), c.getSistemOp(), c.getGama());
            }
        }
        pausa();
    }
    
    private static void actualizarCelular() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    ACTUALIZAR CELULAR");
        System.out.println("═══════════════════════════════════════════════════════");
        
        listarCelulares();
        
        System.out.print("\nIngrese ID de celular a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Nueva marca: ");
            String marca = scanner.nextLine();
            System.out.print("Nuevo modelo: ");
            String modelo = scanner.nextLine();
            System.out.print("Nuevo precio: $");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Nuevo stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            System.out.print("Nuevo S.O: ");
            String so = scanner.nextLine();
            System.out.print("Nueva gama: ");
            String gama = scanner.nextLine();
            
            Celular c = new Celular(id, marca, modelo, precio, stock, so, gama);
            gestionarCel.actualizar(c, id);
            System.out.println("\n✓ Celular actualizado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Datos inválidos");
        }
        pausa();
    }
    
    private static void eliminarCelular() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    ELIMINAR CELULAR");
        System.out.println("═══════════════════════════════════════════════════════");
        
        listarCelulares();
        
        System.out.print("\nIngrese ID de celular a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            gestionarCel.eliminar(id);
            System.out.println("\n✓ Celular eliminado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("\n✗ ID debe ser un número");
        }
        pausa();
    }
    
    // ==================== MENÚ DE VENTAS ====================
    public static void menuRegistroVentas() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    NUEVA VENTA");
        System.out.println("═══════════════════════════════════════════════════════");
        
        ArrayList<Cliente> clientes = gestionarCli.listar();
        ArrayList<Celular> celulares = gestionarCel.listar();
        
        if (clientes.isEmpty() || celulares.isEmpty()) {
            System.out.println("\n✗ Debe tener clientes y celulares registrados");
            pausa();
            return;
        }
        
        try {
            System.out.println("\nClientes disponibles:");
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + clientes.get(i).getNombre() + " (" + clientes.get(i).getIdentificacion() + ")");
            }
            System.out.print("\nSeleccione cliente (número): ");
            int clienteIdx = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (clienteIdx < 0 || clienteIdx >= clientes.size()) {
                System.out.println("\n✗ Cliente no válido");
                pausa();
                return;
            }
            
            int idCliente = clientes.get(clienteIdx).getId();
            ArrayList<gestionarVentaImpl.detalles_temp> detalles = new ArrayList<>();
            double subtotalVenta = 0;
            
            boolean agregarMas = true;
            while (agregarMas) {
                System.out.println("\nCelulares disponibles:");
                for (int i = 0; i < celulares.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + celulares.get(i).getMarca() + " " + celulares.get(i).getModelo() + " - $" + String.format("%.2f", celulares.get(i).getPrecio()) + " (Stock: " + celulares.get(i).getStock() + ")");
                }
                
                System.out.print("\nSeleccione celular (número): ");
                int celularIdx = Integer.parseInt(scanner.nextLine()) - 1;
                
                if (celularIdx < 0 || celularIdx >= celulares.size()) {
                    System.out.println("\n✗ Celular no válido");
                    continue;
                }
                
                Celular celular = celulares.get(celularIdx);
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                
                if (cantidad > celular.getStock()) {
                    System.out.println("\n✗ Stock insuficiente");
                    pausa();
                    continue;
                }
                
                double subtotal = celular.getPrecio() * cantidad;
                detalles.add(gestionarVen.new detalles_temp(celular.getId(), cantidad, subtotal));
                subtotalVenta += subtotal;
                
                System.out.println("\n✓ Producto agregado");
                System.out.print("¿Agregar otro producto? (s/n): ");
                agregarMas = scanner.nextLine().toLowerCase().equals("s");
            }
            
            if (detalles.isEmpty()) {
                System.out.println("\n✗ No hay productos en la venta");
                pausa();
                return;
            }
            
            double iva = subtotalVenta * 0.19;
            double totalConIva = subtotalVenta + iva;
            
            System.out.println("\n" + "═".repeat(30));
            System.out.println("RESUMEN DE VENTA");
            System.out.println("═".repeat(30));
            System.out.printf("Subtotal:        $%.2f\n", subtotalVenta);
            System.out.printf("IVA (19%%):       $%.2f\n", iva);
            System.out.printf("TOTAL:           $%.2f\n", totalConIva);
            System.out.println("═".repeat(30));
            
            System.out.print("\n¿Confirmar venta? (s/n): ");
            if (scanner.nextLine().toLowerCase().equals("s")) {
                gestionarVen.registrarVentaCompleta(idCliente, detalles);
                System.out.println("\n✓ Venta registrada exitosamente");
            } else {
                System.out.println("\n✓ Venta cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Datos inválidos");
        }
        pausa();
    }
    
    // ==================== MENÚ DE REPORTES ====================
    public static void menuReportes() {
        boolean retorno = true;
        while (retorno) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║             GENERACIÓN DE REPORTES                     ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  1. Generar Reporte TXT");
            System.out.println("  2. Reporte en Consola");
            System.out.println("  3. Stock Bajo");
            System.out.println("  4. Top 3 Celulares Más Vendidos");
            System.out.println("  5. Ventas por Mes");
            System.out.println("  0. Volver al Menú Principal");
            System.out.println();
            System.out.print("Seleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        generarReporteTxt();
                        break;
                    case 2:
                        reportes.generarTodosLosReportes();
                        pausa();
                        break;
                    case 3:
                        reportes.reporteStockBajo();
                        pausa();
                        break;
                    case 4:
                        reportes.reporteTop3CelularesMasVendidos();
                        pausa();
                        break;
                    case 5:
                        reportes.reporteVentasPorMes();
                        pausa();
                        break;
                    case 0:
                        retorno = false;
                        break;
                    default:
                        System.out.println("\n✗ Opción no válida");
                        pausa();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n✗ Ingrese un número válido");
                pausa();
            }
        }
    }
    
    private static void generarReporteTxt() {
        limpiarConsola();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    GENERAR REPORTE TXT");
        System.out.println("═══════════════════════════════════════════════════════");
        
        try {
            reportesFile.generarReporte();
            System.out.println("\n✓ Reporte generado exitosamente");
            System.out.println("  Guardado en: " + ConfiguracionApp.getInstance().getRutaReportes());
        } catch (Exception e) {
            System.out.println("\n✗ Error al generar reporte: " + e.getMessage());
        }
        pausa();
    }
    
    // ==================== FUNCIONES AUXILIARES ====================
    private static void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }
    
    private static void pausa() {
        System.out.print("\nPresione Enter para continuar...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
            // Ignorar excepción
        }
    }
}
