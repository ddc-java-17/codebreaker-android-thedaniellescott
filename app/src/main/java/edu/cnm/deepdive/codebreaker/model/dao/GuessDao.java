package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.Iterator;
import java.util.List;

@Dao
public interface GuessDao {

  @Insert
  Single<List<Long>> insert(List<Guess> guesses);

  default Completable insertAndUpdate(Game game) {
    long gameId = game.getId();
    List<Guess> guesses = game.getGuesses();
    guesses.forEach((guess) -> guess.setGameId(gameId));
    return insert(guesses)
        .doOnSuccess((ids) -> {
          Iterator<Guess> guessIterator = guesses.iterator();
          Iterator<Long> idIterator = ids.iterator();
          while (guessIterator.hasNext() && idIterator.hasNext()) {
            guessIterator.next().setId(idIterator.next());
          }
        })
        .ignoreElement();
  }

}
