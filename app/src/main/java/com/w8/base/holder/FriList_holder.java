package com.w8.base.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w8.R;

/**
 * 好友列表,无头
 */
public class FriList_holder extends RecyclerView.ViewHolder {
    public LinearLayout fr_biglinear;
    public TextView fr_rid;
    public ImageView fr_phone;
    public ImageView fr_imagehead;
    public TextView fr_uname;

    public FriList_holder(View view) {
        super(view);
        fr_biglinear = (LinearLayout) view.findViewById(R.id.fr_biglinear);
        fr_rid = (TextView) view.findViewById(R.id.fr_rid);
        fr_phone = (ImageView) view.findViewById(R.id.fr_phone);
        fr_imagehead = (ImageView) view.findViewById(R.id.fr_imagehead);
        fr_uname = (TextView) view.findViewById(R.id.fr_uname);
    }
}
