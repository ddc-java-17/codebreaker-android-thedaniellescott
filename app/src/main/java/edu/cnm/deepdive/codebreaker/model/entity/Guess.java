package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(
    tableName = "guess"
)
public class Guess {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "guess_id")
  private long id;

  @Expose(serialize = false, deserialize = true)
  @SerializedName("id")
  private final String key;

  @SerializedName("text")
  @Expose
  private final String content;

  @SerializedName("exactMatches")
  @Expose(serialize = false, deserialize = true)
  private final int correct;

  @SerializedName("nearMatches")
  @Expose
  private final int close;

  @SerializedName("created")
  @Expose(serialize = false, deserialize = true)
  private final Date timestamp;

  private long gameId;

  Guess (String content) {
    this.content = content;
    key = null;
    correct = 0;
    close = 0;
    timestamp = null;
  }

  public String getKey() {
    return key;
  }

  public String getContent() {
    return content;
  }

  public int getCorrect() {
    return correct;
  }

  public int getClose() {
    return close;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}
