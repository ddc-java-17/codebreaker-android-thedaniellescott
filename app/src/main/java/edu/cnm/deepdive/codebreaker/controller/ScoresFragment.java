package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.FragmentScoresBinding;

public class ScoresFragment extends Fragment {

  private FragmentScoresBinding binding;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Receive and store arguments passed into this fragment.
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentScoresBinding.inflate(inflater, container, false);
    // Initialize UI widgets as appropriate - EG attaching listeners.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Connect to viewmodels(s) and observe LiveData.
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}