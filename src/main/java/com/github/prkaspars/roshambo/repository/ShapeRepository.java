package com.github.prkaspars.roshambo.repository;

import static java.lang.Byte.parseByte;
import static java.util.stream.Collectors.toList;

import com.github.prkaspars.roshambo.game.Shape;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Repository class for accessing shape data source.
 */
public class ShapeRepository {
  private List<Shape> shapes;

  /**
   * Returns all Shape object.
   *
   * @return List of shapes.
   */
  public List<Shape> findAll() {
    return shapes;
  }

  /**
   * Finds and returns a shape by its value or null if shape cannot be found.
   *
   * @param value The identifier of shape.
   * @return The shape.
   */
  public Shape findByValue(byte value) {
    return shapes.stream()
        .filter(shape -> shape.getValue() == value)
        .findFirst()
        .orElse(null);
  }

  /**
   * Sets path to the data file.
   *
   * @param dataPath the path the the data file.
   * @throws IOException if an I/O error occurs.
   */
  public void setDataPath(Path dataPath) throws IOException {
    shapes = Files.lines(dataPath)
        .map(line -> line.split("\\s+"))
        .map(piece -> new Shape(piece[0], parseByte(piece[1], 2), parseByte(piece[2], 2)))
        .collect(toList());
  }
}
