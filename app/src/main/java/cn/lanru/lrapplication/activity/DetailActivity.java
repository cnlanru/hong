package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.GlideImageLoader;
import cn.lanru.lrapplication.adapter.GoodCategoryAdapter;
import cn.lanru.lrapplication.bean.GoodCategory;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.net.URLImageParser;
import cn.lanru.lrapplication.utils.SharedHelper;

public class DetailActivity extends BaseActivity {
    Context mContext;
    int id = 0;

    TextView tvTitle;
    TextView tvPrice;
    TextView tvDetail;
    TextView tvXueDou;
    TextView xudouUnit;
    Banner banner;
    private List<String> images = new ArrayList<>();
    private Spanned spanned;

    ImageLoader imageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mContext = getApplicationContext();

        Bundle bundle = this.getIntent().getExtras();
        try {
            id = bundle.getInt("id");
        } catch (Exception e) {}

        initData();
        initView();

        imageLoader = ImageLoader.getInstance();//ImageLoader需要实例化
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));


    }

    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Log.i("RG", "source---?>>>" + source);
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                Log.i("RG", "url---?>>>" + url);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            Log.i("RG", "url---?>>>" + url);
            return drawable;
        }
    };

    public void initView () {
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvDetail = findViewById(R.id.tvDetail);
        tvXueDou = findViewById(R.id.tvXueDou);
        xudouUnit = findViewById(R.id.xudouUnit);
        banner = (Banner) findViewById(R.id.banner);
    }

    public void onClickBack(View view) {
        this.finish();
    }

    public void initData () {

        RequestParams params = new RequestParams();
        params.put("id", id + "");
        params.put("token", SharedHelper.getString(mContext, "token", ""));

        HttpRequest.getGoodDetial(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {

                    int position = 0;
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONObject data = new JSONObject(jsonObject.getString("data"));

                    JSONArray album = data.getJSONArray("album");
                    if (album.length() > 0) {
                        for (int i = 0; i < album.length(); i++) {
                            String src = album.getJSONObject(i).toString();
                            images.add(src);
                        }
                        //设置图片加载器
                        banner.setImageLoader(new GlideImageLoader());
                        //设置图片集合
                        banner.setImages(images);
                        //banner设置方法全部调用完毕时最后调用
                        banner.start();
                    }

                    tvTitle.setText(data.getString("title"));
                    tvPrice.setText(data.getString("price"));
                    double price =  data.getDouble("price");
                    int xueDou = data.getInt("integral");

                    if (xueDou > 0) {
                        xudouUnit.setVisibility(View.VISIBLE);
                    }

                    if (xueDou > 0 && price == 0) {
                        tvXueDou.setText(xueDou + "");
                    }
                    if (xueDou > 0 && price > 0) {
                        tvXueDou.setText(" + " + xueDou);
                    }


                    URLImageParser imageGetter = new URLImageParser(tvDetail);
                    tvDetail.setText(Html.fromHtml(data.getString("body"), imageGetter, null));




                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("failuer=", e.getMessage());
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
                Log.e("failuer=", failuer.getEmsg());
            }
        });


    }

    public void onClickCart(View view) {
        Intent intent = new Intent(mContext, CarActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
