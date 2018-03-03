package com.example.slava.locationbankomats.fragments;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.slava.locationbankomats.R;
import java.util.concurrent.TimeUnit;
/**
 * Created by Slava on 03.03.2018.
 */

public class SplashFragment extends Fragment{

    public SplashFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SplashTask splashTask = new SplashTask();
        splashTask.execute();
        return inflater.inflate(R.layout.activity_main, container, false);
    }
    class SplashTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getActivity().getFragmentManager().popBackStack();
            return null;
        }
    }
}
