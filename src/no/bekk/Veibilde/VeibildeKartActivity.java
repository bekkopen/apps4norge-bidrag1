package no.bekk.Veibilde;

import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.os.Bundle;
import no.bekk.Veibilde.domain.WeatherCamera;
import no.bekk.Veibilde.service.AsyncTaskDelegate;

public class VeibildeKartActivity extends Activity implements AsyncTaskDelegate<WeatherCamera>  {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kart);
		
		MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		GoogleMap map = fragment.getMap();
		map.setMyLocationEnabled(true);
		
	}

    @Override
    public void publishItem(WeatherCamera weatherCamera) {
        //Plot in weatherCameraObject
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
