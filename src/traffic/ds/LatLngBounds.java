package traffic.ds;


public class LatLngBounds {
	
	private LatLng start, end;
	private double lbound, ubound, rbound, dbound;
	private Polyline u, d, l, r;
	private LatLng u1, u2, d1, d2;
	private static final double GEOPOINT_ERROR = 30.0;
	private static int RADIUS = 6378100;
	private static double RADIAN_TO_DEGREE = 180 / Math.PI;

	
	public LatLngBounds (LatLng start, LatLng end, double lbound,
			double ubound, double rbound, double dbound) {
		this.start = start;
		this.end = end;
		this.lbound = lbound;
		this.ubound = ubound;
		this.rbound = rbound;
		this.dbound = dbound;
		
		double tan = (end.getLatitude() - start.getLatitude())/(end.getLongitude() - start.getLongitude());
		double theta = Math.atan(tan);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		LatLng m1 = new LatLng(start.getLongitude() - (ubound / RADIUS) * RADIAN_TO_DEGREE * cosTheta , start.getLatitude() - (ubound / RADIUS) * RADIAN_TO_DEGREE *sinTheta);
		LatLng m2 = new LatLng(end.getLongitude() + (dbound / RADIUS) * RADIAN_TO_DEGREE * cosTheta, end.getLatitude() + (dbound / RADIUS) * RADIAN_TO_DEGREE *sinTheta);
		if(m1.distanceTo(m2) < start.distanceTo(end)) {
			m1 = new LatLng(start.getLongitude() + (ubound / RADIUS) * RADIAN_TO_DEGREE * cosTheta, start.getLatitude() + (ubound / RADIUS) * RADIAN_TO_DEGREE * sinTheta);
			m2 = new LatLng(end.getLongitude() - (dbound / RADIUS) * RADIAN_TO_DEGREE * cosTheta, end.getLatitude() - (dbound / RADIUS) * RADIAN_TO_DEGREE * sinTheta);
		}

		double inverseTheta = theta + Math.PI/2.0;
		double cosInverseTheta = Math.cos(inverseTheta);
		double sinInverseTheta = Math.sin(inverseTheta);
		u1 = new LatLng (m1.getLongitude() + ((rbound+GEOPOINT_ERROR) / RADIUS) * cosInverseTheta * RADIAN_TO_DEGREE, m1.getLatitude() + ((rbound + GEOPOINT_ERROR) / RADIUS) * sinInverseTheta * RADIAN_TO_DEGREE);
		u2 = new LatLng (m2.getLongitude() + ((rbound + GEOPOINT_ERROR) / RADIUS) * cosInverseTheta * RADIAN_TO_DEGREE, m2.getLatitude() + ((rbound + GEOPOINT_ERROR) / RADIUS) * sinInverseTheta * RADIAN_TO_DEGREE);
		d1 = new LatLng (m1.getLongitude() - ((rbound + GEOPOINT_ERROR) / RADIUS) * cosInverseTheta * RADIAN_TO_DEGREE, m1.getLatitude() - ((rbound + GEOPOINT_ERROR) / RADIUS) * sinInverseTheta * RADIAN_TO_DEGREE);
		d2 = new LatLng (m2.getLongitude() - ((lbound + GEOPOINT_ERROR) / RADIUS) * cosInverseTheta * RADIAN_TO_DEGREE, m2.getLatitude() - ((lbound + GEOPOINT_ERROR) / RADIUS) * sinInverseTheta * RADIAN_TO_DEGREE);	
		
		u = new Polyline(u1, u2);
		d = new Polyline(d1, d2);
		l = new Polyline(u1, d1);
		r = new Polyline(u2, d2);
	}
	
	public boolean contains (LatLng point) {
		if( u.eval(point)*u.eval(d1) > 0 && d.eval(point)*d.eval(u1) > 0 &&
				l.eval(point)*l.eval(u2) > 0 && r.eval(point)*r.eval(u1) > 0) {
			return true;
		}
		return false;
	}
	
	

}
