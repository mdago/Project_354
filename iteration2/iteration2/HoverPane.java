package iteration2;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

class HoverPane extends StackPane {
    HoverPane(DateObject d, double y) {
      setPrefSize(5,5);

      final Label label = createDataThresholdLabel(d, y);

      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getChildren().setAll(label);
          setCursor(Cursor.NONE);
          toFront();
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getChildren().clear();
        }
      });
    }

    private Label createDataThresholdLabel(DateObject d, double price) {
      final Label label = new Label(d.toString() + "\nClosing Price: $" + price);
      label.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
      label.setStyle("-fx-font-size: 13;");

      label.toFront();
      label.setTextFill(Color.CORNFLOWERBLUE);

      label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
      return label;
    }
  }
