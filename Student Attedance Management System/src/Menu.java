import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author 404NotFound
 */
public class Menu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("org/view/formMenu.fxml"));

        Scene scene = new Scene(root);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(false);
        stage.setTitle("Wellcome");
        stage.setScene(scene);
        stage.show();
    }
}
