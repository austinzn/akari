package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlView implements FXComponent {
  private final Model model;
  private final ClassicMvcController controller;
  private Button nextButton;
  private Button prevButton;
  private Button randButton;
  private Button resetButton;

  public ControlView(Model model, ClassicMvcController controller) {
    this.model = model;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox pane1 = new HBox();
    if (model.isSolved()) {
      pane1.getStyleClass().add("solved");
    } else {
      pane1.getStyleClass().add("hbox");
    }
    pane1.setAlignment(Pos.CENTER);

    nextButton = new Button("Next puzzle");
    nextButton.setOnAction(
        event -> {
          controller.clickNextPuzzle();
          controller.clickResetPuzzle();
        });

    prevButton = new Button("Previous puzzle");
    prevButton.setOnAction(
        event -> {
          controller.clickPrevPuzzle();
          controller.clickResetPuzzle();
        });

    randButton = new Button("Random puzzle");
    randButton.setOnAction(
        event -> {
          controller.clickRandPuzzle();
          controller.clickResetPuzzle();
        });

    resetButton = new Button("Reset puzzle");
    resetButton.setOnAction(
        event -> {
          controller.clickResetPuzzle();
          controller.clickResetPuzzle();
        });

    pane1.getChildren().add(nextButton);
    pane1.getChildren().add(prevButton);
    pane1.getChildren().add(randButton);
    pane1.getChildren().add(resetButton);
    return pane1;
  }
}
