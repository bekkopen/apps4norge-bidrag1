package no.bekk.Veibilde;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import no.bekk.Veibilde.domain.WeatherCamera;
import no.bekk.Veibilde.service.AsyncTaskDelegate;
import no.bekk.Veibilde.service.GetWeatherCameraAsyncTask;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VeibildeKartActivity extends Activity implements
		AsyncTaskDelegate<WeatherCamera> {
	private LocationListener locationListener;
	private GoogleMap veiBildeMap;
	private LocationManager locationManager;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kart);

		MapFragment fragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		veiBildeMap = fragment.getMap();
		veiBildeMap.setMyLocationEnabled(true);
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		new GetWeatherCameraAsyncTask(this).execute();
		MarkerOptions weatherCameraMarker = new MarkerOptions();
		// weatherCameraMarker.position(new LatLng(object.getLatitude(),
		// object.getLongitude()));
		weatherCameraMarker.position(new LatLng(59.9032198, 10.7396514));
		Bitmap myMap = getBitmapFromURL("http://webkamera.vegvesen.no/thumbnail?id=100115");
		
		weatherCameraMarker.icon(BitmapDescriptorFactory.fromBitmap(myMap));
		veiBildeMap.addMarker(weatherCameraMarker);
	}

	@Override
	public void publishItem(WeatherCamera object) {
		Log.w("YNWA", "publishItem()");
		MarkerOptions weatherCameraMarker = new MarkerOptions();
		// weatherCameraMarker.position(new LatLng(object.getLatitude(),
		// object.getLongitude()));
		weatherCameraMarker.position(new LatLng(59.9032198, 10.7396514));
		veiBildeMap.addMarker(weatherCameraMarker);
	}

	@Override
	public void didFailWithError(String errorMessage) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void didFinishProsess(String message) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void onBackPressed() {
		if (locationManager != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
		this.finish();
	}
	
	public Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			veiBildeMap
					.animateCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(location.getLatitude(), location
									.getLongitude()), 15.0f));
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}
}
