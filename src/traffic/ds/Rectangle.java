package traffic.ds;

public class Rectangle {
	private LatLngBounds bounds;
	
	public Rectangle(LatLngBounds b) {
		this.setBounds(b);
	}
	
	public LatLngBounds getBounds() {
		return bounds;
	}
	
	public void setBounds(LatLngBounds b) {
		this.bounds = b;
	}
	
	public boolean contains (LatLng location) {
		return bounds.contains(location);
	}
}
