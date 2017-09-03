package com.github.prkaspars.roshambo.game;

import java.util.UUID;

/**
 * An implementation of Participant which behaviour can be controlled.
 */
public class HumanParticipant extends Participant {
  private Shape shape;
  private boolean ready;

  /**
   * Constructs new participant instance with given identifier.
   *
   * @param uuid The identifier.
   */
  public HumanParticipant(UUID uuid) {
    super(uuid);
  }

  @Override
  public Shape getShape() {
    return shape;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  public void setReady(boolean ready) {
    this.ready = ready;
  }
}
