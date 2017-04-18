package com.sulong.elecouple.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.activity.MainActivity;
import com.sulong.elecouple.ui.activity.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ydh on 2017/3/24.
 */

public class GuideFragment extends BaseFragment {
    public static final String EXTRA_SHOW_LAUNCH_BUTTON = "EXTRA_SHOW_LAUNCH_BUTTON";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    @BindView(R.id.guide_image)
    ImageView guideImage;
    @BindView(R.id.guide_title)
    ImageView guideTitle;
    @BindView(R.id.btn_launch)
    Button btnLaunch;

    private int[] mGuideImages = new int[]{
            R.drawable.guide_image_0,
            R.drawable.guide_image_1
    };
    private int[] mGuideTitles = new int[]{
            R.drawable.guide_title_0,
            R.drawable.guide_title_1
    };

    public static GuideFragment newInstance(boolean showLaunchBtn, int position) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_SHOW_LAUNCH_BUTTON, showLaunchBtn);
        args.putInt(EXTRA_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_item_guide, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int position = getArguments().getInt(EXTRA_POSITION, 0);
        boolean showLaunchBtn = getArguments().getBoolean(EXTRA_SHOW_LAUNCH_BUTTON, false);

        // 启动按钮
        if (showLaunchBtn) {
            btnLaunch.setVisibility(View.VISIBLE);
        } else {
            btnLaunch.setVisibility(View.GONE);
        }
        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // 图片
        guideImage.setImageResource(mGuideImages[position]);
        guideTitle.setImageResource(mGuideTitles[position]);
    }
}