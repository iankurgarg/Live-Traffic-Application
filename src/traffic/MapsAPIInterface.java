package traffic;

import java.util.*;
import org.json.JSONObject;
//import traffic.ds.DirectionsLeg;
import traffic.ds.DirectionsResult;
import traffic.ds.DirectionsRoute;
//import traffic.ds.DirectionsStep;
import traffic.ds.LatLng;
import traffic.ds.LatLngBounds;
import traffic.ds.Polyline;
import traffic.ds.Rectangle;
import traffic.util.MapsAPIClient;

import com.google.appengine.api.datastore.*;


public class MapsAPIInterface {
	
	private static LatLng UR = new LatLng(77.297744, 28.759355);
	private static LatLng BL = new LatLng(76.955108,28.525536);
	private double MaxDelta = 5*60*1000;  //300 seconds // 5 minutes
	private static int SIZE = 64;
	private static int speedLimit=50;  //in kmph

	public static DividingMap dMap = new DividingMap(BL, UR, SIZE);
	private DatabaseInterface dbInterface = new DatabaseInterface();
	
	
	public int getSectorNumberOfPolyline(Polyline l){
		if (l.getStartLocation().getLongitude() < l.getEndLocation().getLongitude())
			return dMap.getSectorNumber(l.getStartLocation());
		else
			return dMap.getSectorNumber(l.getEndLocation());
	}
	
	public Rectangle createBoundingRectange(Polyline l){
		LatLngBounds bounds = new LatLngBounds(l.getStartLocation(), l.getEndLocation(), 10, 10, 10, 10);	
		return new Rectangle(bounds);
	}
	
	public double[] getUserInfo(Polyline l, int sector) {

//		System.out.println("running");
		Rectangle boundingRect = createBoundingRectange(l);
		Entity[] usersInSector = dbInterface.getDataOfSector(sector);
		int max = usersInSector.length;
		Entity usersInRect[] = new Entity[max];
		int numberOfUsers = 0;
		LatLng location;
		double sum = 0.0;
		double[] result = new double[2];
		
		for (int i = 0; i < max; i++) {
			Date timeS = (Date) usersInSector[i].getProperty("timeStamp");
			double time=new Date().getTime();
			if((time - timeS.getTime()) < MaxDelta) {
				location = new LatLng(	Double.parseDouble(usersInSector[i].getProperty("lng").toString()), 
										Double.parseDouble(usersInSector[i].getProperty("lat").toString())
										);

				if(boundingRect.getBounds().contains(location)){
					usersInRect[numberOfUsers++] = usersInSector[i];
				}
			}
			else
				continue;
		}
		
		for(int i = 0; i < numberOfUsers; i++) {
			sum = sum + Double.parseDouble(usersInRect[i].getProperty("speed").toString());
		}

		if(numberOfUsers<=5){			//to check if the polyline is not empty . If so then this is not the bottle neck
			result[0]=speedLimit;
			result[1]=0;
		}else{
			result[0] = sum/numberOfUsers;
			result[1] = numberOfUsers;
		}
		return result;
	}
	
	
	public int[] getInstersectingSections(Polyline l) {
		
		ArrayList<Integer> res = dMap.getIntersectingSections(l);
		
		int result[] = new int[res.size()];
		for (int i = 0; i < res.size(); i++) {
			result[i] = res.get(i);
		}
		
		return result;
	}
	
	public double[] getTrafficInfo (Polyline l) {
		
		Entity e;
		int[] sections = getInstersectingSections(l);
		double[] result = new double[2];
		try {
			e = dbInterface.getPolylineData(l);
			Date currTime = new Date();
			result[0] = 0;
			result[1] = 0;

			Date timeStamp = (Date) e.getProperty("timeStamp");
			if((currTime.getTime() - timeStamp.getTime()) < 0) {
				result[0] = Double.parseDouble(e.getProperty("AvgSpeed").toString());
				result[1] = Double.parseDouble(e.getProperty("numberOfUsers").toString());
			}
			else {
				double []temp=new double[2];
				for(int i = 0; i < sections.length; i++){
					temp=getUserInfo(l, sections[i]);
					result[0] += temp[0]*temp[1];
					result[1] += temp[1];
				}
				if(result[1] < 5){
					result[0] = speedLimit;
					result[1] = 0;
				}else{
					result[0] = result[0]/result[1];				
				}

//				dbInterface.updatePolyline(l, result[0], result[1]);				
				/*Key k = KeyFactory.createKey(Integer.toString(getSectorNumberOfPolyline(l))+"_polyline", GMapLib.EncodeCoordinates(l.toLatLngArray()));
				Entity newE = new Entity(k);
				newE.setProperty("timeStamp", new Date());
				newE.setProperty("AvgSpeed", result[0]);
				newE.setProperty("numberOfUsers", result[1]);*/
				//store the data into database
			}

			return result;
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			
			double []temp=new double[2];
			for(int i = 0; i < sections.length; i++){
				temp=getUserInfo(l, sections[i]);
				result[0] += temp[0]*temp[1];
				result[1] += temp[1];
			}
			if(result[1] < 5){
				result[0] = speedLimit;
				result[1] =0;
			}else{
				result[0] = result[0]/result[1];				
			}

			
			/*Key k = KeyFactory.createKey(Integer.toString(getSectorNumberOfPolyline(l)), GMapLib.EncodeCoordinates(l.toLatLngArray()));
			Entity newE = new Entity(k);
			newE.setProperty("timeStamp", new Date());
			newE.setProperty("AvgSpeed", result[0]);
			newE.setProperty("numberOfUsers", result[1]);*/
			dbInterface.updatePolyline(l, result[0], result[1]);
			return result;
		}
		
	}


