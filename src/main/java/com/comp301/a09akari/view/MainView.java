package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainView implements FXComponent, ModelObserver {
  private final FXComponent puzzleView;
  private final FXComponent controlView;
  private final FXComponent messageView;
  private final Scene scene;

  public MainView(Model model, ClassicMvcController controller) {
    this.puzzleView = new PuzzleView(model, controller);
    this.controlView = new ControlView(model, controller);
    this.messageView = new MessageView(model, controller);
    this.scene = new Scene(render());
    this.scene.getStylesheets().add("main.css");
  }

  public Scene getScene() {
    return scene;
  }

  @Override
  public void update(Model model) {
    scene.setRoot(render());
  }

  @Override
  public Parent render() {
    BorderPane pane = new BorderPane();
    pane.setPrefSize(650, 650);
    pane.setBottom(controlView.render());
    pane.setTop(messageView.render());
    pane.setCenter(puzzleView.render());
    pane.getStyleClass().add("borderpane");
    return pane;
  }
}
