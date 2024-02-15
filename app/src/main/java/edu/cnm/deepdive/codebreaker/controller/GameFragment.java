package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import edu.cnm.deepdive.codebreaker.viewmodel.CodebreakerViewModel;

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
    binding.submit.setOnClickListener((v) ->
        viewModel.submitGuess(binding.guess.getText().toString().strip()));
    binding.colorSelector.setAdapter(new SwatchesAdapter(requireContext()));
    // TODO: 2/7/2024 Initialize view widgets as necessary
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    requireActivity().addMenuProvider(this, getViewLifecycleOwner(), State.RESUMED);
    //Connect to view models.
    viewModel = new ViewModelProvider(requireActivity()).get(CodebreakerViewModel.class);
    getLifecycle().addObserver(viewModel);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getGame()
        .observe(owner, (game) -> {
          adapter = new GuessesAdapter(requireContext(), game.getGuesses());
          binding.guesses.setAdapter(adapter);
        });
    viewModel
        .getGuess()
        .observe(owner,
            (guess) -> {
              if (adapter != null) {
                adapter.notifyDataSetChanged();
                binding.guesses.setSelection(adapter.getCount() - 1);
              }
            });
    viewModel
        .getInProgress()
        .observe(owner,
            (inProgress) -> { /*TODO Enable/Display controls on change of game state.*/});
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
}