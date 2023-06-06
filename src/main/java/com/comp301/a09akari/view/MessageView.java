package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MessageView implements FXComponent {
  private final Model model;
  private final ClassicMvcController controller;

  public MessageView(Model model, ClassicMvcController controller) {
    this.model = model;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox pane = new HBox();
    pane.getChildren().clear();
    Label message = makeMsg();
    pane.getChildren().add(message);
    pane.setAlignment(Pos.CENTER);
    if (model.isSolved()) {
      pane.getStyleClass().add("solved");
    } else {
      pane.getStyleClass().add("hbox");
    }
    return pane;
  }

  private Label makeMsg() {
    Label msg = new Label();
    int curr = model.getActivePuzzleIndex() + 1;
    if (model.isSolved()) {
      msg.setText("Puzzle " + curr + " solved!");
      msg.getStyleClass().add("labelblack");
      System.out.println("solved");
    } else {
      int total = model.getPuzzleLibrarySize();
      msg.setText("Currently solving puzzle " + curr + " of " + total);
      msg.getStyleClass().add("label");
    }
    return msg;
  }
}
