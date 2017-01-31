package traffic.ds;


public class DirectionsResultStatus {

	private static String object;
	
	public DirectionsResultStatus (String obj){
		object = obj;
	}
		
	public String getStatus() {
		return object;
	}
}
