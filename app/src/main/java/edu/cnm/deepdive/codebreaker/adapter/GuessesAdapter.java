package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemGuessesBinding;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.util.List;

public class GuessesAdapter extends ArrayAdapter<Guess> {

  private final LayoutInflater inflater;

  public GuessesAdapter(@NonNull Context context, @NonNull List<Guess> guesses) {
    super(context, R.layout.item_guesses, guesses);
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemGuessesBinding binding = (convertView == null)
        ? ItemGuessesBinding.inflate(inflater, parent, false)
        : ItemGuessesBinding.bind(convertView);
    Guess guess = getItem(position);
    binding.guess.setText(guess.getContent());
    binding.correct.setText(String.valueOf(guess.getCorrect()));
    binding.close.setText(String.valueOf(guess.getClose()));
    return binding.getRoot();
  }

}
