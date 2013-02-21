package no.bekk.Veibilde.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.res.XmlResourceParser;
import no.bekk.Veibilde.domain.WeatherCamera;
import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class GetWeatherCameraAsyncTask extends AsyncTask<Void, WeatherCamera, Void>{
	
	private AsyncTaskDelegate<WeatherCamera> delegate;

    private final String API_URL = "http://webkamera.vegvesen.no/metadata";

	public GetWeatherCameraAsyncTask(AsyncTaskDelegate<WeatherCamera> delegate) {
		this.delegate = delegate;
	}

    @Override
    protected Void doInBackground(Void... params) {
        URL xmlUrl;
        try{
            xmlUrl = new URL(API_URL);

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            URLConnection con = xmlUrl.openConnection();
            InputStream inputStream = con.getInputStream();
            parser.setInput(inputStream, "UTF-8");
            if(parser != null){
                processWeatherCams(parser);
            }
        }catch(Exception  ex){
            Log.e(this.getClass().getCanonicalName(),ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return null;
    }

    private void processWeatherCams(XmlPullParser parser) throws XmlPullParserException, IOException{
        int eventType = -1;
        while(eventType != XmlPullParser.END_DOCUMENT){

            if(eventType == XmlPullParser.START_DOCUMENT){
                System.out.println("start document");
            }else if(eventType == XmlPullParser.START_TAG){
                String tagName = parser.getName();
                Log.w(this.getClass().getCanonicalName(), "TAGNAME="+tagName+", value="+parser.getText());

                parser.next();

            }
        }
        Log.w(this.getClass().getCanonicalName(), "DONE");
    }
	

	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.w("GetAirportsDataTask", "Stopped AsyncTask");
	}

	@Override
	protected void onProgressUpdate(WeatherCamera... values) {
		delegate.publishItem(values[0]);
	}
}
