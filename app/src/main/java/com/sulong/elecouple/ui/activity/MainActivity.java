package com.sulong.elecouple.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.eventbus.LoginSuccessEvent;
import com.sulong.elecouple.ui.dialog.CommonDialogFragment;
import com.sulong.elecouple.ui.fragment.HomeFragment;
import com.sulong.elecouple.ui.fragment.MyCenterFragment;
import com.sulong.elecouple.ui.fragment.SearchFragment;
import com.sulong.elecouple.utils.AppUpdateChecker;
import com.sulong.elecouple.utils.Debug;
import com.hk.lib.appupdate.AppUpdateManager;
import com.liulishuo.filedownloader.FileDownloader;

import de.greenrobot.event.EventBus;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author qingyou.ren
 */
@RuntimePermissions
public class MainActivity extends BaseMainActivity {

    public static final String EXTRA_PAGE_INDEX = "EXTRA_INDEX";
    public static final int PAGE_INDEX_HOME = 0;
    public static final int PAGE_INDEX_MINE = 1;
    private Class mTabFragmentClasses[] = {
            HomeFragment.class,
            SearchFragment.class,
            MyCenterFragment.class
    };
    private int[] mTabIconsNormal = new int[]{
            R.drawable.tab_home_normal,
            R.drawable.tab_home_normal,
            R.drawable.tab_mycenter_normal,
    };
    private int[] mTabIconsSelected = new int[]{
            R.drawable.tab_home_selected,
            R.drawable.tab_home_selected,
            R.drawable.tab_mycenter_selected,
    };
    private int mTabTexts[] = new int[]{
            R.string.main_home,
            R.string.main_search,
            R.string.main_find,
            R.string.main_mycenter
    };
    private Fragment[] mFragments;
    private ViewHolder mHolder = new ViewHolder();
    private int mCurIndex = -1;
    private int mLastIndex = -1;
    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag(R.id.index);
            switchPage(index);
            if (index != PAGE_INDEX_HOME
                    && mFragments[PAGE_INDEX_HOME] != null) {
                HomeFragment homeFragment = (HomeFragment) mFragments[PAGE_INDEX_HOME];
//                homeFragment.stopListViewScroll();
            }
            switch (index) {
                case PAGE_INDEX_MINE: {
                    setTintColor(Color.TRANSPARENT);
                }
                break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTintResource(R.color.red);
        findViews();
        initView();
        handleArguments(getIntent());
        EventBus.getDefault().register(this);
        FileDownloader.getImpl().bindService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivityPermissionsDispatcher.requestReadPhoneStateWithCheck(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 我们自己的用户体系登录成功，但尚未登录环信
//        if (LoginManager.getInstance().isLogin() && !HXSDKHelper.getInstance().isLogined()) {
//            // 立即登录环信
//            HXSDKHelper.getInstance().login();
//        }
        // 我们自己的用户体系未登录，但尚已登录环信
//        if (!LoginManager.getInstance().isLogin() && HXSDKHelper.getInstance().isLogined()) {
//            // 退出环信登录
//            HXSDKHelper.getInstance().logout();
//        }
        checkAppUpdate();
    }


    public void onEventMainThread(LoginSuccessEvent event) {
        updateTabMineRedPoint();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        FileDownloader.getImpl().unBindServiceIfIdle();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleArguments(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 将按键事件传递给Fragment处理
//        if (mCurIndex == PAGE_INDEX_NEARBY) {
//            NearbyFragment fragment = (NearbyFragment) getSupportFragmentManager().findFragmentByTag(getTabFragmentTag(mCurIndex));
//            if (fragment != null) {
//                boolean onKeyDown = fragment.onKeyDown(keyCode, event);
//                if (onKeyDown) {
//                    return true;
//                }
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void requestReadPhoneState() {
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    public void onReadPhoneStatePermissionDenied() {
        String permissionName = getString(R.string.permission_read_phone_state);
        String toastText = getString(R.string.permission_deny_temporarily, permissionName, permissionName);
        showMessageDialog(toastText);
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    public void onReadPhoneStatePermissionNeverAsk() {
        String permissionName = getString(R.string.permission_read_phone_state);
        String toastText = getString(R.string.permission_deny_never_ask, permissionName, permissionName);
        showMessageDialog(toastText);
    }

    private void showMessageDialog(String contentText) {
        new CommonDialogFragment.Builder()
                .showTitle(false)
                .setContentText(contentText)
                .showSingleButton(true)
                .setSingleButtonText(R.string.btn_ok)
                .create()
                .show(getSupportFragmentManager());
    }

    private void checkAppUpdate() {
        AppUpdateChecker.checkUpdate(false, new AppUpdateChecker.Callback() {
            @Override
            public void onSuccess() {
                if (isDestroyed) {
                    return;
                }
                AppUpdateManager.showAppUpdateDialogIfNeed(MainActivity.this, true);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void handleArguments(Intent intent) {
        int pageIndex = intent.getIntExtra(EXTRA_PAGE_INDEX, PAGE_INDEX_HOME);
        if (pageIndex >= 0) {
            switchPage(pageIndex);
        }
    }

    private void findViews() {
        mHolder.main_bottom_tab_container = $(R.id.main_bottom_tab_container);
    }

    private void initView() {
        addBottomTabs();
    }

    private void updateTabMineRedPoint() {
        int count = 0;
        updateTabMineRedPoint(count);
    }

    private void updateTabMineRedPoint(int msgCount) {
        mHolder.redPoints[PAGE_INDEX_MINE].setVisibility(msgCount == 0 ? View.GONE : View.VISIBLE);
    }

    private void addBottomTabs() {
        int tabCount = mTabFragmentClasses.length;
        mHolder.tabImageViews = new ImageView[tabCount];
        mHolder.redPoints = new ImageView[tabCount];
        mHolder.tabTextViews = new TextView[tabCount];
        mFragments = new Fragment[tabCount];
        for (int i = 0; i < tabCount; i++) {
            View tabView = LayoutInflater.from(this).inflate(R.layout.tab_item_view, mHolder.main_bottom_tab_container, false);
            tabView.setTag(R.id.index, i);
            tabView.setOnClickListener(mTabOnClickListener);
            mHolder.tabImageViews[i] = $(tabView, R.id.tab_image);
            mHolder.tabTextViews[i] = $(tabView, R.id.tab_text);
            mHolder.redPoints[i] = $(tabView, R.id.red_point);
            mHolder.tabImageViews[i].setImageResource(mTabIconsNormal[i]);
            mHolder.tabTextViews[i].setText(mTabTexts[i]);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
            lp.weight = 1;
            lp.width = 0;
            mHolder.main_bottom_tab_container.addView(tabView, lp);
        }
        updateTabMineRedPoint();
    }

    public void switchPage(int index) {
        int tabCount = mHolder.tabTextViews.length;
        if (mCurIndex == index) {
            Debug.li(getLogTag(), "page index is not changed, do nothing");
            return;
        }
        if (index < 0 || index >= tabCount) {
            Debug.li(getLogTag(), "page index does not exists");
            return;
        }
        showFragment(index);
        if (mCurIndex >= 0 & mCurIndex < tabCount) {
            mHolder.tabImageViews[mCurIndex].setImageResource(mTabIconsNormal[mCurIndex]);
            mHolder.tabImageViews[mCurIndex].setSelected(false);
            mHolder.tabTextViews[mCurIndex].setSelected(false);
        }
        mHolder.tabImageViews[index].setImageResource(mTabIconsSelected[index]);
        mHolder.tabImageViews[index].setSelected(true);
        mHolder.tabTextViews[index].setSelected(true);
        mLastIndex = mCurIndex;
        mCurIndex = index;
    }

    private void showFragment(int index) {
        int tabCount = mHolder.tabTextViews.length;
        if (index < 0 || index >= tabCount) {
            Debug.le(getLogTag(), "unknown page index, do nothing");
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        String logStr = "";
        for (int i = 0; i < tabCount; i++) {
            Fragment tempFragment = fm.findFragmentByTag(getTabFragmentTag(i));
            boolean hasAdded = (tempFragment != null);
            if (i == index) {
                if (hasAdded) {
                    ft.show(tempFragment);
                    logStr += "show fragment " + tempFragment;
                } else {
                    tempFragment = getTabFragmentNewInstance(index);
                    ft.add(R.id.fragment_container, tempFragment, getTabFragmentTag(index));
                    logStr += "add fragment " + tempFragment;
                }
                logStr += "\n";
                if (tempFragment != null) {
                    tempFragment.setMenuVisibility(true);
                    tempFragment.setUserVisibleHint(true);
                }
            } else {
                if (hasAdded) {
                    ft.hide(tempFragment);
                    tempFragment.setMenuVisibility(false);
                    tempFragment.setUserVisibleHint(false);
                    logStr += "hide fragment " + tempFragment + "\n";
                }
            }
        }
        Debug.li(getLogTag(), logStr);
        ft.commitAllowingStateLoss();
    }

    private String getTabFragmentTag(int tabIndex) {
        return "tab:" + tabIndex;
    }

    private Fragment getTabFragmentNewInstance(int tabIndex) {
        if (tabIndex < 0 && tabIndex > mTabFragmentClasses.length - 1) {
            Debug.li(getLogTag(), "unknown page index, do nothing");
            return null;
        }
        Fragment fragment = Fragment.instantiate(this, mTabFragmentClasses[tabIndex].getName(), null);
        mFragments[tabIndex] = fragment;
        return fragment;
    }

    private static class ViewHolder {
        LinearLayout main_bottom_tab_container;
        ImageView[] tabImageViews;
        ImageView[] redPoints;
        TextView[] tabTextViews;
    }
}