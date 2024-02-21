package edu.cnm.deepdive.codebreaker.service;

import android.annotation.SuppressLint;
import android.util.Log;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class CodebreakerRepository {

  private static final String TAG = CodebreakerRepository.class.getSimpleName();

  private final CodebreakerServiceProxy proxy;
  private final GameResultRepository resultRepository;
  private final Scheduler scheduler;
  private Game game;

  @Inject
  CodebreakerRepository(CodebreakerServiceProxy proxy, GameResultRepository resultRepository) {
    this.proxy = proxy;
    this.resultRepository = resultRepository;
    scheduler = Schedulers.single();
  }

  public Single<Game> startGame(Game game) {
    return proxy
        .startGame(game)
        .doOnSuccess((g) -> this.game = g)
        .subscribeOn(scheduler);
  }

  @SuppressLint("CheckResult")
  public Single<Guess> submitGuess(String text) {
    return Single.fromSupplier(() -> game.validate(text))
        .flatMap((guess) -> proxy.submitGuess(game.getId(), guess))
        .doOnSuccess(guess -> game.getGuesses().add(guess))
        .doOnSuccess((guess) -> {
          if (game.isSolved()) {
            GameResult result = toResult(game);
            //noinspection ResultOfMethodCallIgnored
            resultRepository
                .add(result)
                .subscribe(
                    (r) -> {},
                    (throwable) ->
                        Log.e(TAG, throwable.getMessage(), throwable)
                );
          }
        })
        .subscribeOn(scheduler);

  }

  public Single<Game> getGame(String id) {
    return proxy
        .getGame(id)
        .doOnSuccess(this::setGame)
        .subscribeOn(scheduler);
  }

  public Game getGame() {
    return game;
  }

  private void setGame(Game game) {
    this.game = game;
  }

  private GameResult toResult(Game game) {
    GameResult result = new GameResult();
    result.setCodeLength(game.getLength());
    List<Guess> guesses = game.getGuesses();
    int size = guesses.size();
    result.setGuessCount(size);
    Date lastTimestamp = guesses.get(size - 1).getTimestamp();
    result.setTimestamp(lastTimestamp.toInstant());
    result.setDuration(Duration.ofMillis(
            lastTimestamp.getTime() - guesses.get(0).getTimestamp().getTime()
        )
    );
    return result;
  }

}
