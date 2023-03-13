package com.example.workout.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BatteryPermissionDialog extends DialogFragment {

    public static BatteryPermissionDialog newInstance(){
        return new BatteryPermissionDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = "권한이 필요합니다";
        String message = "정상적인 앱 사용을 위해 해당 어플을 \"배터리 사용량 최적화\" 목록에서 \"제외\"하는 권한이 필요합니다 \n\n [확인] 버튼을 누른 후 시스템 알림 대화 상자가 뜨면 [허용]을 선택해주세요";
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:"  + getContext().getPackageName()));
                startActivity(intent);
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton("확인", clickListener);
        return builder.create();

    }
}
