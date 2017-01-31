package traffic.ds;

public class GeoPoint {
	private int lat,lng;
	public GeoPoint(int lat,int lng){
		this.lat=lat;
		this.lng=lng;
	}
	public int getLatitudeE6(){
		return lat;
	}
	public int getLongitudeE6(){
		return lng;
	}
	
}
