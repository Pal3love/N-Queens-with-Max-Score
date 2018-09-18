import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RandomTester {
  
  private final NPolice np;
  private int count;
  
  public RandomTester() {
    this.count = 1;
    this.np = new NPolice();
    return;
  }
  
  public void test(int tests, int grid, int police, int scooters) {
    System.out.println("Test: total " + tests
        + ", grid size " + grid
        + ", police officers " + police
        + ", scooters " + scooters);
    System.out.println("----------------");
    double avgMillis = 0.0;
    for (int i = 0; i < tests; i++) {
      avgMillis += 1.0 * this.testOnce(grid, police, scooters) / tests;
    }
    System.out.println("----------------");
    System.out.println("Done. Average time: " + (int)avgMillis + " ms");
    return;
  }
  
  private int testOnce(int grid, int police, int scooters) {
    Random rand = new Random();
    List<Coord> scooterList = new ArrayList<>();
    for (int i = 0; i < scooters; i++) {
      scooterList.add(new Coord(rand.nextInt(grid), rand.nextInt(grid)));
    }
    System.out.print("Test " + this.count
        + ", ideal pts " + scooters
        + ", pts ");
    long tick = System.currentTimeMillis();
    ScoreAndMode result = this.np.search(grid, police, scooterList);
    long tock = System.currentTimeMillis();
    System.out.print(result.score == -1 ? "invalid configuration" : result.score);
    System.out.println(", strategy [" + result.mode + "]"
        + ", time " + (int)(tock - tick) + " ms");
    this.count++;
    return (int)(tock - tick);
  }

  public static void main(String[] args) {
    if (args.length < 4) {
      System.out.println("Usage: java RandomTester [test amount] [grid size] [police officers] [scooters]");
      System.out.println("Comment: scooters could be duplicate.");
      return;
    }
    RandomTester tester = new RandomTester();
    int tests = Integer.parseInt(args[0]);
    int grid = Integer.parseInt(args[1]);
    int police = Integer.parseInt(args[2]);
    int scooters = Integer.parseInt(args[3]);
    tester.test(tests, grid, police, scooters);
    return;
  }

}
