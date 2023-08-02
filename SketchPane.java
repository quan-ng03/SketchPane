

import com.sun.org.apache.xpath.internal.WhitespaceStrippingElementMatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;

public class SketchPane extends BorderPane {
    //Task 1: Declare all instance variables listed in UML diagram
    ArrayList<Shape> shapeList = new ArrayList<>();
    ArrayList<Shape> tempList = new ArrayList<>();
    Color[] colors;
    Color currentStrokeColor = Color.BLACK, currentFillColor = Color.BLACK;
    int currentStrokeWidth = 1;
    Line line;
    Circle circle;
    Rectangle rectangle;
    double x1 =0;
    double y1 = 0;
    String[] colorLabels;
    Label fillColorLabel;
    Label strokeColorLabel, strokeWidthLabel;
    String[] strokeWidth;
    ComboBox<String> fillColorBox, strokeColorBox, strokeWidthBox;
    RadioButton radioButtonLine, radioButtonRectangle, radioButtonCircle;
    Pane sketchCanvas;
    Button undoButton = new Button("Undo"), eraseButton = new Button("Erase");
    ToggleGroup toggleGroup;


//Task 2: Implement the constructor
    public SketchPane() {

// Colors, labels, and stroke widths that are available to the user
        colors = new Color[] {Color.BLACK, Color.GREY, Color.YELLOW, Color.GOLD, Color.ORANGE, Color.DARKRED, Color.PURPLE, Color.HOTPINK, Color.TEAL, Color.DEEPSKYBLUE, Color.LIME} ;
        colorLabels = new String[] {"black", "grey", "yellow", "gold", "orange",
                "dark red", "purple", "hot pink", "teal", "deep sky blue", "lime"};
        fillColorLabel = new Label("Fill Color:");
        strokeColorLabel = new Label("Stroke Color:");
        strokeWidthLabel = new Label("Stroke Width:");
        strokeWidth = new String[] {"1", "3", "5", "7", "9", "11", "13"};

// INSTANTIATE Buttons, Boxes, Labels, and connect Boxes to Handlers (for HBox1 and SketchPane
        undoButton.setOnAction(new ButtonHandler());
        eraseButton.setOnAction(new ButtonHandler());
        fillColorBox = new ComboBox<String>(FXCollections.observableArrayList(colorLabels));
        strokeColorBox = new ComboBox<String>(FXCollections.observableArrayList(colorLabels));
        strokeWidthBox = new ComboBox<String>(FXCollections.observableArrayList(strokeWidth));
        fillColorBox.getSelectionModel().select(0);
        strokeColorBox.getSelectionModel().select(0);
        strokeWidthBox.getSelectionModel().select(0);
        fillColorLabel.setLabelFor(fillColorBox);
        strokeColorLabel.setLabelFor(strokeColorBox);
        strokeWidthLabel.setLabelFor(strokeWidthBox);
        fillColorBox.setOnAction(new ColorHandler());
        strokeColorBox.setOnAction(new ColorHandler());
        strokeWidthBox.setOnAction(new WidthHandler());

// INSTANTIATE Buttons, ToggleGroup for HBox2
        radioButtonLine = new RadioButton("Line");
        radioButtonRectangle= new RadioButton("Rectangle");
        radioButtonCircle = new RadioButton("Circle");
        toggleGroup = new ToggleGroup();
        radioButtonLine.setToggleGroup(toggleGroup);
        radioButtonRectangle.setToggleGroup(toggleGroup);
        radioButtonCircle.setToggleGroup(toggleGroup);
        radioButtonLine.setSelected(true);
// SET DEFAULT COLOR OF CANVAS
        sketchCanvas = new Pane();
        sketchCanvas.setStyle("-fx-background-color: WHITE;");
// SET SIZE AND DEFAULT COLOR FOR HBOX1
        HBox hBox1 = new HBox(20);
        hBox1.setMinSize(20,40);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setStyle("-fx-background-color: LIGHTGREY");
        hBox1.getChildren().addAll(fillColorLabel,fillColorBox,strokeWidthLabel,strokeWidthBox,strokeColorLabel,strokeColorBox);
// SET SIZE AND DEFAULT COLOR FOR HBOX2
        HBox hBox2 = new HBox(20);
        hBox2.setMinSize(20,40);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.setStyle("-fx-background-color: LIGHTGREY");
        hBox2.getChildren().addAll(radioButtonLine,radioButtonRectangle,radioButtonCircle,undoButton,eraseButton);
// ORIENTATE HBOXES AND CANVAS
        this.setTop(hBox1);
        this.setCenter(sketchCanvas);
        this.setBottom(hBox2);
// CONNECT CANVAS TO MOUSE HANDLER
        sketchCanvas.setOnMousePressed(new MouseHandler());
        sketchCanvas.setOnMouseDragged(new MouseHandler());
        sketchCanvas.setOnMouseReleased(new MouseHandler());

    }
    private class MouseHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
// TASK 3: Implement the mouse handler for Circle and Line
// Rectange Example given!
            if (radioButtonRectangle.isSelected()) {
//Mouse is pressed
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    x1 = event.getX();
                    y1 = event.getY();
                    rectangle = new Rectangle();
                    rectangle.setX(x1);
                    rectangle.setY(y1);
                    shapeList.add(rectangle);
                    rectangle.setFill(Color.WHITE);
                    rectangle.setStroke(Color.BLACK);
                    sketchCanvas.getChildren().add(rectangle);
                }
//Mouse is dragged
                else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED)
                {
                    if (event.getX() - x1 <0)
                        rectangle.setX(event.getX());
                    if (event.getY() - y1 <0)
                        rectangle.setY(event.getY());
                    rectangle.setWidth(Math.abs(event.getX() - x1));
                    rectangle.setHeight(Math.abs(event.getY() - y1));
                }
