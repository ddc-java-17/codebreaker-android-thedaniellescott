package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemGameResultsBinding;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import java.text.DateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class GameResultsAdapter extends Adapter<ViewHolder> {

  private final LayoutInflater inflater;
  private final DateFormat dateFormatter;
  private final String durationFormat;
  private final List<GameResult> gameResults;

  public GameResultsAdapter(Context context, List<GameResult> gameResults) {
    this.gameResults = gameResults;
    inflater = LayoutInflater.from(context);
    dateFormatter = android.text.format.DateFormat.getDateFormat(context);
    durationFormat = context.getString(R.string.duration_format);

  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    return new Holder(
        ItemGameResultsBinding.inflate(inflater, viewGroup, false), dateFormatter, durationFormat);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((Holder) holder).bind(gameResults.get(position));
  }

  @Override
  public int getItemCount() {
    return gameResults.size();
  }

  static class Holder extends RecyclerView.ViewHolder {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MS_PER_SECOND = 1000;
    private final ItemGameResultsBinding binding;
    private final DateFormat dateFormatter;
    private final String durationFormat;

    Holder(ItemGameResultsBinding binding, DateFormat dateFormatter, String durationFormat) {
      super(binding.getRoot());
      this.binding = binding;
      this.dateFormatter = dateFormatter;
      this.durationFormat = durationFormat;
    }

    public void bind(GameResult gameResult) {
      binding.guessCount.setText(String.valueOf(gameResult.getGuessCount()));
      binding.timestamp
          .setText(dateFormatter.format(new Date(gameResult.getTimestamp().toEpochMilli())));
      Duration duration = gameResult.getDuration();
      long hours = duration.toHours();
      long minutes = duration.toMinutes() % MINUTES_PER_HOUR;
      long milliseconds = duration.toMillis();
      long seconds = (milliseconds / MS_PER_SECOND) % SECONDS_PER_MINUTE;
      milliseconds %= MS_PER_SECOND;
      binding.duration
          .setText(String.format(durationFormat, hours, minutes, seconds, milliseconds));
    }

  }

}