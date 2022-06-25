package view.application;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import view.dashboardView.Dashboard;
import view.graphView.Graph;
import view.joystickView.Joystick;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import model.AppModel;
import model.BackendMethods;
import model.algorithms.HybridAlgo;
import model.algorithms.LinearRegression;
import model.statlib.Point;

public class MonitoringController {
    
    /* TODO:
     * -Create a separate instance for anomaly detection.
     * -Feed it with hybrid algorithm by default (remove select algorithm menu).
     * -Define an anomaly detection cycle:
     *  1) Initialization: reset line counter, open a csv file for writing with a constant name, write a properties list as its first line.
     *  2) For each new line read from the backend and sent to the dashboard and joystick, increment counter by 1.
     *  3) If line counter equals the size of the anomaly detection cycle, close the csv file and call detect() on that file.
     *     Try to append the anomaly map and not create a new one each cycle.
     *  4) Add data point of previous csv anomaly map to graphs.
     *  5) Repeat.
     *  NOTE: understand how to update the anomaly detection graphs (different than the time capsule because time is linear, always advances).
     */
    
    AppModel model;
    
    @FXML
    private ListView<String> featureList;
    @FXML
    private Joystick joystick;
    @FXML
    private Dashboard dashboard;
    @FXML
    private Graph graph;
    @FXML
    private ComboBox<String> activePlanes;
    
    private XYChart.Series<Number, Number> seriesA = new XYChart.Series<>();
    private XYChart.Series<Number, Number> seriesB = new XYChart.Series<>();
    private XYChart.Series<Number, Number> seriesNormalPoints = new XYChart.Series<>();
    private XYChart.Series<Number, Number> seriesAnomalyPoints = new XYChart.Series<>();
    private XYChart.Series<Number, Number> seriesRegLine = new XYChart.Series<>();
    
    private float featureAValue;
    private float featureBValue;
    
    private static final int MAX_DATA_POINTS = 20;
    private int xSeriesData = 0;
    
    String regCsvFileName = "resources/reg_flight.csv";
    
    @SuppressWarnings("unchecked")
    @FXML
    private void initialize() {
        if (!Files.exists(Paths.get(regCsvFileName))) {
            regCsvFileName = "frontend/" + regCsvFileName;
        }
        
        model = new AppModel();
        model.setTimeSeriesTrain(regCsvFileName);
        model.setAnomalDetect(new LinearRegression()); // using the linear regression algorithm by default for Monitoring window
        featureList.setItems(FXCollections.observableArrayList(model.getTimeSeriesTrain().namesOfFeatures)); // populating the feature list
        initLineChart(graph.getGraphController().getFeatureA(), seriesA);
        initLineChart(graph.getGraphController().getFeatureB(), seriesB);
        //graph.lookup(".default-color2.chart-series-line").setStyle("-fx-stroke: transparent");
        graph.getGraphController().getAnomalyDetec().getData().addAll(seriesNormalPoints);
        graph.getGraphController().getAnomalyDetec().getData().addAll(seriesAnomalyPoints);
        graph.getGraphController().getAnomalyDetec().getData().addAll(seriesRegLine);
//        seriesNormalPoints.getNode().setStyle(".default-color0.chart-series-line { -fx-stroke: transparent; }");
//        seriesAnomalyPoints.getNode().setStyle(".default-color1.chart-series-line { -fx-stroke: transparent; }");
        seriesNormalPoints.setName("Normal point");
        seriesAnomalyPoints.setName("Anomaly point");
        
        featureList.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->{
            seriesA.getData().clear();
            seriesB.getData().clear();
            seriesNormalPoints.getData().clear();
            seriesNormalPoints.getData().clear();
            seriesRegLine.getData().clear();
            
            model.addLine(nv, seriesRegLine);
            
            graph.getGraphController().getFeatureALabel().setText(nv); // setting label of selected feature graph
            
            // setting label of most correlated feature graph
            String featureB = "No correlated feature found";
            if (((LinearRegression) model.getAnomalDetect()).getHashMap().containsKey(nv)) {
               featureB = ((LinearRegression) model.getAnomalDetect()).getHashMap().get(nv).feature2;
            }
            graph.getGraphController().getFeatureBLabel().setText(featureB);
        });
        
        Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(300), e -> updateDashboardAndJoystick()));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    
    @SuppressWarnings("unchecked")
    private void initLineChart(LineChart<Number, Number> lineChart, XYChart.Series<Number, Number> series) {
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setUpperBound(MAX_DATA_POINTS);
        xAxis.setTickUnit(2);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);
        
