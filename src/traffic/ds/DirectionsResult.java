package traffic.ds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsResult {
	
	private DirectionsRoute routes[];
	private DirectionsResultStatus status;
	private JSONObject obj;
	
	public DirectionsResult (JSONObject jsonObject) {
		obj = jsonObject;
		try {
			if(jsonObject.getString("status").equals("OK")) {	
				
				JSONArray jsonArray = jsonObject.getJSONArray("routes");
				routes = new DirectionsRoute[jsonArray.length()];
				for (int i = 0; i < routes.length; i++) {
					routes[i] = new DirectionsRoute(jsonArray.getJSONObject(i));
				}
				status = new DirectionsResultStatus("OK");
				
			}
			else{
				routes = null;
				status = new DirectionsResultStatus((String)jsonObject.get("status"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			routes = null;
			status = null;
			e.printStackTrace();
		}
	}
	
	public DirectionsRoute[] getDirectionsRoutes() {
		return routes;
	}
	
	public DirectionsResultStatus getDirectionsResultStatus() {
		return status;
	}
	
	public JSONObject toJSONObject () {
		return obj;
	}
}
