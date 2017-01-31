package traffic;

import java.io.IOException;
import javax.servlet.http.*;

import org.json.JSONException;
import org.json.JSONObject;
import traffic.ds.LatLng;

@SuppressWarnings("serial")
public class LiveTrafficServlet extends HttpServlet {
	
	DatabaseInterface di = new DatabaseInterface();
	MapsAPIInterface CalPath = new MapsAPIInterface();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");
		try {
			int type = Integer.parseInt(req.getParameter("type"));
			
			if(type == 1) {
				String username = req.getParameter("username");
				LatLng location = new LatLng(Double.parseDouble(req.getParameter("Lng")), Double.parseDouble(req.getParameter("Lat")));
				double speed = Double.parseDouble(req.getParameter("speed"));
				di.updateUser(location, username, speed);
				resp.getWriter().println("Updated");
			}
			else if (type == 2){
				LatLng from = new LatLng(Double.parseDouble(req.getParameter("fromLng")), Double.parseDouble(req.getParameter("fromLat")));
				LatLng to = new LatLng(Double.parseDouble(req.getParameter("toLng")), Double.parseDouble(req.getParameter("toLat")));
	
				JSONObject shortestPath = CalPath.FindMinPath(from, to);
				if(shortestPath == null){
					String s = "{ \"routes\" : [], \"status\" : \"RUNNING EXCEPTION\"}";
					resp.getWriter().println(new JSONObject(s));
				}
				else
					resp.getWriter().println(shortestPath);
			}
			else{
				String s = "{ \"routes\" : [], \"status\" : \"REQUEST_DENIED\"}";
				try {
					resp.getWriter().println(new JSONObject(s));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}	
			}
		} catch (Exception e){
			String s = "{ \"routes\" : [], \"status\" : \"RUNNING EXCEPTION\"}";
			try {
				resp.getWriter().println(new JSONObject(s));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
