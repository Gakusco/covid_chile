package com.ubb.covidchile.Retrofit.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coordenadas {
    @SerializedName("coordinates")
    @Expose
    private List<List<Double>> coordinates = null;

    /**
     * No args constructor for use in serialization
     */
    public Coordenadas() {
    }

    /**
     * @param coordinates
     */
    public Coordenadas(List<List<Double>> coordinates) {
        super();
        this.coordinates = coordinates;
    }

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }
}
