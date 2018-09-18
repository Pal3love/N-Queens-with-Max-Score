import java.util.List;

/*
 *  Intelligent search. When scooters and police officers are sparse, a
 *  dual-search strategy would be adopted, and when scooters and officers are
 *  dense, the classic search would be used. Pruning occurs in both strategies.
 */
public class NPolice {

  private static final int INVALID = -1;
  private static final double SCOOTER_RATE = 1.75;
  private static final double POLICE_LOW = 0.16;
  private static final double POLICE_HIGH = 1.00;

  /**
   * @param grid Grid size
   * @param police Police officers
   * @param scooterList List of all scooter routes as coordinates
   * @return The maximum score based on grid & police configuration and search strategy.
   */
  public ScoreAndMode search(int grid, int police, List<Coord> scooterList) {
    // Sanity check
    if (grid < 1 || police > grid || scooterList == null) {
      return new ScoreAndMode(NPolice.INVALID, "invalid");
    }
    // Initialization
    Path path = new Path(grid, scooterList);
    // Selection of better search strategy: sparse scooters or dense
    int criticalScooters = (int) (grid * NPolice.SCOOTER_RATE);
    double policeDensity = (double) police / (double) grid;
    if (scooterList.size() <= criticalScooters
        && policeDensity >= NPolice.POLICE_LOW
        && policeDensity <= NPolice.POLICE_HIGH) {
      this.dualSearch(grid, police, 0, path, 0, 0);
      return new ScoreAndMode(path.maxPts, "dual search");
    } else {
      this.classicSearch(grid, police, path, grid - police, 0, 0);
    }
    return new ScoreAndMode(path.maxPts, "classic search");
  }

  /* -- Classic N-Queen Search Strategy -- */
  // Generalized n-queen search in case of police < grid.
  // When scooters are dense, this search strategy works faster.

  /**
   * Mutator method of Path path.
   * @param grid Grid size
   * @param police Remaining police officers to arrange
   * @param path Global Path wrapper
   * @param extra Extra rows/cols when police < grid, i.e. grid - police
   * @param row Current row TO BE tried
   * @param pts Accumulated points so far, NOT INCLUDING current try yet
   */
  private void classicSearch(int grid, int police, Path path, int extra, int row, int pts) {
    assert (row <= grid);
    // Base case
    if (path.getIdealPts(pts, police) <= path.maxPts) {  // Pruning
      return;
    } else if (police < 1) {
      path.maxPts = Math.max(pts, path.maxPts);  // Update max
      return;
    }
    // Backtracking
    for (int offset = 0; offset <= extra; offset++) {
      int realRow = row + offset;  // Original row + offset
      for (int col = 0; col < grid; col++) {
        if (path.locations.isValid(realRow, col)) {
          path.locations.occupy(realRow, col);
          int updatedPts = pts + path.scooterMap[realRow][col];
          this.classicSearch(grid, police - 1, path, extra - offset, realRow + 1, updatedPts);
          path.locations.vacate(realRow, col);
        }
      }
    }
    return;
  }

  /* -- Dual-Search Strategy -- */
  // This method performs a dual-search hierarchy, in which step 1 is to occupy
  // as many scooter cells with police officers as we can to obtain the highest
  // score, and step 2 is to complete the search with remaining officers to
  // find out whether there would be A solution based on the police arrangement
  // in step 1.
  //
  // During step 1, we try each scooter cell from high to low based on its
  // points/frequency, hence the monotonicity of search paths cost would be
  // maintained, which is an important factor of pruning correctness. Before
  // arranging an officer, we first calculate the ideal score regarding current
  // search path, which is obtained points so far + the sum of max scooter on
  // remaining rows, and pruning occurs when such an ideal score is still lower
  // than current max, which means that we've already found a better solution
  // so we don't have to continue current search anymore.
  //
  // During step 2, we run a generalized n-queens algorithm in DFS, in which
  // the grid is pre-arranged with a part of queens and the number of queens
  // could be less than the grid size. Iff no solution found, it falls back to
  // step 1 for another search path; iff there is still no solution found after
  // each step 1 path tried, we don't have to run step 2 anymore as in this
  // case the max score is 0. In other words, neither does step 2 calculates
  // the score, nor it influences the score. To optimize time complexity of
  // cell validation, we maintain couple of boolean arrays to perform O(1)
  // operation with O(n) extra space as trade off.
  //
  // Iff the police officers are already used up during step 1, we also don't
  // have to go step 2. In this case officers < scooter cells.

  /**
   * Step 1: accumulate as much score as we can
   *
   * @param grid Grid size
   * @param police Remaining police officers to arrange
   * @param usedPolice Amount of already arranged police
   * @param path Global Path wrapper
   * @param sIdx Current Scooter TO BE PROCESSED, starting from 0
   * @param pts Accumulated score so far, NOT INCLUDING Scooter[sIdx] yet
   */
  private void dualSearch(int grid, int police, int usedPolice, Path path, int sIdx, int pts) {
    // Base case
    // Police officers are used up ahead of scooters, or
    // All scooters are used up && a valid solution is found
    if (path.getIdealPts(pts, police) <= path.maxPts) {  // Pruning
      return;  // Include the case when all scooters are used up
    } else if (police < 1 || sIdx == path.scooters.length) {
      int extra = grid - usedPolice - police;
      if (this.validate(grid, police, path, extra, 0)) {
        path.maxPts = Math.max(pts, path.maxPts);  // Update max
      }
      return;
    }
    // Backtracking: occupy a scooter or not, from high score to low
    for (int i = sIdx; i < path.scooters.length; i++) {
      Scooter s = path.scooters[i];
      if (path.locations.isValid(s.location.row, s.location.col)) {
        path.locations.occupy(s.location.row, s.location.col);
        this.dualSearch(grid, police - 1, usedPolice + 1, path, i + 1, pts + s.frequency);
        path.locations.vacate(s.location.row, s.location.col);
      }
    }
    // Decide not to occupy any scooter
    this.dualSearch(grid, police, usedPolice, path, path.scooters.length, pts);
    return;
  }

  /**
   * Step 2: modified n-queen search for current police occupation.
   * 
   * @param grid Grid size
   * @param police Remaining police officers to arrange
   * @param path Global Path wrapper
   * @param extra Extra rows/cols when police < grid, i.e. grid - police
   * @param row Current row TO BE tried
   * @return Whether there is a valid solution.
   */
  private boolean validate(int grid, int police, Path path, int extra, int row) {
    assert (row <= grid);
    // Base case
    if (police < 1) {
      return true;  // We successfully find a solution!
    } else if (!path.locations.isValidRow(row)) {  // Skip the row in step 1
      return this.validate(grid, police, path, extra, row + 1);
    }
    // Generalized backtracking in case of police < grid
    for (int offset = 0; offset <= extra; offset++) {
      int realRow = row + offset; // Original row + offset
      for (int col = 0; col < grid; col++) {
        if (path.locations.isValid(realRow, col)) {
          path.locations.occupy(realRow, col);
          if (this.validate(grid, police - 1, path, extra - offset, realRow + 1)) {
            path.locations.vacate(realRow, col);
            return true;
          }
          path.locations.vacate(realRow, col);
        }
      }
    }
    return false;
  }

}
