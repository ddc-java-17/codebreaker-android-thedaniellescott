package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Duration;
import java.time.Instant;

@DatabaseView(
    viewName = "game_result",
    value = "SELECT\n"
        + "    gm.game_id AS game_result_id,\n"
        + "    gm.user_id,\n"
        + "    gm.length AS code_length,\n"
        + "    COUNT(*) as guess_count,\n"
        + "    MAX(gs.timestamp) - MIN(gs.timestamp) AS duration,\n"
        + "    MAX(gs.timestamp) AS timestamp\n"
        + " FROM\n"
        + "   game AS gm\n"
        + "   JOIN guess AS gs\n"
        + "      ON gs.game_id = gm.game_id\n"
        + "  GROUP BY\n"
        + "    gm.game_id\n"
)
public class GameResult {

  @ColumnInfo(name = "game_result_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Instant timestamp = Instant.now();

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @ColumnInfo(name = "guess_count", index = true)
  private int guessCount;

  @NonNull
  @ColumnInfo(index = true)
  private Duration duration = Duration.ZERO;

  @ColumnInfo(name = "user_id", index = true)
  private long userId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@NonNull Instant timestamp) {
    this.timestamp = timestamp;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  public int getGuessCount() {
    return guessCount;
  }

  public void setGuessCount(int guessCount) {
    this.guessCount = guessCount;
  }

  @NonNull
  public Duration getDuration() {
    return duration;
  }

  public void setDuration(@NonNull Duration duration) {
    this.duration = duration;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }
}
