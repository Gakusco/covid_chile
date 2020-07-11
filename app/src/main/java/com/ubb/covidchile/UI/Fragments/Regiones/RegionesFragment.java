package com.ubb.covidchile.UI.Fragments.Regiones;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.ubb.covidchile.R;
import com.ubb.covidchile.Retrofit.Request.ReporteRegionesWS;
import com.ubb.covidchile.Retrofit.Request.RequestWS;
import com.ubb.covidchile.Retrofit.WebService;
import com.ubb.covidchile.Retrofit.WebServiceClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegionesFragment extends Fragment {

    private RegionesViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(RegionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_regional, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        WebService service = WebServiceClient.getInstance().getWebService();

        final Call<ReporteRegionesWS> resp = service.dataAll();

        resp.enqueue(new Callback<ReporteRegionesWS>() {
            @Override
            public void onResponse(Call<ReporteRegionesWS> call, Response<ReporteRegionesWS> response) {
                if(response.isSuccessful()){
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
        return root;
    }
}
