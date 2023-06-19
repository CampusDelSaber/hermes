package com.isc.hermes.utils.offline;

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

public class OfflineCardView {

    private Context context;

    public OfflineCardView(Context context) {
        this.context = context;
    }

    /**
     * Creates a new card view for a downloaded map.
     *
     * @param name The name of the downloaded region.
     * @param size The size of the downloaded region.
     */
    public CardView createNewCardMapDownloaded(String name, String size, PopupMenu.OnMenuItemClickListener onClickListener) {
        LinearLayout textViewsVertical = createVerticalLinearLayout();
        textViewsVertical.setPadding(100, 0, 100, 0);
        textViewsVertical.addView(createTextView(name, 18, Typeface.BOLD));
        textViewsVertical.addView(createTextView(size, 15, Typeface.BOLD));
        return joinComponents(createCheckImageView(), textViewsVertical, createButtonPopup(onClickListener));
    }

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
    private CircleImageView createCheckImageView() {
        CircleImageView checkImageView = new CircleImageView(context);
        checkImageView.setImageResource(R.drawable.img_check);
        checkImageView.setPadding(30, 10, 10, 0);
        checkImageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        return checkImageView;
    }


    /**
     * Creates a popup button as a CircleImageView.
     *
     * @return The created popup button.
     */
    private CircleImageView createButtonPopup(PopupMenu.OnMenuItemClickListener onClickListener) {
        CircleImageView popupButton = new CircleImageView(context);
        popupButton.setImageResource(R.drawable.img_tree_points_vertical);
        popupButton.setPadding(10, 10, 30, 0);
        popupButton.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        popupButton.setOnClickListener(v -> showPopupMenu(v, onClickListener));
        return popupButton;
    }

    /**
     * Creates a vertical LinearLayout.
     *
     * @return The created vertical LinearLayout.
     */
    private LinearLayout createVerticalLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(context);
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
        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.HORIZONTAL);
        cardView.addView(container);

        cardView.setPadding(10, 10, 10, 10);

        container.addView(checkImage);
        container.addView(vBoxTextViews);
        container.addView(popupMenu);

        return cardView;
    }


}
