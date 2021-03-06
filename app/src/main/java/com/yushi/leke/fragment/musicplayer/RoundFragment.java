package com.yushi.leke.fragment.musicplayer;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yushi.leke.R;


import java.lang.ref.WeakReference;


public class RoundFragment extends Fragment {

    private WeakReference<ObjectAnimator> animatorWeakReference;
    private SimpleDraweeView sdv;
    private String albumPath = "";
    private AnimatorSet mAnimatorSet;
    private boolean isEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_roundimage, container, false);
        ((ViewGroup) rootView).setAnimationCacheEnabled(false);
        if (getArguments() != null) {
            albumPath = getArguments().getString("album", "");
        }
        if (TextUtils.isEmpty(albumPath)) {
            albumPath = "res://com.yushi.leke/" + R.drawable.bg_musicpalyer_default;
            isEmpty=true;
        }

        //  CircleImageView  circleImageView = (CircleImageView) rootView.findViewById(R.id.circle);

        sdv = (SimpleDraweeView) rootView.findViewById(R.id.sdv);


        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        //设置圆角半径
        //rp.setCornersRadius(20);
        //分别设置左上角、右上角、左下角、右下角的圆角半径
        //rp.setCornersRadii(20,25,30,35);
        //分别设置（前2个）左上角、(3、4)右上角、(5、6)左下角、(7、8)右下角的圆角半径
        //rp.setCornersRadii(new float[]{20,25,30,35,40,45,50,55});
        //设置边框颜色及其宽度
        rp.setBorder(Color.BLACK, 6);

        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources())
                //设置圆形圆角参数
                .setRoundingParams(rp)
                //设置圆角半径
                //.setRoundingParams(RoundingParams.fromCornersRadius(20))
                //分别设置左上角、右上角、左下角、右下角的圆角半径
                //.setRoundingParams(RoundingParams.fromCornersRadii(20,25,30,35))
                //分别设置（前2个）左上角、(3、4)右上角、(5、6)左下角、(7、8)右下角的圆角半径
                //.setRoundingParams(RoundingParams.fromCornersRadii(new float[]{20,25,30,35,40,45,50,55}))
                //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                //.setRoundingParams(RoundingParams.asCircle())
                //设置淡入淡出动画持续时间(单位：毫秒ms)
                .setFadeDuration(300)
                //构建
                .build();


        //设置Hierarchy
        sdv.setHierarchy(hierarchy);
        //Log.e("music id",musicId + "");
//        String uri = MusicUtils.getAlbumdata(getContext().getApplicationContext(), musicId);
//
//        if (musicId != -1 && uri != null) {
//            //circleImageView.setImageBitmap(bitmap);
//            //circleImageView.setImageURI(Uri.parse(uri));
//            Uri ur = MusicUtils.getAlbumUri(getContext().getApplicationContext(), musicId);
//            sdv.setImageURI(ur);
//        } else {
//
//            // circleImageView.setImageResource(R.drawable.placeholder_disk_play_song);
//            Uri urr = Uri.parse("res:/" + R.drawable.placeholder_disk_play_song);
//            sdv.setImageURI(urr);
//        }

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                //FLog.d("Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                //  sdv.setImageURI(Uri.parse("res:/" + R.drawable.placeholder_disk_play_song));
            }
        };
        if (albumPath != null) {
            sdv.setImageURI(Uri.parse(albumPath));
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        animatorWeakReference = new  WeakReference<ObjectAnimator>(new ObjectAnimator());
//        animator = animatorWeakReference.get();
        animatorWeakReference = new WeakReference(ObjectAnimator.ofFloat(getView(), "rotation", new float[]{0.0F, 360.0F}));
        ObjectAnimator animator = animatorWeakReference.get();
        //animator = ObjectAnimator.ofFloat(getView(), "rotation", new float[]{0.0F, 360.0F});
        animator.setRepeatCount(-1);
        animator.setDuration(25000L);
        animator.setInterpolator(new LinearInterpolator());
        if (getView() != null)
            getView().setTag(R.id.tag_animator, animator);
        if (isEmpty) {
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.play(animator);
            mAnimatorSet.start();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setUri(Uri uri) {
        if (!albumPath.equals(uri.toString())) {
            sdv.setImageURI(uri);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("roundfragment", " id = " + hashCode());
        ObjectAnimator animator = animatorWeakReference.get();
        if (animator != null) {
            animator.cancel();
            Log.e("roundfragment", " id = " + hashCode() + "  , animator destroy");
        }
//        RefWatcher refWatcher = MainApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
        if (mAnimatorSet!= null && mAnimatorSet.isRunning()){
           mAnimatorSet.cancel();
        }
    }


}