//        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
//        yAxis.setAutoRanging(false);
        
        lineChart.setAnimated(false);
        lineChart.getData().addAll(series);
        lineChart.setCreateSymbols(false);
    }
    
    @SuppressWarnings("unchecked")
    private void updateDashboardAndJoystick() {
        if (!((TabPane)activePlanes.getScene().lookup("#tabs")).getSelectionModel().getSelectedItem().getId().equals("monitoring")) {
            return;
        }
        
        String activePlane = activePlanes.getValue();
        if (activePlane == null || activePlane.isEmpty()) {
            return;
        }
        
        String line = BackendMethods.getFlightParamsLine(activePlane);
        if (line == null || line.isEmpty() || line.equals("Client disconnected")) {
            return;
        }
        
        String[] values = line.split(",");
        // updating dashboard
        dashboard.setAltitude(Float.parseFloat(values[25]));
        dashboard.setPitch(Float.parseFloat(values[27]));
        dashboard.setAirspeed(Float.parseFloat(values[24]));
        dashboard.setHeading(Float.parseFloat(values[36]));
        dashboard.setYaw(Float.parseFloat(values[20]));
        dashboard.setRoll(Float.parseFloat(values[28]));
        
        // updating joystick
        joystick.setAileron(Float.parseFloat(values[0]));
        joystick.setElevator(Float.parseFloat(values[1]));
        joystick.setRudder(Float.parseFloat(values[2]));
        joystick.setThrottle(Float.parseFloat(values[6]));
        
        // updating graphs
        int featureAIndex = featureList.getSelectionModel().selectedIndexProperty().get();
        if (featureAIndex != -1) { // returns -1 if no feature is selected
            featureAValue = Float.parseFloat(values[featureAIndex]);
            updateLineChart(graph.getGraphController().getFeatureA(), featureAValue);
            
            int featureBIndex = model.getTimeSeriesTrain().featurePlace(graph.getGraphController().getFeatureBLabel().getText());
            if (featureBIndex != -1) { // returns -1 when the label contains "No correlated feature found"
                featureBValue = Float.parseFloat(values[featureBIndex]);
                updateLineChart(graph.getGraphController().getFeatureB(), featureBValue);   
            }
            
            // anomaly detection
            // check max_x, max_y, min_x, min_y before plotting and limit accordingly
            XYChart.Data<Number, Number> data = new XYChart.Data<>(featureAValue, featureBValue);
            if (((LinearRegression)model.getAnomalDetect()).detectPoint(featureAValue, featureBValue, featureList.getSelectionModel().getSelectedItem())) {
                seriesAnomalyPoints.getData().add(data);
            } else {
                seriesNormalPoints.getData().add(data);
            }
        }
    }
    
    private void updateLineChart(LineChart<Number, Number> lineChart, float featureValue) {
        XYChart.Series<Number, Number> series = lineChart.getData().get(0);
        series.getData().add(new XYChart.Data<>(xSeriesData++, featureValue));
        
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0);
        }
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
//        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
//        yAxis.setAutoRanging(false);
//        if (yValue < yAxis.getLowerBound()) {
//            yAxis.setLowerBound(yValue);
//        }
//        if (yValue > yAxis.getUpperBound()) {
//            yAxis.setUpperBound(yValue);
//        }
    }

    public void cmbActivePlanesOnShowing() {
        String[] res = BackendMethods.getPlaneIDs("active");
        activePlanes.setItems(FXCollections.observableArrayList(res));
    }
    
    public void cmbActivePlanesOnAction() {
        if (activePlanes.getValue() == null || activePlanes.getValue().isEmpty()) {
            return;
        }
        featureList.setDisable(false);
    }

}
