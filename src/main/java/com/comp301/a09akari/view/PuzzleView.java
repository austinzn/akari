package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.CellType;
import com.comp301.a09akari.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PuzzleView implements FXComponent {
  private final Model model;
  private final ClassicMvcController controller;

  public PuzzleView(Model model, ClassicMvcController controller) {
    this.model = model;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    GridPane puzzle = new GridPane();
    puzzle.setGridLinesVisible(true);
    puzzle.setPrefSize(500, 500);
    puzzle.setAlignment(Pos.CENTER);
    int row = 0;
    int col = 0;
    while (row < model.getActivePuzzle().getHeight() && col < model.getActivePuzzle().getWidth()) {
      while (col < model.getActivePuzzle().getWidth()) {
        CellType type = model.getActivePuzzle().getCellType(row, col);
        if (type == CellType.CLUE) {
          if (model.isClueSatisfied(row, col)) {
            puzzle.add(makeSatisfied(row, col), col, row);
          } else {
            puzzle.add(makeCell(model.getActivePuzzle().getClue(row, col)), col, row);
          }
        }
        if (type == CellType.WALL) {
          puzzle.add(makeCell(5), col, row);
        }
        if (type == CellType.CORRIDOR) {
          if (model.isLamp(row, col)) {
            if (model.isLampIllegal(row, col)) {
              puzzle.add(makeBadLight(row, col), col, row);
            } else {
              puzzle.add(makeLight(row, col), col, row);
            }
          } else if (model.isLit(row, col)) {
            // puzzle.add(makeCorridor(1,col, col), col, col);
            puzzle.add(makeCorridor(1, row, col), col, row);
          } else {
            puzzle.add(makeCorridor(0, row, col), col, row);
          }
        }
        col += 1;
      }
      col = 0;
      row += 1;
    }
    return puzzle;
  }

  private Label makeSatisfied(int row, int col) {
    int clue = model.getActivePuzzle().getClue(row, col);
    Label tile = new Label(String.valueOf(clue));
    tile.getStyleClass().add("cell");
    tile.getStyleClass().add("satisfied");
    return tile;
  }

  private Label makeCell(int type) {
    if (type < 0 || type > 6) {
      throw new IllegalArgumentException();
    }
    Label tile = new Label();
    if (type >= 0 && type < 5) {
      tile = new Label(String.valueOf(type));
      tile.getStyleClass().add("cell");
      tile.getStyleClass().add("cell-clue");
    } else if (type == 5) {
      tile.getStyleClass().add("cell");
      tile.getStyleClass().add("cell-wall");
    }
    return tile;
  }

  private Label makeCorridor(int type, int row, int col) {
    if (type != 1 && type != 0) {
      throw new IllegalArgumentException();
    }
    Label tile = new Label();
    if (type == 0) {
      tile.getStyleClass().add("cell");
      tile.getStyleClass().add("cell-corridor");

    } else if (type == 1) {
      tile.getStyleClass().add("cell");
      tile.getStyleClass().add("cell-lit");
    }
    tile.setOnMouseClicked(actionEvent -> controller.clickCell(row, col));
    return tile;
  }

  private ImageView makeLight(int row, int col) {
    Image light = new Image("lightbulb.jpg");
    ImageView imageView = new ImageView();
    imageView.setImage(light);
    imageView.setFitWidth(50.0);
    imageView.setFitHeight(50.0);
    imageView.setPreserveRatio(true);
    imageView.setPickOnBounds(true);
    imageView.setOnMouseClicked(actionEvent -> controller.clickCell(row, col));
    return imageView;
  }

  private ImageView makeBadLight(int row, int col) {
    Image light = new Image("lightbulb - Copy.jpg");
    ImageView imageView = new ImageView();
    imageView.setImage(light);
    imageView.setFitWidth(50.0);
    imageView.setFitHeight(50.0);
    imageView.setPreserveRatio(true);
    imageView.setPickOnBounds(true);
    imageView.setOnMouseClicked(actionEvent -> controller.clickCell(row, col));
    return imageView;
  }
}
