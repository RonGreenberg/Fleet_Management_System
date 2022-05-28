package view.graphView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.AnchorPane;



public class Graph extends AnchorPane {
	private StringProperty nameOfFeatureA;
	private StringProperty nameOfFeatureB;
	private StringProperty spLabelCoralFeatureA;
	private StringProperty spLabelCoralFeatureB;
	private StringProperty spAnomalyClass;

	private GraphController graphController;

	public Graph() {
		super();
		try {

			FXMLLoader fxl=new FXMLLoader();
			AnchorPane graph=fxl.load(getClass().getResource("Graph.fxml").openStream());
			graphController=fxl.getController();
			nameOfFeatureA=new SimpleStringProperty(graphController.getGraphNameOfFeatureA().getValue());
			spLabelCoralFeatureA=new SimpleStringProperty(graphController.getSpLabelCoralFeatureA().getValue());
			nameOfFeatureB=new SimpleStringProperty(graphController.getGraphNameOfFeatureB().getValue());
			spLabelCoralFeatureB=new SimpleStringProperty(graphController.getSpLabelCoralFeatureB().getValue());
			spAnomalyClass=new SimpleStringProperty(graphController.getSpAnomalyClassProperty().getValue());

			graphController.getGraphNameOfFeatureA().bindBidirectional(nameOfFeatureA);
			graphController.getGraphNameOfFeatureB().bindBidirectional(nameOfFeatureB);

			graphController.getSpLabelCoralFeatureA().bindBidirectional(spLabelCoralFeatureA);
			graphController.getSpLabelCoralFeatureB().bindBidirectional(spLabelCoralFeatureB);
			graphController.getSpAnomalyClassProperty().bindBidirectional(spAnomalyClass);

			this.getChildren().add(graph);
		}
		catch(Exception e){
			e.printStackTrace();

		}
	}

	public StringProperty getNameOfFeatureA() {
		return nameOfFeatureA;
	}

	public void setNameOfFeatureA(StringProperty nameOfFeatureA) {
		this.nameOfFeatureA = nameOfFeatureA;
	}

	public StringProperty getNameOfFeatureB() {
		return nameOfFeatureB;
	}

	public void setNameOfFeatureB(StringProperty nameOfFeatureB) {
		this.nameOfFeatureB = nameOfFeatureB;
	}

	public GraphController getGraphController() {
		return graphController;
	}

	public void setGraphController(GraphController graphController) {
		this.graphController = graphController;
	}


	public StringProperty getSpLabelCoralFeatureA() {
		return spLabelCoralFeatureA;
	}

	public void setSpLabelCoralFeatureA(String spLabelCoralFeatureA) {
		this.spLabelCoralFeatureA.set(spLabelCoralFeatureA);
	}


	public StringProperty getSpLabelCoralFeatureB() {
		return spLabelCoralFeatureB;
	}

	public void setSpLabelCoralFeatureB(String spLabelCoralFeatureB) {
		this.spLabelCoralFeatureB.set(spLabelCoralFeatureB);
	}

	public StringProperty getSpAnomalyClassProperty() {
		return spAnomalyClass;
	}

	public void setSpAnomalyClass(String spAnomalyClass) {
		this.spAnomalyClass.set(spAnomalyClass);
	}
}
