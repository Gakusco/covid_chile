package com.ubb.covidchile.Retrofit.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReporteRegionesWS {
    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("estado")
    @Expose
    private boolean estado;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("reporte")
    @Expose
    private List<Reporte> reporte;

    public ReporteRegionesWS(String info, boolean estado, String fecha, List<Reporte> reporte) {
        this.info = info;
        this.estado = estado;
        this.fecha = fecha;
        this.reporte = reporte;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Reporte> getReporte() {
        return reporte;
    }

    public void setReporte(List<Reporte> reporte) {
        this.reporte = reporte;
    }

    @Override
    public String toString() {
        return "ReporteRegionesWS{" +
                "info='" + info + '\'' +
                ", estado=" + estado +
                ", fecha='" + fecha + '\'' +
                ", reporte=" + reporte +
                '}';
    }
}
