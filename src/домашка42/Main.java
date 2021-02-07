package домашка42;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class Main extends Application {
    private Controler k;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader();
        Parent root=loader.load(getClass().getResourceAsStream("sample.fxml"));
        k=loader.getController();
        primaryStage.setTitle("MojChat");
        Scene scene=newScene(root,350,350);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event->{
            k.Dispose();
            Platform.exit();
            System.exit(0);
        });
    }
    public static void main(String[] args){
        launch(args);
    }
}
