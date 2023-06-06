package com.comp301.a09akari.model;

import javafx.util.Pair;

import java.util.*;

public class ModelImpl implements Model {
  private final PuzzleLibrary library;
  private final HashSet<Pair> lamps;
  private final HashMap<Pair, HashSet> lit;
  private final List<ModelObserver> observerList;
  private Puzzle currPuzzle;
  private int libIndex;

  public ModelImpl(PuzzleLibrary library) {
    this.library = library;
    this.currPuzzle = library.getPuzzle(0);
    this.lamps = new HashSet<>();
    this.lit = new HashMap<>();
    this.libIndex = 0;
    this.observerList = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) {
    if (c > currPuzzle.getWidth() || r > currPuzzle.getHeight() || r < 0 || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (currPuzzle.getCellType(r, c) == CellType.CORRIDOR) {
      Pair newLight = new Pair(c, r);
      if (lamps.contains(newLight)) {
        return;
      }
      lamps.add(newLight);
      HashSet<Pair> litCells = new HashSet<>();
      litCells.add(newLight);
      int x = c;
      int y = r;
      // light upwards
      while (inRange(x, y)) {
        if (currPuzzle.getCellType(y, x) == CellType.CORRIDOR) {
          litCells.add(new Pair(x, y));
          y += 1;
        } else {
          break;
        }
      }
      y = r;
      // light downwards
      while (inRange(x, y)) {
        if (currPuzzle.getCellType(y, x) == CellType.CORRIDOR) {
          litCells.add(new Pair(x, y));
          y -= 1;
        } else {
          break;
        }
      }
      y = r;
      // light right
      while (inRange(x, y)) {
        if (currPuzzle.getCellType(y, x) == CellType.CORRIDOR) {
          litCells.add(new Pair(x, y));
          x += 1;
        } else {
          break;
        }
      }
      x = c;
      // light left
      while (inRange(x, y)) {
        if (currPuzzle.getCellType(y, x) == CellType.CORRIDOR) {
          litCells.add(new Pair(x, y));
          x -= 1;
        } else {
          break;
        }
      }
      this.lit.put(newLight, litCells);
    } else {
      throw new IllegalArgumentException();
    }
    for (ModelObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    if (!inRange(c, r)) {
      throw new IndexOutOfBoundsException();
    }
    if (currPuzzle.getCellType(r, c) == CellType.CORRIDOR) {
      Pair toRemove = new Pair(c, r);
      if (lamps.contains(toRemove)) {
        lamps.remove(toRemove);
        lit.remove(toRemove);
      }
    } else {
      throw new IllegalArgumentException();
    }
    for (ModelObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public boolean isLit(int r, int c) {
    if (!inRange(c, r)) {
      throw new IndexOutOfBoundsException();
    }
    if (currPuzzle.getCellType(r, c) == CellType.CORRIDOR) {
      Pair toCheck = new Pair(c, r);
      for (Map.Entry<Pair, HashSet> entry : lit.entrySet()) {
        if (entry.getValue().contains(toCheck)) {
          return true;
        }
      }
      return false;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (!inRange(c, r)) {
      throw new IndexOutOfBoundsException();
    }
    if (currPuzzle.getCellType(r, c) == CellType.CORRIDOR) {
      Pair toCheck = new Pair(c, r);
      return lamps.contains(toCheck);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (!inRange(c, r)) {
      throw new IndexOutOfBoundsException();
    }
    if (isLamp(r, c)) {
      Pair toCheck = new Pair(c, r);
      for (Map.Entry<Pair, HashSet> entry : lit.entrySet()) {
        if (entry.getKey().equals(toCheck)) {
          continue;
        }
        if (entry.getValue().contains(toCheck)) {
          return true;
        }
      }
      return false;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Puzzle getActivePuzzle() {
    Puzzle temp = currPuzzle;
    return temp;
  }

  @Override
  public int getActivePuzzleIndex() {
    int temp = libIndex;
    return temp;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index > (getPuzzleLibrarySize() - 1) || index < 0) {
      throw new IndexOutOfBoundsException();
    }
    libIndex = index;
    currPuzzle = library.getPuzzle(index);
    lamps.clear();
    lit.clear();
    for (ModelObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  @Override
  public void resetPuzzle() {
    lamps.clear();
    lit.clear();
    for (ModelObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public boolean isSolved() {
    //    int row = 0;
    //    int col = 0;
    for (int row = 0; row < currPuzzle.getHeight(); row++) {
      for (int col = 0; col < currPuzzle.getWidth(); col++) {
        if (currPuzzle.getCellType(row, col) == CellType.CLUE) {
          if (!isClueSatisfied(row, col)) {
            return false;
          }
        } else if (currPuzzle.getCellType(row, col) == CellType.CORRIDOR) {
          if (isLamp(row, col)) {
            if (isLampIllegal(row, col)) {
              return false;
            }
          } else if (!isLit(row, col)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (!inRange(c, r)) {
      throw new IndexOutOfBoundsException();
    }
    if (currPuzzle.getCellType(r, c) == CellType.CLUE) {
      int numLight = currPuzzle.getClue(r, c);
      int currLight = 0;
      if (inRange(c, r + 1)) {
        if (currPuzzle.getCellType(r + 1, c) == CellType.CORRIDOR) {
          if (isLamp(r + 1, c)) {
            currLight += 1;
          }
        }
      }
      if (inRange(c, r - 1)) {
        if (currPuzzle.getCellType(r - 1, c) == CellType.CORRIDOR) {
          if (isLamp(r - 1, c)) {
            currLight += 1;
          }
        }
      }
      if (inRange(c + 1, r)) {
        if (currPuzzle.getCellType(r, c + 1) == CellType.CORRIDOR) {
          if (isLamp(r, c + 1)) {
            currLight += 1;
          }
        }
      }
      if (inRange(c - 1, r)) {
        if (currPuzzle.getCellType(r, c - 1) == CellType.CORRIDOR) {
          if (isLamp(r, c - 1)) {
            currLight += 1;
          }
        }
      }
      return currLight == numLight;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void addObserver(ModelObserver observer) {
    if (!observerList.contains(observer)) {
      observerList.add(observer);
    }
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observerList.remove(observer);
  }

  private boolean inRange(int c, int r) {
    return r < currPuzzle.getHeight() && c < currPuzzle.getWidth() && r >= 0 && c >= 0;
  }
}
