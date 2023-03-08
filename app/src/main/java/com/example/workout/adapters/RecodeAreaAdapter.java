package com.example.workout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workout.R;
import com.example.workout.dialogs.SelectTimerInsertDialog;
import com.example.workout.models.CalendarStructureModel;
import com.example.workout.models.ExerciseRecodeListItemModel;
import com.example.workout.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RecodeAreaAdapter extends RecyclerView.Adapter<RecodeAreaAdapter.CalendarViewHolder> {

    ArrayList<ExerciseRecodeListItemModel> dayList;



    Context context;



    public RecodeAreaAdapter(ArrayList<ExerciseRecodeListItemModel> dayList, Context context){
        this.dayList = dayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecodeAreaAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_list_recode_area_item, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        //날짜 변수에 담기
        ExerciseRecodeListItemModel exerciseRecodeListItemModel = dayList.get(position);

        holder.txtExerciseAreaItem.setText(exerciseRecodeListItemModel.getExercise_area_name());


       int time = exerciseRecodeListItemModel.getExercies_recode_time();


        int hour = time / 3600;
        int minute = time / 60;
        int second = time % 60;

        String text = "";
        if(hour > 0){
            text += hour + "시 ";
        }
        if(minute > 0){
            text += minute + "분 ";
        }
        if(second > 0){
            text += second + "초";
        }

        holder.txtExerciseTiemItem.setText(text);





        //달력 초기화

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{
        TextView txtExerciseAreaItem;
        TextView txtExerciseTiemItem;

        public CalendarViewHolder(View itemView){
            super(itemView);

            txtExerciseAreaItem = itemView.findViewById(R.id.txt_exercise_area_item);
            txtExerciseTiemItem = itemView.findViewById(R.id.txt_exercise_tiem_item);

        }
    }


}
