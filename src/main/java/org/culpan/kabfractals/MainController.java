package org.culpan.kabfractals;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.culpan.kabfractals.generators.FractalSpace;
import org.culpan.kabfractals.generators.MandelbrotGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {
    private enum FractalColor { cRed, cGreen, cBlue, cGrey }

    @FXML
    private Canvas canvas;

    @FXML
    private ChoiceBox fractalChoiceBox;

    @FXML
    private Button generateButton;

    @FXML
    private HBox configHBox;

    private RadioButton redButton;

    private RadioButton greenButton;

    private RadioButton blueButton;

    private RadioButton greyButton;

    private int [][] lastResults;

    private int iterations;

    private int brightness = 0;

    private TextField iterationsTextField;

    public MainController() {
    }

    @FXML
    public void initialize() {
        fractalChoiceBox.getItems().add("Mandelbrot Set");

        fractalChoiceBox.setValue("Mandelbrot Set");

        configHBox.setAlignment(Pos.CENTER_LEFT);
        configHBox.setSpacing(10);

        Label iterationsLabel = new Label("Iterations   ");
        configHBox.getChildren().add(iterationsLabel);

        iterationsTextField = new TextField();
        iterationsTextField.setText("128");
        configHBox.getChildren().add(iterationsTextField);

        Region region1 = new Region();
        region1.setMaxWidth(50);
        configHBox.getChildren().add(region1);

        ToggleGroup colorGroup = new ToggleGroup();
        redButton = new RadioButton("Red");
        redButton.setToggleGroup(colorGroup);
        redButton.setOnAction(e -> drawImage());
        greenButton = new RadioButton("Green");
        greenButton.setToggleGroup(colorGroup);
        greenButton.setOnAction(e -> drawImage());
        blueButton = new RadioButton("Blue");
        blueButton.setToggleGroup(colorGroup);
        blueButton.setOnAction(e -> drawImage());
        greyButton = new RadioButton("Greyscale");
        greyButton.setToggleGroup(colorGroup);
        greyButton.setOnAction(e -> drawImage());
        redButton.setSelected(true);
        configHBox.getChildren().addAll(redButton, greenButton, blueButton, greyButton);

        Region region2 = new Region();
        region2.setMaxWidth(50);
        configHBox.getChildren().add(region2);

        Label brightLabel = new Label("Bright  ");
        configHBox.getChildren().add(brightLabel);

        Button dimButton = new Button("-");
        dimButton.setOnAction(e -> {
            brightness -= 10;
            if (brightness < -255) brightness = -255;
            drawImage();
        });
        configHBox.getChildren().add(dimButton);

        Button brightButton = new Button("+");
        brightButton.setOnAction(e -> {
            brightness += 10;
            if (brightness > 255) brightness = 255;
            drawImage();
        });
        configHBox.getChildren().add(brightButton);

    }

    @FXML
    void generateAction() {
        try {
            iterations = Integer.parseInt(iterationsTextField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Iterations '" + iterationsTextField.getText() + "' is not a valid number.");
            alert.showAndWait();
            return;
        }

        Scene scene = canvas.getScene();

        canvas.setWidth(scene.getWidth());
        canvas.setHeight(scene.getHeight() - 40);
        canvas.setLayoutX(0);
        canvas.setLayoutY(40);

        FractalSpace fractalSpace = new FractalSpace(-2.0, 1.0, -1.2, (int)canvas.getWidth(), (int)canvas.getHeight());
        MandelbrotGenerator generator = new MandelbrotGenerator(fractalSpace, iterations);
        generator.setOnRunning((succeesesEvent) -> {
            generateButton.setDisable(true);
            fractalChoiceBox.setDisable(true);
        });

        generator.setOnSucceeded((succeededEvent) -> {
            lastResults = generator.getValue();
            drawImage();
            generateButton.setDisable(false);
            fractalChoiceBox.setDisable(false);
        });

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(generator);
        executorService.shutdown();
    }

    protected void drawImage(){
        if (lastResults == null) return;

        FractalColor fc = FractalColor.cGrey;
        if (redButton.isSelected()) {
            fc = FractalColor.cRed;
        } else if (greenButton.isSelected()) {
            fc = FractalColor.cGreen;
        } else if (blueButton.isSelected()) {
            fc = FractalColor.cBlue;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double color_factor = 256 / (double)iterations;

        Color color = Color.rgb(0, 0, 0);
        for (int y = 0; y < lastResults.length; y++) {
            for (int x = 0; x < lastResults[y].length; x++) {
                if (lastResults[y][x] > 0) {
                    int c = (int)(lastResults[y][x] * color_factor) + brightness;
                    if (c > 255) c = 255;
                    else if (c < 0) c = 0;
                    switch (fc) {
                        case cRed:
                            color = Color.rgb(c, 0, 0);
                            break;
                        case cGreen:
                            color = Color.rgb(0, c, 0);
                            break;
                        case cBlue:
                            color = Color.rgb(0, 0, c);
                            break;
                        case cGrey:
                            color = Color.rgb(c, c, c);
                            break;
                    }
                } else {
                    color = Color.rgb(0, 0, 0);
                }
                gc.getPixelWriter().setColor(x, y, color);
            }
        }
    }
}
