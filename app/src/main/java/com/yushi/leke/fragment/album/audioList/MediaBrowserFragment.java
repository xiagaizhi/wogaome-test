package com.yushi.leke.fragment.album.audioList;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.Toast;

import com.yufan.library.Global;
import com.yufan.library.base.BaseListFragment;
import com.yufan.library.inject.VuClass;
import com.yufan.library.manager.SPManager;
import com.yushi.leke.uamp.ui.MediaBrowserProvider;
import com.yushi.leke.uamp.utils.LogHelper;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(MediaBrowserVu.class)
public class MediaBrowserFragment extends BaseListFragment<MediaBrowserContract.IView> implements MediaBrowserContract.Presenter {
    private MultiTypeAdapter adapter;
    private String TAG="MediaBrowserFragment";


    private MediaBrowserProvider mMediaFragmentListener;
    private String mAlbumId;




    @Override
    public void onStart() {
        super.onStart();

        // fetch browsing information to fill the listview:
        MediaBrowserCompat mediaBrowser = mMediaFragmentListener.getMediaBrowser();

        LogHelper.d(TAG, "fragment.onStart, mediaId=", mAlbumId,
                "  onConnected=" + mediaBrowser.isConnected());

        if (mediaBrowser.isConnected()) {
            onConnected();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        MediaBrowserCompat mediaBrowser = mMediaFragmentListener.getMediaBrowser();
        if (mediaBrowser != null && mediaBrowser.isConnected() && mAlbumId != null) {
            mediaBrowser.unsubscribe(mAlbumId);
        }
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.unregisterCallback(mMediaControllerCallback);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMediaFragmentListener = null;
    }

    public String getMediaId() {
        Bundle args = getArguments();
        if (args != null) {
            return args.getString(Global.BUNDLE_KEY_ALBUMID);
        }
        return null;
    }

    // Called when the MediaBrowser is connected. This method is either called by the
    // fragment.onStart() or explicitly by the activity in the case where the connection
    // completes after the onStart()
    // 当MediaBrowser已经连接时调用
    // 这个方法可在 fragment.onStart() 或 已知Activity在onStart后完成了连接的情况下调用
    public void onConnected() {
        if (isDetached()) {
            return;
        }
        mAlbumId = getMediaId();
        if (mAlbumId != null) {
            SPManager.getInstance().saveValue(Global.SP_KEY_ALBUM_ID,mAlbumId);
        }

        getVu().updateTitle(mAlbumId);

        // Unsubscribing before subscribing is required if this mediaId already has a subscriber
        // on this MediaBrowser instance. Subscribing to an already subscribed mediaId will replace
        // the callback, but won't trigger the initial callback.onChildrenLoaded.
        //
        // This is temporary: A bug is being fixed that will make subscribe
        // consistently call onChildrenLoaded initially, no matter if it is replacing an existing
        // subscriber or not. Currently this only happens if the mediaID has no previous
        // subscriber or if the media content changes on the service side, so we need to
        // unsubscribe first.
        mMediaFragmentListener.getMediaBrowser().unsubscribe(mAlbumId);

        mMediaFragmentListener.getMediaBrowser().subscribe(mAlbumId, mSubscriptionCallback);

        // Add MediaController callback so we can redraw the list when metadata changes:
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.registerCallback(mMediaControllerCallback);
        }
    }

    public void onRefresh(){
        mMediaFragmentListener.getMediaBrowser().unsubscribe(mAlbumId);

        mMediaFragmentListener.getMediaBrowser().subscribe(mAlbumId, mSubscriptionCallback);
    }

    // Receive callbacks from the MediaController. Here we update our state such as which queue
    // is being shown, the current title and description and the PlaybackState.
    /**
     * 媒体控制器控制播放过程中的回调接口
     */
    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    if (metadata == null) {
                        return;
                    }
                    SPManager.getInstance().saveValue(Global.SP_KEY_MEDIA_ID,metadata.getDescription().getMediaId());
                    LogHelper.d(TAG, "Received metadata change to media ", metadata.getDescription().getMediaId());
                    getVu().notifyDataSetChanged();
                }//播放的媒体数据发生变化时的回调

                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                    LogHelper.d(TAG, "Received state change: ", state);
                    getVu().notifyDataSetChanged();
                    getVu().getRecyclerView().getAdapter().notifyDataSetChanged();
                }//播放状态发生改变时的回调
            };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMediaFragmentListener = (MediaBrowserProvider) activity;
    }

    /**
     * 向媒体流量服务(MediaBrowserService)发起媒体浏览请求的回调接口
     */
    private final MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    try {
                        LogHelper.d(TAG, "fragment onChildrenLoaded, parentId=" + parentId +
                                "  count=" + children.size());
                        list.clear();
                        list.addAll(children);
                        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                    } catch (Throwable t) {
                        LogHelper.e(TAG, "Error on childrenloaded", t);
                    }
                }

                @Override
                public void onError(@NonNull String id) {
                    LogHelper.e(TAG, "browse fragment subscription onError, id=" + id);
                    Toast.makeText(getActivity(), com.yushi.leke.plugin.musicplayer.R.string.error_loading_media, Toast.LENGTH_LONG).show();

                }
            };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new MultiTypeAdapter();
        adapter.setItems(list);
        adapter.register(MediaBrowserCompat.MediaItem.class,new MediaBrowserViewBinder(getActivity(),new MediaBrowserViewBinder.OnItemClick() {
            @Override
            public void onClick(MediaBrowserCompat.MediaItem mediaItem) {
                mMediaFragmentListener.onMediaItemSelected(mediaItem);
                getVu().getRecyclerView().getAdapter().notifyDataSetChanged();
            }
        }));
        vu.getRecyclerView().setAdapter(adapter);
    }

    @Override
    public void onLoadMore(int index) {

    }



}
