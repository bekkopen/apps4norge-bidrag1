package no.bekk.Veibilde.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import no.bekk.Veibilde.domain.WeatherCamera;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetWeatherCameraAsyncTask extends AsyncTask<Void, MarkerOptions, Void> {

	private final AsyncTaskDelegate<MarkerOptions> delegate;

	private final String API_URL = "http://webkamera.vegvesen.no/metadata";

	public GetWeatherCameraAsyncTask(final AsyncTaskDelegate<MarkerOptions> delegate) {
		this.delegate = delegate;
	}

	@Override
	protected Void doInBackground(final Void... params) {
		URL xmlUrl;
		try {
			xmlUrl = new URL(API_URL);

			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			URLConnection con = xmlUrl.openConnection();
			InputStream inputStream = con.getInputStream();
			parser.setInput(inputStream, "UTF-8");
			if (parser != null) {
				processWeatherCams(parser);
			}
		} catch (Exception ex) {
			Log.e(this.getClass().getCanonicalName(), ex.getLocalizedMessage());
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

		return null;
	}

	private void processWeatherCams(final XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = -1;
		WeatherCamera camera = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {

			if (eventType == XmlPullParser.START_DOCUMENT) {
				System.out.println("start document");
			} else if (eventType == XmlPullParser.START_TAG) {
				String tagName = parser.getName();

				if (tagName.equals("webkamera")) {
					String id = parser.getAttributeValue(0);
					camera = new WeatherCamera();
					camera.setId(id);
				} else if (tagName.equals("lengdegrad")) {
					eventType = parser.next();
					camera.setLongitude(Double.parseDouble(parser.getText()));
				} else if (tagName.equals("breddegrad")) {
					eventType = parser.next();
					camera.setLatitude(Double.parseDouble(parser.getText()));
					MarkerOptions markerOptions = createMarkerOptions(camera);
					publishProgress(markerOptions);

				}
				// Log.w(this.getClass().getCanonicalName(), "TAGNAME=" +
				// tagName + ", value=" + parser.getText());

			}
			eventType = parser.next();
		}
		Log.w(this.getClass().getCanonicalName(), "DONE");
	}

	private MarkerOptions createMarkerOptions(final WeatherCamera camera) {
		MarkerOptions weatherCameraMarker = new MarkerOptions();
		weatherCameraMarker.position(new LatLng(camera.getLatitude(), camera.getLongitude()));
		return weatherCameraMarker;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.w("GetAirportsDataTask", "Stopped AsyncTask");
	}

	@Override
	protected void onProgressUpdate(final MarkerOptions... values) {
		delegate.publishItem(values[0]);
	}
}
