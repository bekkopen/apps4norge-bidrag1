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

public class GetWeatherIconIDAsyncTask extends AsyncTask<Void, WeatherCamera, Void> {

	
	private final AsyncTaskDelegate<WeatherCamera> delegate; //reference to object listening for chenges
	private final WeatherCamera currentWeatherCameraModel;
    private final static int CONNECT_TIME_OUT = 5000;
    private final static int READ_TIME_OUT = 10000;


	private final String API_URL = "http://webkamera.vegvesen.no/metadata";

	public GetWeatherIconIDAsyncTask(final AsyncTaskDelegate<WeatherCamera> delegate, WeatherCamera weatherModel) {
		this.delegate = delegate;
		this.currentWeatherCameraModel = weatherModel;
	}

	@Override
	protected Void doInBackground(final Void... params) {
		Log.e(this.getClass().getCanonicalName(), "Start fetching weather for url"+this.currentWeatherCameraModel.getWeatherURL()+"varsel.xml");

        String weatherURL = this.currentWeatherCameraModel.getWeatherURL()+"varsel.xml";
        URL xmlUrl;
        InputStream inputStream = null;
        try {
            xmlUrl = new URL(API_URL);
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            URLConnection con = xmlUrl.openConnection();
            con.setConnectTimeout(CONNECT_TIME_OUT);
            con.setReadTimeout(READ_TIME_OUT);
            inputStream = con.getInputStream();
            parser.setInput(inputStream, "UTF-8");
            if (parser != null) {
                //this.processParser(parser);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getCanonicalName(), ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(this.getClass().getCanonicalName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }

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
