package com.sulong.elecouple.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.ui.activity.BaseActivity;
import com.sulong.elecouple.utils.Debug;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

/**
 * Created by ydh on 2015/9/11.
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    IWXAPI msgApi;
    EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        eventBus = ComponentHolder.getAppComponent().defaultEventBus();
        msgApi = WXAPIFactory.createWXAPI(this, getString(R.string.wechat_app_id));
        msgApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        msgApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Debug.li(getLogTag(), "WeChat Request:" + req);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Debug.i("rqy", "onResp--errCode=" + resp.errCode);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
//                    eventBus.post(new PayEvent(PayResultState.WX_SUCCESS));
                    Debug.li(getLogTag(), "PAY_WX_SUCCESS ");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                default:
//                    CustomToast.makeText(this, getString(R.string.pay_fail), CustomToast.LENGTH_SHORT).show();
//                    // 新的支付代码
//                    eventBus.post(new PayEvent(PayResultState.WX_FAILED));
                    Debug.li(getLogTag(), "PAY_WX_FAILED");
                    finish();
                    break;
            }
        }
    }
}
