package com.ubb.covidchile.UI.Fragments.Regionales;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ubb.covidchile.Common.Constantes;
import com.ubb.covidchile.Common.Utilities;
import com.ubb.covidchile.R;
import com.ubb.covidchile.Retrofit.Request.Coordenadas;
import com.ubb.covidchile.Retrofit.Request.RequestWS;
import com.ubb.covidchile.Retrofit.WebService;
import com.ubb.covidchile.Retrofit.WebServiceClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class RegionesFragment extends Fragment {

    private WebServiceClient webServiceClient;
    private WebService webService;
    private TextView tvNameRegion;

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
        retrofitInit();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            setConfigMap(googleMap);

            googleMap.setOnPolygonClickListener(polygon -> {
                Call<RequestWS> dataId = webService.dataId(Integer.parseInt(polygon.getTag().toString()));
                dataId.enqueue(new Callback<RequestWS>() {
                    @Override
                    public void onResponse(Call<RequestWS> call, Response<RequestWS> response) {
                        Toast.makeText(getActivity().getApplicationContext(), "Casos totales: " + response.body().getReporte().getAcumuladoTotal() + " region: " + response.body().getInfo(), Toast.LENGTH_SHORT).show();
                        openBottomSheetDialog(response.body().getInfo());
                    }

                    @Override
                    public void onFailure(Call<RequestWS> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    };

    private void openBottomSheetDialog(String nameRegion) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View bottom = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_bottom_sheet, getView().findViewById(R.id.bottomSheetContainer));

        tvNameRegion = bottom.findViewById(R.id.tv_regionName);
        tvNameRegion.setText(nameRegion);

        bottom.findViewById(R.id.buttonShare).setOnClickListener(v -> {
            Toast.makeText(getActivity().getApplicationContext(), "Cerrando", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottom);
        bottomSheetDialog.show();
    }

    private void setConfigMap(GoogleMap googleMap) {
        googleMap.clear();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        generarRegiones(googleMap);
    }

    private void generarRegiones(GoogleMap googleMap) {
        for (int i = 0; i < Constantes.NUM_REGIONES; i++) {
            setPolygonToMap(googleMap, i + 1);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.06665213857739, -70.64208984375), 2));
    }

    private void setPolygonToMap(GoogleMap googleMap, int numRegion) {
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

    private Coordenadas readJsonFromAssets(int numRegion) {
        String jsonFileString = Utilities.getJsonFromAssets(getActivity().getApplicationContext(), "regiones/" + numRegion + ".json");
        Gson gson = new Gson();
        Type coordenadas = new TypeToken<Coordenadas>() {
        }.getType();
        return gson.fromJson(jsonFileString, coordenadas);
    }

    private void retrofitInit() {
        webServiceClient = WebServiceClient.getInstance();
        webService = webServiceClient.getWebService();
    }
}