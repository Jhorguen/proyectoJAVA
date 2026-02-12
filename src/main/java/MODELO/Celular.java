package MODELO;

public class Celular {
    private int id;
    private String marca;
    private String modelo;
    private Double precio;
    private int stock;
    private String sistemOp;
    private String gama;

    public Celular() {
    }

    public Celular(int id, String marca, String modelo, Double precio, int stock, String sistemOp, String gama) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
        this.sistemOp = sistemOp;
        this.gama = gama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSistemOp() {
        return sistemOp;
    }

    public void setSistemOp(String sistemOp) {
        this.sistemOp = sistemOp;
    }

    public String getGama() {
        return gama;
    }

    public void setGama(String gama) {
        this.gama = gama;
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " - $" + String.format("%.2f", precio) + " (Stock: " + stock + ")";
    }
}
