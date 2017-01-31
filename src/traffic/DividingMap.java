package traffic;

import java.util.ArrayList;

import traffic.ds.LatLng;
import traffic.ds.Polyline;

public class DividingMap {	
	
	private static LatLng bottomLeft;
	private static LatLng topRight;
	private static int gridSide;
	
	public DividingMap(LatLng bottomLeft,LatLng topRight,int gridSide ){
	
		DividingMap.bottomLeft=bottomLeft;
		DividingMap.topRight=topRight;
		DividingMap.gridSide=gridSide;
	}
	
	public int getNumberOfDivisions() {
		return gridSide;
	}
	
	public LatLng[] getCoordinates(){
	
		LatLng[] temp = { bottomLeft,topRight };
		return temp;
	}
	
	public int getSectorNumber (LatLng userPosition) {
		
		double xSide=Math.abs(topRight.getLongitude()-bottomLeft.getLatitude());
		double ySide=Math.abs(topRight.getLatitude()-bottomLeft.getLatitude());
		double latDiff=Math.abs(userPosition.getLatitude()-bottomLeft.getLatitude());
		double lngDiff=Math.abs(userPosition.getLongitude()-bottomLeft.getLongitude());
		int sectorX,sectorY;
		sectorX=(int)Math.floor(gridSide*lngDiff/xSide);
		sectorY=(int)Math.floor(gridSide*latDiff/ySide);
		int sectorNo=sectorX+sectorY*gridSide;
		return sectorNo;
	}
	
	public ArrayList<Integer> getIntersectingSections(Polyline line){
		LatLng startPoint = line.getStartLocation();
		LatLng endPoint = line.getEndLocation();
		double xSide=Math.abs(topRight.getLongitude()-bottomLeft.getLatitude());
		double ySide=Math.abs(topRight.getLatitude()-bottomLeft.getLatitude());
		double latDiff=Math.abs(startPoint.getLatitude()-bottomLeft.getLatitude());
		double lngDiff=Math.abs(startPoint.getLongitude()-bottomLeft.getLongitude());
		LatLng normalizedStart=new LatLng(gridSide*lngDiff/xSide,gridSide*latDiff/ySide);
		
		latDiff=Math.abs(endPoint.getLatitude()-bottomLeft.getLatitude());
		lngDiff=Math.abs(endPoint.getLongitude()-bottomLeft.getLongitude());
		LatLng normalizedEnd=new LatLng(gridSide*lngDiff/xSide,gridSide*latDiff/ySide);
		
		ArrayList<Integer> sectorNo = new ArrayList<Integer>();
		int index=0;
		int sectorX,sectorY;
		sectorX=(int) Math.floor(normalizedStart.getLongitude());
		sectorY=(int)Math.floor(normalizedStart.getLatitude());
		//sectorNo[index++]=sectorX*gridSide+sectorY;
		double slope=(normalizedEnd.getLatitude()-normalizedStart.getLatitude())/(normalizedEnd.getLongitude()-normalizedStart.getLongitude());
		for(int i= (int) Math.ceil(normalizedStart.getLongitude());i<=(int)Math.floor(normalizedEnd.getLongitude());i++){
			int temp;
			temp=(int) (slope*(i-normalizedStart.getLongitude())+normalizedStart.getLatitude());
			while(sectorY<=temp){
				sectorNo.add(index, sectorX+sectorY*gridSide);
				//sectorNo[index]=sectorX+sectorY*gridSide;
				sectorY++;
				index++;
			}
			sectorX=i;
		}
		while(sectorY<normalizedEnd.getLatitude()){
			sectorNo.add(index, sectorX+sectorY*gridSide);
			sectorY++;
			index++;
		}

		return sectorNo;
		
	}
}
