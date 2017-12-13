package mx.com.develop.intimex;

import java.io.File;
import java.io.Serializable;

public class Producto implements Serializable {
    private File foto;
    private String nombre;
    private String marca;
    private float precioCompra;
    private float precioVenta;
    private int cantidad;

    public Producto() {
    }

    public Producto(File foto, String nombre, String marca,float precioCompra, float precioVenta, int cantidad ) {
        this.foto=foto;
        this.cantidad = cantidad;
        this.marca = marca;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
    }

    public File getFoto() {
        return foto;
    }

    public void setFoto(File foto) {
        this.foto = foto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }
}
