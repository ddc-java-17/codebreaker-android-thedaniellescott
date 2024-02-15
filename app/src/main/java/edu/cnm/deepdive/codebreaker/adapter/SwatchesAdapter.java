package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemSwatchesBinding;

public class SwatchesAdapter extends ArrayAdapter<String> {

  private final LayoutInflater inflater;
  private final int[] colorValues;

  public SwatchesAdapter(@NonNull Context context) {
    super(context, R.layout.item_swatches, context.getResources().getStringArray(R.array.color_names));
    inflater = LayoutInflater.from(context);
    colorValues = context.getResources().getIntArray(R.array.color_values);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return getView(position, convertView, parent, View.GONE);
  }


  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return getView(position, convertView, parent, View.VISIBLE);
  }

  @NonNull
  private View getView(int position, @Nullable View convertView,
      @NonNull ViewGroup parent, int labelVisibility) {
    ItemSwatchesBinding binding = (convertView == null)
        ? ItemSwatchesBinding.inflate(inflater, parent, false)
        : ItemSwatchesBinding.bind(convertView);
    binding.swatch.setColorFilter(colorValues[position]);
    binding.label.setText(getItem(position));
    binding.label.setVisibility(labelVisibility);
    return binding.getRoot();
  }

}
