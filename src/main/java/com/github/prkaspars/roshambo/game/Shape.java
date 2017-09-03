package com.github.prkaspars.roshambo.game;

/**
 * Object represents hand shape.
 */
public class Shape {
  private String name;
  private byte value;
  private byte mask;

  public Shape(String name, byte value, byte mask) {
    this.name = name;
    this.value = value;
    this.mask = mask;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte getValue() {
    return value;
  }

  public void setValue(byte value) {
    this.value = value;
  }

  public byte getMask() {
    return mask;
  }

  public void setMask(byte mask) {
    this.mask = mask;
  }

  public int compareTo(Shape b) {
    return compare(this, b);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || !getClass().equals(obj.getClass())) {
      return false;
    }

    Shape that = (Shape) obj;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return value * mask;
  }

  /**
   * Compares its two arguments for strength.  Returns a negative integer,
   * zero, or a positive integer as the first argument is weaker, equal
   * to, or stronger than the second.
   *
   * @param a The first Shape to be compared.
   * @param b The second Shape to be compared.
   * @return a negative integer, zero, or a positive integer as the
   *         first argument is weaker than, equal to, or stronger than the
   *         second.
   */
  public static int compare(Shape a, Shape b) {
    if (a == null || b == null) {
      throw new NullPointerException("Shapes may not be null.");
    }

    if (a.getValue() == b.getValue()) {
      return 0;
    }

    return ((b.getValue() & a.getMask()) == 0) ? -1 : 1;
  }

  @Override
  public String toString() {
    return name + " " + value;
  }
}
