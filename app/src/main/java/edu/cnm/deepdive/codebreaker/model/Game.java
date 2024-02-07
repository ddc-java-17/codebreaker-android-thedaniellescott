package edu.cnm.deepdive.codebreaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {

  @Expose(serialize = false, deserialize = true)
  private final String id;

  @Expose
  private final String pool;

  @Expose
  private final int length;

  @Expose(serialize = false, deserialize = true)
  private final List<Guess> guesses;

  @Expose(serialize = false, deserialize = true)
  @SerializedName("created")
  private final Date start;

  public Game(String pool, int length) throws BadCodeLengthException, BadPoolException {
    if (length <= 0) {
      throw new BadCodeLengthException();
    }
    if (pool.isEmpty()) {
      throw new BadPoolException();
    }
    this.pool = pool;
    this.length = length;
    id = null;
    guesses = new LinkedList<>();
    start = null;
  }

  public Guess validate(String content)
      throws BadGuessLengthException, CharacterNotInPoolException {
    if (codePointLength(content) != length) {
      throw new BadGuessLengthException();
    }
    Set<Integer> poolCodePoints = getCodePointSet(pool);
    Set<Integer> contentCodePoints = getCodePointSet(content);
    if (!poolCodePoints.containsAll(contentCodePoints)) {
      throw new CharacterNotInPoolException();
    }
    return new Guess(content);
  }

  private Set<Integer> getCodePointSet(String src) {
    return src
        .codePoints()
        .boxed()
        .collect(Collectors.toSet());
  }

  public String getId() {
    return id;
  }

  public String getPool() {
    return pool;
  }

  public int getLength() {
    return length;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  public Date getStart() {
    return start;
  }

  public boolean isSolved() {
    List<Guess> guesses = getGuesses();
    return !guesses.isEmpty() && guesses.getLast().getCorrect() == length;
  }

  private int codePointLength(String src) {
    return (int) src
        .codePoints()
        .count();
  }

}
