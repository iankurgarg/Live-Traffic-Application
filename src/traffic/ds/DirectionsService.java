package traffic.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
//import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;


public class DirectionsService {

	//TODO: change this url
	public String sendGETRequest(String url) {
	    StringBuffer sb = new StringBuffer();
	    try {
	        URL requestUrl = new URL(url);
//	        java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.10.78.22", 3128));
	        HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
   
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	        String in = "";

	        while ((in = br.readLine()) != null) sb.append(in + "\n");

	        br.close();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		} 
	    return sb.toString();
	}
	
}
