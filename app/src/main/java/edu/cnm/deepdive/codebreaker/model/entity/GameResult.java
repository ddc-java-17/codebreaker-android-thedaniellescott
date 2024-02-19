package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Duration;
import java.time.Instant;

@Entity(
    tableName = "game_result",
    indices = @Index(value = {"guess_count", "duration"})
)
public class GameResult {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_result_id")
  private long id;

  @ColumnInfo(index = true)
  @NonNull
  private Instant timestamp = Instant.now();

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @ColumnInfo(name = "guess_count", index = true)
  private int guessCount;

  @ColumnInfo(index = true)
  @NonNull
  private Duration duration = Duration.ZERO;

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
}
