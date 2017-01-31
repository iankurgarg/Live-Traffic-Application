package traffic.util;

import java.util.ArrayList;
import java.util.List;
import traffic.ds.LatLng;
public class GMapLib {
	
//	private static double LEFT_BOUND = 10.0;
//	private static double UP_BOUND = 10.0;
//	private static double RIGHT_BOUND = 10.0;
//	private static double DOWN_BOUND = 10.0;

	/*public static void getPath (Location src, Location dest, boolean isMultiple, AsyncHttpResponseHandler handler) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		//TODO: change the request url
		httpClient.get("http://maps.googleapis.com/maps/api/directions/json?origin=IIT%20Delhi&destination=India%20Gate&sensor=false", handler);
	}
	
	public static boolean isOnStep(Location location, DirectionsStep step) {
		LatLng[] path = step.getPath();
		for (int i = 0; i < path.length - 1; i++) {
			LatLngBounds box = new LatLngBounds(path[i], path[i+1], LEFT_BOUND, UP_BOUND, RIGHT_BOUND, DOWN_BOUND);
			if (box.contains(LatLng.instance(location))) {
				return true;
			}
		}
		return false;
	}*/
	
	public static List<LatLng> decodePoly(String encoded) {
		
	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;
	        LatLng p=new LatLng((double)lng/1E5,(double)lat/1E5);
	      //  GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
	       //      (int) (((double) lng / 1E5) * 1E6));
	        poly.add(p);
	    }

	    return poly;
	}


	public static String EncodeCoordinates(LatLng[] coordinates) {
		int plat = 0;
		int plng = 0;
		StringBuilder encodedCoordinates = new StringBuilder();
		for(int i=0;i<coordinates.length;i++ ) {
			
		  int late5 = (int)(coordinates[i].getLatitude() * 1e5);
		  int lnge5 = (int)(coordinates[i].getLongitude() * 1e5);

		  encodedCoordinates.append(EncodeSignedNumber(late5 - plat));
		  encodedCoordinates.append(EncodeSignedNumber(lnge5 - plng));

		  plat = late5;
		  plng = lnge5;
		}
		return encodedCoordinates.toString();
	}
	
	private static String EncodeSignedNumber(int num) {
		
	  int sgn_num = num << 1;
	  if (num < 0){
		  
	    sgn_num = ~(sgn_num);
	  }
	  return (EncodeNumber(sgn_num));
	}

	private static String EncodeNumber(int num) {
		
	  StringBuilder encodeString = new StringBuilder();
	  while (num >= 0x20) {
		  
	    encodeString.append((char)((0x20 | (num & 0x1f)) + 63));
	    num >>= 5;
	  }
	  encodeString.append((char)(num + 63));

	  return encodeString.toString().replace("\\", "\\\\");
	}
}
