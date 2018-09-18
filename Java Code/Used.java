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
    assert (row < this.size && col < this.size);
    this.rows[row] = true;
    this.cols[col] = true;
    this.diag[row + col] = true;
    this.revD[row - col + this.size - 1] = true;
    return;
  }

  public void vacate(int row, int col) {
    assert (row < this.size && col < this.size);
    this.rows[row] = false;
    this.cols[col] = false;
    this.diag[row + col] = false;
    this.revD[row - col + this.size - 1] = false;
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
