package edu.cnm.deepdive.codebreaker.adapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import edu.cnm.deepdive.codebreaker.controller.ScoresFragment;

public class ScoresFragmentsAdapter extends FragmentStateAdapter {

  public ScoresFragmentsAdapter(@NonNull Fragment fragment) {
    super(fragment);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    Fragment fragment = new ScoresFragment();
    Bundle args = new Bundle();
    args.putBoolean(ScoresFragment.ALL_USERS_KEY, position > 0);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public int getItemCount() {
    return 2;
  }
}
