package javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * JavaFX Example 1 – Hello World
 *
 * The minimal JavaFX application.  Everything you need:
 *   Application  – base class; override start(Stage).
 *   Stage        – the top-level window (the OS window).
 *   Scene        – the container for all UI content; attached to a Stage.
 *   Node         – any UI element (Label, Button, Pane, …) inside a Scene.
 *
 * How to run (requires JavaFX on the module path):
 *   javac --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls \
 *         -d out src/javafx/HelloWorldFX.java
 *   java  --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls \
 *         -cp out javafx.HelloWorldFX
 */
public class HelloWorldFX extends Application {

    @Override
    public void start(Stage primaryStage) {

        // --- UI Nodes ---
        Label titleLabel = new Label("Hello, JavaFX!");
        titleLabel.setFont(new Font("Arial", 32));

        Label subLabel = new Label("Click the button below");
        subLabel.setFont(new Font("Arial", 14));

        Button clickBtn = new Button("Click Me!");
        clickBtn.setPrefWidth(120);
        clickBtn.setOnAction(e -> {
            titleLabel.setText("Button clicked! 🎉");
            clickBtn.setDisable(true);
        });

        // --- Layout ---
        VBox root = new VBox(20, titleLabel, subLabel, clickBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // --- Scene + Stage ---
        Scene scene = new Scene(root, 400, 250);
        primaryStage.setTitle("Hello JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);   // launches the JavaFX runtime and calls start()
    }
}
