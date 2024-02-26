package edu.cnm.deepdive.codebreaker.service;

import androidx.lifecycle.LiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.codebreaker.model.dao.UserDao;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

  public final UserDao userDao;
  private final GoogleSignInService signInService;

  @Inject
  UserRepository(UserDao userDao) {
    this.userDao = userDao;
  }

  public Single<User> getCurrentUser() {
    return signInService
        .refresh()
        .flatMap(this::getOrAdd);
  }

  public LiveData<User> get(long userId) {
    return userDao.select(userId);
  }

  private Single<User> getOrAdd(GoogleSignInAccount account) {
    return userDao
        .select(account.getId())
        .switchIfEmpty(
            Single.fromCallable(() -> {
                  User user = new User();
                  user.setOauthKey(account.getId());
                  user.setDisplayName(account.getDisplayName());
                  return user;
                })
                .flatMap((user) ->
                    userDao
                        .insert(user)
                        .map((id) -> {
                          user.setId(id);
                          return user;
                        })
                )
                .subscribeOn(Schedulers.io())
        );
  }

}
