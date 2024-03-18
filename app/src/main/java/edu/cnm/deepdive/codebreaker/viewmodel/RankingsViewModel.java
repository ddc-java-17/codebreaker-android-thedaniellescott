package edu.cnm.deepdive.codebreaker.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.codebreaker.model.Ranking;
import edu.cnm.deepdive.codebreaker.service.RankingsRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class RankingsViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = RankingsViewModel.class.getSimpleName();
  private final RankingsRepository repository;
  private final MutableLiveData<List<Ranking>> rankings;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public RankingsViewModel(RankingsRepository repository) {
    this.repository = repository;
    rankings = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    // FIXME: 3/18/202 Delete line below.
    fetch(4,2);
  }

  public LiveData<List<Ranking>> getRankings() {
    return rankings;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetch(int codeLength, int gamesThreshold) {
    throwable.setValue(null);
    pending.clear();
    repository.getRankings(codeLength, gamesThreshold)
        .subscribe(
            rankings::postValue,
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
