package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.utils.SharedHelper;

public class TeamActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        mContext = getApplicationContext();

        initView();
    }

    public void initView () {

        RequestOptions options = new RequestOptions().error(R.drawable.user_face)
                .bitmapTransform(new RoundedCorners(100));
        Glide.with(getApplicationContext())
                .load(SharedHelper.getString(getApplicationContext(), "avatar", "")).apply(options)
                .into((ImageView) findViewById(R.id.face));
    }

    public void back (View view) {
        this.finish();
    }
}
