package traffic.ds;

import org.json.JSONObject;

//import android.location.Location;

//import com.google.android.maps.*;

public class LatLng {
	
	private double lat, lng;
	
	public LatLng (JSONObject jsonObject) {
		this(jsonObject.optDouble("lng"), jsonObject.optDouble("lat"));
	}
	
	public LatLng (double longitude, double latitude) {
		this.lng = longitude;
		this.lat = latitude;
	}
	
	public LatLng () {
		this(0.0, 0.0);
	}
	
	public double getLatitude () {
		return lat;
	}
	
	public double getLongitude () {
		return lng;
	}
	
	public GeoPoint toGeoPoint() {
		return new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));
	}
	
	public static LatLng instance(GeoPoint geoPoint) {
		LatLng latLng = new LatLng();
		latLng.lat = ((double)geoPoint.getLatitudeE6()/(double)1E6);
		latLng.lng = ((double)geoPoint.getLongitudeE6()/(double)1E6);
		return latLng;
	}
	
//	public static LatLng instance (Location loc) {
//		return new LatLng(loc.getLongitude(), loc.getLatitude());
//	}
	
	private double deg2rad(double deg) {
	      return (deg * Math.PI / 180.0);
	}
	
	private double rad2deg(double rad) {
	      return (rad * 180.0 / Math.PI);
	}
	
    public double distanceTo(LatLng to) {
        double theta = lng - to.getLongitude();
        double dist = Math.sin(deg2rad(lat)) * Math.sin(deg2rad(to.getLatitude())) + Math.cos(deg2rad(lat)) * Math.cos(deg2rad(to.getLatitude())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        char unit = 'K';
        if (unit == 'K') {
          	dist = dist * 1.609344;
        } 
        else if (unit == 'N') {
          dist = dist * 0.8684;
        }
        else if (unit == 'M'){
        	dist = dist * 1000;
        }
        return (dist);
    }


}
