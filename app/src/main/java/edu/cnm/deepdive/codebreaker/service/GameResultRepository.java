package edu.cnm.deepdive.codebreaker.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.codebreaker.model.dao.GameResultDao;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;

public class GameResultRepository {

  private final GameResultDao gameResultDao;

  @Inject
  GameResultRepository(GameResultDao gameResultDao) {
    this.gameResultDao = gameResultDao;
  }

  public Single<GameResult> add(GameResult gameResult) {
    return gameResultDao
        .insert(gameResult)
        .map((id) -> {
          gameResult.setId(id);
          return gameResult;
    })
        .subscribeOn(Schedulers.io());
  }

  LiveData<List<GameResult>> getAll(int codeLength) {
    return gameResultDao.getRankedResults(codeLength);
  }

  Completable clear() {
    return gameResultDao.truncateResults();
  }
}
