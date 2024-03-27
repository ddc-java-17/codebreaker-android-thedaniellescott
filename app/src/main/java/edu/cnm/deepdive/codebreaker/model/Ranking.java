package edu.cnm.deepdive.codebreaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.codebreaker.model.entity.User;

/** @noinspection ConstantValue*/
public class Ranking {

  @Expose
  private final User user;

  @Expose
  private final int poolSize;

  @Expose
  @SerializedName("length")
  private final int codeLength;

  @Expose
  private final int gameCount;

  @Expose
  private final double avgGuessCount;

  @Expose
  private final double avgDuration;

  public Ranking(User user, int poolSize, int length, int gameCount, double avgGuessCount,
      double avgDuration) {
    this.user = user;
    this.poolSize = poolSize;
    codeLength = length;
    this.gameCount = gameCount;
    this.avgGuessCount = avgGuessCount;
    this.avgDuration = avgDuration;
  }

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
