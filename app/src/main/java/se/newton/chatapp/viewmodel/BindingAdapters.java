package se.newton.chatapp.viewmodel;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import se.newton.chatapp.R;

public class BindingAdapters {
    @BindingAdapter("profile")
    public static void loadProfileImage(ImageView view, Bitmap image) {
        if (image == null)
            view.setImageResource(R.drawable.ic_profile_image_placeholder_circular);
        else
            view.setImageBitmap(image);
    }
}