	public double getETAForRoute(DirectionsRoute r) {
//		System.out.println("running eta");
		LatLng[] overView = r.getOverviewPolyline();
		
		LatLng origin = new LatLng();
		LatLng destination = new LatLng();
		
		double ETA = 0.0;
		
		Polyline l;
		double avgSpeed = 0,distance = 0;
		double []result =new double[2];
//		double totalD=0;
		for (int i = 0; i < overView.length - 1; i++) {
			origin = overView[i];
			destination = overView[i+1];
			l = new Polyline(origin, destination);
			result=getTrafficInfo(l);
			//this algorithm has to to be improved
			if(result[0]==0){
				if(result[1]>10){
					ETA=-1;
					break;
				}
				avgSpeed=speedLimit;
				
			}else{		
				avgSpeed=result[0];
			}
			//avgSpeed+=result[0];
			//totalUsers+=result[1];
			distance=origin.distanceTo(destination);
//			totalD+=distance;
			ETA+=distance/avgSpeed;
		}
		/*origin = overView[0];
		destination = overView[overView.length - 1];
		if(totalUsers<10){
			avgSpeed=speedLimit;
		}else{
			avgSpeed=avgSpeed/totalUsers;
		}
		ETA=distance/avgSpeed;*/
		return ETA;
	}
	
//	public double getETAForRoutev2 (DirectionsRoute r){
//		LatLng[] overView = r.getOverviewPolyline();
//		
//		LatLng origin = new LatLng();
//		LatLng destination = new LatLng();
//		
//		double ETA = 0.0;
//		
//		Polyline l;
//		double avgSpeed = 0,distance = 0;
//		double result = 0;
////		double totalD=0;
//		for (int i = 0; i < overView.length - 1; i++) {
//			origin = overView[i];
//			destination = overView[i+1];
//			l = new Polyline(origin, destination);
//			result=getPolylineSpeed(l);
//			avgSpeed=result;
//		}
//		distance=origin.distanceTo(destination);
////		totalD+=distance;
//		ETA+=distance/avgSpeed;
//		return ETA;
//	}
//	
//	public double getETAForLeg (DirectionsLeg leg) {
//		double sum = 0.0;
//		DirectionsStep[] steps = leg.getSteps();
//		for(int i = 0; i < steps.length;  i++)
//			sum = sum + getETAForStep(steps[i]);
//		
//		return sum;
//	}
//	
//	public double getETAForStep(DirectionsStep step){
//		LatLng[] path = step.getPath();
//		double sum = 0.0;
//		Polyline l;
//		for (int i = 0; i < path.length; i++) {
//			l = new Polyline(path[i], path[i+1]);
//			sum = sum + getPolylineSpeed(l);
//		}
//		return sum;
//	}
//	
//	public double getPolylineSpeed (Polyline l){
//		Entity e;
//		int[] sections = getInstersectingSections(l);
//		double[] result = new double[2];
//		try {
//			e = dbInterface.getPolylineData(l);
//			Date currTime = new Date();
//			result[0] = 0;
//			result[1] = 0;
//
//			Date timeStamp = (Date) e.getProperty("timeStamp");
//			if((currTime.getTime() - timeStamp.getTime()) < 0) {
//				result[0] = Double.parseDouble(e.getProperty("AvgSpeed").toString());
//				result[1] = Double.parseDouble(e.getProperty("numberOfUsers").toString());
//			}
//			else {
//				double []temp=new double[2];
//				for(int i = 0; i < sections.length; i++){
//					temp=getUserInfo(l, sections[i]);
//					result[0] += temp[0]*temp[1];
//					result[1] += temp[1];
//				}
//				if(result[1] < 5){
//					result[0] = speedLimit;
//					result[1] = 0;
//				}else{
//					result[0] = result[0]/result[1];				
//				}
////				dbInterface.updatePolyline(l, result[0], result[1]);				
//			}
//			return result[0];
//		} catch (EntityNotFoundException e1) {
//			double []temp=new double[2];
//			for(int i = 0; i < sections.length; i++){
//				temp=getUserInfo(l, sections[i]);
//				result[0] += temp[0]*temp[1];
//				result[1] += temp[1];
//			}
//			if(result[1] < 5){
//				result[0] = speedLimit;
//				result[1] =0;
//			}else{
//				result[0] = result[0]/result[1];				
//			}
////			dbInterface.updatePolyline(l, result[0], result[1]);
//			return result[0];
//		}
//	}
	
	public JSONObject FindMinPath(LatLng from, LatLng to) {
		
		MapsAPIClient apiClient = new MapsAPIClient();
		
		DirectionsResult result = apiClient.findPath(from, to);
		if(result == null) {
			return null;
		}
		else {
			DirectionsRoute[] routes = result.getDirectionsRoutes();
			if(routes == null){
				return result.toJSONObject();
			}
			else{
				
				double minETA = getETAForRoute(routes[0]);
				int minETAIndex = 0;
				double ETA = 0;
				
				for (int i = 1; i < routes.length; i++) {
					ETA = getETAForRoute(routes[i]);
//					System.out.println(ETA);
					if(ETA==-1)
						continue;
					if(ETA < minETA) {
						minETA = ETA;
						minETAIndex = i;
					}
				}
				//return routes[minETAIndex].toJSONObject();
				return apiClient.prepareOutput(routes[minETAIndex], result.getDirectionsResultStatus());			
			}
		}

	}
	
}
