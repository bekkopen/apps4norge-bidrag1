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

public class GetWeatherIconIDAsyncTask extends VeiBildeAsynTask<Void, WeatherCamera, Void> {

	
	private final AsyncTaskDelegate<WeatherCamera> delegate;
	private final WeatherCamera currentWeatherCameraModel;

	public GetWeatherIconIDAsyncTask(final AsyncTaskDelegate<WeatherCamera> delegate, WeatherCamera weatherModel) {
		this.delegate = delegate;
		this.currentWeatherCameraModel = weatherModel;
	}


    @Override
    protected String getAPIURL() {
        return this.currentWeatherCameraModel.getWeatherURL()+"varsel.xml";
    }


    @Override
    protected void processParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = -1;
        int symbolIdInteger = -1;

        while (symbolIdInteger == -1 && eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {
                String tagName = parser.getName();

                if (tagName.equals("symbol")) {
                    symbolIdInteger = Integer.parseInt(parser.getAttributeValue(0));
                    this.currentWeatherCameraModel.setWeatherIconId(symbolIdInteger);
                    publishProgress(this.currentWeatherCameraModel);
                }
            }
            eventType = parser.next();
        }

    }

	@Override
	protected void onProgressUpdate(final WeatherCamera... values) {
		delegate.publishItem(values[0]);
	}

}
