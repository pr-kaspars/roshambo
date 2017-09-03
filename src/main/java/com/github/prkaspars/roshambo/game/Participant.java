package com.github.prkaspars.roshambo.game;

import java.util.UUID;

/**
 * Abstract game participant.
 */
public abstract class Participant {
  private UUID uuid;

  /**
   * Constructs new Participant with given identifier.
   *
   * @param uuid Participant identifier.
   */
  public Participant(UUID uuid) {
    this.uuid = uuid;
  }

  public UUID getUuid() {
    return uuid;
  }

  public abstract Shape getShape();

  public abstract boolean isReady();

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || !getClass().equals(obj.getClass())) {
      return false;
    }

    Participant that = (Participant) obj;
    return uuid != null && uuid.equals(that.uuid);
  }

  @Override
  public int hashCode() {
    return 12 * uuid.toString().hashCode();
  }

  /**
   * Compares a Participant to the instance.
   *
   * @param b The Participant to be compared.
   * @return a negative integer, zero, or a positive integer as the shape of the
   *         instance is weaker than, equal to, or stronger than the argument.
   */
  public int compareTo(Participant b) {
    return compare(this, b);
  }

  /**
   * Compares two participant shapes.
   *
   * @param a The first Participant to be compared.
   * @param b The second Participant to be compared.
   * @return a negative integer, zero, or a positive integer as the shape of the
   *         first argument is weaker than, equal to, or stronger than the
   *         second.
   */
  public static int compare(Participant a, Participant b) {
    return Shape.compare(a.getShape(), b.getShape());
  }
}
