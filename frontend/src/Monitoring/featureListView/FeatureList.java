package Monitoring.featureListView;

import view.featureListView.FeatureListController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;

import javafx.scene.layout.AnchorPane;


public class FeatureList extends AnchorPane {
    private ListProperty<String> listViewP;
    private FeatureListController featureListController;

    private StringProperty nameOfFeature;

    public FeatureList() {
        super();
        try {

            FXMLLoader fxl = new FXMLLoader();
            AnchorPane fList = fxl.load(getClass().getResource("FeatureList.fxml").openStream());
            FeatureListController featureController = fxl.getController();
            nameOfFeature=new SimpleStringProperty(featureController.getNameOfFeature().getValue());
            listViewP = new SimpleListProperty<>();
            featureController.getListViewP().bind(listViewP);
            featureController.getNameOfFeature().bindBidirectional(nameOfFeature);

            this.getChildren().add(fList);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public ListProperty<String> getListViewP() {
        return listViewP;
    }

    public void setListViewP(ListProperty<String> listViewP) {
        this.listViewP = listViewP;
    }

    public FeatureListController getFeatureListController() {
        return featureListController;
    }

    public void setFeatureListController(FeatureListController featureListController) {
        this.featureListController = featureListController;
    }

    public StringProperty getNameOfFeature() {
        return nameOfFeature;
    }

    public void setNameOfFeature(StringProperty nameOfFeature) {
        this.nameOfFeature = nameOfFeature;
    }


}
