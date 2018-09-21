import java.util.Arrays;

// Wrapper class of used/empty positions on the grid.
class Used {

  private final int size;
  private final boolean[] rows;  // Extended for general search
  private final boolean[] cols;
  private final boolean[] diag;
  private final boolean[] revD;

  public Used(int grid) {
    this.size = grid;
    this.rows = new boolean[grid];
    this.cols = new boolean[grid];
    this.diag = new boolean[2 * grid - 1];
    this.revD = new boolean[2 * grid - 1];
    return;
  }

  public void occupy(int row, int col) {
    this.mark(row, col, true);
    return;
  }

  public void vacate(int row, int col) {
    this.mark(row, col, false);
    return;
  }

  private void mark(int row, int col, boolean flag) {
    assert (row < this.size && col < this.size);
    this.rows[row] = flag;
    this.cols[col] = flag;
    this.diag[row + col] = flag;
    this.revD[row - col + this.size - 1] = flag;
    return;
  }

  public boolean isValid(int row, int col) {
    return !this.rows[row]
        && !this.cols[col]
        && !this.diag[row + col]
        && !this.revD[row - col + this.size - 1];
  }
  
  public boolean isValidRow(int row) {
    return !this.rows[row];
  }
  
  // Returns a deep copy of this.rows
  // Note that Arrays.copyOf() is native implemented and it takes advantage of
  // memory copy, so it executes like O(1) time.
  public boolean[] getUsedRows() {
    return Arrays.copyOf(this.rows, this.rows.length);
  }

}
