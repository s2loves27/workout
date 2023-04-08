package com.soti.workout.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soti.workout.R;
import com.soti.workout.adapters.RecodeAreaAdapter;
import com.soti.workout.models.ExerciseRecodeListItemModel;

import java.util.ArrayList;


public class RecodeAreaListDialog extends Dialog {



    RecyclerView recyclerView;

    ArrayList<ExerciseRecodeListItemModel> exerciseRecodeListItemModelList;


    Context context;
    public RecodeAreaListDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recode_area_list);

        recyclerView = findViewById(R.id.recyclerView);

    }

    public void setListItem(ArrayList<ExerciseRecodeListItemModel> exerciseRecodeListItemModelList){
        this.exerciseRecodeListItemModelList = exerciseRecodeListItemModelList;
    }

    @Override
    public void show() {
        super.show();

        RecodeAreaAdapter adapter = new RecodeAreaAdapter(exerciseRecodeListItemModelList, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void dismiss() {
        super.dismiss();


    }
}
