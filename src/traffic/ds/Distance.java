package traffic.ds;

import org.json.JSONException;
import org.json.JSONObject;

public class Distance {
	
	private String text;
	private int value;
	
	public Distance (JSONObject jsonObject) {
		try {
			text = jsonObject.getString("text");
			value = jsonObject.getInt("value");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getText () {
		return text;
	}
	
	public int getValue () {
		return value;
	}

}
