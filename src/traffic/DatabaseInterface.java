package traffic;

import com.google.appengine.api.datastore.*;

import traffic.ds.*;
import traffic.util.GMapLib;

import java.util.*;

public class DatabaseInterface {

	private final static DatastoreService database = DatastoreServiceFactory.getDatastoreService();

	public Entity getPolylineData(Polyline l) throws EntityNotFoundException {
		
		Key k = KeyFactory.createKey(Integer.toString(new MapsAPIInterface().getSectorNumberOfPolyline(l))+"_polyline", GMapLib.EncodeCoordinates(l.toLatLngArray()));
		return database.get(k);
		
	}
	public void updatePolyline(Polyline l,double avgSpeed,double noOfUsers){
		Transaction trans = database.beginTransaction();
		Entity user;
		Key k = KeyFactory.createKey(Integer.toString(new MapsAPIInterface().getSectorNumberOfPolyline(l))+"_polyline", GMapLib.EncodeCoordinates(l.toLatLngArray()));
		try {
			user = database.get(trans, k);
		} catch (EntityNotFoundException e) {
			user = new Entity(k);
		}
		user.setProperty("timeStamp", new Date());
		user.setProperty("AvgSpeed", avgSpeed);
		user.setProperty("numberOfUsers", noOfUsers);
		database.put(trans, user);
		trans.commit();
	}
	
	public Entity[] getDataOfSector(int sector) {
		
		int i = 0;
		Query query = new Query(Integer.toString(sector));
		
		PreparedQuery prepQuery = database.prepare(query);

		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		Entity ans[] = new Entity[prepQuery.countEntities(fetchOptions)];

		for (Entity e : prepQuery.asIterable())
			ans[i++] = e;

		return ans;
	}

	public void updateUser(LatLng location, String username, double speed) {
//		MapsAPIInterface CalPath = new MapsAPIInterface();
		Key k = KeyFactory.createKey(Integer.toString(MapsAPIInterface.dMap.getSectorNumber(location)), username);
	  
		Transaction trans = database.beginTransaction();
		Entity user;

		try {
			user = database.get(trans, k);
		} catch (EntityNotFoundException e) {
			user = new Entity(k);
		}
		user.setProperty("username", username);
		user.setProperty("lat", location.getLatitude());
		user.setProperty("lng", location.getLongitude());
		user.setProperty("speed", Double.toString(speed));
		user.setProperty("timeStamp", new Date());

		database.put(trans, user);
		trans.commit();
	}
	
	public void removeKind(String kind){
		Transaction trans = database.beginTransaction();
		
		Query query = new Query(kind);
		PreparedQuery prepQuery = database.prepare(query);
		
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		Entity[] users = new Entity[prepQuery.countEntities(fetchOptions)];
		
		int i = 0;
		for (Entity e : prepQuery.asIterable())
			users[i++] = e;
		
		try {
			for (int j = 0;j < i; j++){
				database.delete(trans, users[j].getKey());
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}