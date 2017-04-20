package com.sulong.elecouple.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.AreaItem;
import com.sulong.elecouple.ui.dialog.CommonBottomSwitchDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ydh on 2017/4/18.
 */

public class SearchActivity extends BaseActivity {
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_human_height)
    TextView humanHeight;
    @BindView(R.id.tv_qualification)
    TextView tvQualification;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_is_buy_house)
    TextView tvIsBuyHouse;
    @BindView(R.id.tv_is_buy_car)
    TextView tvIsBuyCar;
    @BindView(R.id.tv_marriage)
    TextView tvMarriage;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_age, R.id.tv_address, R.id.tv_human_height, R.id.tv_qualification, R.id.tv_income, R.id.tv_is_buy_house, R.id.tv_is_buy_car, R.id.tv_marriage, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_age:
                showAgeDialog();
                break;
            case R.id.tv_address:
                break;
            case R.id.tv_human_height:
                break;
            case R.id.tv_qualification:
                break;
            case R.id.tv_income:
                break;
            case R.id.tv_is_buy_house:
                break;
            case R.id.tv_is_buy_car:
                break;
            case R.id.tv_marriage:
                break;
            case R.id.btn_confirm:
                break;
        }
    }

    private void showAgeDialog() {
        ArrayList<AreaItem> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AreaItem item = new AreaItem();
            item.area_id = i+"";
            item.area_name = "第"+i+"个";
            list.add(item);
        }
        new CommonBottomSwitchDialogFragment.Builder()
                .setList(list)
                .setSelectedFirst(1)
                .setOnSelectChangeListener(new CommonBottomSwitchDialogFragment.OnSelectChangeListener() {
                    @Override
                    public void onChanged(String selectValue) {
                        tvAge.setText(selectValue);
                    }
                })
                .create()
                .show(getSupportFragmentManager(),"AGE_DIALOG");
    }
}
