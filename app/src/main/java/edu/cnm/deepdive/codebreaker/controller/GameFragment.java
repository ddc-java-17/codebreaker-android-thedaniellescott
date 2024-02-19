package edu.cnm.deepdive.codebreaker.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.GuessesAdapter;
import edu.cnm.deepdive.codebreaker.adapter.SwatchesAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentGameBinding;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.viewmodel.CodebreakerViewModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AndroidEntryPoint
public class GameFragment extends Fragment implements MenuProvider {

  private FragmentGameBinding binding;
  private CodebreakerViewModel viewModel;
  private GuessesAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //This is where we read arguments passed to the fragment.
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Load and bind layout.
    binding = FragmentGameBinding.inflate(inflater, container, false);
    binding.submit.setOnClickListener((v) -> submitGuess());
    // TODO: 2/7/2024 Initialize view widgets as necessary
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    requireActivity().addMenuProvider(this, getViewLifecycleOwner(), State.RESUMED);
    //Connect to view models.
    setupViewModels();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.game_options, menu);
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = true;
    if (menuItem.getItemId() == R.id.new_game) {
      viewModel.startGame();
    } else {
      handled = false;
    }
    return handled;
  }

  private void setupViewModels() {
    connectToViewModel();
    LifecycleOwner owner = getViewLifecycleOwner();
    observeGame(owner);
    observeGuess(owner);
    observeInProgress(owner);
  }

  private void connectToViewModel() {
    viewModel = new ViewModelProvider(requireActivity()).get(CodebreakerViewModel.class);
    getLifecycle().addObserver(viewModel);
  }

  private void observeGame(LifecycleOwner owner) {
    viewModel
        .getGame()
        .observe(owner, (game) -> {
          adapter = new GuessesAdapter(requireContext(), game.getGuesses());
          binding.guesses.setAdapter(adapter);
          createSpinners(game);
        });
  }

  private void observeGuess(LifecycleOwner owner) {
    viewModel
        .getGuess()
        .observe(owner,
            (guess) -> {
              if (adapter != null) {
                adapter.notifyDataSetChanged();
                binding.guesses.setSelection(adapter.getCount() - 1);
              }
            });
  }

  private void observeInProgress(LifecycleOwner owner) {
    viewModel
        .getInProgress()
        .observe(owner,
            (inProgress) -> { /*TODO Enable/Display controls on change of game state.*/});
  }

  private void createSpinners(Game game) {
    int codeLength = game.getLength();
    List<Guess> guesses = game.getGuesses();
    String lastGuess = (guesses.isEmpty()) ? null : guesses.get(guesses.size() - 1).getContent();
    Context context = requireContext();
    binding.colorSelectors.removeAllViews();
    for (int i = 0; i < codeLength; i++) {
      Spinner spinner = (Spinner) getLayoutInflater()
          .inflate(R.layout.color_spinner, binding.colorSelectors, false);
      spinner.setAdapter(new SwatchesAdapter(context));
      binding.colorSelectors.addView(spinner);
    }

  }

  private void submitGuess() {
    String guess = IntStream.range(0, binding.colorSelectors.getChildCount())
        .mapToObj((i) -> (Spinner) binding.colorSelectors.getChildAt(i))
        .map((spinner) -> (String) spinner.getSelectedItem())
        .map((colorName) -> colorName.substring(0, 1))
        .collect(Collectors.joining());
    viewModel.submitGuess(guess);
  }

}