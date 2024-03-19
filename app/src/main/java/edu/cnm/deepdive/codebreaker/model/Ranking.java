package edu.cnm.deepdive.codebreaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.codebreaker.model.entity.User;

/** @noinspection ConstantValue*/
public class Ranking {

  @Expose
  private final User user = null;

  @Expose
  private final int poolSize = 0;

  @Expose
  @SerializedName("length")
  private final int codeLength = 0;

  @Expose
  private final int gameCount = 0;

  @Expose
  private final double avgGuessCount = 0;

  @Expose
  private final double avgDuration = 0;

  public User getUser() {
    return user;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public int getGameCount() {
    return gameCount;
  }

  public double getAvgGuessCount() {
    return avgGuessCount;
  }

  public double getAvgDuration() {
    return avgDuration;
  }
}
