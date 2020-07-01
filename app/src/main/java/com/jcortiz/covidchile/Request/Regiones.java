package com.jcortiz.covidchile.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Regiones {

    // Tengo mis dudas en esta parte.

    @SerializedName("id")
    @Expose
    private List<Integer> id;

    @SerializedName("nombre")
    @Expose
    private List<String> nombre;

    public Regiones(List<Integer> id, List<String> nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public List<String> getNombre() {
        return nombre;
    }

    public void setNombre(List<String> nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Regiones{" +
                "id=" + id +
                ", nombre=" + nombre +
                '}';
    }
}
