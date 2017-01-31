package traffic.ds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import com.google.android.maps.GeoPoint;
import traffic.ds.GeoPoint;
public class DirectionsLeg {
	
	private Duration arrivalTime, departureTime, duration;
	private Distance distance;
	private String startAddress, endAddress;
	private GeoPoint startLocation, endLocation;
	private DirectionsStep steps[];
	private GeoPoint viaWayPoints[];
	
	public DirectionsLeg (JSONObject jsonObject) {
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("steps");
			steps = new DirectionsStep[jsonArray.length()];
			for (int i = 0; i < steps.length; i++) {
				steps[i] = new DirectionsStep(jsonArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			arrivalTime = new Duration(jsonObject.getJSONObject("arrival_time"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			departureTime = new Duration(jsonObject.getJSONObject("departure_time"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			distance = new Distance(jsonObject.getJSONObject("distance"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			duration = new Duration(jsonObject.getJSONObject("duration"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		endAddress = jsonObject.optString("end_address");
		try {
			endLocation = new LatLng(jsonObject.getJSONObject("end_location")).toGeoPoint();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		startAddress = jsonObject.optString("start_address");
		try {
			startLocation = new LatLng(jsonObject.getJSONObject("start_location")).toGeoPoint();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("via_waypoint");
			viaWayPoints = new GeoPoint[jsonArray.length()];
			for (int i = 0; i < viaWayPoints.length; i++) {
				viaWayPoints[i] = new LatLng(jsonArray.getJSONObject(i)).toGeoPoint();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Duration getArrivalTime () {
		return arrivalTime;
	}
	
	public Duration getDepartureTime () {
		return departureTime;
	}
	
	public Distance getDistance () {
		return distance;
	}
	
	public Duration getDuration () {
		return duration;
	}
	
	public String getEndAddress() {
		return endAddress;
	}
	
	public GeoPoint getEndLocation () {
		return endLocation;
	}
	
	public String getStartAddress () {
		return startAddress;
	}
	
	public GeoPoint getStartLocation () {
		return startLocation;
	}
	
	public DirectionsStep[] getSteps () {
		return steps;
	}
	
	public GeoPoint[] getWayPoints () {
		return viaWayPoints;
	}

}
