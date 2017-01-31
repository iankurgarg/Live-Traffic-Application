package traffic.ds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import traffic.util.GMapLib;

//import com.google.android.maps.GeoPoint;

public class DirectionsRoute {
	
	private DirectionsLeg legs[];
	private LatLng[] overviewPolyline;
	private int[] wayPointOrder;
	private JSONObject jsonObj;
	private String[] warnings;
	private LatLng[] bounds;
	
	public DirectionsRoute (JSONObject jsonObject) {
		jsonObj = jsonObject;
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("legs");
			legs = new DirectionsLeg[jsonArray.length()];
			for (int i = 0; i < legs.length; i++) {
				legs[i] = new DirectionsLeg(jsonArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String encodedPolylinePoints = jsonObject.getJSONObject("overview_polyline").getString("points");
			
			List<LatLng> decodedPointsList = GMapLib.decodePoly(encodedPolylinePoints);
			overviewPolyline = new LatLng[decodedPointsList.size()];
			Iterator<LatLng> itr = decodedPointsList.iterator();
			int i = 0;
			while (itr.hasNext()) {
				overviewPolyline[i] = (itr.next());
				i++;
			}
//			for (i = 0; i < overviewPolyline.length; i++) {
//				overviewPolyline[i] = new LatLng(jsonArray.getJSONObject(i));
//			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("waypoint_order");
			wayPointOrder = new int[jsonArray.length()];
			for (int i = 0; i < wayPointOrder.length; i++) {
				wayPointOrder[i] = jsonArray.getInt(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("waypoint_order");
			warnings = new String[jsonArray.length()];
			for (int i = 0; i < wayPointOrder.length; i++) {
				warnings[i] = jsonArray.get(i).toString();
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			JSONObject jsonObject2 = jsonObject.getJSONObject("bounds");
			bounds = new LatLng[2];
			bounds[0] = new LatLng(jsonObject2.getJSONObject("northeast"));
			bounds[1] = new LatLng(jsonObject2.getJSONObject("southwest"));
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public DirectionsLeg[] getLegs () {
		return legs;
	}
	
	public LatLng[] getOverviewPolyline () {
		return overviewPolyline;
	}
	
	public ArrayList<LatLng> getPath () {
		ArrayList<LatLng> path = new ArrayList<LatLng>();
		for (DirectionsLeg leg : this.getLegs()) {
			for (DirectionsStep step : leg.getSteps()) {
				for (LatLng point : step.getPath()) {
					path.add(point);
				}
			}
		}
		return path;
	}

	public JSONObject toJSONObject() {
		return jsonObj;
	}
	public int[] getWayPointOrder () {
		return wayPointOrder;
	}
	
}
