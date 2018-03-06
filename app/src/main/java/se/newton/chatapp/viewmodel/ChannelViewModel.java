package se.newton.chatapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.HashMap;
import java.util.Map;

import se.newton.chatapp.BR;
import se.newton.chatapp.adapter.MessageAdapter;
import se.newton.chatapp.service.AdapterManager;
import se.newton.chatapp.service.Database;

public class ChannelViewModel extends BaseObservable{
    private static Map<String, ChannelViewModel> viewModels = new HashMap<>();
    private String cid;
    private RequestManager glideManager;
    private MessageAdapter adapter;
    private int scrollPos;

    public static ChannelViewModel getViewModel(RequestManager glideManager, String cid) {
        if (!viewModels.containsKey(cid)) {
            ChannelViewModel viewModel = new ChannelViewModel(glideManager, cid);
            viewModels.put(cid, viewModel);
            return viewModel;
        }
        return viewModels.get(cid);
    }

    private ChannelViewModel(@NonNull RequestManager glideManager, @NonNull String cid) {
        this.cid = cid;
        this.glideManager = glideManager;
        this.adapter = AdapterManager.getAdapter(glideManager, cid);
        this.adapter.startListening();

    }

    public MessageAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public int getScrollPos() {
        return scrollPos;
    }

    public void setScrollPos(int scrollPos) {
        this.scrollPos = scrollPos;
        notifyPropertyChanged(BR.scrollPos);
    }
}
