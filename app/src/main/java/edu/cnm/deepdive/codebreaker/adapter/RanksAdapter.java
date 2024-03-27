package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemRanksBinding;
import edu.cnm.deepdive.codebreaker.model.Ranking;
import java.util.ArrayList;
import java.util.List;


public class RanksAdapter extends Adapter<ViewHolder> {

  private final List<Ranking> ranks;
  private final String durationFormat;
  private final String guessCountFormat;
  private final LayoutInflater inflater;

  public RanksAdapter(Context context, List<Ranking> ranks) {
    this.ranks = new ArrayList<>(ranks);
    durationFormat = context.getString(R.string.duration_format);
    guessCountFormat = context.getString(R.string.guess_count_format);
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    ItemRanksBinding binding = ItemRanksBinding.inflate(inflater, viewGroup, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    ((Holder) viewHolder).bind(position);
  }

  @Override
  public int getItemCount() {
    return ranks.size();
  }

  private class Holder extends ViewHolder {

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private final ItemRanksBinding binding;

    public Holder(@NonNull ItemRanksBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(int position) {

      // If you have interactable stuff in your RecyclerView, the listeners go here.
      Ranking ranking = ranks.get(position);
      binding.rank.setText(String.valueOf(position + 1));
      binding.displayName.setText(ranking.getUser().getDisplayName());
      binding.avgGuessCount.setText(String.format(guessCountFormat, ranking.getAvgGuessCount()));
      double milliseconds = ranking.getAvgDuration();
      double seconds = milliseconds / MILLISECONDS_PER_SECOND;
      int minutes = (int) seconds / SECONDS_PER_MINUTE;
      seconds %= SECONDS_PER_MINUTE;
      int hours = minutes / MINUTES_PER_HOUR;
      minutes %= MINUTES_PER_HOUR;
      binding.avgDuration.setText(String.format(durationFormat, hours, minutes, seconds));
    }
  }

}
