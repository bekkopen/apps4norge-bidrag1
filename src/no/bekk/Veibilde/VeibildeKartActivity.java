package no.bekk.Veibilde;

import no.bekk.Veibilde.domain.WeatherCamera;
import no.bekk.Veibilde.service.AsyncTaskDelegate;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class VeibildeKartActivity extends Activity implements AsyncTaskDelegate<WeatherCamera> {
	private LocationListener locationListener;
	private GoogleMap veiBildeMap;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kart);

		MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		veiBildeMap = fragment.getMap();
		veiBildeMap.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		// GetWeatherCameraAsyncTask task = new GetWeatherCameraAsyncTask(this);
		// task.execute();
	}

	@Override
	public void publishItem(final WeatherCamera object) {
		Log.w("VeilbildeKartActivity", "Found weatherCamera " + object.getId());
	}

	@Override
	public void didFailWithError(final String errorMessage) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void didFinishProsess(final String message) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(final Location location) {
			veiBildeMap
					.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
			Toast.makeText(getApplicationContext(), "Lokasjon funnet", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(final String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(final String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(final String provider, final int status, final Bundle extras) {
			// TODO Auto-generated method stub

		}

	}
}
