package com.healthtracker.controller;

import com.healthtracker.dao.ExerciseDAO;
import com.healthtracker.model.Exercise;
import com.healthtracker.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddExerciseController {

    //leaga elem din .fxml cu variabila din controller
    //fara nu va injecta contollerul si variabila va ramane null
    //DatePicker calendarul si poti salva o data
    @FXML private DatePicker exerciseDatePicker;
    @FXML private TextField typeField;
    @FXML private TextField durationField;
    @FXML private TextField caloriesField;
    @FXML private Button saveButton;

    private User loggedInUser;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> saveExercise());
    }

    private void saveExercise() {
        if (loggedInUser == null) return;
//id 0 nu este inserat in baza de date  va fi generat automat de baza de date ,baza de date genereaza un id
        try {
            Exercise exercise = new Exercise(
                    0,
                    loggedInUser.getId(),
                    exerciseDatePicker.getValue(),
                    typeField.getText(),
                    Integer.parseInt(durationField.getText()),
                    Integer.parseInt(caloriesField.getText())
            );
            ExerciseDAO.addExercise(exercise);

            //se inchide fereastra dupa salvare
            ((Stage) saveButton.getScene().getWindow()).close();

        } catch (Exception ex) {
            System.out.println(" Eroare la salvarea exercițiului: " + ex.getMessage());
        }
    }
}
