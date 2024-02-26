package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemGuessesBinding;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GuessesAdapter extends ArrayAdapter<Guess> {

  private final LayoutInflater inflater;
  private final Map<Integer, String> colorNameLookup;
  private final Map<Integer, Integer> colorValueLookup;


  public GuessesAdapter(@NonNull Context context, @NonNull List<Guess> guesses,
      Map<Integer, String> colorNameLookup, Map<Integer, Integer> colorValueLookup) {
    super(context, R.layout.item_guesses, guesses);
    inflater = LayoutInflater.from(context);
    this.colorNameLookup = colorNameLookup;
    this.colorValueLookup = colorValueLookup;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemGuessesBinding binding = (convertView == null)
        ? ItemGuessesBinding.inflate(inflater, parent, false)
        : ItemGuessesBinding.bind(convertView);

    binding.guess.removeAllViews();
    Guess guess = getItem(position);
    guess.getContent()
        .codePoints()
        .forEach((codePoint) -> {
          ImageView swatch =
              (ImageView) inflater.inflate(R.layout.item_guess_chars, binding.guess, false);
          swatch.setContentDescription(colorNameLookup.get(codePoint));
          //noinspection DataFlowIssue
          swatch.setColorFilter(colorValueLookup.get(codePoint));
          binding.guess.addView(swatch);
        });
    binding.correct.setText(String.valueOf(guess.getCorrect()));
    binding.close.setText(String.valueOf(guess.getClose()));
    return binding.getRoot();
  }

}
