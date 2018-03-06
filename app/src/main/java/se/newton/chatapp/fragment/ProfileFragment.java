package se.newton.chatapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import se.newton.chatapp.R;
import se.newton.chatapp.service.Database;
import se.newton.chatapp.viewmodel.ProfileViewModel;
import se.newton.chatapp.databinding.ProfileFragmentBinding;

/**
 * Created by fr9b on 2018-02-21.
 */

public class ProfileFragment extends Fragment {

    private String fUserUid;
    private ProfileViewModel viewModel;
    private static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String fUserUid) {
        Log.d(TAG, "Creating profile fragment");
        ProfileFragment fragment = new ProfileFragment();
        fragment.fUserUid = fUserUid;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ProfileViewModel(Glide.with(this), fUserUid);

        getActivity().setTitle("Profile");

        // Inflate the layout for this fragment
        ProfileFragmentBinding binding = DataBindingUtil.inflate(inflater,R.layout.profile_fragment, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

}
