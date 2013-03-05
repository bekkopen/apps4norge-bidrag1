package no.bekk.Veibilde.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: thomas johan eggum
 * Date: 3/5/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageHelper {
    private final static int CONNECT_TIME_OUT = 5000;
    private final static int READ_TIME_OUT = 10000;

    public static ImageHelper instance = new ImageHelper();

    public ImageHelper getInstance(){
        return instance;
    }

    public Bitmap getBitmap(String imageUrl){
        Bitmap bitMap = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setDoInput(true);
            connection.connect();
            inputStream = connection.getInputStream();
            bitMap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(this.getClass().getCanonicalName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return bitMap;
    }

}
