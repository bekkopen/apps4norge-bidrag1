package no.bekk.Veibilde;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import no.bekk.Veibilde.domain.WeatherCamera;
import no.bekk.Veibilde.helpers.ImageHelper;
import no.bekk.Veibilde.service.AsyncTaskDelegate;
import no.bekk.Veibilde.service.GetWeatherCameraAsyncTask;
import no.bekk.Veibilde.service.GetWeatherIconIDAsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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

	private static final int FIVE_MINUTES = 1000 * 60 * 5;

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
			findBestLocation(30, System.currentTimeMillis() - FIVE_MINUTES);

			GetWeatherCameraAsyncTask task = new GetWeatherCameraAsyncTask(this);
			task.execute();
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Noe gikk galt");
			alertDialog.setMessage("Ingen internettilkobling. Slå på WiFi/3G og prøv igjen");

			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Avslutt",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							VeibildeKartActivity.this.finish();
						}
					});

			alertDialog.show();
		}
	}

	private void findBestLocation(final int minDistance, final long minTime) {
		Location bestLocation = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;
		List<String> matchingProviders = locationManager.getAllProviders();
		for (String provider : matchingProviders) {
			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if (time > minTime && accuracy < bestAccuracy) {
					bestLocation = location;
					bestAccuracy = accuracy;
					bestTime = time;
				} else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
					bestLocation = location;
					bestTime = time;
				}
			}
		}

		if (bestLocation != null && (bestTime > minTime || bestAccuracy < minDistance)) {
			veiBildeMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude()), 15.0f));
		} else {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_LOW);
			locationManager.requestSingleUpdate(criteria, locationListener, getMainLooper());
		}
	}

	@Override
	public void publishItem(final WeatherCamera weatherCamera) {
		if(weatherCamera.getWeatherIconId() != -1){ //weather icon task
            String fieldName = "s"+weatherCamera.getWeatherIconId();
            try {
                Field f = R.drawable.class.getDeclaredField(fieldName);
                int drawableId = f.getInt(f);
                ImageView weatherView = (ImageView) infoWindow
                        .findViewById(R.id.weatherIconImageView);
                weatherView.setImageDrawable(getResources().getDrawable(drawableId));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }else{//weather camera task
            veiBildeMap.setOnMarkerClickListener(new MyMarkerClickListener());
            Marker mapMarker = veiBildeMap.addMarker(weatherCamera.getLokasjon().title(
                    weatherCamera.getDisplayString()));
            myMap.put(mapMarker, weatherCamera);

        }

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
			WeatherCamera weatherCamera = myMap.get(marker);
			infoWindowDescription.setText(weatherCamera.getDisplayString());

            marker.showInfoWindow();
            final String imageUrl = weatherCamera.getImageUrl();
            AsyncTask getBitmapImageTask = new AsyncTask() {

                @Override
                protected Object doInBackground(Object... params) {
                    Bitmap bitmap = ImageHelper.instance.getBitmap(imageUrl);
                    publishProgress(bitmap);

                    return null;
                }

                @Override
                protected void onProgressUpdate(final Object... values) {
                    if(values.length > 0)
                        infoWindowImageView.setImageBitmap((Bitmap)values[0]);
                }

            };
            getBitmapImageTask.execute();


            GetWeatherIconIDAsyncTask task = new GetWeatherIconIDAsyncTask(VeibildeKartActivity.this, weatherCamera);
            task.execute();
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
		public void onStatusChanged(final String provider, final int status,
				final Bundle extras) {
			// TODO Auto-generated method stub

		}

	}



}
