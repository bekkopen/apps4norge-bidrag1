package no.bekk.Veibilde.service;

import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: thomas johan eggum
 * Date: 2/25/13
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class VeiBildeAsynTask<Params, Progress, Result>  extends AsyncTask<Params, Progress, Result> {

    private final static int CONNECT_TIME_OUT = 5000;
    private final static int READ_TIME_OUT = 10000;

    protected abstract String getAPIURL();


    protected Void doInBackground(final Void... params) {
        URL xmlUrl;
        InputStream inputStream = null;
        try {
            xmlUrl = new URL(getAPIURL());
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            URLConnection con = xmlUrl.openConnection();
            con.setConnectTimeout(CONNECT_TIME_OUT);
            con.setReadTimeout(READ_TIME_OUT);
            inputStream = con.getInputStream();
            parser.setInput(inputStream, "UTF-8");
            if (parser != null) {
                processParser(parser);
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

    protected abstract void processParser(XmlPullParser parser)throws XmlPullParserException, IOException;
}
