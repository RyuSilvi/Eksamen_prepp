package animations;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX Animation Example 2 – Built-in Transitions
 *
 * JavaFX includes several ready-made Transition classes that
 * cover the most common animation needs.
 *
 * Transitions covered:
 *   TranslateTransition  – moves a node along x / y axes
 *   FadeTransition       – changes opacity
 *   ScaleTransition      – scales a node up/down
 *   RotateTransition     – rotates a node
 *   FillTransition       – animates a Shape's fill color
 *   SequentialTransition – plays transitions one after another
 *   ParallelTransition   – plays transitions simultaneously
 *
 * Click each button to trigger the corresponding animation.
 */
public class AnimationTransitionsFX extends Application {

    @Override
    public void start(Stage stage) {

        // ---- Shared pane where animations play ----
        Pane canvas = new Pane();
        canvas.setPrefSize(500, 200);
        canvas.setStyle("-fx-background-color: #1a1a2e;");

        Circle circle = new Circle(50, 100, 30, Color.DEEPSKYBLUE);
        Rectangle rect = new Rectangle(250, 70, 60, 60);
        rect.setFill(Color.TOMATO);
        rect.setArcWidth(8); rect.setArcHeight(8);

        canvas.getChildren().addAll(circle, rect);

        // ============================================================
        // 1. TranslateTransition – move the circle left ↔ right
        // ============================================================
        Button translateBtn = new Button("Translate");
        translateBtn.setOnAction(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.seconds(1), circle);
            tt.setByX(120);
            tt.setCycleCount(2);
            tt.setAutoReverse(true);
            tt.play();
        });

        // ============================================================
        // 2. FadeTransition – fade the rectangle in and out
        // ============================================================
        Button fadeBtn = new Button("Fade");
        fadeBtn.setOnAction(e -> {
            FadeTransition ft = new FadeTransition(Duration.seconds(1), rect);
            ft.setFromValue(1.0);
            ft.setToValue(0.1);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();
        });

        // ============================================================
        // 3. ScaleTransition – grow the circle then shrink it back
        // ============================================================
        Button scaleBtn = new Button("Scale");
        scaleBtn.setOnAction(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(800), circle);
            st.setToX(2.0);
            st.setToY(2.0);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
        });

        // ============================================================
        // 4. RotateTransition – spin the rectangle 360°
        // ============================================================
        Button rotateBtn = new Button("Rotate");
        rotateBtn.setOnAction(e -> {
            RotateTransition rt = new RotateTransition(Duration.seconds(1), rect);
            rt.setByAngle(360);
            rt.setCycleCount(1);
            rt.play();
        });

        // ============================================================
        // 5. FillTransition – animate color from blue → orange
        // ============================================================
        Button fillBtn = new Button("Fill Color");
        fillBtn.setOnAction(e -> {
            FillTransition fill = new FillTransition(Duration.seconds(1), circle);
            fill.setFromValue(Color.DEEPSKYBLUE);
            fill.setToValue(Color.ORANGE);
            fill.setCycleCount(2);
            fill.setAutoReverse(true);
            fill.play();
        });

        // ============================================================
        // 6. SequentialTransition – move, then rotate (one after another)
        // ============================================================
        Button seqBtn = new Button("Sequential");
        seqBtn.setOnAction(e -> {
            TranslateTransition move = new TranslateTransition(Duration.millis(600), rect);
            move.setByX(-60);

            RotateTransition spin = new RotateTransition(Duration.millis(600), rect);
            spin.setByAngle(180);

            TranslateTransition moveBack = new TranslateTransition(Duration.millis(600), rect);
            moveBack.setByX(60);

            SequentialTransition seq = new SequentialTransition(move, spin, moveBack);
            seq.play();
        });

        // ============================================================
        // 7. ParallelTransition – move AND fade simultaneously
        // ============================================================
        Button parallelBtn = new Button("Parallel");
        parallelBtn.setOnAction(e -> {
            TranslateTransition move2 = new TranslateTransition(Duration.seconds(1), circle);
            move2.setByY(-40);
            move2.setCycleCount(2);
            move2.setAutoReverse(true);

            FadeTransition fade2 = new FadeTransition(Duration.seconds(1), circle);
            fade2.setFromValue(1.0);
            fade2.setToValue(0.3);
            fade2.setCycleCount(2);
            fade2.setAutoReverse(true);

            new ParallelTransition(move2, fade2).play();
        });

        // ---- Layout ----
        HBox controls = new HBox(8,
                translateBtn, fadeBtn, scaleBtn, rotateBtn,
                fillBtn, seqBtn, parallelBtn);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        VBox root = new VBox(10, canvas, controls);
        root.setStyle("-fx-background-color: #0f3460;");

        stage.setTitle("JavaFX Transitions");
        stage.setScene(new Scene(root, 520, 280));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
