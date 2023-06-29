package com.isc.hermes.controller.PopUp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * This class manages the creation, display, and dismissal of a progress dialog.
 * It also provides methods to update the progress bar and percentage text.
 */
public class ProgressDialogManager {
    private final Activity activity;
    private AlertDialog progressDialog;
    private ProgressBar progressBar;
    private TextView progressText;

    /**
     * This method constructs a new ProgressDialogManager object.
     *
     * @param activity The activity in which the progress dialog is shown.
     */
    public ProgressDialogManager(Activity activity,String message) {
        this.activity = activity;
        createProgressDialog(message);
    }

    /**
     * Creates the components for the progress dialog and assigns them to variables.
     *
     * @param message The message to be displayed in the loading text.
     * @param builder The AlertDialog.Builder instance for creating the dialog.
     */
    private void createComponents(String message, AlertDialog.Builder builder) {
        LinearLayout layout = createLayoutContainer();
        LinearLayout textLayout = createTextLayout();
        TextView loadingText = createLoadingTextView(message);
        TextView progressText = createProgressTextView();
        ProgressBar progressBar = createProgressBar();

        textLayout.addView(loadingText);
        textLayout.addView(progressText);
        layout.addView(textLayout);
        layout.addView(progressBar);
        builder.setView(layout);
        assignValuesToVariables(builder, progressBar, progressText);
    }

    /**
     * Assigns the created components to their respective class variables.
     *
     * @param builder      The AlertDialog.Builder instance used to create the dialog.
     * @param progressBar  The ProgressBar component of the dialog.
     * @param progressText The TextView component for displaying progress.
     */
    private void assignValuesToVariables(AlertDialog.Builder builder, ProgressBar progressBar,
                                         TextView progressText){
        this.progressDialog = builder.create();
        this.progressBar = progressBar;
        this.progressText = progressText;
    }

    /**
     * This method creates a new AlertDialog with a progress bar and percentage text.
     */
    private void createProgressDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        createComponents(message, builder);
    }

    /**
     * This method creates a LinearLayout container for the progress dialog.
     *
     * @return The created LinearLayout.
     */
    private LinearLayout createLayoutContainer() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        return layout;
    }

    /**
     * This method creates a LinearLayout for holding the loading text and progress percentage.
     *
     * @return The created LinearLayout.
     */
    private LinearLayout createTextLayout() {
        LinearLayout textLayout = new LinearLayout(activity);
        textLayout.setOrientation(LinearLayout.HORIZONTAL);
        textLayout.setGravity(Gravity.CENTER);
        return textLayout;
    }

    /**
     * This method creates a TextView for displaying the loading text.
     *
     * @return The created TextView.
     */
    private TextView createLoadingTextView(String message) {
        TextView loadingText = new TextView(activity);
        loadingText.setText(message);
        loadingText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        )); return loadingText;
    }

    /**
     * This method creates a TextView for displaying the progress percentage.
     *
     * @return The created TextView.
     */
    private TextView createProgressTextView() {
        TextView progressText = new TextView(activity);
        progressText.setText("0%");
        progressText.setGravity(Gravity.END);
        progressText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        )); return progressText;
    }

    /**
     * This method creates a ProgressBar for indicating the progress.
     *
     * @return The created ProgressBar.
     */
    private ProgressBar createProgressBar() {
        ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )); return progressBar;
    }

    /**
     * This method shows the progress dialog.
     */
    public void showProgressDialog() {
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    /**
     * This method dismiss the progress dialog.
     */
    public void dismissProgressDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    /**
     * This method updates the progress of the progress bar and the percentage text.
     *
     * @param progress The progress value to set.
     */
    @SuppressLint("SetTextI18n")
    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
        progressText.setText(progress + "%");
    }
}