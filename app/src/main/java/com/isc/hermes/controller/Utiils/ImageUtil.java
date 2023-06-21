package com.isc.hermes.controller.Utiils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Utility class containing helper methods for image manipulation.
 */
public class ImageUtil {

    private static ImageUtil instance;

    /**
     * Returns the singleton instance of ImageUtil.
     *
     * @return The ImageUtil instance.
     */
    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        } return instance;
    }

    /**
     * Crops a bitmap to a square shape.
     *
     * @param bitmap The bitmap to be cropped.
     * @return The cropped bitmap.
     */
    public Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = Math.min(width, height);

        int x = (width - size) / 2;
        int y = (height - size) / 2;

        return Bitmap.createBitmap(bitmap, x, y, size, size);
    }

    /**
     * Gets a Uri from a Bitmap object.
     *
     * @param context the current context.
     * @param bitmap the Bitmap object to get the Uri from.
     * @return the Uri corresponding to the Bitmap object, or null if an error occurs.
     */
    public Uri getUriFromBitmap(Context context, Bitmap bitmap) {
        Uri uri = null;
        try {
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                    "Title", null);
            uri = Uri.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
        }return uri;
    }


    /**
     * Updates the image of an ImageView with the image located at the specified path using Picasso.
     *
     * @param context       The context used to load the image. It should be an instance of Context.
     * @param pathImage     The path to the image file to be loaded. It should be a valid file path or a URL.
     * @param imageToUpdate The ImageView object to be updated with the loaded image.
     */
    public void updateImageOfImageView(Context context, String pathImage,
                                                   ImageView imageToUpdate) {
        Picasso.with(context).load(pathImage).into(imageToUpdate);
    }
}
