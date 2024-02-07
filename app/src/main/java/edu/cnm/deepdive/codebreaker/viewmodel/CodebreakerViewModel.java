package edu.cnm.deepdive.codebreaker.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.service.CodebreakerRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

@HiltViewModel
public class CodebreakerViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = CodebreakerViewModel.class.getSimpleName();

  private final Context context;
  private final CodebreakerRepository repository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Guess> guess;
  private final LiveData<Boolean> inProgress;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  CodebreakerViewModel(@ApplicationContext Context context, CodebreakerRepository repository) {
    this.context = context;
    this.repository = repository;
    game = new MutableLiveData<>();
    guess = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    inProgress = Transformations.map(game, (game) -> game != null && game.isSolved());
  }

  public void startGame() {
    Game game = new Game("ABCDEF", 4);// FIXME: 2/7/2024 Use preferences (settings) for the code pool and length.
    repository
        .startGame(game)
        .subscribe(
            this.game::postValue,
            this::postThrowable,
            pending
        );
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Guess> getGuess() {
    return guess;
  }

  public LiveData<Boolean> getInProgress() {
    return inProgress;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  @Override
  public void onStop(@NotNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void postThrowable(Throwable throwable){
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
