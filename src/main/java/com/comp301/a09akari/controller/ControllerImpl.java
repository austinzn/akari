package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;

import java.util.concurrent.ThreadLocalRandom;

public class ControllerImpl implements ClassicMvcController {
  private final Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  @Override
  public void clickNextPuzzle() {
    try {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() + 1);
    } catch (IndexOutOfBoundsException e) {

    }
  }

  @Override
  public void clickPrevPuzzle() {
    try {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
    } catch (IndexOutOfBoundsException e) {

    }
  }

  @Override
  public void clickRandPuzzle() {
    int numPuzzles = model.getPuzzleLibrarySize();
    int curr = model.getActivePuzzleIndex();
    int randomNum = curr;
    while (randomNum == curr) {
      randomNum = ThreadLocalRandom.current().nextInt(0, numPuzzles);
    }
    model.setActivePuzzleIndex(randomNum);
  }

  @Override
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    } else {
      model.addLamp(r, c);
    }
  }
}
