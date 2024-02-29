package edu.cnm.deepdive.codebreaker.service;

import android.annotation.SuppressLint;
import android.util.Log;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
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
  private final GameResultRepository resultRepository;
  private final UserRepository userRepository;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Game game;

  @Inject
  CodebreakerRepository(CodebreakerServiceProxy proxy, GameResultRepository resultRepository,
      UserRepository userRepository, GoogleSignInService signInService) {
    this.proxy = proxy;
    this.resultRepository = resultRepository;
    this.userRepository = userRepository;
    this.signInService = signInService;
    scheduler = Schedulers.single();
  }

  public Single<Game> startGame(Game game) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.startGame(game, token))
        .doOnSuccess(this::setGame);
  }

  @SuppressLint("CheckResult")
  public Single<Guess> submitGuess(String text) {
    return Single.fromSupplier(() -> game.validate(text))
        .flatMap((guess) -> proxy.submitGuess(game.getId(), guess))
        .flatMap((guess) -> {
          game.getGuesses().add(guess);
          return game.isSolved()
              ? userRepository
              .getCurrentUser()
              .map((user) -> toResult(game, user))
              .flatMap(resultRepository::add)
              .map((result) -> guess)
              : Single.just(guess);
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
