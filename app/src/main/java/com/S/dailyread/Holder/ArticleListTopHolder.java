package com.S.dailyread.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.S.dailyread.Acticity.R;
import com.S.dailyread.View.Banner;



public class ArticleListTopHolder extends RecyclerView.ViewHolder{
    public Banner banner;

    public ArticleListTopHolder(View itemView) {
        super(itemView);
        banner = (Banner)itemView.findViewById(R.id.banner);
    }
}
