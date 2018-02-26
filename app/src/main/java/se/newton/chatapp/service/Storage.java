package se.newton.chatapp.service;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;

public final class Storage {
    private static final String TAG = "Storage";

    private Storage() {
    }


    // -- Image uploading --

    // Uploads an image to Firebase. It returns an UploadTask object which can be used to check
    //  progress and attach listeners.
    @NonNull
    public static UploadTask uploadImage(Uri file, String filename) {
        return FirebaseStorage.getInstance()
                .getReference("images/" + filename).putFile(file);
    }

    // Same as above, but attaching a callback that executes when the upload is complete
    public static UploadTask uploadImage(Uri file, String filename, Database.Callback<Task<UploadTask.TaskSnapshot>> onCompleteCallback) {
        UploadTask task = uploadImage(file, filename);
        task.addOnCompleteListener(onCompleteCallback::callback);
        return task;
    }

    // This method takes an UploadTask and calculates the progress percentage (between 0-1)
    public static double getProgress(UploadTask task) {
        UploadTask.TaskSnapshot snap = task.getSnapshot();
        return ((double) snap.getBytesTransferred()) / snap.getTotalByteCount();
    }
}
