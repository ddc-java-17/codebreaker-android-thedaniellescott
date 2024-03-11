package edu.cnm.deepdive.codebreaker.controller;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.MainNavigationMapDirections;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ActivityMainBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private LoginViewModel loginViewModel;
  private NavController navController;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(loginViewModel);
    loginViewModel
        .getAccount()
        .observe(this, this::handleAccount);
    loginViewModel
        .getThrowable()
        .observe(this, this::handleThrowable);
    setContentView(binding.getRoot());
    setupNavigation();
    setupDrawer();
  }

  @Override
  public boolean onSupportNavigateUp() {
    getOnBackPressedDispatcher().onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = binding.getRoot();
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account == null) {
      Intent intent = new Intent(this, LoginActivity.class)
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }

  private void handleThrowable(Throwable throwable) {

  }

  private void setupNavigation() {
    AppBarConfiguration config = new AppBarConfiguration.Builder(
        R.id.game_fragment, R.id.scores_fragment, R.id.ranks_fragment)
        .setFallbackOnNavigateUpListener(this::onSupportNavigateUp)
        .build();
    //noinspection DataFlowIssue
    navController = ((NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment))
        .getNavController();
//    setSupportActionBar(binding.appBarLayout.toolbar);
//    NavigationUI.setupActionBarWithNavController(this, navController, config);
    NavigationUI.setupWithNavController(binding.appBarLayout.toolbar, navController, config);
  }

  private void setupDrawer() {
    DrawerLayout drawer = binding.getRoot();
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
        binding.appBarLayout.toolbar, R.string.nav_open, R.string.nav_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

  }

}
