package com.example.lab8;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class DragonsOnMap extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Map<Integer, Color> pointColors;
    private Map<Integer, Integer> pointXCoordinates;
    private Map<Integer, Integer> pointYCoordinates;

    public DragonsOnMap() {
        // Initialize maps to store point data
        pointColors = new HashMap<>();
        pointXCoordinates = new HashMap<>();
        pointYCoordinates = new HashMap<>();
    }

    // Method to add a point with specified color and coordinates
    public void addPoint(int id, Color color, int x, int y) {
        pointColors.put(id, color);
        pointXCoordinates.put(id, x);
        pointYCoordinates.put(id, y);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create Canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw points on the canvas
        for (int id : pointColors.keySet()) {
            Color color = pointColors.get(id);
            int x = pointXCoordinates.get(id);
            int y = pointYCoordinates.get(id);
            drawPoint(gc, color, x, y);
        }

        // Create StackPane and add canvas
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        // Create Scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Set the Scene to the Stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Map");
        primaryStage.show();
    }

    // Method to draw a point on the canvas
    private void drawPoint(GraphicsContext gc, Color color, double x, double y) {
        gc.setFill(color);
        gc.fillOval(x, y, 5, 5); // Adjust size as needed
    }

    public void main() {
        launch();
    }
}
