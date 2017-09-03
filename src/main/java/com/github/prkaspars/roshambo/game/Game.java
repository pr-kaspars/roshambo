package com.github.prkaspars.roshambo.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Object represents game.
 */
public class Game {
  private UUID uuid;
  private List<Participant> participants = new ArrayList<>(2);

  /**
   * Creates an instance of Game with an empty participant list and an unique id.
   */
  public Game(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Adds a participant to the list.
   *
   * @param participant The participant.
   */
  public void addParticipant(Participant participant) {
    participants.add(participant);
  }

  public List<Participant> getParticipants() {
    return participants;
  }

  /**
   * Returns true if all participants are ready to play.
   *
   * @return Participant readiness.
   */
  public boolean isReady() {
    return participants.stream().filter(Participant::isReady).count() == participants.size();
  }

  /**
   * Returns the winner of of the game or null if players played draw.
   *
   * @return The winner.
   */
  public Participant getWinner() {
    long distinctCount = participants.stream()
        .map(Participant::getShape)
        .distinct()
        .count();

    if (distinctCount != participants.size()) {
      return null;
    }

    Comparator<Participant> comparator = (a, b) -> a.compareTo(b);
    return participants.stream()
        .sorted(comparator.reversed())
        .findFirst()
        .orElseGet(null);
  }
}
