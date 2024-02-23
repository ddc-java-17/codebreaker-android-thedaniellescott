package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import edu.cnm.deepdive.codebreaker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupActionBar();
  }

  private void setupActionBar() {
    ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
    //noinspection DataFlowIssue
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
  }


}
