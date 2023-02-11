package com.example.workout.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workout.R;

import java.util.ArrayList;

import kotlin.reflect.KParameter;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    ArrayList<String> dayList;

    OnItemListener onItemListener;

    Context context;

    public CalendarAdapter(ArrayList<String> dayList, Context context, OnItemListener onitemListener){
        this.dayList = dayList;
        this.context = context;
        this.onItemListener = onitemListener;
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

        String day = dayList.get(position);

        holder.dayText.setText(day);

        if((position + 1) % 7 == 0 ){
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sat_color));
        }else if(position == 0 || position % 7 == 0){
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sun_color));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(day);
            }
        });
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

    public interface OnItemListener {

        void onItemClick(String dayText);
    }
}
