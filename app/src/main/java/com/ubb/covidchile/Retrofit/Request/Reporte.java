package com.ubb.covidchile.Retrofit.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reporte {

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("acumulado_total")
    @Expose
    private int acumuladoTotal;

    @SerializedName("casos_nuevos_total")
    @Expose
    private int casosNuevosTotal;

    @SerializedName("casos_nuevos_csintomas")
    @Expose
    private int casosNuevosCSintomas;

    @SerializedName("casos_nuevos_ssintomas")
    @Expose
    private int casosNuevosSSintomas;

    @SerializedName("casos_nuevos_snotificar")
    @Expose
    private int casosNuevosSNotificar;

    @SerializedName("fallecidos")
    @Expose
    private int fallecidos;

    @SerializedName("casos_activos_confirmados")
    @Expose
    private int casosActivosConfirmados;

    public Reporte(String region, int acumuladoTotal, int casosNuevosTotal, int casosNuevosCSintomas, int casosNuevosSSintomas, int casosNuevosSNotificar, int fallecidos, int casosActivosConfirmados) {
        this.region = region;
        this.acumuladoTotal = acumuladoTotal;
        this.casosNuevosTotal = casosNuevosTotal;
        this.casosNuevosCSintomas = casosNuevosCSintomas;
        this.casosNuevosSSintomas = casosNuevosSSintomas;
        this.casosNuevosSNotificar = casosNuevosSNotificar;
        this.fallecidos = fallecidos;
        this.casosActivosConfirmados = casosActivosConfirmados;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getAcumuladoTotal() {
        return acumuladoTotal;
    }

    public void setAcumuladoTotal(int acumuladoTotal) {
        this.acumuladoTotal = acumuladoTotal;
    }

    public int getCasosNuevosTotal() {
        return casosNuevosTotal;
    }

    public void setCasosNuevosTotal(int casosNuevosTotal) {
        this.casosNuevosTotal = casosNuevosTotal;
    }

    public int getCasosNuevosCSintomas() {
        return casosNuevosCSintomas;
    }

    public void setCasosNuevosCSintomas(int casosNuevosCSintomas) {
        this.casosNuevosCSintomas = casosNuevosCSintomas;
    }

    public int getCasosNuevosSSintomas() {
        return casosNuevosSSintomas;
    }

    public void setCasosNuevosSSintomas(int casosNuevosSSintomas) {
        this.casosNuevosSSintomas = casosNuevosSSintomas;
    }

    public int getCasosNuevosSNotificar() {
        return casosNuevosSNotificar;
    }

    public void setCasosNuevosSNotificar(int casosNuevosSNotificar) {
        this.casosNuevosSNotificar = casosNuevosSNotificar;
    }

    public int getFallecidos() {
        return fallecidos;
    }

    public void setFallecidos(int fallecidos) {
        this.fallecidos = fallecidos;
    }

    public int getCasosActivosConfirmados() {
        return casosActivosConfirmados;
    }

    public void setCasosActivosConfirmados(int casosActivosConfirmados) {
        this.casosActivosConfirmados = casosActivosConfirmados;
    }

    @Override
    public String toString() {
        return "Reporte{" +
                "region='" + region + '\'' +
                ", acumuladoTotal=" + acumuladoTotal +
                ", casosNuevosTotal=" + casosNuevosTotal +
                ", casosNuevosCSintomas=" + casosNuevosCSintomas +
                ", casosNuevosSSintomas=" + casosNuevosSSintomas +
                ", casosNuevosSNotificar=" + casosNuevosSNotificar +
                ", fallecidos=" + fallecidos +
                ", casosActivosConfirmados=" + casosActivosConfirmados +
                '}';
    }
}
