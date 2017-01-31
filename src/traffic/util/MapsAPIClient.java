package traffic.util;

import org.json.JSONException;
import org.json.JSONObject;
import traffic.ds.DirectionsResult;
import traffic.ds.DirectionsResultStatus;
import traffic.ds.DirectionsRoute;
import traffic.ds.DirectionsService;
import traffic.ds.LatLng;

public class MapsAPIClient {
	
	private static final String PATH_QUERY = "http://maps.googleapis.com/maps/api/directions/json?sensor=false&";
	
	public DirectionsResult findPath (LatLng src, LatLng dest) {
		String query = PATH_QUERY + "origin=" + src.getLatitude() + "," + src.getLongitude() + 
				"&destination=" + dest.getLatitude() + "," + dest.getLongitude()+"&alternatives=true";
		DirectionsService dirService = new DirectionsService ();
		try {
			DirectionsResult dirResult = new DirectionsResult(new JSONObject(dirService.sendGETRequest(query))); 
			return dirResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONObject prepareOutput (DirectionsRoute r, DirectionsResultStatus status){
		JSONObject out = null;
		try {
			String res = " { \"routes\" : [" + r.toJSONObject().toString() + "], \"status\" : " + status.getStatus() + "}";
			out = new JSONObject(res);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

}
