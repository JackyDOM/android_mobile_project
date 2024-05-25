package edu.rupp.firstite.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.rupp.firstite.R;

public class ToastUtil {
    public static void showCustomToast(Context context, String message, boolean isSuccess) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(isSuccess ? R.layout.toast_success : R.layout.toast_failure,
                null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}