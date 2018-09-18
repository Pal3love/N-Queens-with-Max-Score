import java.util.Arrays;
import java.util.List;

// Wrapper class of search path.
class Path {

  public final int[][] scooterMap;  // index as coord, value as frequency/score
  public final Scooter[] scooters;  // Desc-sorted array of Scooter objects
  public final Used locations;  // Wrapper of used cells
  public int maxPts;  // Maximum points

  public Path(int grid, List<Coord> scooterRoutes) {
    this.scooterMap = this.list2map(scooterRoutes, grid);
    this.scooters = this.map2sorted(this.scooterMap);
    this.locations = new Used(grid);
    this.maxPts = 0;
    return;
  }
  
  // Calculates current ideal score based on the obtained score so far and sum
  // of maximum scooters on the remaining rows to be discovered. Considered
  // that the limit of grid size is < 100, which is approximately constant, it
  // does not matter that some calculations have O(n) time, especially for
  // pruning calculations.
  public int getIdealPts(int pts, int remainingPolice) {
    boolean[] usedRows = this.locations.getUsedRows();
    for (Scooter s : this.scooters) {  // Try in descending order
      if (remainingPolice < 1) {
        break;
      } else if (!usedRows[s.location.row] && this.locations.isValid(s.location.row, s.location.col)) {
        usedRows[s.location.row] = true;  // Occupy the row locally
        pts += s.frequency;
        remainingPolice--;
      }
    }
    return pts;
  }
  
  // Converts the given list of scooter routes into a 2D array map with index
  // as coordinate <row, col> and value as frequency/score.
  private int[][] list2map(List<Coord> routes, int grid) {
    assert (routes != null);
    int[][] map = new int[grid][grid];
    for (Coord c : routes) {
      assert (c != null);
      map[c.row][c.col]++;
    }
    return map;
  }
  
  // Converts the given map of coordinate & frequency pair of scooters into an
  // descending-sorted array of wrapper Scooter objects.
  private Scooter[] map2sorted(int[][] map) {
    assert (map != null);
    Scooter[] sorted = new Scooter[this.countIndividualScooters(map)];
    int index = 0;
    for (int row = 0; row < map.length; row++) {
      for (int col = 0; col < map[row].length; col++) {
        if (map[row][col] > 0) {
          sorted[index++] = new Scooter(new Coord(row, col), map[row][col]);
        }
      }
    }
    Arrays.sort(sorted);  // Descending order due to its compareTo() 
    return sorted;
  }
  
  private int countIndividualScooters(int[][] map) {
    assert (map != null);
    int count = 0;
    for (int row = 0; row < map.length; row++) {
      for (int col = 0; col < map[row].length; col++) {
        if (map[row][col] > 0) {
          count++;
        }
      }
    }
    return count;
  }

}
