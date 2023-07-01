package com.progetto.packController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarApp extends Application {

    private YearMonth currentYearMonth;
    private Label monthLabel;

    @Override
    public void start(Stage primaryStage) {
        currentYearMonth = YearMonth.now();

        FlowPane calendarPane = new FlowPane();
        calendarPane.setVgap(5);
        calendarPane.setHgap(5);
        calendarPane.setAlignment(Pos.CENTER);

        Button previousButton = new Button("<");
        previousButton.setOnAction(e -> showPreviousMonth());

        Button nextButton = new Button(">");
        nextButton.setOnAction(e -> showNextMonth());

        monthLabel = new Label();
        updateMonthLabel();

        HBox navigationBox = new HBox(10, previousButton, monthLabel, nextButton);
        navigationBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, navigationBox, calendarPane);
        root.setAlignment(Pos.CENTER);

        showCalendar(calendarPane);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("JavaFX Calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCalendar(FlowPane calendarPane) {
        calendarPane.getChildren().clear();

        int daysInMonth = currentYearMonth.lengthOfMonth();

        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= daysInMonth; i++) {
            VBox dayBox = new VBox();

            Button dayButton = new Button(String.valueOf(i));
            dayButton.setPrefWidth(60);
            dayButton.setOnAction(e -> handleDayButtonClick(dayButton.getText()));

            Button button1 = new Button("Button 1");
            Button button2 = new Button("Button 2");

            dayBox.getChildren().addAll(dayButton, button1, button2);
            calendarPane.getChildren().add(dayBox);
        }
    }

    private void updateMonthLabel() {
        String monthName = currentYearMonth.getMonth().toString();
        int year = currentYearMonth.getYear();
        monthLabel.setText(monthName + " " + year);
    }

    private void showNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        showCalendar((FlowPane) monthLabel.getParent().getParent());
        updateMonthLabel();
    }

    private void showPreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        showCalendar((FlowPane) monthLabel.getParent().getParent());
        updateMonthLabel();
    }

    private void handleDayButtonClick(String day) {
        System.out.println("Clicked on day: " + day);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
