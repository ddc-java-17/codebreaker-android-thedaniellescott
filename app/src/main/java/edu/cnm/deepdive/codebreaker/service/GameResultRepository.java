package edu.cnm.deepdive.codebreaker.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GameResultDao;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;

public class GameResultRepository {

  private final GameResultDao gameResultDao;
  private final GameDao gameDao;

  @Inject
  GameResultRepository(GameResultDao gameResultDao, GameDao gameDao) {
    this.gameResultDao = gameResultDao;
    this.gameDao = gameDao;
  }

  public LiveData<List<GameResult>> getAll(int codeLength, User user) {
    return (user != null)
        ? gameResultDao.getRankedResults(codeLength, user.getId())
        : gameResultDao.getRankedResults(codeLength);
  }

  public Completable clear() {
    return gameDao
        .truncate()
        .subscribeOn(Schedulers.io());
  }

}
