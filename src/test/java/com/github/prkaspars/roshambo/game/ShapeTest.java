package com.github.prkaspars.roshambo.game;

import static com.github.prkaspars.roshambo.game.Shape.compare;
import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ShapeTest {
  private Shape rock;
  private Shape paper;
  private Shape scissors;

  @Before
  public void setUp() {
    rock = new Shape("Rock", (byte) 0b00000001, (byte) 0b00000100);
    paper = new Shape("Paper", (byte) 0b00000010, (byte) 0b00000001);
    scissors = new Shape("Scissors", (byte) 0b00000100, (byte) 0b00000010);
  }

  @Test
  public void compareEqualShapes() throws Exception {
    assertEquals("Paper must be equal to paper", 0, compare(paper, paper));
    assertEquals("Rock must be equal to rock", 0, compare(rock, rock));
    assertEquals("Scissors must be equal to scissors", 0, compare(scissors, scissors));
  }

  @Test
  public void compareWeakerShapes() throws Exception {
    assertEquals("Paper must beat rock", 1, compare(paper, rock));
    assertEquals("Rock must beat scissors", 1, compare(rock, scissors));
    assertEquals("Scissors must beat paper", 1, compare(scissors, paper));
  }

  @Test
  public void compareStrongerShapes() throws Exception {
    assertEquals("Scissors must beat paper", -1, compare(paper, scissors));
    assertEquals("Paper must beat rock", -1, compare(rock, paper));
    assertEquals("Rock must beat scissors", -1, compare(scissors, rock));
  }
}
