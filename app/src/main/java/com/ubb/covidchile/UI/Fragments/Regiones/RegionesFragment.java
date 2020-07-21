package com.ubb.covidchile.UI.Fragments.Regiones;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ubb.covidchile.Common.Constantes;
import com.ubb.covidchile.R;
import com.ubb.covidchile.Retrofit.Request.Reporte;
import com.ubb.covidchile.Retrofit.Request.ReporteRegionesWS;
import com.ubb.covidchile.Retrofit.WebService;
import com.ubb.covidchile.Retrofit.WebServiceClient;
import com.ubb.covidchile.UI.Fragments.Nacional.CantidadPersonas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegionesFragment extends Fragment {

    private RegionesViewModel notificationsViewModel;
    private WebService servicio;
    private TextView regionTextView;

    private BarChart chart;
    private TextView fechaTextView;
    private ProgressBar progressBar;
    private CantidadPersonas[][] datos;
    private BarEntry[][] barEntries;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(RegionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_regional, container, false);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        llamarServicio();
        inflarComponentes(root);

        datosRegiones();

        return root;
    }

    private void datosRegiones() {
        final Call<ReporteRegionesWS> resp = servicio.dataAll();

        resp.enqueue(new Callback<ReporteRegionesWS>() {
            @Override
            public void onResponse(Call<ReporteRegionesWS> call, Response<ReporteRegionesWS> response) {
                if(response.isSuccessful()){
                    chart.setVisibility(View.VISIBLE);
                    generarGrafico(response.body());
                    progressBar.setVisibility(View.GONE);
                    interaccionConElGrafico();
                    Log.d("Retrofit",response.body().toString());
                } else if (!response.isSuccessful()) {
                    Log.d("Retrofit",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ReporteRegionesWS> call, Throwable t) {
                Log.d("Retrofit",t.getMessage());
            }
        });
    }

    private void interaccionConElGrafico() {
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int indice = chart.getData().getDataSetForEntry(e).getEntryIndex((BarEntry) e);
                String region = datos[indice][0].getCaracteristica();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.informacion_del_caso,null);
                regionTextView = view.findViewById(R.id.casoTextView);

                regionTextView.setText("Caso de la región: "+region);
                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void generarGrafico(ReporteRegionesWS body) {
        List<Reporte> reportes= body.getReporte();
        fechaTextView.setText(body.getFecha());

        guardarDatosDelReporte(reportes);
        desplegarDatosEnElGrafico();

        BarDataSet barDataSet[] = new BarDataSet[barEntries.length];
        ArrayList valores = new ArrayList<>();
        for (int i = 0; i< barEntries.length; i++) {
            List<BarEntry> barEntry = Arrays.asList(barEntries[i]);
            barDataSet[i] = new BarDataSet(barEntry,"");
        }
        for (int i = 0; i<16; i++) {
            valores.add(datos[i][0].getCaracteristica());
        }

        barDataSet[0].setLabel(Constantes.CASOS_TOTALES);
        barDataSet[1].setLabel(Constantes.NUEVOS_CASOS);
        barDataSet[2].setLabel(Constantes.CON_SINTOMAS);
        barDataSet[3].setLabel(Constantes.SIN_SINTOMAS);
        barDataSet[4].setLabel(Constantes.SIN_NOTIFICAR);
        barDataSet[5].setLabel(Constantes.FALLECIDOS);
        barDataSet[6].setLabel(Constantes.ACTIVOS);

        barDataSet[0].setColors(Color.RED);
        barDataSet[1].setColors(Color.BLACK);
        barDataSet[2].setColors(Color.BLUE);
        barDataSet[3].setColors(Color.YELLOW);
        barDataSet[4].setColors(Color.GRAY);
        barDataSet[5].setColors(Color.GREEN);
        barDataSet[6].setColors(Color.CYAN);


        BarData barData = new BarData(barDataSet[0], barDataSet[1], barDataSet[2], barDataSet[3], barDataSet[4], barDataSet[5],
                barDataSet[6]);
        barData.setBarWidth(0.10f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMaximum(16);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(valores));

        chart.setData(barData);
        chart.groupBars(0f,0f,0f);
        chart.animateY(1000);

        Legend l = chart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        chart.invalidate();
    }

    private void guardarDatosDelReporte(List<Reporte> reporte) {
        datos = new CantidadPersonas[reporte.size()][8];
        for (int i = 0; i<reporte.size(); i++) {
            // i = numero de region
            // j = Cantidad de casos por region
            datos[i][0] = new CantidadPersonas(reporte.get(i).getRegion(),0);
            datos[i][1] = new CantidadPersonas(Constantes.CASOS_TOTALES,reporte.get(i).getAcumuladoTotal());
            datos[i][2] = new CantidadPersonas(Constantes.NUEVOS_CASOS,reporte.get(i).getCasosNuevosTotal());
            datos[i][3] = new CantidadPersonas(Constantes.CON_SINTOMAS,reporte.get(i).getCasosNuevosCSintomas());
            datos[i][4] = new CantidadPersonas(Constantes.SIN_SINTOMAS,reporte.get(i).getCasosNuevosSSintomas());
            datos[i][5] = new CantidadPersonas(Constantes.SIN_NOTIFICAR,reporte.get(i).getCasosNuevosSNotificar());
            datos[i][6] = new CantidadPersonas(Constantes.FALLECIDOS,reporte.get(i).getFallecidos());
            datos[i][7] = new CantidadPersonas(Constantes.ACTIVOS,reporte.get(i).getCasosActivosConfirmados());

        }
    }

    private void desplegarDatosEnElGrafico() {
        llenarLasEntradasDelGrafico();
    }

    private void llenarLasEntradasDelGrafico() {
        barEntries = new BarEntry[datos[0].length-1][datos.length];
        for(int j = 0;j<(datos[0].length-1);j++) {
            for (int i = 0;i< datos.length; i++){
                int cantidad= datos[i][j+1].getCantidad();
                barEntries[j][i] = (new BarEntry(i,cantidad));
                Log.d("Tabla",i+" = "+barEntries[j][i]);
            }
        }
    }


    private void inflarComponentes(View root) {
        chart = root.findViewById(R.id.chart);
        fechaTextView = root.findViewById(R.id.fechaTextView);
        progressBar = root.findViewById(R.id.progressBar);
    }

    private void llamarServicio() {
        servicio = WebServiceClient.getInstance().getWebService();
    }
}
