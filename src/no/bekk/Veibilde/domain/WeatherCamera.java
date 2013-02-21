package no.bekk.Veibilde.domain;

/**
 * Created with IntelliJ IDEA.
 * User: thomas johan eggum
 * Date: 2/21/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeatherCamera {

    private String id;
    private double latitude;
    private double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getImageUrl() {
		return "http://webkamera.vegvesen.no/kamera?id=" + id;
	}
	
	public String getThumbnailImageUrl() {
		return "http://webkamera.vegvesen.no/thumbnail?id=" + id;
	}
    
    
}
