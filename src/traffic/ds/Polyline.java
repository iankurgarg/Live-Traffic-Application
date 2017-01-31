package traffic.ds;

public class Polyline {
	
	private LatLng start, end;
	private double tan;
	
	public Polyline(LatLng start, LatLng end) {
		this.start = start;
		this.end = end;
		this.tan = (end.getLatitude() - start.getLatitude())/(end.getLongitude() - start.getLongitude());
	}
	
	public LatLng getStartLocation () {
		return start;
	}
	
	public LatLng getEndLocation () {
		return end;
	}
	
	public double getSlope () {
		return tan;
	}
	
	public double length () {
		return start.distanceTo(end);
	}
	
	public double eval(LatLng point) {
		double value = (point.getLatitude() - start.getLatitude())
				- (point.getLongitude() - start.getLongitude())*tan;
		return value;
	}
	
	public LatLng[] toLatLngArray(){
		LatLng[] result = new LatLng[2];
		result[0] = start;
		result[1] = end;
		return result;
	}

}
