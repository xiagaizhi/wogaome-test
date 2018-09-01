/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yushi.leke.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yufan.library.inter.ICallBack;
import com.yushi.leke.R;
import com.yushi.leke.activity.MusicPlayerActivity;
import com.yushi.leke.widget.transformer.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.relex.circleindicator.CircleIndicator;


/**
 * @author drakeet
 */
public class SubscriptionsBannerViewBinder extends ItemViewBinder<SubscriptionBanner, SubscriptionsBannerViewBinder.ViewHolder> {
    private ICallBack callBack;
    public static final int BANNER_BINDER_MUSIC = 1;
    public static final int BANNER_BINDER_SEARCH = 2;

    public SubscriptionsBannerViewBinder(ICallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        return new ViewHolder(inflater.inflate(R.layout.item_top_subscriptions, parent, false));

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final SubscriptionBanner category) {

        holder.rightMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.OnBackResult(BANNER_BINDER_MUSIC);

            }
        });

        holder.rl_searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.OnBackResult(BANNER_BINDER_SEARCH);
            }
        });
        holder.rightMusic.setBackgroundResource(R.drawable.ic_blue_music);
        holder.viewPager.setPageTransformer(false, new ScaleTransformer());
        holder.viewPager.setPageMargin(20);
        holder.viewPager.setOffscreenPageLimit(3);
        List<String> list = new ArrayList<>();
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535618883148&di=2f748f45c0d1cdd47bc1617c59ca5e8f&imgtype=0&src=http%3A%2F%2Fimg.pintu360.com%2Feditor%2F5b2ef0f9-1071-d1a9-e76d-a9db826ddb70.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535627957737&di=97e2e0e75f0c17b859e6da832b260c02&imgtype=0&src=http%3A%2F%2Fpic116.nipic.com%2Ffile%2F20161202%2F24310187_164918489000_2.jpg");
        list.add("http://www.91danji.com/attachments/201612/12/11/3xowh1aml.jpg");
        list.add("http://www.sheng-han.com/images/webwxgetmsgimg.jpg");
        TopAdapter adapter = new TopAdapter(holder.itemView.getContext(), list);
        holder.viewPager.setAdapter(adapter);
       holder.circleIndicator.setViewPager(holder.viewPager);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleIndicator circleIndicator;
        private ViewPager viewPager;
        private ImageView rightMusic;
        private RelativeLayout rl_searchbar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewpager);
            rightMusic = itemView.findViewById(R.id.iv_anim_icon);
            rl_searchbar = itemView.findViewById(R.id.rl_searchbar);
            circleIndicator=itemView.findViewById(R.id.indicator);
        }
    }

    public class TopAdapter extends PagerAdapter {
        private List<String> list;
        private Context context;

        public TopAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(container.getContext());
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(container.getContext().getResources())
                    //设置圆形圆角参数
                    //设置圆角半径
                    .setRoundingParams(RoundingParams.fromCornersRadius(20))
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

            simpleDraweeView.setHierarchy(hierarchy);
            simpleDraweeView.setImageURI(Uri.parse(list.get(position)));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(simpleDraweeView, layoutParams);
            return simpleDraweeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
