package com.github.prkaspars.roshambo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.prkaspars.roshambo.game.Shape;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class ShapeRepositoryTest {
  private ShapeRepository shapeRepository;

  @Before
  public void setUp() throws Exception {
    shapeRepository = new ShapeRepository();
    shapeRepository.setDataPath(Paths.get(getClass().getResource("/test_shapes.dat").toURI()));
  }

  @Test
  public void findAll() throws Exception {
    List<Shape> shapeList = shapeRepository.findAll();
    assertEquals("List should containt three items", 3, shapeList.size());
    List<String> nameList = shapeList.stream().map(Shape::getName).collect(Collectors.toList());
    assertTrue("List should contain rock", nameList.contains("Rock"));
    assertTrue("List should contain paper", nameList.contains("Paper"));
    assertTrue("List should contain scissors", nameList.contains("Scissors"));
  }

  @Test
  public void findByValue() throws Exception {
    assertNotNull("Scissors must not be null", shapeRepository.findByValue((byte) 4));
    assertNotNull("Paper must not be null", shapeRepository.findByValue((byte) 2));
    assertNotNull("Rock must not be null", shapeRepository.findByValue((byte) 1));
    assertNull("Rock must not be null", shapeRepository.findByValue((byte) 8));
  }
}
