/*package com.progetto.packController;

import com.progetto.packModel.CalendarActivity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus; // Focus date for the calendar
    ZonedDateTime today; // Current date

    @FXML
    private Text year; // Text element displaying the year

    @FXML
    private Text month; // Text element displaying the month

    @FXML
    private FlowPane calendar; // FlowPane representing the calendar grid
    @FXML
    private Button backOne = new Button();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now(); // Set the focus date to the current date
        today = ZonedDateTime.now(); // Set today's date
        drawCalendar(); // Draw the calendar
        updateButtonStatus(backOne); // Update the initial button status
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1); // Move focus date one month back
        calendar.getChildren().clear(); // Clear the calendar grid
        drawCalendar(); // Draw the updated calendar
        updateButtonStatus(backOne); // Update the button status after changing the date
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1); // Move focus date one month forward
        calendar.getChildren().clear(); // Clear the calendar grid
        drawCalendar(); // Draw the updated calendar
        updateButtonStatus(backOne); // Update the button status after changing the date
    }

    private void updateButtonStatus(Button button) {
        button.setDisable(!canGoBack());
    }

    private boolean canGoBack() {
        ZonedDateTime initialDate = ZonedDateTime.now(); // Set the initial date
        return !dateFocus.isBefore(initialDate);
    }


    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear())); // Set the year text
        month.setText(dateFocus.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN).toUpperCase());

        // Dimensions and styling for the calendar elements
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength(); // Get the maximum number of days in the month
        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue(); // Get the day of the week offset for the first day of the month

        for (int i = 0; i < 6; i++) { // Iterate over the calendar grid rows
            for (int j = 0; j < 7; j++) { // Iterate over the calendar grid columns
                StackPane stackPane = new StackPane(); // Create a stack pane for each calendar cell

                Rectangle rectangle = new Rectangle(); // Create a rectangle element for the cell
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH; // Calculate the width of the rectangle
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV; // Calculate the height of the rectangle
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle); // Add the rectangle to the stack pane

                int calculatedDate = (j + 1) + (7 * i); // Calculate the date for the current cell

                if (calculatedDate > dateOffset) { // Check if the date is valid for the current month
                    int currentDate = calculatedDate - dateOffset; // Calculate the current date of the month
                    if (currentDate <= monthMaxDate) { // Check if the current date is within the valid range
                        Text date = new Text(String.valueOf(currentDate)); // Create a text element for the date
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY); // Position the date text
                        stackPane.getChildren().add(date); // Add the date text to the stack pane
                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate); // Get the activities for the current date
                        int cYear = dateFocus.getYear();
                        int cMonth = dateFocus.getMonthValue();
                        LocalDate dateC = LocalDate.of(cYear, cMonth, currentDate);

                        if(dateC.getDayOfWeek()==DayOfWeek.SATURDAY || dateC.getDayOfWeek()==DayOfWeek.SUNDAY){
                            rectangle.setFill(Paint.valueOf("#808080"));
                        }

                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane); // Create activity elements for the date
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Paint.valueOf("#add8e6"));
                    }

                }
                calendar.getChildren().add(stackPane); // Add the stack pane to the calendar grid
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox(); // Create a vertical box for calendar activities
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("..."); // Display ellipsis when there are more than 2 activities
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    // On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime()); // Create text for each activity
            calendarActivityBox.getChildren().add(text); // Add the activity text to the calendar activity box
            text.setOnMouseClicked(mouseEvent -> {
                // On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20); // Position the calendar activity box
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY"); // Set the background color of the calendar activity box
        stackPane.getChildren().add(calendarActivityBox); // Add the calendar activity box to the stack pane
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>(); // Create a map to store activities by date

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth(); // Get the day of the month for the activity
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity)); // If the date is not in the map, add a new list with the activity
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate); // If the date already exists in the map, retrieve the existing list

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate); // Create a new list by copying the existing list
                newList.add(activity); // Add the new activity to the list
                calendarActivityMap.put(activityDate, newList); // Put the updated list back into the map
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>(); // Create a list to store calendar activities
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27) + 1, 16, 0, 0, 0, dateFocus.getZone()); // Create a random date and time within the given month
            calendarActivities.add(new CalendarActivity(time, "Hans", 111111)); // Create a new calendar activity and add it to the list
        }

        return createCalendarMap(calendarActivities); // Return the map of calendar activities for the month
    }
}
*/