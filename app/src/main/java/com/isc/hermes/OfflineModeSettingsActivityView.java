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
 * This class control the offline mode settings UI.
 */
public class OfflineModeSettingsActivityView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    /**
     * Method for creating a configuration of the activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_mode_settings_view);
        createNewCardMapDownloaded("Prueba View","100 Mb");
    }

    /**
     * This method create a card view of a downloaded map.
     * @param name the name of the downloaded region
     * @param size the size path of the downloaded region
     */
    protected void createNewCardMapDownloaded(String name, String size){
        LinearLayout textViewsVertical = new LinearLayout(this);
        textViewsVertical.setOrientation(LinearLayout.VERTICAL);
        textViewsVertical.setPadding(100,0,100,0);

        TextView name_downloaded_Map = new TextView(this);
        name_downloaded_Map.setText(name);
        name_downloaded_Map.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        name_downloaded_Map.setTypeface(null, Typeface.BOLD);;
        name_downloaded_Map.setPadding(0,10,0,10);


        TextView size_downloaded_Map = new TextView(this);
        size_downloaded_Map.setText(size);
        size_downloaded_Map.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        size_downloaded_Map.setPadding(0,0,10,0);

        textViewsVertical.addView(size_downloaded_Map);
        textViewsVertical.addView(name_downloaded_Map);

        CardView cardView = joinComponents(createCheckImageView(), textViewsVertical, createButtonPopup());
        showCard(cardView);
    }

    /**
     * This method join all parts of the downloaded map card view.
     * @param checkImage a check image
     * @param vBoxTextViews A vbox containing the two text Views needed from the downloaded map.
     * @param popupMenu is the view that will serve as the button to display the popup menu.
     * @return a cardView of a downloaded map
     */
    protected CardView joinComponents(CircleImageView checkImage,LinearLayout vBoxTextViews, CircleImageView popupMenu){
        CardView cardView = new CardView(this);
        cardView.setPadding(10,10,10,10);

        cardView.addView(popupMenu);
        cardView.addView(vBoxTextViews);
        cardView.addView(checkImage);

        return cardView;
    }

    /**
     * This method join the new card view to the UI of download maps.
     * @param cardView the cardView with join to the downloads maps.
     */
    protected void showCard(CardView cardView){
        LinearLayout vBoxDownloadedMaps = findViewById(R.id.vBoxMapsDownloaded);
        vBoxDownloadedMaps.addView(cardView);
    }

    /**
     * This method creates a view image with a check image.
     * @return the button whith show popupButton
     */
    protected CircleImageView createButtonPopup(){
        CircleImageView popupButton = new CircleImageView(this);
        popupButton.setImageResource(R.drawable.img_tree_points_vertical);
        popupButton.setPadding(10,10,30,0);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50,50);
        popupButton.setLayoutParams(params);

        View.OnClickListener miClickListener = this::showPopupMenu;

        popupButton.setOnClickListener(miClickListener);
        return popupButton;
    }

    /**
     * This method return a circleImageView.
     * @return a circle image view with contains the check image.
     */
    protected CircleImageView createCheckImageView(){
        CircleImageView check_image_View = new CircleImageView(this);
        check_image_View.setImageResource(R.drawable.img_check);
        check_image_View.setPadding(30,10,10,0);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50,50);
        check_image_View.setLayoutParams(params);

        return check_image_View;
    }

    /**
     * @param view view
     */
    public void download_new_map(View view) {
        // code to download new map
    }

    /**
     * This method close the activity offline settings.
     *
     * @param view view
     */
    public void back_to_map(View view) {
        finish();
    }

    /**
     * This method assigns an action to the popup menu options.
     * @param item the menu item that was clicked
     * @return boolean
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rename:
                //code
                return true;
            case R.id.navigateTo:
                //code
                return true;
            case R.id.delete:
                //code
                return true;
            default:
                return false;
        }
    }

    /**
     * This method displays the options menu of the downloaded map.
     * @param view view
     */
    public void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.downloaded_maps_options);
        popupMenu.show();
    }

    protected void navigateToDownloadedMap(){

    }

    protected void renameDownloadedMap(){

    }

    protected void delete(){

    }

}
