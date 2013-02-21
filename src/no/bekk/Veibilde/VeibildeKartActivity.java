package no.bekk.Veibilde;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VeibildeKartActivity extends Activity implements AsyncTaskDelegate<MarkerOptions> {
	private LocationListener locationListener;
	private GoogleMap veiBildeMap;
	private LocationManager locationManager;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kart);

		MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		veiBildeMap = fragment.getMap();
		veiBildeMap.setMyLocationEnabled(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		GetWeatherCameraAsyncTask task = new GetWeatherCameraAsyncTask(this);
		task.execute();
	}

	@Override
	public void publishItem(final MarkerOptions object) {

		// Bitmap myMap =
		// getBitmapFromURL("http://webkamera.vegvesen.no/thumbnail?id=100115");
		// weatherCameraMarker.icon(BitmapDescriptorFactory.fromBitmap(myMap));
		veiBildeMap.addMarker(object);
	}

	@Override
	public void didFailWithError(final String errorMessage) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void didFinishProsess(final String message) {
		// GetWeatherCameraAsyncTask task = new GetWeatherCameraAsyncTask(this);
		// task.execute();
	}

	@Override
	public void onBackPressed() {
		if (locationManager != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
		finish();
	}

	public Bitmap getBitmapFromURL(final String src) {
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
		public void onLocationChanged(final Location location) {
			veiBildeMap
					.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
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
