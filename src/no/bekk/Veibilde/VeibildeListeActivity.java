package no.bekk.Veibilde;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import no.bekk.Veibilde.domain.WeatherCamera;
import no.bekk.Veibilde.service.AsyncTaskDelegate;
import no.bekk.Veibilde.service.GetWeatherCameraAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class VeibildeListeActivity  extends Activity implements AsyncTaskDelegate<WeatherCamera>
{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GetWeatherCameraAsyncTask task = new GetWeatherCameraAsyncTask(this);
        task.execute();

    }

    @Override
    public void publishItem(WeatherCamera object) {
        System.out.println(object.getId());
    }

    @Override
    public void didFailWithError(String errorMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void didFinishProsess(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
