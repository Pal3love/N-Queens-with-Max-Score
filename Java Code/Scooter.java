// Wrapper class of a scooter coordinate with its frequency/points.
// Implements Comparable interface with higher score higher priority, so that
// its natural ordering is descending order.

class Scooter implements Comparable<Scooter> {

  public Coord location;
  public int frequency;
  
  public Scooter(Coord location, int frequency) {
    this.location = location;
    this.frequency = frequency;
    return;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + frequency;
    result = prime * result + ((location == null) ? 0 : location.hashCode());
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
    Scooter other = (Scooter) obj;
    if (frequency != other.frequency)
      return false;
    if (location == null) {
      if (other.location != null)
        return false;
    } else if (!location.equals(other.location))
      return false;
    return true;
  }

  @Override
  public int compareTo(Scooter o) {
    if (o == null) {
      throw new NullPointerException("Cannot compare a object with null");
    } else if (this.frequency == o.frequency) {
      return 0;
    } else {
      return this.frequency > o.frequency ? -1 : 1;
    }
  }
  
}
