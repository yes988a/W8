package com.w8.base;

public class NologinActivity extends WxActivity {
    protected String TAG = NologinActivity.class.getSimpleName();//其他继承者，会改变它

    @Override
    protected void onStart() {
        super.onStart();
        AppUtil.quitSafe(NologinActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtil.setTag(NologinActivity.class.getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtil.setTag(NologinActivity.class.getSimpleName());
    }

    /**
     * 密码简单验证
     */
    protected boolean testPass(String pass) {
        if (pass == null || pass.length() > RetNumUtil.n_32 || pass.length() < RetNumUtil.n_6) {
            return false;
        }
        return true;
    }
}
