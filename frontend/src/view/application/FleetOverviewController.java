package view.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import model.BackendMethods;
import model.PlaneData;

public class FleetOverviewController {
    
	@FXML
	private MapView mapView;
	@FXML
	private PieChart pieChart;
	@FXML
	private LineChart<String, Number> lineChart;
	
	private Map<String, Marker> markers = new HashMap<>(); // maintains all planes located on the map
	private Map<Marker, PlaneData> dataPlanes = new HashMap<>(); // maintains the data associated with each plane (callsign, altitude, airspeed...)
	private MapLabel popup; // a label for the popup window when clicking a plane
	
	
	public FleetOverviewController() {
		String[] planeIDs = BackendMethods.getPlaneIDs("all"); // getting all planes from the DB
		for(String planeID : planeIDs)
		{
			markers.put(planeID, Marker.createProvided(Provided.RED));
		}
		popup = new MapLabel("text", 40, 20).setCssClass("blue-label");
	}
	
	@FXML
	private void initialize() {
        // using a cache provided by mapjfx to improve performance
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
		mapView.setMapType(MapType.OSM); // OpenStreetMap
		mapView.setZoom(6);
        
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
            	mapView.setCenter(new Coordinate(31.41377634, 34.92747986)); // centering the map in Israel
            	popup.setPosition(mapView.getCenter()).setVisible(false);
            	mapView.addLabel(popup);
                updateMap();
                // setting a timer to update the map every 15 seconds
            	Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(15), e -> updateMap()));
            	timeLine.setCycleCount(Timeline.INDEFINITE);
            	timeLine.play();
            }
        });
        
        // hide the popup when clicking anywhere on the map
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
        	popup.setVisible(false);
        });
       
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            PlaneData data = dataPlanes.get(event.getMarker());
            
            // if popup is visible and we clicked the same plane, then hide the popup
            if (popup.getVisible() && popup.getPosition().equals(event.getMarker().getPosition())) {
                popup.setVisible(false);
            } else { // we need to set a new position anyway
                updateLabel(data, event.getMarker().getPosition());
            }
        });
		mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));
		mapView.initialize();
		
		pieChart.setTitle("Active/Inactive Planes");
		
		Axis<String> xAxis = lineChart.getXAxis();
		xAxis.setLabel("Days");
		
		Axis<Number> yAxis = lineChart.getYAxis();
		yAxis.setLabel("Size");
		
		lineChart.setTitle("Fleet Size Over Time");
		lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);
	}
	
	public void updateLabel(PlaneData data, Coordinate position) {
	    mapView.removeLabel(popup);
	    popup = new MapLabel("Callsign: <b>" + data.getPlaneID() + "</b><br>Heading: " + data.getHeading() + "°<br>Altitude: " + data.getAltitude()/1000
                + " kft<br>Airspeed: " + data.getAirspeed() + " kn", 40, 20).setCssClass("blue-label");
	    popup.setPosition(position);
	    popup.setVisible(true); // won't have an effect if the popup is already visible
	    mapView.addLabel(popup);
	}
	
	public void updateMap() {
	    // doing something only if the Fleet Overview tab is currently in focus
	    if (!((TabPane)pieChart.getScene().lookup("#tabs")).getSelectionModel().getSelectedItem().getId().equals("fleetOverview")) {
	        return;
	    }
	    
	    int countActivePlanes = 0;
	    
		for(Map.Entry<String, Marker> entry : markers.entrySet()) {
			PlaneData data = BackendMethods.getPlaneData(entry.getKey()); // getting data of the current plane (the backend can either return from the database or from a live agent)
			
			Coordinate oldPos = entry.getValue().getPosition();
			dataPlanes.remove(entry.getValue());
			mapView.removeMarker(entry.getValue());
			
			Marker marker;
			if(BackendMethods.isPlaneActive(entry.getKey())){
			    countActivePlanes++;
				marker = new Marker(getClass().getResource("/ActivePlane.png")).setVisible(true); // green plane
			}else{
				marker = new Marker(getClass().getResource("/InactivePlane.png")).setVisible(true);
			}
			markers.put(entry.getKey(), marker);
			dataPlanes.put(marker, data);
			marker.setPosition(new Coordinate(data.getLatitude(), data.getLongitude())).setRotation((int)data.getHeading()); // setting position and rotation according to data
	        
            // if the popup was visible and was opened on the current plane, we need to move it to the new position
            if (popup.getVisible() && popup.getPosition().equals(oldPos)) {
                updateLabel(data, marker.getPosition());
            }
			mapView.addMarker(marker);
		}
		
		int countInactivePlanes = markers.size() - countActivePlanes;
		updatePieChart(countActivePlanes, countInactivePlanes);
		updateLineChart();
	}
	
	private void updatePieChart(int countActivePlanes, int countInactivePlanes)
	{
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Inactive", countInactivePlanes), new PieChart.Data("Active", countActivePlanes));
		pieChart.setData(pieChartData); // setting the data according to the updated counts
		
		pieChart.requestLayout();
		pieChart.applyCss();
		
        // taking care of colors
		List<String> colors = Arrays.asList("#404040", "#007F0E");
		for (int i = 0; i < pieChartData.size(); i++) {
		    PieChart.Data data = pieChartData.get(i);
		    String colorClass = "";
		    for (String cls : data.getNode().getStyleClass()) {
		        // Nodes in a pie chart are assigned a style class from the eight classes default-color0 to default-color7. Each of these style classes by default has -fx-pie-color
		        if (cls.startsWith("default-color")) {
		            colorClass = cls;
		            break;
		        }
		    }
		    for (Node n : pieChart.lookupAll("." + colorClass)) {
		        n.setStyle("-fx-pie-color:" + colors.get(i));
		    }
		}
	}
	
	private Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return new Date(cal.getTime().getTime()); // convert from java.util.Date to java.sql.Date
    }
	
	public void updateLineChart()
	{
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		List<Date> datesAdded =  dataPlanes.values().stream().map(d->d.getDateAdded()).sorted().collect(Collectors.toList());
		
        // drawing a point on each day from the earliest date added to the latest
		int currentIndex = 0, sizeUpToNow = 0;
		Date sequential = datesAdded.get(0), lastDate = datesAdded.get(datesAdded.size() - 1);
		while (sequential.compareTo(lastDate) <= 0) {
			while (currentIndex < datesAdded.size() && sequential.equals(datesAdded.get(currentIndex))) {
				sizeUpToNow++;
				currentIndex++;
			}
			series.getData().add(new XYChart.Data<String, Number>(sequential.toString(), sizeUpToNow));
			sequential = addDays(sequential, 1);
		}
		lineChart.getData().clear();
		lineChart.getData().add(series);
	}
}