//Mouse is released
                else if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
                {
                    rectangle.setFill(currentFillColor);
                    rectangle.setStroke(currentStrokeColor);
                    rectangle.setStrokeWidth(currentStrokeWidth);
                }
            }
            else if (radioButtonCircle.isSelected()){
//Mouse is pressed
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED){
                    x1 = event.getX();
                    y1 = event.getY();
                    circle = new Circle();
                    circle.setCenterX(x1);
                    circle.setCenterY(y1);
                    shapeList.add(circle);
                    circle.setFill(Color.WHITE);
                    circle.setStroke(Color.WHITE);
                    sketchCanvas.getChildren().add(circle);
                }
//Mouse is dragged
                else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                    double x2 = event.getX();
                    double y2 = event.getY();
                    circle.setRadius(getDistance(x1,y1,x2,y2));
                }
//Mouse is released
                else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
                    circle.setFill(currentFillColor);
                    circle.setStroke(currentStrokeColor);
                    circle.setStrokeWidth(currentStrokeWidth);
                }
            }
            else if (radioButtonLine.isSelected()){
//Mouse is pressed
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED){
                    x1 = event.getX();
                    y1 = event.getY();
                    line = new Line();
                    line.setStartX(x1);
                    line.setStartY(y1);
                    shapeList.add(line);
                    sketchCanvas.getChildren().add(line);
                }

                else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                    double x2 = event.getX();
                    double y2 = event.getY();
                    line.setEndX(x2);
                    line.setEndY(y2);
                }

                else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
                    line.setStroke(currentStrokeColor);
                    line.setStrokeWidth(currentStrokeWidth);
                }
            }
        }
    }
    private class ButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
// TASK 4: Implement the button handler
            if (event.getSource() == undoButton && shapeList.size() > 0){
                shapeList.remove(shapeList.size()-1);
                sketchCanvas.getChildren().remove(sketchCanvas.getChildren().size()-1);
            }
            else if (event.getSource() == undoButton && shapeList.size() == 0){
                for (int i = 0; i < tempList.size();i++){
                    shapeList.add(tempList.get(i));
                }
                tempList.clear();
                sketchCanvas.getChildren().clear();
                for (int i = 0; i < shapeList.size(); i++){
                    sketchCanvas.getChildren().add(shapeList.get(i));
                }
            }
            else if (event.getSource() == eraseButton && shapeList.size() > 0){
                tempList.clear();
                tempList = shapeList;
                shapeList.clear();
                sketchCanvas.getChildren().clear();
            }
        }
    }
    private class ColorHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
// TASK 5: Implement the color handler
            int i = fillColorBox.getSelectionModel().getSelectedIndex();
            currentFillColor = colors[i];
            int j = strokeColorBox.getSelectionModel().getSelectedIndex();
            currentStrokeColor = colors[j];
        }
    }
    private class WidthHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
// TASK 6: Implement the stroke width handler
            int i = strokeWidthBox.getSelectionModel().getSelectedIndex();
            currentStrokeWidth = Integer.parseInt(strokeWidth[i]);
        }
    }
    // Get the Euclidean distance between (x1,y1) and (x2,y2)
    private double getDistance(double x1, double y1, double x2, double y2)  {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}