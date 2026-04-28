package animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * JavaFX Animation Example 1 – Timeline
 *
 * Timeline is the most flexible JavaFX animation class.
 * You define KeyFrames at specific time offsets.
 * Between keyframes, JavaFX interpolates property values automatically.
 *
 * Demos:
 *   A) Moving ball  – x-position animated from left to right (looping)
 *   B) Blinking text – opacity toggled using a Timeline
 *   C) Growing rectangle – width grows, then shrinks (auto-reverse)
 *
 * Tip: combine Timeline with TranslateTransition / FadeTransition
 *      (see AnimationTransitionsFX) for common ready-made animations.
 */
public class TimelineAnimationFX extends Application {

    @Override
    public void start(javafx.stage.Stage stage) {

        // ============================================================
        // A) Moving Ball
        // ============================================================
        Pane ballPane = new Pane();
        ballPane.setPrefSize(400, 80);
        ballPane.setStyle("-fx-background-color: #1a1a2e;");

        Circle ball = new Circle(20, Color.DODGERBLUE);
        ball.setCenterY(40);
        ball.setCenterX(20);
        ballPane.getChildren().add(ball);

        Timeline ballTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(ball.centerXProperty(), 20)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(ball.centerXProperty(), 380))
        );
        ballTimeline.setCycleCount(Timeline.INDEFINITE);
        ballTimeline.setAutoReverse(true);   // bounces back and forth
        ballTimeline.play();

        // ============================================================
        // B) Blinking Text
        // ============================================================
        Text blinkText = new Text("Hello, JavaFX!");
        blinkText.setFont(Font.font("Arial", 24));
        blinkText.setFill(Color.ORANGERED);

        Timeline blinkTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(blinkText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(blinkText.opacityProperty(), 0.0)),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(blinkText.opacityProperty(), 1.0))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.play();

        // ============================================================
        // C) Growing Rectangle
        // ============================================================
        Rectangle rect = new Rectangle(20, 30, 10, 30);
        rect.setFill(Color.MEDIUMSEAGREEN);
        rect.setArcWidth(6); rect.setArcHeight(6);

        Pane rectPane = new Pane(rect);
        rectPane.setPrefSize(400, 60);
        rectPane.setStyle("-fx-background-color: #16213e;");

        Timeline rectTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(rect.widthProperty(), 10)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(rect.widthProperty(), 360))
        );
        rectTimeline.setCycleCount(Timeline.INDEFINITE);
        rectTimeline.setAutoReverse(true);
        rectTimeline.play();

        // ============================================================
        // Controls
        // ============================================================
        Button pauseAll = new Button("Pause All");
        Button playAll  = new Button("Play All");

        pauseAll.setOnAction(e -> {
            ballTimeline.pause();
            blinkTimeline.pause();
            rectTimeline.pause();
        });
        playAll.setOnAction(e -> {
            ballTimeline.play();
            blinkTimeline.play();
            rectTimeline.play();
        });

        HBox controls = new HBox(10, pauseAll, playAll);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(15,
                ballPane,
                blinkText,
                rectPane,
                controls);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #0f3460;");

        stage.setTitle("Timeline Animations");
        stage.setScene(new Scene(root, 430, 280));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
