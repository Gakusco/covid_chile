package com.ubb.covidchile.UI.Fragments.PorRegion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

public class PorRegionFragment extends Fragment {

    private WebServiceClient webServiceClient;
    private WebService webService;
    private TextView tvNameRegion;
    private TextView tvDate;
    private TextView tvCasosTotales;
    private TextView tvNuevosCasos;
    private TextView tvconSintomas;
    private TextView tvsinSintomas;
    private TextView tvSinNotificar;
    private TextView tvFallecidos;
    private TextView tvActivos;

    private TextView progressBarinsideText;
    private ProgressBar progressBar;

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

        initProgressBar(view);

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
                showProgressBar();
                Call<RequestWS> dataId = webService.dataId(Integer.parseInt(polygon.getTag().toString()));
                dataId.enqueue(new Callback<RequestWS>() {
                    @Override
                    public void onResponse(Call<RequestWS> call, Response<RequestWS> response) {
                        hiddenProgressBar();
                        openBottomSheetDialog(response.body());
                    }

                    @Override
                    public void onFailure(Call<RequestWS> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    };

    private void openBottomSheetDialog(RequestWS res) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View bottom = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_bottom_sheet, getView().findViewById(R.id.bottomSheetContainer));

        initTextViews(bottom);
        setTextAndDataTextViews(res);

        bottom.findViewById(R.id.buttonShare).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottom);
        bottomSheetDialog.show();
    }

    private void initTextViews(View bottom) {
        tvNameRegion = bottom.findViewById(R.id.tv_regionName);
        tvDate = bottom.findViewById(R.id.tv_calendario);
        tvCasosTotales = bottom.findViewById(R.id.tv_casosTotales);
        tvNuevosCasos = bottom.findViewById(R.id.tv_nuevosCasos);
        tvconSintomas = bottom.findViewById(R.id.tv_conSintomas);
        tvsinSintomas = bottom.findViewById(R.id.tv_sinSintomas);
        tvSinNotificar = bottom.findViewById(R.id.tv_sinNotificar);
        tvFallecidos = bottom.findViewById(R.id.tv_fallecidos);
        tvActivos = bottom.findViewById(R.id.tv_activos);
    }

    private void setTextAndDataTextViews(RequestWS res) {
        tvNameRegion.setText(res.getInfo());
        tvDate.setText(Constantes.FECHA.concat(res.getFecha()));
        tvCasosTotales.setText(Constantes.CASOS_TOTALES.concat(String.valueOf(res.getReporte().getAcumuladoTotal())));
        tvNuevosCasos.setText(Constantes.NUEVOS_CASOS.concat(String.valueOf(res.getReporte().getCasosNuevosTotal())));
        tvconSintomas.setText(Constantes.CON_SINTOMAS.concat(String.valueOf(res.getReporte().getCasosNuevosCSintomas())));
        tvsinSintomas.setText(Constantes.SIN_SINTOMAS.concat(String.valueOf(res.getReporte().getCasosNuevosSSintomas())));
        tvSinNotificar.setText(Constantes.SIN_NOTIFICAR.concat(String.valueOf(res.getReporte().getCasosNuevosSNotificar())));
        tvFallecidos.setText(Constantes.FALLECIDOS.concat(String.valueOf(res.getReporte().getFallecidos())));
        tvActivos.setText(Constantes.ACTIVOS.concat(String.valueOf(res.getReporte().getCasosActivosConfirmados())));
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
        googleMap.setMinZoomPreference(4.5f);
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

    private void initProgressBar(View view) {
        progressBar = view.findViewById(R.id.progressBarRegion);
        progressBarinsideText = view.findViewById(R.id.progressBarinsideText);
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        progressBarinsideText.setVisibility(View.VISIBLE);
    }

    private void hiddenProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
        progressBarinsideText.setVisibility(View.INVISIBLE);
    }
}