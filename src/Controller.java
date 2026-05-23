package com.example.studentmanagementsystem;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class Controller {

    // 1. These match the fx:id names we typed in SceneBuilder!
    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, String> colYear;

    // A list to hold the data so the table can display it
    private ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1; // Keeps track of which student you click on

    // 2. This method runs automatically when the app opens
    @FXML
    public void initialize() {
        conn = DBConnection.connect(); // Connects to PostgreSQL!

        // Load the 1st, 2nd, 3rd, 4th Year options into the dropdown
        cbYear.getItems().setAll(YearLevel.values());

        // Tell the columns which data to look for
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        loadData(); // Fetch existing students from the database

        // Make the table clickable! When you click a row, it fills the text boxes
        table.setOnMouseClicked(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedId = s.getId();
                txtName.setText(s.getName());
                txtCourse.setText(s.getCourse());

                // Match the dropdown to the student's year level
                for (YearLevel y : YearLevel.values()) {
                    if (y.toString().equals(s.getYearLevel())) {
                        cbYear.setValue(y);
                    }
                }
            }
        });
    }

    // 3. READ: Pulls data from PostgreSQL and puts it in the TableView
    private void loadData() {
        list.clear();
        try {
            String query = "SELECT * FROM students ORDER BY id ASC";
            ResultSet rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. CREATE: The exact code that runs when you click the "Add" button
    @FXML
    private void addStudent() {
        // Validation: Prevent adding if fields are empty
        if (txtName.getText().isEmpty() || txtCourse.getText().isEmpty() || cbYear.getValue() == null) {
            return;
        }

        try {
            String query = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtCourse.getText());
            pst.setString(3, cbYear.getValue().toString());

            pst.executeUpdate(); // Sends the command to Postgres
            loadData();          // Refreshes the table
            clearFields();       // Empties the text boxes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. UPDATE: The code that runs when you click the "Update" button
    @FXML
    private void updateStudent() {
        if (selectedId == -1) return; // Stop if no student is selected

        try {
            String query = "UPDATE students SET name=?, course=?, year_level=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtCourse.getText());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);

            pst.executeUpdate();
            loadData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. DELETE: The code that runs when you click the "Delete" button
    @FXML
    private void deleteStudent() {
        if (selectedId == -1) return;

        try {
            String query = "DELETE FROM students WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 7. CLEAR: The code that runs when you click the "Clear" button
    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
    }
}