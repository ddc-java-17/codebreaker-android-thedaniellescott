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
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.service.CodebreakerRepository;
import edu.cnm.deepdive.codebreaker.service.PreferencesRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

@HiltViewModel
public class CodebreakerViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = CodebreakerViewModel.class.getSimpleName();

  private final Context context;
  private final CodebreakerRepository codebreakerRepository;
  private final PreferencesRepository preferencesRepository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Guess> guess;
  private final LiveData<Boolean> inProgress;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final String pool;
  private final String codeLengthKey;
  private final int codeLengthDefault;

  @Inject
  CodebreakerViewModel(@ApplicationContext Context context,
      CodebreakerRepository codebreakerRepository, PreferencesRepository preferencesRepository1) {
    this.context = context;
    this.codebreakerRepository = codebreakerRepository;
    this.preferencesRepository = preferencesRepository1;
    game = new MutableLiveData<>();
    guess = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    inProgress = Transformations.map(game, (game) -> game != null && game.isSolved());
    pool = Stream.of(context.getResources().getStringArray(R.array.color_names))
        .map((name) -> name.substring(0, 1))
        .collect(Collectors.joining());
    codeLengthKey = context.getString(R.string.code_length_key);
    codeLengthDefault = context.getResources().getInteger(R.integer.code_length_default);
    startGame(); // FIXME: 2/13/2024 Should usually be driven by the UI.
  }

  public void startGame() {
    throwable.setValue(null);
    int length = preferencesRepository.get(codeLengthKey, codeLengthDefault);
    Game game = new Game(pool, length);
    codebreakerRepository
        .startGame(game)
        .subscribe(
            this.game::postValue,
            this::postThrowable,
            pending
        );
  }

  public void resumeGame(String id) {
    throwable.setValue(null);
    codebreakerRepository.getGame(id)
        .subscribe(
            this.game::postValue,
            this::postThrowable,
            pending
        );
  }

  public void submitGuess(String text) {
    throwable.setValue(null);
    codebreakerRepository.submitGuess(text)
        .subscribe(
            guess::postValue,
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

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
