package com.soti.workout.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.soti.workout.R;


public class BatteryOptimizationPermissionDialog extends Dialog {

    TextView btnCancel;
    TextView btnSetting;

    public interface BatteryOptimizationPermissionDialogClickListener {
        void onCancelClick();

        void onSettingClick();
    }

    private final BatteryOptimizationPermissionDialogClickListener batteryOptimizationPermissionDialogClickListener;



    public BatteryOptimizationPermissionDialog(@NonNull Context context, BatteryOptimizationPermissionDialogClickListener batteryOptimizationPermissionDialogClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.batteryOptimizationPermissionDialogClickListener = batteryOptimizationPermissionDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_battery_permission);

        btnCancel = findViewById(R.id.btn_cancel);
        btnSetting = findViewById(R.id.btn_setting);

        btnCancel.setOnClickListener(v -> batteryOptimizationPermissionDialogClickListener.onCancelClick());

        btnSetting.setOnClickListener(v -> batteryOptimizationPermissionDialogClickListener.onSettingClick());
    }


}
