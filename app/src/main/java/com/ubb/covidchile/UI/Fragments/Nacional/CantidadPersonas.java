package com.ubb.covidchile.UI.Fragments.Nacional;

public class CantidadPersonas {
    private String caracteristica;
    private int cantidad;

    public CantidadPersonas(String caracteristica, int cantidad) {
        this.caracteristica = caracteristica;
        this.cantidad = cantidad;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "CantidadPersonas{" +
                "caracteristica='" + caracteristica + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
