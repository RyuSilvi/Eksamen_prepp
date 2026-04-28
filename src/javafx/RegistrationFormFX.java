package javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * JavaFX Example 3 – Registration Form
 *
 * Demonstrates the most common JavaFX input controls:
 *   TextField     – single-line text input
 *   PasswordField – masked text input
 *   ComboBox      – drop-down list
 *   CheckBox      – boolean toggle
 *   RadioButton   – mutually exclusive choice (in a ToggleGroup)
 *   TextArea      – multi-line text input
 *   Button        – triggers an action
 *   Label         – displays read-only text
 *
 * Also shows basic form validation and how to display feedback.
 */
public class RegistrationFormFX extends Application {

    @Override
    public void start(Stage stage) {

        // ---- Title ----
        Label title = new Label("User Registration");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // ---- Name ----
        Label nameLbl = new Label("Full Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");

        // ---- Email ----
        Label emailLbl = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("example@email.com");

        // ---- Password ----
        Label pwLbl = new Label("Password:");
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Min 6 characters");

        // ---- Country (ComboBox) ----
        Label countryLbl = new Label("Country:");
        ComboBox<String> countryBox = new ComboBox<>();
        countryBox.getItems().addAll("Norway", "Sweden", "Denmark", "Finland", "Iceland");
        countryBox.setPromptText("Select country");
        countryBox.setPrefWidth(200);

        // ---- Gender (RadioButtons) ----
        Label genderLbl = new Label("Gender:");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleBtn     = new RadioButton("Male");
        RadioButton femaleBtn   = new RadioButton("Female");
        RadioButton otherBtn    = new RadioButton("Other");
        maleBtn.setToggleGroup(genderGroup);
        femaleBtn.setToggleGroup(genderGroup);
        otherBtn.setToggleGroup(genderGroup);
        HBox genderBox = new HBox(15, maleBtn, femaleBtn, otherBtn);

        // ---- Newsletter (CheckBox) ----
        CheckBox newsletterBox = new CheckBox("Subscribe to newsletter");

        // ---- Bio (TextArea) ----
        Label bioLbl = new Label("Short Bio:");
        TextArea bioArea = new TextArea();
        bioArea.setPromptText("Tell us about yourself...");
        bioArea.setPrefRowCount(3);
        bioArea.setWrapText(true);

        // ---- Feedback label ----
        Label feedbackLbl = new Label();
        feedbackLbl.setFont(Font.font("Arial", 13));

        // ---- Submit button ----
        Button submitBtn = new Button("Register");
        submitBtn.setPrefWidth(120);
        submitBtn.setOnAction(e -> {
            String name    = nameField.getText().trim();
            String email   = emailField.getText().trim();
            String password = pwField.getText();
            String country = countryBox.getValue();
            RadioButton selectedGender =
                    (RadioButton) genderGroup.getSelectedToggle();

            // Validation
            if (name.isEmpty()) {
                showError(feedbackLbl, "Name is required.");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                showError(feedbackLbl, "Please enter a valid email.");
                return;
            }
            if (password.length() < 6) {
                showError(feedbackLbl, "Password must be at least 6 characters.");
                return;
            }
            if (country == null) {
                showError(feedbackLbl, "Please select a country.");
                return;
            }
            if (selectedGender == null) {
                showError(feedbackLbl, "Please select a gender.");
                return;
            }

            // Success
            String gender = selectedGender.getText();
            boolean newsletter = newsletterBox.isSelected();
            feedbackLbl.setTextFill(Color.GREEN);
            feedbackLbl.setText("✓ Registered: " + name + " | " + email
                    + " | " + country + " | " + gender
                    + (newsletter ? " | 📧 Newsletter" : ""));
        });

        // ---- Clear button ----
        Button clearBtn = new Button("Clear");
        clearBtn.setPrefWidth(80);
        clearBtn.setOnAction(e -> {
            nameField.clear(); emailField.clear(); pwField.clear();
            countryBox.setValue(null); genderGroup.selectToggle(null);
            newsletterBox.setSelected(false); bioArea.clear();
            feedbackLbl.setText("");
        });

        HBox buttons = new HBox(10, submitBtn, clearBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);

        // ---- Layout (GridPane for aligned label+field rows) ----
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        int row = 0;
        grid.add(title,          0, row++, 2, 1);
        grid.add(nameLbl,        0, row);   grid.add(nameField,    1, row++);
        grid.add(emailLbl,       0, row);   grid.add(emailField,   1, row++);
        grid.add(pwLbl,          0, row);   grid.add(pwField,      1, row++);
        grid.add(countryLbl,     0, row);   grid.add(countryBox,   1, row++);
        grid.add(genderLbl,      0, row);   grid.add(genderBox,    1, row++);
        grid.add(new Label(""),  0, row);   grid.add(newsletterBox,1, row++);
        grid.add(bioLbl,         0, row);   grid.add(bioArea,      1, row++);
        grid.add(buttons,        1, row++);
        grid.add(feedbackLbl,    0, row,    2, 1);

        // Make the field column grow with the window
        ColumnConstraints col0 = new ColumnConstraints(120);
        ColumnConstraints col1 = new ColumnConstraints(200, 250, Double.MAX_VALUE);
        col1.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col0, col1);

        stage.setTitle("Registration Form");
        stage.setScene(new Scene(grid, 480, 520));
        stage.show();
    }

    private void showError(Label lbl, String msg) {
        lbl.setTextFill(Color.RED);
        lbl.setText("✗ " + msg);
    }

    public static void main(String[] args) { launch(args); }
}
