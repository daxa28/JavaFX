package com.example.demo1;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Spinner<Integer> maxPointsSpinner , intervalsSpinner, aSpinner, bSpinner, x0Spinner, leftLimitSpinner, rightLimitSpinner;
    @FXML
    public AnchorPane controlPane;
    @FXML
    public Pane randomizerPane, dataPane, infoPane;
    @FXML
    public RadioButton absoluteValuesRadioButton, deviationsRadioButton, lcgRadioButton, randTwoRadioButton;
    @FXML
    public ToggleGroup  randomize_group, plot_type_group;
    @FXML
    public Text intervalsText, randomizerText, plotTypeText, maxPointsText, informationText, xText, dataText, totalPointsText, bitText, modMText, meanSquareDeviationText, leftLimitText, aText, bText, rightLimitText;
    @FXML
    public Button goButton;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public CategoryAxis yAxis;
    @FXML
    public BarChart<String,Number> barChart;
    @FXML
    public Label totalPointsLabel, meanSquareDeviationLabel, duplicateValuesLabel;
    @FXML
    public XYChart.Series<String,Number> series1;
    @FXML
    public ChoiceBox<String> modMChoiceBox;

    @FXML
    protected void onGoButtonClick() {
        RadioButton selection1 = (RadioButton) randomize_group.getSelectedToggle();
        RadioButton selection2 = (RadioButton) plot_type_group.getSelectedToggle();
        barChart.getData().clear();
        long constant_a = aSpinner.getValue();
        long constant_b = bSpinner.getValue();
        long max_value_random_number = (long) Math.pow(2, Long.parseLong(modMChoiceBox.getValue())) - 1;
        long[] max_points_array = new long[(int) maxPointsSpinner.getValue()];
        int quantity_of_intervals = intervalsSpinner.getValue();
        double fist_value_interval = 0;
        double end_value_interval = (double) max_value_random_number / quantity_of_intervals;
        this.series1 = new XYChart.Series<>();
        meanSquareDeviationLabel.setText("");
        if (selection1 ==  lcgRadioButton && selection2 == deviationsRadioButton){
            duplicateValuesLabel.setText("");
            for (int i = 1; i <= quantity_of_intervals; i++) {
                if (end_value_interval <= max_value_random_number) {
                    String interval = "" + (i);
                    this.series1.getData().add(new XYChart.Data<>(interval, 0));
                }
                fist_value_interval = end_value_interval;
                end_value_interval += (double) max_value_random_number / quantity_of_intervals;
            }
            barChart.getData().add(this.series1);

            max_points_array[0] = x0Spinner.getValue();
            double average_value = 0;

            ObservableList<XYChart.Data<String, Number>> chartData = this.series1.getData();
            for (int x = 1; x < max_points_array.length; x++) {
                max_points_array[x] = (constant_a * max_points_array[x - 1] + constant_b) % max_value_random_number;
                int total_points =  x + 1;
                totalPointsLabel.setText("Total points: " + total_points);
                for (int i = 1; i <= quantity_of_intervals; i++) {
                    if (end_value_interval <= max_value_random_number) {
                        if (fist_value_interval < max_points_array[x] && max_points_array[x] <= end_value_interval) {
                            int newValue = chartData.get(i - 1).getYValue().intValue() + 1;
                            chartData.get(i - 1).setYValue(newValue);
                            average_value = (average_value + (double) newValue)/i;
                            double s = Math.pow((newValue-average_value)*(newValue-average_value)/i,0.5);
                            meanSquareDeviationLabel.setText("Mean square deviation: " + String.format("%.2f",s) + "%");


                            // TODO - запустить перерисовку графика
                            //                        try {
                            //                            Thread.sleep(500);
                            //                        } catch (InterruptedException e) {
                            //                            throw new RuntimeException(e);
                            //                        }
                            break;
                        }
                    }
                    fist_value_interval = end_value_interval;
                    end_value_interval += (double) max_value_random_number / quantity_of_intervals;
                }
                fist_value_interval = 0;
                end_value_interval = (double) max_value_random_number / quantity_of_intervals;
            }
        }
        else if (selection1 ==  lcgRadioButton && selection2 == absoluteValuesRadioButton){
            max_points_array[0] = x0Spinner.getValue();
            for (int x = 1; x < max_points_array.length; x++) {
                max_points_array[x] = (constant_a * max_points_array[x - 1] + constant_b) % max_value_random_number;
                int total_points =  x + 1;
                totalPointsLabel.setText("Total points: " + total_points);
                this.series1.getData().add(new XYChart.Data<>( ""+ x, max_points_array[x]));
            }
            barChart.getData().add(this.series1);
            int duplicate_values = 0;
            for(int i=0; i<max_points_array.length; i++) {
                for (int j=i+1; j<max_points_array.length; j++) {
                    if(max_points_array[i] == max_points_array[j]) {
                        duplicate_values = duplicate_values + 1;
                    }
                }
            }
            duplicateValuesLabel.setText("Duplicate values: " + duplicate_values);
        }
        else if (selection1 ==  randTwoRadioButton && selection2 == deviationsRadioButton) {
            duplicateValuesLabel.setText("");
            long leftLimit = leftLimitSpinner.getValue();
            long rightLimit = rightLimitSpinner.getValue();
            end_value_interval = (double) rightLimit/quantity_of_intervals;
            for (int i = 1; i <= quantity_of_intervals; i++) {
                if (end_value_interval <= rightLimit) {
                    String interval = "" + (i);
                    this.series1.getData().add(new XYChart.Data<>(interval, 0));
                }
                fist_value_interval = end_value_interval;
                end_value_interval += (double) rightLimit / quantity_of_intervals;
            }
            barChart.getData().add(this.series1);

            double average_value = 0;

            ObservableList<XYChart.Data<String, Number>> chartData = this.series1.getData();
            for (int x = 1; x < max_points_array.length; x++) {
                max_points_array[x] = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
                int total_points =  x + 1;
                totalPointsLabel.setText("Total points: " + total_points);
                for (int i = 1; i <= quantity_of_intervals; i++) {
                    if (end_value_interval <= rightLimit) {
                        if (fist_value_interval < max_points_array[x] && max_points_array[x] <= end_value_interval) {
                            int newValue = chartData.get(i - 1).getYValue().intValue() + 1;
                            chartData.get(i - 1).setYValue(newValue);
                            average_value = (average_value + (double) newValue)/i;
                            double s = Math.pow((newValue-average_value)*(newValue-average_value)/i,0.5);
                            meanSquareDeviationLabel.setText("Mean square deviation: " + String.format("%.2f",s) + "%");


                            // TODO - запустить перерисовку графика
                            //                        try {
                            //                            Thread.sleep(500);
                            //                        } catch (InterruptedException e) {
                            //                            throw new RuntimeException(e);
                            //                        }
                            break;
                        }
                    }
                    fist_value_interval = end_value_interval;
                    end_value_interval += (double) rightLimit / quantity_of_intervals;
                }
                fist_value_interval = 0;
                end_value_interval = (double) rightLimit / quantity_of_intervals;
            }
        }
        else if (selection1 ==  randTwoRadioButton && selection2 == absoluteValuesRadioButton){
            long leftLimit = leftLimitSpinner.getValue();
            long rightLimit = rightLimitSpinner.getValue();
            for (int x = 1; x < max_points_array.length; x++) {
                max_points_array[x] = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
                int total_points =  x + 1;
                totalPointsLabel.setText("Total points: " + total_points);
                this.series1.getData().add(new XYChart.Data<>( ""+ x, max_points_array[x]));
            }
            barChart.getData().add(this.series1);
            int duplicate_values = 0;
            for(int i=0; i<max_points_array.length; i++) {
                for (int j=i+1; j<max_points_array.length; j++) {
                    if(max_points_array[i] == max_points_array[j]) {
                        duplicate_values = duplicate_values + 1;
                    }
                }
            }
            duplicateValuesLabel.setText("Duplicate values: " + duplicate_values);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> maxPointsSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000);
        SpinnerValueFactory<Integer> intervalsSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000);
        SpinnerValueFactory<Integer> aSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000);
        SpinnerValueFactory<Integer> bSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000);
        SpinnerValueFactory<Integer> x0SpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000);
        SpinnerValueFactory<Integer> leftLimitSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000000);
        SpinnerValueFactory<Integer> rightLimitSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000000);
        maxPointsSpinnerValueFactory.setValue(20000);
        intervalsSpinnerValueFactory.setValue(200);
        aSpinnerValueFactory.setValue(252149031);
        bSpinnerValueFactory.setValue(11);
        x0SpinnerValueFactory.setValue(1);
        leftLimitSpinnerValueFactory.setValue(1);
        rightLimitSpinnerValueFactory.setValue(1000);
        maxPointsSpinner.setValueFactory(maxPointsSpinnerValueFactory);
        intervalsSpinner.setValueFactory(intervalsSpinnerValueFactory);
        aSpinner.setValueFactory(aSpinnerValueFactory);
        bSpinner.setValueFactory(bSpinnerValueFactory);
        x0Spinner.setValueFactory(x0SpinnerValueFactory);
        leftLimitSpinner.setValueFactory(leftLimitSpinnerValueFactory);
        rightLimitSpinner.setValueFactory(rightLimitSpinnerValueFactory);


        modMChoiceBox.getItems().addAll("16","32");
        modMChoiceBox.setValue("32");


        randomize_group.selectToggle(lcgRadioButton);
        plot_type_group.selectToggle(deviationsRadioButton);
    }
}