package com.example.test2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class HelloController {
    public TextField username;
    public PasswordField password;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        String uname = username.getText().trim();
        String upass = password.getText().trim();

        // Check for empty fields
        if (uname.isEmpty() || upass.isEmpty()) {
            welcomeText.setText("Please enter both username and password.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db_test2";
        String dbUser = "root";
        String dbPassword = "";

        // Using try-with-resources to ensure resources are closed properly
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tbl_user WHERE `email` = ? AND `password` = ?")) {

            // Set parameters
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, upass);

            ResultSet resultSet = preparedStatement.executeQuery(); // Execute query

            // Populate the table with data from the database
            if (resultSet.next()) { // If user exists
                try {
                    Parent secondScene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));

                    Stage secondStage = new Stage();
                    secondStage.setTitle("Dashboard");
                    secondStage.setScene(new Scene(secondScene));
                    // Use the actual TextField or PasswordField for closing the stage
                    Stage firstSceneStage = (Stage) username.getScene().getWindow();
                    firstSceneStage.close();

                    secondStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    welcomeText.setText("Failed to load dashboard.");
                }
            } else {
                welcomeText.setText("Invalid login credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            welcomeText.setText("Database error occurred.");
        }
    }
}
