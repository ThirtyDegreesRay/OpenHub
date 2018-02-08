package com.thirtydegreesray.openhub.ui.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.ui.widget.colorChooser.ColorChooserDialog;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;
import com.thirtydegreesray.openhub.util.WindowUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ThirtyDegreesRay on 2018/1/11 15:49:08
 */

public class EditLabelDialog implements DialogInterface.OnDismissListener,
        ColorChooserDialog.ColorCallback, TextWatcher {

    private final Pattern COLOR_PATTERN = Pattern.compile("([a-f]|[A-F]|\\d){6}");

    private View contentView;
    @BindView(R.id.label_preview) TextView labelPreview;
    @BindView(R.id.label_name_et) TextInputEditText labelNameEt;
    @BindView(R.id.label_name_layout) TextInputLayout labelNameLayout;
    @BindView(R.id.label_color_et) TextInputEditText labelColorEt;
    @BindView(R.id.label_color_layout) TextInputLayout labelColorLayout;
    @BindView(R.id.colors_lay) LinearLayout colorsLay;
    private Unbinder unbinder;

    private Activity activity;
    private Label label;
    private EditLabelListener listener;

    private AlertDialog dialog;

    private boolean isEditMode;
    private int[] colorArray;

    public EditLabelDialog(@NonNull Activity activity, @NonNull EditLabelListener editLabelListener) {
        this(activity, editLabelListener, null);
    }


    public EditLabelDialog(@NonNull Activity activity, @NonNull EditLabelListener editLabelListener,
                           @Nullable Label label) {
        this.activity = activity;
        this.label = label;
        this.listener = editLabelListener;
        isEditMode = label != null;
        colorArray = activity.getResources().getIntArray(R.array.labels_color_array);

        contentView = activity.getLayoutInflater().inflate(R.layout.layout_edit_label, null);
        unbinder = ButterKnife.bind(this, contentView);
        initContentView();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(isEditMode ? R.string.edit_label : R.string.create_label)
                .setView(contentView)
                .setOnDismissListener(this)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null);
        if (label != null) {
            builder.setNeutralButton(R.string.delete, (dialog, which) -> {
                showDeleteLabelWarning();
            });
        }
        dialog = builder.create();
    }

    private void initContentView() {
        labelNameEt.addTextChangedListener(this);
        labelColorEt.addTextChangedListener(this);
        if (isEditMode) {
            labelNameEt.setText(label.getName());
            labelColorEt.setText(label.getColor());
        } else {
            int randomColor = getRandomColor();
            String rgbColor = ViewUtils.getRGBColor(randomColor, false);
            labelColorEt.setText(rgbColor);
        }

        int[] colorsResId = activity.getResources().getIntArray(R.array.simple_labels_color_array);
        LinearLayout rowLay = null;
        int colorViewHeight = WindowUtil.dipToPx(activity, 36);

        for (int i = 0; i < colorsResId.length; i++) {
            if (rowLay == null || i % 4 == 0) {
                rowLay = new LinearLayout(activity);
                rowLay.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowLay.setLayoutParams(layoutParams);
                colorsLay.addView(rowLay);
            }
            TextView colorView = new TextView(activity);
            LinearLayout.LayoutParams colorLayoutParams = new LinearLayout.LayoutParams(0, colorViewHeight);
            colorLayoutParams.weight = 1;
            colorView.setLayoutParams(colorLayoutParams);
            colorView.setGravity(Gravity.CENTER);
            colorView.setTextColor(ViewUtils.getTitleColor(activity));
            if (i != colorsResId.length - 1) {
                String rgbColor = ViewUtils.getRGBColor(colorsResId[i], false);
                colorView.setText("#".concat(rgbColor));
                colorView.setBackgroundColor(colorsResId[i]);
                colorView.setTag(rgbColor);
            } else {
                colorView.setBackgroundColor(ViewUtils.getAccentColor(activity));
                colorView.setTag("");
                colorView.setText(R.string.more);
            }

            colorView.setOnClickListener(v -> {
                String colorStr = (String) v.getTag();
                if (!colorStr.equals("")) {
                    labelColorEt.setText(colorStr);
                } else {
                    showChooseColorDialog();
                }

            });
            rowLay.addView(colorView);
        }
    }

    public void show() {
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    if(validateLabel()){
                        updateOrCreateLabel();
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        unbinder.unbind();
    }

    private void showChooseColorDialog() {
        ColorChooserDialog.Builder builder = new ColorChooserDialog
                .Builder((AppCompatActivity) activity, this, R.string.choose_color)
                .titleSub(R.string.choose_theme)
                .customColors(colorArray, null)
                .accentMode(true)
                .doneButton(R.string.done)
                .cancelButton(R.string.cancel)
                .presetsButton(R.string.presets)
                .customButton(R.string.custom)
                .allowUserColorInputAlpha(false);
        int currentColor = getCurrentColorValue();
        if(currentColor != 0){
            builder.preselect(currentColor);
        }
        builder.show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        String rgbColor = ViewUtils.getRGBColor(selectedColor, false);
        labelColorEt.setText(rgbColor);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    private String getCurrentName() {
        return labelNameEt.getText().toString().trim().replaceAll(" ", "");
    }

    private String getCurrentColor() {
        String colorStr = labelColorEt.getText().toString().trim().replaceAll(" ", "");
        return COLOR_PATTERN.matcher(colorStr).matches() ? colorStr : null;
    }

    private int getCurrentColorValue() {
        String colorStr = getCurrentColor();
        if (colorStr != null) {
            return Color.parseColor("#".concat(colorStr));
        } else {
            return 0;
        }
    }

    private int getRandomColor(){
        return colorArray[(int)((colorArray.length - 1) * Math.random())];
    }

    private boolean isColorInLocalArray(int color){
        for (int aColorArray : colorArray) {
            if (color == aColorArray) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateLabelPreview();
        labelNameLayout.setErrorEnabled(false);
        labelColorLayout.setErrorEnabled(false);
    }

    private void updateLabelPreview(){
        String name = labelNameEt.getText().toString();
        int color = getCurrentColorValue();
        if(color == 0){
            labelPreview.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
            labelPreview.setText(R.string.invalid_color);
            labelPreview.setTextColor(ViewUtils.getSecondaryTextColor(activity));
        } else {
            labelPreview.setTextColor(ViewUtils.getLabelTextColor(activity, color));
            labelPreview.setBackgroundColor(getCurrentColorValue());
            if(StringUtils.isBlank(name)){
                labelPreview.setText(R.string.label);
            } else {
                labelPreview.setText(name);
            }
        }
    }

    private boolean validateLabel(){
        String name = getCurrentName();
        if(StringUtils.isBlank(name)){
            labelNameLayout.setError(activity.getString(R.string.invalid_name));
            return false;
        }
        String color = getCurrentColor();
        if(StringUtils.isBlank(color)){
            labelColorLayout.setError(activity.getString(R.string.invalid_color));
            return false;
        }
        return true;
    }

    private void showDeleteLabelWarning(){
        new AlertDialog.Builder(activity)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.delete_label_warning)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, (dialog, which) -> deleteLabel())
                .show();
    }

    private void deleteLabel(){
        listener.onDeleteLabel(label);
    }

    private void updateOrCreateLabel(){
        String name = getCurrentName();
        String color = getCurrentColor();
        Label newLabel = new Label(name, color);
        if(isEditMode){
            listener.onUpdateLabel(label, newLabel);
        } else {
            listener.onCreateLabel(newLabel);
        }
    }

    public interface EditLabelListener{

        void onUpdateLabel(@NonNull Label oriLabel, @NonNull Label newLabel);

        void onCreateLabel(@NonNull Label label);

        void onDeleteLabel(@NonNull Label label);

    }

}
