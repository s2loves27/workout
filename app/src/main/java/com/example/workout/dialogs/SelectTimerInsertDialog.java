package com.example.workout.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.workout.R;

import java.util.Date;


public class SelectTimerInsertDialog extends Dialog {
    TextView txtDate;
    TextView btnTimer;
    TextView btnInput;

    public interface SelectTimerInsertDialogClickListener {
        void onTimerClick();

        void onInputClick();
    }

    private final SelectTimerInsertDialogClickListener selectTimerInsertDialogClickListener;

    public SelectTimerInsertDialog(@NonNull Context context, SelectTimerInsertDialogClickListener selectTimerInsertDialogClickListener) {
        super(context);
        this.selectTimerInsertDialogClickListener = selectTimerInsertDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_time_insert);

        btnTimer = findViewById(R.id.btn_timer);
        btnInput = findViewById(R.id.btn_input);
        txtDate = findViewById(R.id.txt_date);

        btnTimer.setOnClickListener(v -> selectTimerInsertDialogClickListener.onTimerClick());

        btnInput.setOnClickListener(v -> selectTimerInsertDialogClickListener.onInputClick());
    }


    public void setDate(String yearMonthDay){
        txtDate.setText(yearMonthDay);
    }
}
