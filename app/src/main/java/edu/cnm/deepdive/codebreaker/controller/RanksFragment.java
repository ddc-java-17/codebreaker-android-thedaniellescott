package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.databinding.FragmentRanksBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.CodebreakerViewModel;
import edu.cnm.deepdive.codebreaker.viewmodel.PreferencesViewModel;
import edu.cnm.deepdive.codebreaker.viewmodel.RankingsViewModel;

public class RanksFragment extends Fragment {

  private FragmentRanksBinding binding;
  private RankingsViewModel rankingsViewModel;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentRanksBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(requireActivity());
    LifecycleOwner owner = getViewLifecycleOwner();
    rankingsViewModel = provider.get(RankingsViewModel.class);
    rankingsViewModel.getRankings().observe(owner, (rankings) -> {
      // TODO: 3/18/2024 Populate recycler view with rankings.
    });
    CodebreakerViewModel codebreakerViewModel = provider.get(CodebreakerViewModel.class);
    codebreakerViewModel.getGame().observe(owner, (game) -> {
      // TODO: 3/18/2024 If we have both codeLength and gamesThreshold, invoke RankingsViewModel.fetch(codeLength, gamesThreshold)
    });
    PreferencesViewModel preferencesViewModel = provider.get(PreferencesViewModel.class);
    preferencesViewModel.getPreferredGamesThreshold().observe(owner, (threshold) -> {
      // TODO: 3/18/2024 If we have both codeLength and gamesThreshold, invoke RankingsViewModel.fetch(codeLength, gamesThreshold)
    });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}