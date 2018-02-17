package se.newton.chatapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.newton.chatapp.R;

/**
 * Created by oretx on 2018-02-17.
 */

public class ChannelThreeFragment extends Fragment {

    public ChannelThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.channel_three, container, false);
    }
}
