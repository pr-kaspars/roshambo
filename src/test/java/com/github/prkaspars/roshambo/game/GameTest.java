package com.github.prkaspars.roshambo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
  private Game game;
  private Shape rock;
  private Shape paper;
  private Shape scissors;

  @Before
  public void setUp() throws Exception {
    game = new Game(UUID.randomUUID());
    rock = new Shape("Rock", (byte) 0b00000001, (byte) 0b00000100);
    paper = new Shape("Paper", (byte) 0b00000010, (byte) 0b00000001);
    scissors = new Shape("Scissors", (byte) 0b00000100, (byte) 0b00000010);
  }

  @Test
  public void isReadyReturnsTrue() throws Exception {
    HumanParticipant p1 = new HumanParticipant(UUID.randomUUID());
    p1.setReady(true);
    game.addParticipant(p1);

    HumanParticipant p2 = new HumanParticipant(UUID.randomUUID());
    p2.setReady(true);
    game.addParticipant(p2);

    assertTrue("Game participants are ready", game.isReady());
  }

  @Test
  public void isReadyReturnsFalse() throws Exception {
    HumanParticipant p1 = new HumanParticipant(UUID.randomUUID());
    p1.setReady(false);
    game.addParticipant(p1);

    HumanParticipant p2 = new HumanParticipant(UUID.randomUUID());
    p2.setReady(true);
    game.addParticipant(p2);

    assertFalse("Game participants are not ready", game.isReady());
  }

  @Test
  public void getWinnerReturnsWinnerA() throws Exception {
    HumanParticipant p1 = new HumanParticipant(UUID.randomUUID());
    p1.setShape(rock);
    game.addParticipant(p1);

    HumanParticipant p2 = new HumanParticipant(UUID.randomUUID());
    p2.setShape(scissors);
    game.addParticipant(p2);

    Participant winner = game.getWinner();
    assertNotNull("Winner must not be null", winner);
    assertEquals("Shape must be rock", rock, winner.getShape());
  }

  @Test
  public void getWinnerReturnsWinnerA1() throws Exception {
    HumanParticipant p1 = new HumanParticipant(UUID.randomUUID());
    p1.setShape(paper);
    game.addParticipant(p1);

    HumanParticipant p2 = new HumanParticipant(UUID.randomUUID());
    p2.setShape(rock);
    game.addParticipant(p2);

    Participant winner = game.getWinner();
    assertNotNull("Winner must not be null", winner);
    assertEquals("Shape must be paper", paper, winner.getShape());
  }

  @Test
  public void getWinnerReturnsWinnerB() throws Exception {
    HumanParticipant p1 = new HumanParticipant(UUID.randomUUID());
    p1.setShape(rock);
    game.addParticipant(p1);

    HumanParticipant p2 = new HumanParticipant(UUID.randomUUID());
    p2.setShape(paper);
    game.addParticipant(p2);

    Participant winner = game.getWinner();
    assertNotNull("Winner must not be null", winner);
    assertEquals("Shape must be rock", paper, winner.getShape());
  }

  @Test
  public void getWinnerReturnsNull() throws Exception {
    HumanParticipant p1 = new HumanParticipant(UUID.randomUUID());
    p1.setShape(rock);
    game.addParticipant(p1);

    HumanParticipant p2 = new HumanParticipant(UUID.randomUUID());
    p2.setShape(rock);
    game.addParticipant(p2);

    Participant winner = game.getWinner();
    assertNull("Winner must be null", winner);
  }
}
