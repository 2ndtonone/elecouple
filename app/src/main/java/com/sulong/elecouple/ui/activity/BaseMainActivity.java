package com.sulong.elecouple.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.views.CustomToast;

/**
 * Created by ydh on 2017/4/7.
 */

public class BaseMainActivity extends BaseActivity {

    private static boolean isExit = false;
    private Handler mHandler = new Handler();
    private Runnable mCancelExitTask = new Runnable() {
        @Override
        public void run() {
            isExit = false;//取消退出
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTintResource(R.color.gray_trans);
    }

    @Override
    protected void addStatusBarPadding() {
        //do nothing to skip add status bar padding for home page.
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitBy2Click() {
        if (!isExit) {
            isExit = true;//准备退出
            CustomToast.showToast(R.string.toast_click_again_to_exit);
            mHandler.removeCallbacksAndMessages(mCancelExitTask);
            mHandler.postDelayed(mCancelExitTask, 2000); //如果2秒钟内没有按下返回键,则启动定时器取消刚才执行的任务
        } else {
            this.finish();
        }
    }
}
