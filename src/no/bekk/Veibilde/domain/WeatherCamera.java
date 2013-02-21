package no.bekk.Veibilde.domain;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created with IntelliJ IDEA. User: thomas johan eggum Date: 2/21/13 Time: 2:55
 * PM To change this template use File | Settings | File Templates.
 */
public class WeatherCamera {

    private String id;
    private MarkerOptions lokasjon;


    public void setId(String id) {
        this.id = id;
    }

	public String getImageUrl() {
		return "http://webkamera.vegvesen.no/kamera?id=" + id;
	}
	
	public String getThumbnailImageUrl() {
		return "http://webkamera.vegvesen.no/thumbnail?id=" + id;
	}


	public MarkerOptions getLokasjon() {
		return lokasjon;
	}


	public void setLokasjon(MarkerOptions lokasjon) {
		this.lokasjon = lokasjon;
	}

}
