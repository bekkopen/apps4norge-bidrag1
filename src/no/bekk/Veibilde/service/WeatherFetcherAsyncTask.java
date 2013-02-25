package no.bekk.Veibilde.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import no.bekk.Veibilde.domain.WeatherCamera;
import android.os.AsyncTask;
import android.util.Log;

public class WeatherFetcherAsyncTask extends AsyncTask<Void, WeatherCamera, Void> {

	
	private final AsyncTaskDelegate<WeatherCamera> delegate; //reference to object listening for chenges
	private final WeatherCamera currentWeatherCameraModel;
    private final static int CONNECT_TIME_OUT = 5000;
    private final static int READ_TIME_OUT = 10000;


	private final String API_URL = "http://webkamera.vegvesen.no/metadata";

	public WeatherFetcherAsyncTask(final AsyncTaskDelegate<WeatherCamera> delegate, WeatherCamera weatherModel) {
		this.delegate = delegate;
		this.currentWeatherCameraModel = weatherModel;
	}

	@Override
	protected Void doInBackground(final Void... params) {
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.w("WeatherFetcher", "Stopped AsyncTask");
	}

	@Override
	protected void onProgressUpdate(final WeatherCamera... values) {
		delegate.publishItem(values[0]);
	}

}
