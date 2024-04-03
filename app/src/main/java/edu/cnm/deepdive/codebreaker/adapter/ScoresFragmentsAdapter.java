package edu.cnm.deepdive.codebreaker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScoresFragmentsAdapter extends FragmentStateAdapter {

  public ScoresFragmentsAdapter(@NonNull Fragment fragment) {
    super(fragment);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    return null;
    // TODO: 4/3/2024 Create an instance of ScoresFragment and pass it a bundle of arguments.
  }

  @Override
  public int getItemCount() {
    return 2;
  }
}
