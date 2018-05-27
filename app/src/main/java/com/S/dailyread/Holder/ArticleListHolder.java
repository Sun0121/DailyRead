package com.S.dailyread.Holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.S.dailyread.Acticity.R;
import com.S.dailyread.Listener.OnArticleItemClickListener;



public class ArticleListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView articleTitle;

    public ImageView articleImage;

    public OnArticleItemClickListener itemClickListener;

    public ArticleListHolder(View itemView) {
        super(itemView);
        articleTitle = (TextView) itemView.findViewById(R.id.article_title);
        articleImage = (ImageView) itemView.findViewById(R.id.article_image);
        articleTitle.setOnClickListener(this);
        articleImage.setOnClickListener(this);
    }

    public void setItemClickListener(OnArticleItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.OnItemClickListener(getAdapterPosition());         //此处使用的itemClickListener据是ArticleListAdapter中定义的clickListener
        }
    }
}
