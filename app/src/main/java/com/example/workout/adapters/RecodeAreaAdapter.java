package com.example.workout.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workout.R;
import com.example.workout.activaties.MainActivity;
import com.example.workout.dialogs.SelectTimerInsertDialog;
import com.example.workout.managers.PreferenceHelper;
import com.example.workout.models.CalendarStructureModel;
import com.example.workout.models.ExerciseRecodeListItemModel;
import com.example.workout.restapi.ServerApiService;
import com.example.workout.restapi.ServiceGenerator;
import com.example.workout.utils.CalendarUtil;
import com.example.workout.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecodeAreaAdapter extends RecyclerView.Adapter<RecodeAreaAdapter.CalendarViewHolder> {

    ArrayList<ExerciseRecodeListItemModel> dayList;



    Context context;

    ServerApiService serverApiService;


    int currentPosition = 0;


    private final Callback<String> exerciseRecodeDeleteCall = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
//                String result = response.body();
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.txt_main_delete_complete), Toast.LENGTH_SHORT).show();


                Log.i("TEST", "currentPosition : " + currentPosition + " \ngetItemCount() : " + getItemCount());
                deleteItem(currentPosition);
//                notify();


            } else if (response.code() == 400) {
//                String result = response.body();

                Toast.makeText(context.getApplicationContext(), context.getString(R.string.txt_join_error_400), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Toast.makeText(context, context.getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    public RecodeAreaAdapter(ArrayList<ExerciseRecodeListItemModel> dayList, Context context){
        this.dayList = dayList;
        this.context = context;

        serverApiService = ServiceGenerator.createService(ServerApiService.class, new PreferenceHelper(context).getToken());

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



        String locale = Util.getLocale(context);



        if(locale.equals("ko")) {
            holder.txtExerciseAreaItem.setText(exerciseRecodeListItemModel.getExercise_area_name());

        }else {
            holder.txtExerciseAreaItem.setText(exerciseRecodeListItemModel.getExercise_area_name_en());
        }




       int time = exerciseRecodeListItemModel.getExercies_recode_time();


        int hour = time / 3600;
        int minute = time / 60;
        int second = time % 60;

        String text = "";
        if(hour > 0){
            text += hour + "h ";
        }
        if(minute > 0){
            text += minute + "m ";
        }
        if(second > 0){
            text += second + "s";
        }

        holder.txtExerciseTiemItem.setText(text);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = position;
                Log.i("TEST", "exerciseRecodeListItemModel.getExercise_recode_id() : " + exerciseRecodeListItemModel.getExercise_recode_id());
                serverApiService.exerciseRecodeDelete(exerciseRecodeListItemModel.getExercise_recode_id()).enqueue(exerciseRecodeDeleteCall);

            }
        });



        //달력 초기화

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{
        TextView txtExerciseAreaItem;
        TextView txtExerciseTiemItem;

        LinearLayout parentView;

        RelativeLayout btnDelete;

        public CalendarViewHolder(View itemView){
            super(itemView);

            txtExerciseAreaItem = itemView.findViewById(R.id.txt_exercise_area_item);
            txtExerciseTiemItem = itemView.findViewById(R.id.txt_exercise_tiem_item);
            parentView = itemView.findViewById(R.id.parentView);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }

    public void deleteItem(int position){
        dayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dayList.size());
    }


}
