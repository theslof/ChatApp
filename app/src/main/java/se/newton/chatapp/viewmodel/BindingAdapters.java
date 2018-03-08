package se.newton.chatapp.viewmodel;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {
    @BindingAdapter(value={"image", "placeholder"}, requireAll=false)
    public static void loadProfileImage(ImageView view, Bitmap image,
                                        Drawable placeHolder) {
        if (image == null)
            view.setImageDrawable(placeHolder);
        else
            view.setImageBitmap(image);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(new RequestOptions()
                        //.placeholder(R.drawable.ic_profile_image_placeholder)
                        .override(300)
                )
                .into(view);
    }
}
