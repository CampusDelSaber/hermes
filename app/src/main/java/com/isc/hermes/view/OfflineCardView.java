package com.isc.hermes.view;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.isc.hermes.R;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * This class is responsible for creating CardViews.
 */
public class OfflineCardView {

    private Context context;

    public OfflineCardView(Context context) {
        this.context = context;
    }

    /**
     * Creates a new card view for a downloaded map.
     *
     * @param name The name of the downloaded region.
     */
    public CardView createNewCardMapDownloaded(String name, PopupMenu.OnMenuItemClickListener onClickListener) {
        LinearLayout textViewsVertical = createLinearLayout(LinearLayout.VERTICAL);
        textViewsVertical.setPadding(100, 0, 100, 0);
        textViewsVertical.addView(createTextView(name, 18, Typeface.BOLD));
        return joinComponents(createImageView(null,R.drawable.img_check, 30, 10), textViewsVertical, createImageView(onClickListener,R.drawable.img_tree_points_vertical, 10, 30));
    }

    /**
     * This method shows a menu popup
     *
     * @param view            parent view
     * @param onClickListener listener hears when a button is clicked
     */

    public void showPopupMenu(View view, PopupMenu.OnMenuItemClickListener onClickListener) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(onClickListener);
        popupMenu.inflate(R.menu.downloaded_maps_options);
        popupMenu.show();
    }

    /**
     * Creates a check image as a CircleImageView.
     *
     * @return The created check image.
     */
    private CircleImageView createImageView(PopupMenu.OnMenuItemClickListener onClickListener , int resID, int left, int right) {
        CircleImageView circleImageView = new CircleImageView(context);
        circleImageView.setImageResource(resID);
        circleImageView.setPadding(left, 10, right, 0);
        circleImageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        if(onClickListener != null){
            circleImageView.setOnClickListener(v -> showPopupMenu(v, onClickListener));
        }
        return circleImageView;
    }

    /**
     * Creates a vertical LinearLayout.
     *
     * @return The created vertical LinearLayout.
     */
    private LinearLayout createLinearLayout(int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
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
    private TextView createTextView(String text, int textSize, int textStyle) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTypeface(null, textStyle);
        textView.setPadding(0, 10, 0, 10);
        return textView;
    }

    /**
     * Joins the components (check image, text views, and popup button) to create a CardView.
     *
     * @param checkImage    The check image.
     * @param vBoxTextViews A vertical LinearLayout containing the text views.
     * @param popupMenu     The popup button.
     * @return The created CardView.
     */
    private CardView joinComponents(CircleImageView checkImage, LinearLayout vBoxTextViews, CircleImageView popupMenu) {
        CardView cardView = new CardView(context);
        cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        LinearLayout container = createLinearLayout(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        cardView.addView(container);

        cardView.setPadding(10, 10, 10, 10);
        container.addView(checkImage);
        container.addView(vBoxTextViews);
        container.addView(popupMenu);
        return cardView;
    }
}