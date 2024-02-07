package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

public class CodebreakerRepository {

  private final CodebreakerServiceProxy proxy;
  private final Scheduler scheduler;
  private Game game;

  @Inject
  CodebreakerRepository(CodebreakerServiceProxy proxy) {
    this.proxy = proxy;
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
        .flatMap((guess) -> proxy.submitGuess(game.getId(), guess))
        .doOnSuccess(guess -> game.getGuesses().add(guess))
        .subscribeOn(scheduler);

  }

  public Single<Game> getGame(String id) {
    return proxy
        .getGame(id)
        .subscribeOn(scheduler);
  }

  public Game getGame() {
    return game;
  }

}
