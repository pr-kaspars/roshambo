package com.github.prkaspars.roshambo.server;

import static java.lang.Byte.parseByte;
import static java.lang.String.format;
import static java.nio.file.Files.probeContentType;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.joining;

import com.github.prkaspars.roshambo.game.AndroidParticipant;
import com.github.prkaspars.roshambo.game.HumanParticipant;
import com.github.prkaspars.roshambo.game.Game;
import com.github.prkaspars.roshambo.game.Participant;
import com.github.prkaspars.roshambo.game.Shape;
import com.github.prkaspars.roshambo.repository.ShapeRepository;
import com.github.prkaspars.roshambo.server.HttpRequest.Method;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Main game request handler.
 */
public class RequestHandler {
  private Random random = new Random();
  private ShapeRepository shapeRepository;

  @RequestMapping(path = "/game.*", method = Method.POST)
  public void createGame(HttpRequest request, HttpResponse response) throws IOException {
    Game game = new Game(randomUUID());

    if (request.containsParameter("human")) {
      byte shapeValue = parseByte(request.getParameter("shapeValue"));

      HumanParticipant participant = new HumanParticipant(randomUUID());
      participant.setShape(shapeRepository.findByValue(shapeValue));
      participant.setReady(true);
      game.addParticipant(participant);

      AndroidParticipant participant2 = new AndroidParticipant(randomUUID());
      participant2.setShapeSupplier(() -> {
        List<Shape> shapes = shapeRepository.findAll();
        return shapes.get(random.nextInt(shapes.size()));
      });
      game.addParticipant(participant2);

    } else {
      AndroidParticipant participant1 = new AndroidParticipant(randomUUID());
      participant1.setShapeSupplier(() -> {
        List<Shape> shapes = shapeRepository.findAll();
        return shapes.get(random.nextInt(shapes.size()));
      });
      game.addParticipant(participant1);

      AndroidParticipant participant2 = new AndroidParticipant(randomUUID());
      participant2.setShapeSupplier(() -> {
        List<Shape> shapes = shapeRepository.findAll();
        return shapes.get(random.nextInt(shapes.size()));
      });
      game.addParticipant(participant2);
    }

    StringBuilder body = new StringBuilder();
    if (game.isReady()) {
      Participant participant = game.getWinner();
      byte value = (participant != null) ? participant.getShape().getValue() : 0;
      body.append(value);
    }

    String values = game.getParticipants().stream()
        .map(Participant::getShape)
        .map(Shape::getValue)
        .map(String::valueOf)
        .collect(joining("\r\n"));

    response
        .putHeader("Content-Type", "text/roshambo")
        .putHeader("Connection", "close")
        .body(body.append("\r\n").append(values).toString().getBytes());
  }

  @RequestMapping(path = "/shapes", method = Method.GET)
  public void listShapes(HttpRequest request, HttpResponse response) {
    String shapes = shapeRepository.findAll().stream()
        .map(shape -> format("%s %s", shape.getName(), shape.getValue()))
        .collect(joining("\n"));

    response
        .putHeader("Content-Type", "text/roshambo")
        .putHeader("Connection", "close")
        .body(shapes.getBytes());
  }

  public void setShapeRepository(ShapeRepository shapeRepository) {
    this.shapeRepository = shapeRepository;
  }
}
