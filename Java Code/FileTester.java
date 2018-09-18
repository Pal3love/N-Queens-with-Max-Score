import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileTester {
  
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java FileTester [input test file]");
      return;
    }
    FileTester.test(args[0]);
    return;
  }
  
  private static void test(String filePath) {
    try {
      Scanner in = new Scanner(new File(filePath));
      int grid = Integer.parseInt(in.nextLine().trim());
      int police = Integer.parseInt(in.nextLine().trim());
      in.nextLine();  // Skip the line of scooter number
      List<Coord> scooterList = new ArrayList<>();
      while (in.hasNextLine()) {
        String coordLine = in.nextLine().trim();
        if (coordLine.length() > 0) {  // In case of empty lines
          scooterList.add(FileTester.convert(coordLine));
        }
      }
      in.close();
      NPolice np = new NPolice();
      long tick = System.currentTimeMillis();
      ScoreAndMode result = np.search(grid, police, scooterList);
      long tock = System.currentTimeMillis();
      System.out.println("Result: " + result.score + " pts"
          + ", strategy [" + result.mode + "]"
          + ", time " + (int)(tock - tick) + " ms");
    } catch (FileNotFoundException e) {
      System.err.println("ERROR: File not found.");
      System.exit(1);
    }
    return;
  }
  
  // Converts a coordinate line into a Coord object.
  private static Coord convert(String ln) {
    assert (ln != null && ln.length() > 0);
    int comma = ln.indexOf(",");
    int row = Integer.parseInt(ln.substring(0, comma));
    int col = Integer.parseInt(ln.substring(comma + 1));
    return new Coord(row, col);
  }

}
