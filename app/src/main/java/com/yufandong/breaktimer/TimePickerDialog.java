package com.yufandong.breaktimer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TimePickerDialog extends DialogFragment {

    private View rootView;
    private Bundle returnBunduru; // Brack Friday Bunduru!
    private String title;
    private NumberPicker hourPicker;
    private NumberPicker minPicker;
    private Button okButton;
    private Button cancelButton;
    private OnDialogDismissListener dismissListener;
    private int initHour;
    private int initMin;

    public static TimePickerDialog newInstance(Bundle returnBundle, String title, int initHour, int initMin) {
        TimePickerDialog newDialog = new TimePickerDialog();

        Bundle args = new Bundle();
        args.putBundle("Return Bunduru", returnBundle);
        args.putString("Title", title);
        args.putInt("Hour", initHour);
        args.putInt("Minute", initMin);

        newDialog.setArguments(args);

        return newDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            returnBunduru = savedInstanceState.getBundle("Return Bunduru");
            title = savedInstanceState.getString("Title");
            initHour = savedInstanceState.getInt("Hour");
            initMin = savedInstanceState.getInt("Minute");
            OnDialogDismissListener listener = savedInstanceState.getParcelable("Listener");
            if(listener != null)
                dismissListener = listener;
        } else if(getArguments() != null){
            returnBunduru = getArguments().getBundle("Return Bunduru");
            title = getArguments().getString("Title");
            initHour = getArguments().getInt("Hour");
            initMin = getArguments().getInt("Minute");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.dialog_time_picker, container);

        // Set up number pickers
        hourPicker = (NumberPicker) rootView.findViewById(R.id.hour_picker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(9);
        hourPicker.setWrapSelectorWheel(false);
        hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int num) {
                return String.format("%d h", num);
            }
        });
        minPicker = (NumberPicker) rootView.findViewById(R.id.min_picker);
        minPicker.setMinValue(0);
        minPicker.setMaxValue(60);
        minPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int num) {
                return String.format("%02d m", num);
            }
        });

            hourPicker.setValue(initHour);
            minPicker.setValue(initMin);

        fixFirstRenderPickerBug(hourPicker);
        fixFirstRenderPickerBug(minPicker);

        // Set up the buttons
        okButton = (Button) rootView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAndSaveDialog();
            }
        });
        cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        // Make the dialog not close when touched outside
//        getDialog().setCanceledOnTouchOutside(false);

        getDialog().setTitle(title);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("Return Bunduru", returnBunduru);
        outState.putString("Title", title);
        outState.putInt("Hour", hourPicker.getValue());
        outState.putInt("Minute", minPicker.getValue());
        outState.putParcelable("Listener", dismissListener);
    }

    public void setOnDialogDismissListener(OnDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void closeDialog() {
        this.dismiss();
    }

    private void closeAndSaveDialog() {
        returnBunduru.putInt("Hour", hourPicker.getValue());
        returnBunduru.putInt("Minute", minPicker.getValue());
        dismissListener.onDismiss();
        closeDialog();
    }

    private void fixFirstRenderPickerBug(NumberPicker picker) {
        try {
            Method method = picker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(picker, true);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static abstract class OnDialogDismissListener implements Parcelable{
        abstract void onDismiss();

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this);
        }
    }
}
