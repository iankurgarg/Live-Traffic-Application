package traffic.ds;

import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import traffic.util.GMapLib;


public class DirectionsStep {
	
	private Distance distance;
	private Duration duration;
	private LatLng startLocation;
	private LatLng endLocation;
	private LatLng path[];
	private DirectionsStep[] sub_steps;
	
	public DirectionsStep (JSONObject jsonObject) {
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
		try {
			endLocation = new LatLng((JSONObject)jsonObject.getJSONObject("end_location"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String encodedPolylinePoints = jsonObject.getJSONObject("polyline").getString("points");
			
			List<LatLng> decodedPointsList = GMapLib.decodePoly(encodedPolylinePoints);
			path = new LatLng[decodedPointsList.size()];
			Iterator<LatLng> itr = decodedPointsList.iterator();
			int i = 0;
			while (itr.hasNext()) {
				path[i] = (itr.next());
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
			startLocation = new LatLng((JSONObject)jsonObject.getJSONObject("start_location"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: Instructions, transit detail and travel mode still to be done, sub_steps, html_instructions
		
	}
	
	public Distance getDistance () {
		return distance;
	}
	
	public Duration getDuration () {
		return duration;
	}
	
	public LatLng getEndLocation () {
		return endLocation;
	}
	
	public LatLng[] getPath () {
		return path;
	}
	
	public LatLng getStartLocation () {
		return startLocation;
	}
	
	public DirectionsStep[] getSteps () {
		return sub_steps;
	}

}
