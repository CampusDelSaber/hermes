package com.isc.hermes;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * This class control the offline mode settings UI.
 */
public class OfflineModeSettingsActivityView extends AppCompatActivity {
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
    }

    /**
     * This method create a card view of a downloaded map.
     * @param name the name of the downloaded region
     * @param size the size path of the downloaded region
     * @param ExpireTime the time whith the region expire
     */
    protected void createNewCardMapDownloaded(String name, String size, String ExpireTime){
        LinearLayout textViewsVertical = new LinearLayout(this);
        textViewsVertical.setOrientation(LinearLayout.VERTICAL);

        TextView name_downloaded_Map = new TextView(this);
        name_downloaded_Map.setText(name);
        name_downloaded_Map.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        name_downloaded_Map.setTypeface(null, Typeface.BOLD);;
        name_downloaded_Map.setPadding(0,10,0,10);

        LinearLayout textViewsHorizontal = new LinearLayout(this);
        textViewsVertical.setOrientation(LinearLayout.HORIZONTAL);

        TextView size_downloaded_Map = new TextView(this);
        size_downloaded_Map.setText(size);
        size_downloaded_Map.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        size_downloaded_Map.setPadding(0,0,10,0);

        TextView time_expire_downloaded_Map = new TextView(this);
        time_expire_downloaded_Map.setText(ExpireTime);
        time_expire_downloaded_Map.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        textViewsHorizontal.addView(time_expire_downloaded_Map);
        textViewsHorizontal.addView(size_downloaded_Map);

        textViewsVertical.addView(textViewsHorizontal);
        textViewsVertical.addView(name_downloaded_Map);

        CardView cardView = joinComponents(createCircleImageView(), textViewsVertical, createHamburgerButton());
        showCard(cardView);
    }

    /**
     * @param checkImage
     * @param vBoxTextViews
     * @param hamburgerButton
     * @return a cardView of a downloaded map
     */
    protected CardView joinComponents(CircleImageView checkImage,LinearLayout vBoxTextViews, Button hamburgerButton){
        CardView cardView = new CardView(this);
        cardView.setPadding(10,10,10,10);

        LinearLayout MainHorizontal = new LinearLayout(this);
        MainHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        MainHorizontal.addView(hamburgerButton);
        MainHorizontal.addView(vBoxTextViews);
        MainHorizontal.addView(checkImage);

        cardView.addView(MainHorizontal);

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

    protected Button createHamburgerButton(){
        return new Button(this);
    }

    /**
     * This method return a circleImageView.
     * @return a circle image view with contains the check image.
     */
    protected CircleImageView createCircleImageView(){
        CircleImageView check_image_View = new CircleImageView(this);
        check_image_View.setImageResource(R.drawable.img_back_button);
        check_image_View.setPadding(30,10,0,0);

        ViewGroup.LayoutParams params = check_image_View.getLayoutParams();
        params.width = 50;  // Ancho en píxeles
        params.height = 50; // Largo en píxeles
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

    protected void navigateToDownloadedMap(){

    }

    protected void renameDownloadedMap(){

    }

    protected void delete(){

    }
}
