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

public class FleetOverviewController {
	@FXML
	private MapView mapView;
	
	
	private Map<String, Marker> markers = new HashMap<>();
	
	public FleetOverviewController() {
		Marker markerCenter = new Marker(getClass().getResource("/ActivePlane.png")).setVisible(true);
		markerCenter.setRotation(68);
		markers.put(markerCenter.getId(), markerCenter);
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
	
	public void addMarkers() {
		for (Map.Entry<String, Marker> entry : markers.entrySet()) {
			entry.getValue().setPosition(mapView.getCenter());
			mapView.addMarker(entry.getValue());
		}
	}
}
