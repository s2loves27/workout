package com.example.workout.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workout.R;

import java.util.ArrayList;

import kotlin.reflect.KParameter;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    ArrayList<String> dayList;

    public CalendarAdapter(ArrayList<String> dayList){
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_list_calendar_item, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.CalendarViewHolder holder, int position) {
        holder.dayText.setText(dayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{
        TextView dayText;

        public CalendarViewHolder(View itemView){
            super(itemView);

            dayText = itemView.findViewById(R.id.dayText);

        }
    }
}
