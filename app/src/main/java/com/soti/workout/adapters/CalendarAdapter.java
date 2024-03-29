package com.soti.workout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.soti.workout.R;
import com.soti.workout.dialogs.SelectTimerInsertDialog;
import com.soti.workout.models.CalendarStructureModel;
import com.soti.workout.models.ExerciseRecodeListItemModel;
import com.soti.workout.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    ArrayList<CalendarStructureModel> dayList;



    Context context;

    private SelectTimerInsertDialog selectTimerInsertDialog;

    OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<CalendarStructureModel> dayList, Context context, OnItemListener onItemListener){
        this.dayList = dayList;
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_list_calendar_item, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        //날짜 변수에 담기
        Date monthDate = dayList.get(position).getDate();
        CalendarStructureModel calendarStructureModel = dayList.get(position);

        //달력 초기화
        Calendar dateCalendar = Calendar.getInstance();


        dateCalendar.setTime(monthDate);



        //현재 년 월
        int currentDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = CalendarUtil.selectedDate.get(Calendar.MONTH) + 1;
        int currentYear = CalendarUtil.selectedDate.get(Calendar.YEAR);

        //넘어온 데이터
        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);



        if(displayMonth == currentMonth && displayYear == currentYear){
            if(currentDay == displayDay){
                holder.parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.sameDayColor));

            }else {
                holder.parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.sameMonthColor));
            }
        }else{
            holder.parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.diffMonthColor));
        }

        //날짜 변수에 담기
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);

        holder.dayText.setText(String.valueOf(dayNo));


        if((position + 1) % 7 == 0 ){
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sat_color));
        }else if(position == 0 || position % 7 == 0){
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sun_color));
        }

        List<ExerciseRecodeListItemModel> exerciseRecodeListItemModels = dayList.get(position).getExerciseRecodeListItemModel();

        if(exerciseRecodeListItemModels!= null && !exerciseRecodeListItemModels.isEmpty()){
            int time = 0;
            for(int i = 0 ; i < exerciseRecodeListItemModels.size(); i++){
                time += exerciseRecodeListItemModels.get(i).getExercies_recode_time();
            }

            int hour = time / 3600;
            int minute = time / 60;
            int second = time % 60;
            String text = "";
            if(hour > 0){
                text += hour + "h ";
            }
            if(minute > 0 ){
                text += minute + "m ";
            }
            if(second > 0){
                text += second + "s";
            }

            holder.txtWorkOutTime.setText(text);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemListener.onItemClick(calendarStructureModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{
        TextView dayText;
        TextView txtWorkOutTime;
        LinearLayout parentView;

        public CalendarViewHolder(View itemView){
            super(itemView);

            dayText = itemView.findViewById(R.id.dayText);
            parentView = itemView.findViewById(R.id.parentView);
            txtWorkOutTime = itemView.findViewById(R.id.txt_workout_time);

        }
    }

    public interface OnItemListener{
        void onItemClick(CalendarStructureModel calendarStructureModel);
    }
}
