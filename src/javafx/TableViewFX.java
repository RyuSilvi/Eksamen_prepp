package javafx;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX Example 2 – TableView
 *
 * TableView is one of the most useful JavaFX controls.
 * It displays data in rows and columns and automatically updates
 * when the underlying ObservableList changes.
 *
 * Key concepts:
 *   ObservableList   – a List that notifies the TableView when items change
 *   TableColumn      – one column; uses a cell-value factory to read the data
 *   Property         – wraps a value so JavaFX can observe changes to it
 *   SimpleXxxProperty – common property types (String, Integer, Double, …)
 */
public class TableViewFX extends Application {

    // =========================================================================
    // Model class – each instance is one row in the table
    // =========================================================================
    public static class Student {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty  name;
        private final SimpleIntegerProperty grade;

        public Student(int id, String name, int grade) {
            this.id    = new SimpleIntegerProperty(id);
            this.name  = new SimpleStringProperty(name);
            this.grade = new SimpleIntegerProperty(grade);
        }

        // JavaFX property accessors (required by TableColumn cell-value factory)
        public SimpleIntegerProperty idProperty()    { return id; }
        public SimpleStringProperty  nameProperty()  { return name; }
        public SimpleIntegerProperty gradeProperty() { return grade; }

        // Standard getters
        public int    getId()    { return id.get(); }
        public String getName()  { return name.get(); }
        public int    getGrade() { return grade.get(); }
    }

    // =========================================================================
    // UI
    // =========================================================================
    @Override
    public void start(Stage stage) {

        // --- Data source ---
        ObservableList<Student> students = FXCollections.observableArrayList(
                new Student(1, "Alice",   92),
                new Student(2, "Bob",     78),
                new Student(3, "Charlie", 85),
                new Student(4, "Diana",   95)
        );

        // --- Table ---
        TableView<Student> table = new TableView<>(students);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Student, Number> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(data -> data.getValue().gradeProperty());

        table.getColumns().addAll(idCol, nameCol, gradeCol);

        // --- Add row form ---
        TextField idField    = new TextField(); idField.setPromptText("ID");
        TextField nameField  = new TextField(); nameField.setPromptText("Name");
        TextField gradeField = new TextField(); gradeField.setPromptText("Grade");
        idField.setPrefWidth(60);
        gradeField.setPrefWidth(60);

        Button addBtn = new Button("Add Student");
        addBtn.setOnAction(e -> {
            try {
                int    id    = Integer.parseInt(idField.getText().trim());
                String name  = nameField.getText().trim();
                int    grade = Integer.parseInt(gradeField.getText().trim());
                students.add(new Student(id, name, grade));
                idField.clear(); nameField.clear(); gradeField.clear();
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input – ID and Grade must be integers.");
            }
        });

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) students.remove(selected);
        });

        HBox form    = new HBox(10, idField, nameField, gradeField, addBtn, deleteBtn);
        form.setPadding(new Insets(8));

        VBox root = new VBox(10, table, form);
        root.setPadding(new Insets(15));

        // --- Scene ---
        stage.setTitle("Student Table");
        stage.setScene(new Scene(root, 500, 350));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
