package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.Ranking;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RankingsRepository {

  public static final int THREAD_POOL_SIZE = 4;

  private final CodebreakerServiceProxy serviceProxy;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
private final int poolSize;

  @Inject
  RankingsRepository(@ApplicationContext Context context, CodebreakerServiceProxy serviceProxy,
      GoogleSignInService signInService) {
    this.serviceProxy = serviceProxy;
    this.signInService = signInService;
    scheduler = Schedulers.from(Executors.newFixedThreadPool(THREAD_POOL_SIZE));
    poolSize = context.getResources().getStringArray(R.array.color_names).length;
  }

  public Single<List<Ranking>> getRankings(int codeLength, int gamesThreshold) {
    return signInService.refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> serviceProxy.getRankings(poolSize, codeLength, gamesThreshold, token));
  }

}
