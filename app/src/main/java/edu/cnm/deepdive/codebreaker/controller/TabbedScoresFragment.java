package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout.Tab;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.FragmentTabbedScoresBinding;

@AndroidEntryPoint
public class TabbedScoresFragment extends Fragment {

  private FragmentTabbedScoresBinding binding;
  private String[] scoreTabNames;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentTabbedScoresBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // TODO: 4/3/2024 Read allUsers argument.
    scoreTabNames = getResources().getStringArray(R.array.score_tab_names);
    // TODO: 4/3/2024 Connect viewpager with tab layout.
    new TabLayoutMediator(binding.scoresTabs, binding.scoresFragmentHost,
        (tab, position) -> tab.setText(scoreTabNames[position]))
        .attach();
  }
}
