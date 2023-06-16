package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

/**
 * Utility class containing helper methods for image manipulation.
 */
public class Utils {

    /**
     * Crops a bitmap to a square shape.
     *
     * @param bitmap The bitmap to be cropped.
     * @return The cropped bitmap.
     */
    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = Math.min(width, height);

        int x = (width - size) / 2;
        int y = (height - size) / 2;

        return Bitmap.createBitmap(bitmap, x, y, size, size);
    }

    public static Uri getUriFromBitmap(Context context, Bitmap bitmap) {
        Uri uri = null;
        try {
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                    "Title", null);
            uri = Uri.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
        }return uri;
    }

}
