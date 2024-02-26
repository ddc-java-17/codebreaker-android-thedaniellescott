package edu.cnm.deepdive.codebreaker.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import edu.cnm.deepdive.codebreaker.service.GameResultRepository;
import edu.cnm.deepdive.codebreaker.service.UserRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class GameResultViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = GameResultViewModel.class.getSimpleName();

  private final GameResultRepository resultRepository;
  private final UserRepository userRepository;
  private final MutableLiveData<Integer> codeLength;
  private final LiveData<List<GameResult>> gameResults;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private User currentUser;

  @Inject
  public GameResultViewModel(GameResultRepository resultRepository, UserRepository userRepository) {
    this.resultRepository = resultRepository;
    this.userRepository = userRepository;
    codeLength = new MutableLiveData<>();
    gameResults = Transformations.switchMap(codeLength,
        codeLength -> resultRepository.getAll(codeLength, currentUser));
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    loadCurrentUser();
  }

  public LiveData<Integer> getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int length) {
    codeLength.setValue(length);
  }

  public LiveData<List<GameResult>> getGameResults() {
    return gameResults;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void clearResults() {
    resultRepository
        .clear()
        .subscribe(
            () -> {
            },
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  private void loadCurrentUser() {
    throwable.postValue(null);
    userRepository
        .getCurrentUser()
        .subscribe(
            (user) -> this.currentUser = user,
            this::postThrowable,
            pending
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
  }

}
