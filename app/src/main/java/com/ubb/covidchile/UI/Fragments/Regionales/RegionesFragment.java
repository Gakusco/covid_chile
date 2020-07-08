package com.ubb.covidchile.UI.Fragments.Regionales;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ubb.covidchile.Common.Constantes;
import com.ubb.covidchile.Common.Utilities;
import com.ubb.covidchile.R;
import com.ubb.covidchile.Retrofit.Request.Coordenadas;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegionesFragment extends Fragment {
    GoogleMap globalMap;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            generarRegiones(googleMap);
            googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                @Override
                public void onPolygonClick(Polygon polygon) {
                    Toast.makeText(getActivity().getApplicationContext(), polygon.getTag() + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regiones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void lol(GoogleMap googleMap, int numRegion) {
        Coordenadas coordenadas = readJsonFromAssets(numRegion);
        List<LatLng> listLatLng = new ArrayList<>();

        for (int i = 0; i < coordenadas.getCoordinates().size(); i++) {
            listLatLng.add(new LatLng(coordenadas.getCoordinates().get(i).get(1), coordenadas.getCoordinates().get(i).get(0)));
        }

        PolygonOptions polOpt = new PolygonOptions().addAll(listLatLng)
                .fillColor(Color.parseColor(Constantes.COLOR_CELESTE_CLARO))
                .strokeColor(Color.parseColor(Constantes.COLOR_BLANCO))
                .strokeWidth(1);
        Polygon polygon2 = googleMap.addPolygon(polOpt);
        polygon2.setClickable(true);
        polygon2.setTag(numRegion);

    }

    private Coordenadas readJsonFromAssets(int numRegion){
        String jsonFileString = Utilities.getJsonFromAssets(getActivity().getApplicationContext(), "regiones/" + numRegion + ".json");
        Gson gson = new Gson();
        Type coordenadas = new TypeToken<Coordenadas>() {
        }.getType();
        return gson.fromJson(jsonFileString, coordenadas);
    }

    private void generarRegiones(GoogleMap googleMap) {
        lol(googleMap,1);
        lol(googleMap,2);
        lol(googleMap,3);
        lol(googleMap,4);
        lol(googleMap,5);
        lol(googleMap,6);
        lol(googleMap,7);
        lol(googleMap,8);
        lol(googleMap,9);
        lol(googleMap,10);
        lol(googleMap,11);
        lol(googleMap,12);
        lol(googleMap,13);
        lol(googleMap,14);
        lol(googleMap,15);
        lol(googleMap,16);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.06665213857739, -70.64208984375), 2));
    }
}