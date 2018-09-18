// Wrapper class of location on the grid, implementing hashCode() and equals().
class Coord {

  public int row;
  public int col;

  public Coord(int row, int col) {
    this.row = row;
    this.col = col;
    return;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + col;
    result = prime * result + row;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Coord other = (Coord) obj;
    if (col != other.col)
      return false;
    if (row != other.row)
      return false;
    return true;
  }
  
}
