package se.newton.chatapp.service;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;

public final class Storage {
    private static final String TAG = "Storage";

    private Storage(){}

    public void uploadImage(File file, String filename, Database.Callback<ImageTask> onCompleteCallback){
        ImageTask task = new ImageTask(FirebaseStorage.getInstance()
                .getReference("images/" + filename).putFile(Uri.fromFile(file)));
    }

    public final class ImageTask {
        UploadTask uploadTask;
        ImageTask(UploadTask uploadTask){
            this.uploadTask = uploadTask;
        }


    }
}
