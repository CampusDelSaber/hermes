package com.isc.hermes;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * This class represents the Offline Mode Settings UI.
 */
public class OfflineModeSettingsActivityView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private LinearLayout vBoxDownloadedMaps;

    /**
     * Method for creating the activity configuration.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_mode_settings_view);
        vBoxDownloadedMaps = findViewById(R.id.vBoxMapsDownloaded);
    }

    /**
     * Creates a new card view for a downloaded map.
     *
     * @param name The name of the downloaded region.
     * @param size The size of the downloaded region.
     */
    protected void createNewCardMapDownloaded(String name, String size) {
        LinearLayout textViewsVertical = createVerticalLinearLayout();
        textViewsVertical.setPadding(100, 0, 100, 0);

        TextView nameTextView = createTextView(name, 18, Typeface.BOLD);
        TextView sizeTextView = createTextView(size, 16, Typeface.NORMAL);

        textViewsVertical.addView(nameTextView);
        textViewsVertical.addView(sizeTextView);

        CardView cardView = joinComponents(createCheckImageView(), textViewsVertical, createButtonPopup());
        showCard(cardView);
    }

    /**
     * Creates a vertical LinearLayout.
     *
     * @return The created vertical LinearLayout.
     */
    protected LinearLayout createVerticalLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.6f
        ));
        return linearLayout;
    }

    /**
     * Creates a TextView with the specified text, text size, and text style.
     *
     * @param text      The text to be displayed.
     * @param textSize  The size of the text.
     * @param textStyle The style of the text.
     * @return The created TextView.
     */
    protected TextView createTextView(String text, int textSize, int textStyle) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTypeface(null, textStyle);
        textView.setPadding(0, 10, 0, 10);
        return textView;
    }

    /**
     * Joins the components (check image, text views, and popup button) to create a CardView.
     *
     * @param checkImage   The check image.
     * @param vBoxTextViews A vertical LinearLayout containing the text views.
     * @param popupMenu    The popup button.
     * @return The created CardView.
     */
    protected CardView joinComponents(CircleImageView checkImage, LinearLayout vBoxTextViews, CircleImageView popupMenu) {
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.HORIZONTAL);
        cardView.addView(container);

        cardView.setPadding(10, 10, 10, 10);

        container.addView(checkImage);
        container.addView(vBoxTextViews);
        container.addView(popupMenu);

        return cardView;
    }

    /**
     * Displays the created CardView.
     *
     * @param cardView The CardView to be displayed.
     */
    protected void showCard(CardView cardView) {
        vBoxDownloadedMaps.addView(cardView);
    }

    /**
     * Creates a popup button as a CircleImageView.
     *
     * @return The created popup button.
     */
    protected CircleImageView createButtonPopup() {
        CircleImageView popupButton = new CircleImageView(this);
        popupButton.setImageResource(R.drawable.img_tree_points_vertical);
        popupButton.setPadding(10, 10, 30, 0);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        popupButton.setLayoutParams(params);

        popupButton.setOnClickListener(this::showPopupMenu);
        return popupButton;
    }

    /**
     * Creates a check image as a CircleImageView.
     *
     * @return The created check image.
     */
    protected CircleImageView createCheckImageView() {
        CircleImageView checkImageView = new CircleImageView(this);
        checkImageView.setImageResource(R.drawable.img_check);
        checkImageView.setPadding(30, 10, 10, 0);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        checkImageView.setLayoutParams(params);

        return checkImageView;
    }

    /**
     * Handles the click event for the popup menu items.
     *
     * @param item The menu item that was clicked.
     * @return True if the click event was handled, false otherwise.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rename:
                renameDownloadedMap();
                return true;
            case R.id.navigateTo:
                navigateToDownloadedMap();
                return true;
            case R.id.delete:
                deleteDownloadedMap();
                return true;
            default:
                return false;
        }
    }

    /**
     * Shows the popup menu.
     *
     * @param view The view that triggered the popup menu.
     */
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.downloaded_maps_options);
        popupMenu.show();
    }

    /**
     * Navigates to the selected downloaded map.
     */
    protected void navigateToDownloadedMap() {
        // Code to navigate to the selected downloaded map
    }

    /**
     * Renames the selected downloaded map.
     */
    protected void renameDownloadedMap() {
        // Code to rename the selected downloaded map
    }

    /**
     * Deletes the selected downloaded map.
     */
    protected void deleteDownloadedMap() {
        // Code to delete the selected downloaded map
    }

    /**
     * Handles the click event for the "Download New Map" button.
     *
     * @param view The view that triggered the click event.
     */
    public void downloadNewMap(View view) {
        // Code to download a new map
    }

    /**
     * Closes the activity and returns to the map.
     *
     * @param view The view that triggered the click event.
     */
    public void backToMap(View view) {
        finish();
    }
}
