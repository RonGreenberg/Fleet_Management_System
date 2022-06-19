package view.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.MapType;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Marker.Provided;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.util.Duration;
import model.BackendMethods;

public class FleetOverviewController {
	@FXML
	private MapView mapView;
	
	private Map<String, Marker> markers = new HashMap<>();
	private MapLabel popup;
	
	public FleetOverviewController() {
		//String[] planeIDs = BackendMethods.getPlaneIDs("all");
//		for(String planeID : planeIDs)
//		{
//			markers.put(planeID, Marker.createProvided(Provided.RED));
//		}
		popup = new MapLabel("text<br>text", 40, 20).setCssClass("blue-label");
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
		mapView.setZoom(6);
        mapView.setCenter(new Coordinate(31.41377634, 34.92747986));
        
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
            	popup.setPosition(mapView.getCenter()).setVisible(true);
            	mapView.addLabel(popup);
//                updateMap();
//            	Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(60), e -> updateMap()));
//            	timeLine.setCycleCount(Timeline.INDEFINITE);
//            	timeLine.play();
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
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
        	event.consume();
        	popup.setVisible(!popup.getVisible());
        });
        
		mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));
		mapView.initialize();
	}
	
	public void updateMap() {
		for(Map.Entry<String, Marker> entry : markers.entrySet()) {
			String[] planeData = BackendMethods.getPlaneData(entry.getKey());
			String[] xyStr = planeData[3].split(";");
			Coordinate xy = new Coordinate(Double.parseDouble(xyStr[0]), Double.parseDouble(xyStr[1]));
			double heading = Double.parseDouble(planeData[4]);
			
			Marker marker;
			mapView.removeMarker(entry.getValue());
			if(BackendMethods.isPlaneActive(entry.getKey())){
				marker = new Marker(getClass().getResource("/ActivePlane.png")).setVisible(true);
			}else{
				marker = new Marker(getClass().getResource("/InactivePlane.png")).setVisible(true);
			}
			markers.put(entry.getKey(), marker);
			marker.setPosition(xy).setRotation((int)heading);
			mapView.addMarker(marker);
		}
	}

}
