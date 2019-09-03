package cn.lanru.lrapplication.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.lanru.lrapplication.R;

public class TabViewA extends TabView {

    private ImageView mIvIcon;

    private static final int COLOR_DEFAULT = Color.parseColor("#ff000000");
    private static final int COLOR_SELECT = Color.parseColor("#ff007aff");

    public TabViewA(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tab_view_a, this);

        mIvIcon = findViewById(R.id.ico);
    }

    public void setIconAndText(int icon, int iconSelect, String text) {
        mIvIcon.setImageResource(icon);

    }

    public void setProgress(float progress) {
        mIvIcon.setAlpha(1 - progress);
    }

}
