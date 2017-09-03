package com.github.prkaspars.roshambo.game;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * A participant implementation that returns randomly generated shapes.
 */
public class AndroidParticipant extends Participant {
  private Supplier<Shape> shapeSupplier;
  private Shape shape;

  /**
   * Constructs new participant instance with given identifier.
   *
   * @param uuid The identifier.
   */
  public AndroidParticipant(UUID uuid) {
    super(uuid);
  }

  @Override
  public Shape getShape() {
    if (shape == null) {
      shape = shapeSupplier.get();
    }

    return shape;
  }

  @Override
  public boolean isReady() {
    return true;
  }

  public void setShapeSupplier(Supplier<Shape> shapeSupplier) {
    this.shapeSupplier = shapeSupplier;
  }
}
