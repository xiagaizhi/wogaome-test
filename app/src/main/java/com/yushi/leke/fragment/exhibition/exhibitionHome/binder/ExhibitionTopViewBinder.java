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

package com.yushi.leke.fragment.exhibition.exhibitionHome.binder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yufan.library.inter.ICallBack;
import com.yushi.leke.R;
import com.yushi.leke.UIHelper;
import com.yushi.leke.fragment.exhibition.exhibitionHome.bean.ExhibitionTopInfo;

import me.drakeet.multitype.ItemViewBinder;


/**
 *路演大厅 头部
 */
public class ExhibitionTopViewBinder extends ItemViewBinder<ExhibitionTopInfo, ExhibitionTopViewBinder.ViewHolder> {
    private ICallBack callBack;
    public static final int MUSIC_EVENT = 1;
    public static final int HISTORY_EVENT = 2;
    private Activity activity;
    public ExhibitionTopViewBinder(ICallBack callBack, Activity activity) {
this.callBack=callBack;
this.activity=activity;
    }

    @Override
    protected @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        return new ViewHolder(inflater.inflate(R.layout.item_top_exhibition, parent, false));

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ExhibitionTopInfo category) {
        holder.rightMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.OnBackResult(MUSIC_EVENT);
                }

            }
        });
        UIHelper. putImageView(activity, holder.rightMusic,ExhibitionTopViewBinder.class);
        holder.img_history_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callBack!=null){
                    callBack.OnBackResult(HISTORY_EVENT);
                }
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView rightMusic;
        private ImageView img_history_ic;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rightMusic=itemView.findViewById(R.id.iv_anim_icon);
            img_history_ic=itemView.findViewById(R.id.img_history_icon);
        }
    }


}
