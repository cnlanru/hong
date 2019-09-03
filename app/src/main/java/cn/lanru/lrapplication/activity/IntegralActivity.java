package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.GlideImageLoader;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;

public class IntegralActivity extends BaseActivity {

    private Context mContext;
    private Intent intent;
    private Banner banner;
    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);

        mContext = getApplicationContext();
        super.menuInit("3");
        super.menuClick();

        initData();
        banner = (Banner) findViewById(R.id.banner);
        initBanner();
    }

    public void initData() {

    }

    public void initBanner() {
        RequestParams params = new RequestParams();
        params.put("pid", "6");
        HttpRequest.getAd(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        String src = listArray.getJSONObject(i).getString("remark");
                        images.add(src);
                    }
                    //设置图片加载器
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合
                    banner.setImages(images);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();

                } catch (Exception e){}
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    public void onClickCoupn (View view) {
        intent = new Intent(mContext, CouponActivity.class);
        startActivity(intent);
    }
}
