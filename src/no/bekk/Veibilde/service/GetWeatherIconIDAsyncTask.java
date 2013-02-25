package no.bekk.Veibilde.service;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import no.bekk.Veibilde.domain.WeatherCamera;

public class GetWeatherIconIDAsyncTask extends VeiBildeXMLAsyncTask<Void, WeatherCamera, Void> {

	
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
