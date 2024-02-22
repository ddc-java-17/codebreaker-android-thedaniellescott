package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.codebreaker.adapter.GameResultsAdapter.Holder;
import edu.cnm.deepdive.codebreaker.databinding.ItemGameResultsBinding;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class GameResultsAdapter extends RecyclerView.Adapter<Holder> {

  private final LayoutInflater inflater;
  private final DateFormat dateFormatter;
  private final DateFormat timeFormatter;
  private final List<GameResult> gameResults;

  public GameResultsAdapter(Context context, List<GameResult> gameResults) {
    this.gameResults = gameResults;
    inflater = LayoutInflater.from(context);
    dateFormatter = android.text.format.DateFormat.getDateFormat(context);
    timeFormatter = android.text.format.DateFormat.getTimeFormat(context);

  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    return new Holder(
        ItemGameResultsBinding.inflate(inflater, viewGroup, false), dateFormatter, timeFormatter);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(gameResults.get(position));
  }

  @Override
  public int getItemCount() {
    return gameResults.size();
  }

  static class Holder extends RecyclerView.ViewHolder {

    private final ItemGameResultsBinding binding;
    private final DateFormat dateFormatter;
    private final DateFormat timeFormatter;

    Holder(ItemGameResultsBinding binding, DateFormat dateFormatter, DateFormat timeFormatter) {
      super(binding.getRoot());
      this.binding = binding;
      this.dateFormatter = dateFormatter;
      this.timeFormatter = timeFormatter;
    }

    public void bind(GameResult gameResult) {
      binding.guessCount.setText(String.valueOf(gameResult.getGuessCount()));
      binding.duration
          .setText(String.valueOf(String.valueOf(gameResult.getDuration().getSeconds()))); // FIXME: use time formatter
      binding.timestamp
          .setText(dateFormatter.format(new Date(gameResult.getTimestamp().toEpochMilli())));
    }

  }

}
