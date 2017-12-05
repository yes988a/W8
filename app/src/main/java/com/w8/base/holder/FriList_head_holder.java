package com.w8.base.holder;

import android.view.View;
import android.widget.TextView;

import com.w8.R;

/**
 * 好友列表,有abc头
 */
public class FriList_head_holder  extends FriList_holder {
    public TextView fr_head_abc;

    public FriList_head_holder(View view) {
        super(view);
        fr_head_abc = (TextView) view.findViewById(R.id.fr_head_abc);
    }
}