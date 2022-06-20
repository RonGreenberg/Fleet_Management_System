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
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.util.Duration;
import model.BackendMethods;
import model.PlaneData;

public class FleetOverviewController {
    
	@FXML
	private MapView mapView;
	
	private Map<String, Marker> markers = new HashMap<>();
	private MapLabel popup;
	
	public FleetOverviewController() {
		String[] planeIDs = BackendMethods.getPlaneIDs("all");
		for(String planeID : planeIDs)
		{
			markers.put(planeID, Marker.createProvided(Provided.RED));
		}
		popup = new MapLabel("text", 40, 20).setCssClass("blue-label");
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
            	popup.setPosition(mapView.getCenter()).setVisible(false);
            	mapView.addLabel(popup);
                updateMap();
            	Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(60), e -> updateMap()));
            	timeLine.setCycleCount(Timeline.INDEFINITE);
            	timeLine.play();
            }
        });
        
        // hide the popup when clicking anywhere on the map
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
        	popup.setVisible(false);
        });
       
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            // if popup is visible and we clicked the same plane, then hide the popup
            if (popup.getVisible() && popup.getPosition().equals(event.getMarker().getPosition())) {
                popup.setVisible(false);
            } else { // we need to set a new position anyway
                popup.setPosition(event.getMarker().getPosition());
                popup.setVisible(true); // won't have an effect if the popup is already visible
            }
        });
        
		mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));
		mapView.initialize();
	}
	
	public void updateMap() {
		for(Map.Entry<String, Marker> entry : markers.entrySet()) {
			PlaneData data = BackendMethods.getPlaneData(entry.getKey());
			
			Marker marker;
			mapView.removeMarker(entry.getValue());
			if(BackendMethods.isPlaneActive(entry.getKey())){
				marker = new Marker(getClass().getResource("/ActivePlane.png")).setVisible(true);
			}else{
				marker = new Marker(getClass().getResource("/InactivePlane.png")).setVisible(true);
			}
			markers.put(entry.getKey(), marker);
			marker.setPosition(new Coordinate(data.getLatitude(), data.getLongitude())).setRotation((int)data.getHeading());
			mapView.addMarker(marker);
		}
	}

}
