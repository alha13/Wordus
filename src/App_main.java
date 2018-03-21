import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;


public class App_main extends Application {


    //public static final String SPLASH_IMAGE = "http://abload.de/img/airbus-easyjetw5rhz.jpg";
    public static final String SPLASH_IMAGE = "font/svg ori/giphy.gif";

    private Pane splashLayout;

    private Stage primaryStage;
    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 407;


    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(SPLASH_IMAGE));

        splashLayout = new VBox();

        splashLayout.getChildren().addAll(splash);

        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: cornsilk; " +
                        "-fx-border-width:3; " +
                        "-fx-border-color: " +
                        "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                        ");"
        );
        splashLayout.setEffect(new DropShadow());
    }


    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler) {

        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(0.1);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }
    @Override
    public void start(final Stage initStage) throws Exception {
        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> foundFriends =
                        FXCollections.<String>observableArrayList();
                ObservableList<String> availableFriends =
                        FXCollections.observableArrayList(
                                "1", "1", "1", "1"
                        );


                for (int i = 0; i < availableFriends.size(); i++) {
                    Thread.sleep(30);
                    updateProgress(i + 1, availableFriends.size());

                }
                Thread.sleep(30);


                return foundFriends;
            }
        };

        showSplash(
                initStage,
                friendTask,
                () -> showMainStage(friendTask.valueProperty())
        );
        new Thread(friendTask).start();
    }








    protected interface InitCompletionHandler {
        void complete();
    }
    private void showMainStage(ReadOnlyObjectProperty<ObservableList<String>> Wordus) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Graph.fxml"));
        primaryStage = new Stage(StageStyle.DECORATED);
        try {
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Wordus");
        primaryStage.show();
        //icon
        primaryStage.getIcons().add(new Image("font/icon/text-editor.png"));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}