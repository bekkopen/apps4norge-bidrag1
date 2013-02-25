package no.bekk.Veibilde;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import no.bekk.Veibilde.domain.WeatherCamera;
import no.bekk.Veibilde.service.AsyncTaskDelegate;
import no.bekk.Veibilde.service.GetWeatherCameraAsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

public class VeibildeKartActivity extends Activity implements
		AsyncTaskDelegate<WeatherCamera> {
	private LocationListener locationListener;
	private GoogleMap veiBildeMap;
	private LocationManager locationManager;
	private Map<Marker, WeatherCamera> myMap;
	private ImageView infoWindowImageView;
	private TextView infoWindowDescription;
	private View infoWindow;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isNetworkAvailable()) {
			setContentView(R.layout.kart);
			myMap = new HashMap<Marker, WeatherCamera>();
			MapFragment fragment = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.map);
			infoWindow = (View) getLayoutInflater().inflate(
					R.layout.infowindow, null);
			veiBildeMap = fragment.getMap();
			veiBildeMap.setMyLocationEnabled(true);
			veiBildeMap.setInfoWindowAdapter(new MyInfoWindowAdapter());  
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationListener = new MyLocationListener();
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			GetWeatherCameraAsyncTask task = new GetWeatherCameraAsyncTask(this);
			task.execute();
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Noe gikk galt");
			alertDialog
					.setMessage("Ingen internettilkobling. Slå på WiFi/3G og prøv igjen");
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Avslutt",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							VeibildeKartActivity.this.finish();
						}
					});

			alertDialog.show();
		}
	}

	@Override
	public void publishItem(final WeatherCamera object) {
		veiBildeMap.setOnMarkerClickListener(new MyMarkerClickListener());
		Marker mapMarker = veiBildeMap.addMarker(object.getLokasjon().title(
				object.getDisplayString()));
		myMap.put(mapMarker, object); 

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

	class MyInfoWindowAdapter implements InfoWindowAdapter {
		@Override
		public View getInfoWindow(Marker arg0) {
			return null;
		}

		@Override
		public View getInfoContents(Marker arg0) {
			return infoWindow;

		}
	}

	class MyMarkerClickListener implements OnMarkerClickListener {

		public MyMarkerClickListener() {

		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			infoWindowImageView = (ImageView) infoWindow
					.findViewById(R.id.infoWindowImageView);
			infoWindowDescription = (TextView) infoWindow
					.findViewById(R.id.infoWindowDescription);
			WeatherCamera pressedMarker = myMap.get(marker);
			infoWindowDescription.setText(pressedMarker.getDisplayString());
			infoWindowImageView.setImageResource(R.drawable.kamera);
			marker.showInfoWindow();
			return true;
		}

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(final Location location) {

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
		public void onStatusChanged(final String provider, final int status,
				final Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

	private Bitmap getBitmapFromURL(final String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
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

}
