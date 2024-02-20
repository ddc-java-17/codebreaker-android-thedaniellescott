package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GuessesAdapter extends ArrayAdapter<Guess> {

  private final LayoutInflater inflater;
  private final Map<Integer, String> colorNameLookup;
  private final Map<Integer, Integer> colorValueLookup;


  public GuessesAdapter(@NonNull Context context, @NonNull List<Guess> guesses) {
    super(context, R.layout.item_guesses, guesses);
    inflater = LayoutInflater.from(context);
    Resources res = context.getResources();
    int[] colorValues = res.getIntArray(R.array.color_values);
    String[] colorNames = res.getStringArray(R.array.color_names);
    colorNameLookup = Stream.of(colorNames)
        .collect(
            Collectors.toMap(
                (name) -> name.codePointAt(0),
                (name) -> name
            )
        );
    colorValueLookup = IntStream.range(0, colorNames.length)
        .boxed()
        .collect(
            Collectors.toMap(
                (pos) -> colorNames[pos].codePointAt(0),
                (pos) -> colorValues[pos]
            )
        );
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

            });

    // TODO: 2/19/2024 Iterate over characters of guess, using each to look up the color name and
    //  value from the maps above. Then, set tint of drawable to color value and add to container.
    binding.guess.setText(guess.getContent());
    binding.correct.setText(String.valueOf(guess.getCorrect()));
    binding.close.setText(String.valueOf(guess.getClose()));
    return binding.getRoot();
  }

}
