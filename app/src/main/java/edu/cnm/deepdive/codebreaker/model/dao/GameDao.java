package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameDao {

  @Insert
  Single<Long> insert(Game game);

  @Query("DELETE FROM game")
  Completable truncate();

  default Single<Game> insertAndUpdate(Game game) {
    return insert(game)
        .map((id) -> {
          game.setId(id);
          return game;
        });
  }

}
