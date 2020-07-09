package com.ubb.covidchile.UI.Fragments.Nacional;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ubb.covidchile.R;
import com.ubb.covidchile.Retrofit.Request.Reporte;
import com.ubb.covidchile.Retrofit.Request.RequestWS;
import com.ubb.covidchile.Retrofit.WebService;
import com.ubb.covidchile.Retrofit.WebServiceClient;
import com.ubb.covidchile.UI.Activities.MainActivity;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NacionalFragment extends Fragment {

    private NacionalViewModel homeViewModel;

    private WebService servicio;
    private BarChart chart;
    private ArrayList<CantidadPersonas> datos;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<String> nombresReporte;
    private TextView acumuladoTotal;
    private TextView casoTextView;
    private TextView cantidadTextView;
    private TextView fechaTextView;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(NacionalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nacional, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });

        llamarServicio();
        chart = root.findViewById(R.id.chart);
        acumuladoTotal = root.findViewById(R.id.textAcumulado);
        fechaTextView = root.findViewById(R.id.fechaTextView);
        progressBar = root.findViewById(R.id.progressBar);
        declararVariables();
        datosNivelNacional();
        return root;
    }

    private void declararVariables() {
        datos = new ArrayList<>();
        barEntries = new ArrayList<>();
        nombresReporte = new ArrayList<>();
    }

    private void datosNivelNacional() {
        final Call<RequestWS> resp = servicio.dataNacional();

        resp.enqueue(new Callback<RequestWS>() {
            @Override
            public void onResponse(Call<RequestWS> call, Response<RequestWS> response) {
                if(response.isSuccessful()){
                    Log.d("Retrofit",response.body().toString());
                    chart.setVisibility(View.VISIBLE);
                    Reporte reporte = response.body().getReporte();
                    acumuladoTotal.setText("Acumulado total: "+NumberFormat.getInstance().format(reporte.getAcumuladoTotal()));
                    fechaTextView.setText(response.body().getFecha());

                    datos.add(new CantidadPersonas("Casos nuevos total",reporte.getCasosNuevosTotal()));
                    datos.add(new CantidadPersonas("Casos nuevos con síntomas",reporte.getCasosNuevosCSintomas()));
                    datos.add(new CantidadPersonas("Casos nuevos sin síntomas",reporte.getCasosNuevosSSintomas()));
                    datos.add(new CantidadPersonas("Casos nuevos sin notificar",reporte.getCasosNuevosSNotificar()));
                    datos.add(new CantidadPersonas("Fallecidos",reporte.getFallecidos()));
                    datos.add(new CantidadPersonas("Casos activos confirmados",reporte.getCasosActivosConfirmados()));
                    String nombreReporte = "";
                    int cantidad = 0;
                    for(int i = 0;i<datos.size();i++) {
                        cantidad= datos.get(i).getCantidad();
                        nombreReporte = datos.get(i).getCaracteristica();

                        barEntries.add(new BarEntry(i,cantidad));
                        nombresReporte.add(nombreReporte);
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries,"Cantidad de personas");
                    barDataSet.setColors(ColorTemplate.PASTEL_COLORS);

                    Description description = new Description();

                    description.setText("Casos");
                    chart.setDescription(description);

                    BarData barData = new BarData(barDataSet);
                    chart.setData(barData);


                    chart.animateY(1000);
                    chart.invalidate();

                    progressBar.setVisibility(View.GONE);
                    chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            int indice = chart.getData().getDataSetForEntry(e).getEntryIndex((BarEntry) e);
                            String nombreReporte = datos.get(indice).getCaracteristica();
                            String cantidadCasos = NumberFormat.getInstance().format(datos.get(indice).getCantidad());

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setCancelable(true);

                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.informacion_del_caso,null);
                            casoTextView = view.findViewById(R.id.casoTextView);
                            cantidadTextView = view.findViewById(R.id.cantidadTextView);

                            casoTextView.setText(nombreReporte);
                            cantidadTextView.setText(cantidadCasos);
                            builder.setView(view);
                            AlertDialog alertDialog= builder.create();
                            alertDialog.show();
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                } else if (!response.isSuccessful()) {
                    Log.d("Retrofit",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RequestWS> call, Throwable t) {
                Log.d("Retrofit",t.getMessage());
            }
        });
    }

    private void llamarServicio() {
        servicio = WebServiceClient.getInstance().getWebService();
    }
}
