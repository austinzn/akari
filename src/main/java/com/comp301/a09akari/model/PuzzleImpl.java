package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {
  private final int[][] board;

  public PuzzleImpl(int[][] board) {
    this.board = board;
  }

  @Override
  public int getWidth() {
    if (board.length == 0) {
      return 0;
    } else {
      return board[0].length;
    }
  }

  @Override
  public int getHeight() {
    return board.length;
  }

  @Override
  public CellType getCellType(int r, int c) {
    if (c > this.getWidth() || r > this.getHeight() || r < 0 || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    int cellNum = board[r][c];
    if (cellNum < 5 && cellNum >= 0) {
      return CellType.CLUE;
    } else if (cellNum == 5) {
      return CellType.WALL;
    } else if (cellNum == 6) {
      return CellType.CORRIDOR;
    } else {
      return null;
    }
  }

  @Override
  public int getClue(int r, int c) {
    if (c > this.getWidth() || r > this.getHeight() || r < 0 || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    int cellNum = board[r][c];
    if (this.getCellType(r, c) == CellType.CLUE) {
      return cellNum;
    } else {
      throw new IllegalArgumentException();
    }
  }
}
