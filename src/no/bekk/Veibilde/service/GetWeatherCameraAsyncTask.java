package no.bekk.Veibilde.service;

import java.io.IOException;

import no.bekk.Veibilde.domain.WeatherCamera;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetWeatherCameraAsyncTask extends VeiBildeXMLAsyncTask<Void, WeatherCamera, Void> {

	private final AsyncTaskDelegate<WeatherCamera> delegate;



	private final String API_URL = "http://webkamera.vegvesen.no/metadata";

	public GetWeatherCameraAsyncTask(final AsyncTaskDelegate<WeatherCamera> delegate) {
		this.delegate = delegate;
	}

    protected String getAPIURL(){
        return this.API_URL;
    }


    protected void processParser(final XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = -1;
		WeatherCamera camera = null;
		double latitude = -1;
		double longitude = -1;
		String descripton = "";
		String road = "";
		String weatherURL = "";
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
					longitude = Double.parseDouble(parser.getText());
				} else if (tagName.equals("stedsnavn")) {
					eventType = parser.next();
					descripton = parser.getText();
				} else if (tagName.equals("veg")) {
					eventType = parser.next();
					road = parser.getText();
				} else if (tagName.equals("breddegrad")) {
					eventType = parser.next();
					latitude = Double.parseDouble(parser.getText());
				} else if (tagName.equals("vaervarsel")) {
					eventType = parser.next();
					weatherURL = parser.getText();
					MarkerOptions markerOptions = createMarkerOptions(latitude, longitude);
					camera.setDescription(descripton);
					camera.setRoad(road);
					camera.setLokasjon(markerOptions);
					camera.setWeatherURL(weatherURL);
					publishProgress(camera);
				}
				// Log.w(this.getClass().getCanonicalName(), "TAGNAME=" +
				// tagName + ", value=" + parser.getText());

			}
			eventType = parser.next();
		}
		Log.w(this.getClass().getCanonicalName(), "DONE");
	}

	private MarkerOptions createMarkerOptions(final double latitude, final double longitude) {
		MarkerOptions weatherCameraMarker = new MarkerOptions();
		weatherCameraMarker.position(new LatLng(latitude, longitude));
		return weatherCameraMarker;
	}


	@Override
	protected void onProgressUpdate(final WeatherCamera... values) {
		delegate.publishItem(values[0]);
	}
}
