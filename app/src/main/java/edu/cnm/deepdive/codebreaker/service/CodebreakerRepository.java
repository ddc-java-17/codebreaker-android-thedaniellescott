package edu.cnm.deepdive.codebreaker.service;

import androidx.annotation.NonNull;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import edu.cnm.deepdive.codebreaker.model.entity.User;
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
  private final UserRepository userRepository;
  private final GameDao gameDao;
  private final GuessDao guessDao;
  private final Scheduler scheduler;
  private Game game;

  @Inject
  CodebreakerRepository(CodebreakerServiceProxy proxy, UserRepository userRepository, GameDao gameDao, GuessDao guessDao) {
    this.proxy = proxy;
    this.userRepository = userRepository;
    this.gameDao = gameDao;
    this.guessDao = guessDao;
    scheduler = Schedulers.single();
  }

  public Single<Game> startGame(Game game) {
    return proxy
        .startGame(game)
        .doOnSuccess((g) -> this.game = g)
        .subscribeOn(scheduler);
  }

  public Single<Guess> submitGuess(String text) {
    return Single.fromSupplier(() -> game.validate(text))
        .flatMap((guess) -> proxy.submitGuess(game.getKey(), guess))
        .flatMap((guess) -> {
          game.getGuesses().add(guess);
          return game.isSolved()
              ? persist(guess)
              : Single.just(guess);
        })
        .subscribeOn(scheduler);

  }

  @NonNull
  private Single<Guess> persist(Guess guess) {
    return userRepository
        .getCurrentUser()
        .map((user) -> {
          game.setUserId(user.getId());
          return game;
        })
        .flatMap(gameDao::insertAndUpdate)
        .flatMapCompletable(guessDao::insertAndUpdate)
        .andThen(Single.just(guess));
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

  private GameResult toResult(Game game, User user) {
    GameResult result = new GameResult();
    result.setUserId(user.getId());
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
