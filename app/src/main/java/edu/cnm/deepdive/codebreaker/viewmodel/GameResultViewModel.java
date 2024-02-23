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
import edu.cnm.deepdive.codebreaker.service.GameResultRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class GameResultViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = GameResultViewModel.class.getSimpleName();

  private final GameResultRepository repository;
  private final MutableLiveData<Integer> codeLength;
  private final LiveData<List<GameResult>> gameResults;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public GameResultViewModel(GameResultRepository repository) {
    this.repository = repository;
    codeLength = new MutableLiveData<>();
    gameResults = Transformations.switchMap(codeLength, repository::getAll);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
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
    repository
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

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
  }

}
