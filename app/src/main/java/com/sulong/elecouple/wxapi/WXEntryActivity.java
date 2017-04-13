package com.sulong.elecouple.wxapi;

import android.os.Bundle;

import com.sulong.elecouple.R;
import com.sulong.elecouple.eventbus.WXAuthResultEvent;
import com.sulong.elecouple.ui.activity.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, getString(R.string.wechat_app_id));
        msgApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登录
            WXAuthResultEvent event = new WXAuthResultEvent();
            event.baseResp = resp;
            EventBus.getDefault().post(event);
        }
        finish();
    }
}