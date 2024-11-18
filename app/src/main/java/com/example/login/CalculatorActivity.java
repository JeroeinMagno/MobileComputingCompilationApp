package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.text.NumberFormat;

public class CalculatorActivity extends AppCompatActivity {

    // UI elements for displaying the current calculation and the history
    private TextView display;  // For displaying the current input and result
    private TextView historyDisplay;  // For showing the calculation history
    private String currentDisplay = "";  // Stores the current input as a string
    private double firstNumber = 0;  // Holds the first operand in a calculation
    private String operator = "";  // Stores the operator for the calculation (+, -, *, /)
    private boolean isOperatorClicked = false;  // Tracks whether an operator was clicked
    private boolean isChainedCalculation = false;  // Tracks if multiple operations are chained together

    // ArrayList to store the history of calculations
    private ArrayList<String> history = new ArrayList<>();  // Keeps the history of all calculations
    private StringBuilder historyBuilder = new StringBuilder();  // Helper to build history display text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Set the color using a resource or a color value
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.spotify_dark_gray));

        // Initialize display components
        display = findViewById(R.id.display);  // The main display for the current calculation
        historyDisplay = findViewById(R.id.history);  // The history display

        // Set up listeners for buttons (number and operation)
        setButtonListeners();
    }

    private void setButtonListeners() {
        // Assign listeners for number buttons
        findViewById(R.id.btn0).setOnClickListener(v -> appendNumber("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> appendNumber("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> appendNumber("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> appendNumber("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> appendNumber("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> appendNumber("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> appendNumber("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> appendNumber("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> appendNumber("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> appendNumber("9"));

        // Assign listeners for operation buttons
        findViewById(R.id.btn_add).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btn_subtract).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btn_multiply).setOnClickListener(v -> setOperator("x"));
        findViewById(R.id.btn_divide).setOnClickListener(v -> setOperator("÷"));

        // Equals button to compute the result
        findViewById(R.id.btn_equals).setOnClickListener(v -> calculateResult());

        // Clear button to reset all input and history
        findViewById(R.id.btn_ac).setOnClickListener(v -> clearAll());

        // Button to toggle the sign of the current input (+/-)
        findViewById(R.id.btn_plusminus).setOnClickListener(v -> toggleSign());

        // Button to add a decimal point
        findViewById(R.id.btn_dot).setOnClickListener(v -> appendDot());

        // Backspace button to remove the last character
        findViewById(R.id.btn_back).setOnClickListener(v -> backspace());
    }

    // Appends a number to the current display when a number button is clicked
    private void appendNumber(String number) {
        if (isOperatorClicked) {  // If an operator was clicked, start fresh
            currentDisplay = "";  // Clear the current input
            isOperatorClicked = false;  // Reset operator flag
        }
        currentDisplay += number;  // Add the number to the current display
        display.setText(currentDisplay);  // Update the UI with the new number
    }

    // Appends a decimal point to the current input
    private void appendDot() {
        if (!currentDisplay.contains(".")) {  // Prevent multiple decimal points
            currentDisplay += ".";  // Add the decimal point
            display.setText(currentDisplay);  // Update the display
        }
    }

    // Sets the operator (+, -, x, ÷) and prepares for the next number input
    private void setOperator(String op) {
        if (!currentDisplay.isEmpty()) {  // Ensure there's a number to operate on
            if (operator.isEmpty()) {  // If this is the first operator
                firstNumber = Double.parseDouble(currentDisplay);  // Store the first number
            } else {
                calculateIntermediateResult();  // If chaining operations, calculate the result so far
            }

            operator = op;  // Set the operator
            isOperatorClicked = true;  // Flag that an operator was clicked
            currentDisplay = "";  // Clear the input for the next number
            display.setText(removeTrailingZero(firstNumber) + " " + operator);  // Show the operator and first number
        }
    }

    // Calculates the final result when the equals button is pressed
    // Calculates the final result when the equals button is pressed
    private void calculateResult() {
        if (!operator.isEmpty() && !currentDisplay.isEmpty()) {
            double secondNumber = Double.parseDouble(currentDisplay);  // Get the second operand
            double result = 0;

            // Perform the operation based on the operator
            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "x":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        display.setText("Error");  // Handle division by zero
                        return;
                    }
                    break;
            }

            // Build history for the current operation
            historyBuilder.append(removeTrailingZero(firstNumber))
                    .append(" ")
                    .append(operator)
                    .append(" ")
                    .append(removeTrailingZero(secondNumber))
                    .append(" = ")
                    .append(removeTrailingZero(result))
                    .append("\n");

            // Add to history and clear the builder for the next operation
            addToHistory(historyBuilder.toString());
            historyBuilder.setLength(0);  // Clear for next round

            // Update the display with the result
            display.setText(removeTrailingZero(result));
            firstNumber = result;  // Set the result as the new first number for chaining
            currentDisplay = "0";  // Reset the current display to 0
            operator = "";  // Reset the operator for the next calculation
            isChainedCalculation = true;  // Flag to indicate chained calculations
        }
    }


    // Calculates an intermediate result if chaining operations
    private void calculateIntermediateResult() {
        if (!currentDisplay.isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(currentDisplay);  // Get the second number
            double result = 0;

            // Perform the operation based on the operator
            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "x":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        display.setText("Error");  // Handle division by zero
                        return;
                    }
                    break;
            }

            // Build history for intermediate results
            if (isChainedCalculation) {
                historyBuilder.append(" ").append(operator).append(" ")
                        .append(removeTrailingZero(secondNumber)).append(" = ")
                        .append(removeTrailingZero(result));
            } else {
                historyBuilder.append(removeTrailingZero(firstNumber))
                        .append(" ").append(operator).append(" ")
                        .append(removeTrailingZero(secondNumber)).append(" = ")
                        .append(removeTrailingZero(result));
            }

            // Add to history and clear the builder for the next input
            addToHistory(historyBuilder.toString());
            historyBuilder.setLength(0);  // Clear for next operation

            // Update the display with the intermediate result
            firstNumber = result;  // Set the result as the new first number
            currentDisplay = String.valueOf(result);  // Set the current display to the result
            display.setText(removeTrailingZero(result));  // Update the display

            operator = "";  // Clear the operator for the next operation
            isChainedCalculation = true;  // Set the chaining flag
        }
    }

    // Adds the calculation to the history
    private void addToHistory(String calculation) {
        history.add(calculation);  // Add the current calculation to the history list
        updateHistoryDisplay();  // Update the history UI
    }

    // Updates the history display with all past calculations
    private void updateHistoryDisplay() {
        StringBuilder historyText = new StringBuilder();
        for (String record : history) {
            historyText.append(record).append("\n");  // Add each record to the display text
        }
        historyDisplay.setText(historyText.toString());  // Update the history display with the full history
    }

    // Clears all inputs and resets the calculator to its initial state
    private void clearAll() {
        currentDisplay = "";  // Reset the current input
        firstNumber = 0;  // Reset the first number
        operator = "";  // Clear the operator
        isOperatorClicked = false;  // Reset the operator flag
        isChainedCalculation = false;  // Reset the chaining flag
        display.setText("0");  // Reset the display to 0
        historyBuilder.setLength(0);  // Clear the history string builder
        history.clear();  // Clear the history list
        updateHistoryDisplay();  // Update the history display to show an empty history
    }

    // Toggles the sign of the current input (positive/negative)
    private void toggleSign() {
        if (!currentDisplay.isEmpty()) {  // Ensure there's a number to toggle
            double value = Double.parseDouble(currentDisplay);  // Convert the input to a double
            value = value * -1;  // Multiply the value by -1 to toggle the sign
            currentDisplay = String.valueOf(value);  // Update the current display value
            display.setText(removeTrailingZero(value));  // Update the display with the toggled value
        }
    }

    // Deletes the last character in the current input
    private void backspace() {
        if (!currentDisplay.isEmpty()) {  // Ensure there's something to delete
            currentDisplay = currentDisplay.substring(0, currentDisplay.length() - 1);  // Remove the last character
            display.setText(currentDisplay.isEmpty() ? "0" : currentDisplay);  // Update the display, show "0" if empty
        }
    }

    // Helper method to remove trailing zeros from a number and format it for display
    private String removeTrailingZero(double number) {
        // Format the number using the default locale with commas for readability
        NumberFormat numberFormat = NumberFormat.getInstance();
        String formattedNumber = numberFormat.format(number);  // Format the number

        // Check if the number is an integer (e.g., 5.0) and return without decimal places if true
        if (number == (long) number) {
            return formattedNumber;  // Return the formatted integer value
        } else {
            return String.format("%.2f", number);  // Return the number with two decimal places if it's a floating-point number
        }
    }
}
