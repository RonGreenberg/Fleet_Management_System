package TimeCapsule.menuBarView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import view.menuBarView.MenuBarController;


public class MenuBar extends AnchorPane {
    MenuBarController menuBarButton;
    private StringProperty sSettingFile;
    private StringProperty sCsvFile;
    private StringProperty sAlgoFile;

    public MenuBar() {
        super();
        try {

            FXMLLoader fxl = new FXMLLoader();
            AnchorPane menuBar = fxl.load(getClass().getResource("MenuBar.fxml").openStream());
            MenuBarController menuBarController = fxl.getController();

            this.getChildren().add(menuBar);
            sSettingFile = new SimpleStringProperty(menuBarController.getsSettingFile().getValue());
            sCsvFile = new SimpleStringProperty(menuBarController.getsCsvFile().getValue());
            sAlgoFile = new SimpleStringProperty(menuBarController.getsAlgoFile().getValue());

            menuBarController.getsAlgoFile().bindBidirectional(sAlgoFile);
            menuBarController.getsCsvFile().bindBidirectional(sCsvFile);
            menuBarController.getsSettingFile().bindBidirectional(sSettingFile);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public MenuBarController getMenuBarButton() {
        return menuBarButton;
    }

    public void setMenuBarButton(MenuBarController menuBarButton) {
        this.menuBarButton = menuBarButton;
    }

    public StringProperty getsSettingFile() {
        return sSettingFile;
    }

    public void setsSettingFile(StringProperty sSettingFile) {
        this.sSettingFile = sSettingFile;
    }

    public StringProperty getsCsvFile() {
        return sCsvFile;
    }

    public void setsCsvFile(StringProperty sCsvFile) {
        this.sCsvFile = sCsvFile;
    }

    public StringProperty getsAlgoFile() {
        return sAlgoFile;
    }

    public void setsAlgoFile(StringProperty sAlgoFile) {
        this.sAlgoFile = sAlgoFile;
    }

}
