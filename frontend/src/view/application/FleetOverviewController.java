package view.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapType;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.offline.OfflineCache;

import javafx.fxml.FXML;
import model.BackendMethods;

public class FleetOverviewController {
	@FXML
	private MapView mapView;
	
	
	private Map<String, Marker> markers = new HashMap<>();
	
	public FleetOverviewController() {
		String[] planeIDs = BackendMethods.getPlaneIDs("All");
		for(String planeID : planeIDs)
		{
			markers.put(planeID, new Marker(getClass().getResource("/InactivePlane.png")).setVisible(true));
		}
	}
	
	@FXML
	private void initialize() {
		final OfflineCache offlineCache = mapView.getOfflineCache();
		final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";
		try {
			Files.createDirectories(Paths.get(cacheDir));
			offlineCache.setCacheDirectory(cacheDir);
			offlineCache.setActive(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mapView.setAnimationDuration(250);
		mapView.setMapType(MapType.OSM);
		mapView.setZoom(8);
        mapView.setCenter(new Coordinate(32.01377634, 34.92747986));
        
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
            	addMarkers();
            	while(true)
            	{
	            	update();
	            	try {
	        			Thread.sleep(60000);
	        		
	        		} catch (InterruptedException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
            	}
            }
        });
        
//        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
//        	final Marker marker = Marker.createProvided(Marker.Provided.RED)
//        	.setPosition(event.getCoordinate())
//        	.setVisible(true);
//        	mapView.addMarker(marker);
//        	markersCreatedOnClick.put(marker.getId(), marker);
//        });
//        
//        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
//        	event.consume();
//        	Marker marker = markersCreatedOnClick.remove(event.getMarker().getId());
//        	if (null != marker) {
//        		mapView.removeMarker(marker);
//        	}
//        });
        
		//mapView.setCustomMapviewCssURL(getClass().getResource("custom_mapview.css"));
		mapView.initialize();
	}
	
	public void update()
	{
		for(Map.Entry<String, Marker> entry : markers.entrySet())
		{
			String[] planeData = BackendMethods.getPlaneData(entry.getKey());
			String[] xyStr = planeData[3].split(";");
			Coordinate xy = new Coordinate(Double.parseDouble(xyStr[0]), Double.parseDouble(xyStr[1]));
			entry.getValue().setPosition(xy);
			
			entry.getValue().setRotation(Integer.parseInt(planeData[4]));
			
		}
	}
	
	public void addMarkers() {
		for (Map.Entry<String, Marker> entry : markers.entrySet()) {
			mapView.addMarker(entry.getValue());
		}
	}
}
