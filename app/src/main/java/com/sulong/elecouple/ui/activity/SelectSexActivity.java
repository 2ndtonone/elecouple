package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sulong.elecouple.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ydh on 2017/4/16.
 */

public class SelectSexActivity extends BaseActivity {
    @BindView(R.id.iv_girl)
    ImageView ivGirl;
    @BindView(R.id.iv_boy)
    ImageView ivBoy;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private boolean boySelected = false;
    private boolean girlSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sex);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_girl, R.id.iv_boy, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_girl:
                boySelected = girlSelected;
                girlSelected = !girlSelected;
                updateSelect();
                break;
            case R.id.iv_boy:
                girlSelected = boySelected;
                boySelected = !boySelected;
                updateSelect();
                break;
            case R.id.btn_ok:
                goToMain();
                break;
        }
    }

    private void goToMain() {
        startActivity(new Intent(this,MainActivity.class));
    }

    private void updateSelect() {
        ivBoy.setSelected(boySelected);
        ivGirl.setSelected(girlSelected);
    }
}
