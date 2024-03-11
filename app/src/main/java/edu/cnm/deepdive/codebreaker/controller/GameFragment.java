package edu.cnm.deepdive.codebreaker.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.GuessesAdapter;
import edu.cnm.deepdive.codebreaker.adapter.SwatchesAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentGameBinding;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.viewmodel.CodebreakerViewModel;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AndroidEntryPoint
public class GameFragment extends Fragment implements MenuProvider {

  private FragmentGameBinding binding;
  private CodebreakerViewModel viewModel;
  private GuessesAdapter adapter;
  private Map<Integer, String> colorNameLookup;
  private Map<Integer, Integer> colorValueLookup;
  private Map<Integer, Integer> colorPositionLookup;
  private int codeLength;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupColors();
  }

  private void setupColors() {
    Resources res = requireContext().getResources();
    int[] colorValues = res.getIntArray(R.array.color_values);
    String[] colorNames = res.getStringArray(R.array.color_names);
    colorNameLookup = Stream.of(colorNames)
        .collect(Collectors.toMap(
                (name) -> name.codePointAt(0),
                Function.identity()
            )
        );
    colorValueLookup = IntStream.range(0, colorNames.length)
        .boxed().collect(Collectors.toMap(
                (pos) -> colorNames[pos].codePointAt(0),
                (pos) -> colorValues[pos]
            )
        );
    colorPositionLookup = IntStream.range(0, colorNames.length)
        .boxed()
        .collect(Collectors.toMap((pos) -> colorNames[pos].codePointAt(0), (pos) -> pos));
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
  public void onDestroyView() {
    adapter = null;
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.game_options, menu);
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = true;
    int itemId = menuItem.getItemId();
    if (itemId == R.id.new_game) {
      adapter = null;
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
    observeInProgress(owner);
  }

  private void connectToViewModel() {
    viewModel = new ViewModelProvider(requireActivity()).get(CodebreakerViewModel.class);
    getLifecycle().addObserver(viewModel);
  }

  private void observeGame(LifecycleOwner owner) {
    viewModel
        .getGame()
        .observe(owner, this::handleGame);
  }

  private void handleGame(Game game) {
    List<Guess> guesses = game.getGuesses();
    if (adapter == null) {
      adapter = new GuessesAdapter(requireContext(), guesses, colorNameLookup, colorValueLookup);
      binding.guesses.setAdapter(adapter);
      createSpinners(game);
    }
    adapter.notifyDataSetChanged();
    binding.guesses.post(() -> binding.guesses.smoothScrollToPosition(guesses.size() - 1));
    codeLength = game.getLength();
  }

  private void observeInProgress(LifecycleOwner owner) {
    viewModel
        .getInProgress()
        .observe(owner,
            (inProgress) -> {
              int visibility = inProgress ? View.VISIBLE : View.INVISIBLE;
              binding.colorSelectors.setVisibility(visibility);
              binding.submit.setVisibility(visibility);
            });
  }

  private void createSpinners(Game game) {
    int codeLength = game.getLength();
    List<Guess> guesses = game.getGuesses();
    int[] selectedItems = guesses.isEmpty()
        ? new int[codeLength]
        : guesses
            .get(guesses.size() - 1)
            .getContent()
            .codePoints()
            .map(colorPositionLookup::get)
            .toArray();
    Context context = requireContext();
    binding.colorSelectors.removeAllViews();
    LayoutInflater layoutInflater = getLayoutInflater();
    for (int i = 0; i < codeLength; i++) {
      Spinner spinner = (Spinner) layoutInflater
          .inflate(R.layout.color_spinner, binding.colorSelectors, false);
      spinner.setAdapter(new SwatchesAdapter(context));
      spinner.setSelection(selectedItems[i]);
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