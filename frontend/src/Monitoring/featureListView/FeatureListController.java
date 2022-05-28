package Monitoring.featureListView;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class FeatureListController {
    @FXML
    private ListView<String> listView;
    private ListProperty<String> listViewP;
    private StringProperty nameOfFeature;

    // Add a public no-args constructor
    public FeatureListController() {
    }

    @FXML
    private void initialize() {

//        listView.getItems().clear();
        listViewP = new SimpleListProperty<>();
        nameOfFeature=new SimpleStringProperty();
        listView.itemsProperty().bind(listViewP);
      listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            nameOfFeature.setValue(selectedItem);
        });
    }

    public ListProperty<String> getListViewP() {
        return listViewP;
    }

    public void setListViewP(ListProperty<String> listViewP) {
        this.listViewP = listViewP;
    }

    public ListView<String> getListView() {
        return listView;
    }

    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    public ListProperty<String> listViewPProperty() {
        return listViewP;
    }

    public void setListViewP(ObservableList<String> listViewP) {
        this.listViewP.set(listViewP);
    }

    public StringProperty getNameOfFeature() {
        return nameOfFeature;
    }

    public void setNameOfFeature(StringProperty nameOfFeature) {
        this.nameOfFeature = nameOfFeature;
    }



}
