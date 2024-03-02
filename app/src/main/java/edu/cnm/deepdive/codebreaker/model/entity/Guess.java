package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(
    tableName = "guess",
    indices = {
        @Index(value = "key", unique = true),
        @Index(value = {"game_id", "timestamp"})
    },
    foreignKeys = @ForeignKey(
        entity = Game.class,
        childColumns = "game_id",
        parentColumns = "game_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Guess {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "guess_id")
  private long id;

  @Expose(serialize = false, deserialize = true)
  @SerializedName("id")
  private String key;

  @SerializedName("text")
  @Expose
  private String content;

  @SerializedName("exactMatches")
  @Expose(serialize = false, deserialize = true)
  private int correct;

  @SerializedName("nearMatches")
  @Expose
  private int close;

  @SerializedName("created")
  @Expose(serialize = false, deserialize = true)
  private Date timestamp;

  @ColumnInfo(name = "game_id", index = true)
  private long gameId;

  @Ignore
  public Guess() {
    // provided for use by Room.
  }

  Guess (String content) {
    this.content = content;
    key = null;
    correct = 0;
    close = 0;
    timestamp = null;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getCorrect() {
    return correct;
  }

  public void setCorrect(int correct) {
    this.correct = correct;
  }

  public int getClose() {
    return close;
  }

  public void setClose(int close) {
    this.close = close;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public long getGameId() {
    return gameId;
  }

  public void setGameId(long gameId) {
    this.gameId = gameId;
  }
}
