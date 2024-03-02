package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.codebreaker.model.BadCodeLengthException;
import edu.cnm.deepdive.codebreaker.model.BadGuessLengthException;
import edu.cnm.deepdive.codebreaker.model.BadPoolException;
import edu.cnm.deepdive.codebreaker.model.CharacterNotInPoolException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(
    tableName = "game",
    indices = @Index(value = "key", unique = true),
    foreignKeys = @ForeignKey(entity = User.class,
        childColumns = "user_id",
        parentColumns = "user_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Game {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_id")
  private int id;

  @Expose(serialize = false, deserialize = true)
  @SerializedName("id")
  private String key;

  @Expose
  private String pool;

  @Expose
  @ColumnInfo(index = true)
  private int length;

  @Expose(serialize = false, deserialize = true)
  @SerializedName("created")
  private Date start;

  @ColumnInfo(name = "user_id", index = true)
  private long userId;

  @Expose(serialize = false, deserialize = true)
  @Ignore
  private final List<Guess> guesses;

  { // Initialization statement
    guesses = new LinkedList<>();
  }

  public Game() {
    // Included for use by Room.
  }

  @Ignore
  public Game(String pool, int length) throws BadCodeLengthException, BadPoolException {
    if (length <= 0) {
      throw new BadCodeLengthException();
    }
    if (pool.isEmpty()) {
      throw new BadPoolException();
    }
    this.pool = pool;
    this.length = length;
    key = null;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getPool() {
    return pool;
  }

  public void setPool(String pool) {
    this.pool = pool;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  public boolean isSolved() {
    List<Guess> guesses = getGuesses();
    return !guesses.isEmpty() && guesses.get(guesses.size() - 1).getCorrect() == length;
  }

  private Set<Integer> getCodePointSet(String src) {
    return src
        .codePoints()
        .boxed()
        .collect(Collectors.toSet());
  }

  private int codePointLength(String src) {
    return (int) src
        .codePoints()
        .count();
  }

}
