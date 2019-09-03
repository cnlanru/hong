package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.GlideImageLoader;
import cn.lanru.lrapplication.adapter.GoodAdapter;
import cn.lanru.lrapplication.adapter.GoodCategoryAdapter;
import cn.lanru.lrapplication.bean.Good;
import cn.lanru.lrapplication.bean.GoodCategory;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.MyGridView;

public class ShopActivity extends BaseActivity implements OnRefreshListener {

    private Context mContext;
    private Intent intent;
    private Banner banner;
    private List<String> images = new ArrayList<>();

    GridView gvCategory;
    List<GoodCategory> categoryData = new ArrayList<>();
    GoodCategoryAdapter categoryAdapter;

    MyGridView gvGoods;
    List<Good> goodData = new ArrayList<>();
    GoodAdapter goodAdapter;

    RefreshLayout refreshLayout;

    EditText etKeyword;
    String searchKeyWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mContext = getApplicationContext();

        super.menuInit("2");
        super.menuClick();

        initView();
        initCategory();
        initBanner();
        initCommend();

        //分类
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodCategory item = categoryData.get(position);
                if (item.getId() > 0) {
                    Intent intent = new Intent(mContext, GoodListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("cid", item.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        //商品
        gvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Good item = goodData.get(position);
                Log.e("item=", item.getId() + "");
                if (item.getId() > 0) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id", item.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    public void initView() {
        banner = (Banner) findViewById(R.id.banner);
        gvCategory = findViewById(R.id.gvCategory);
        gvGoods = findViewById(R.id.gvGoods);
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.Main_SRLayout);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableLoadMore(false);
        etKeyword = findViewById(R.id.etKeyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)  {
                    searchKeyWord = String.valueOf(etKeyword.getText()).trim();
                    Intent intent = new Intent(mContext, GoodListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keyword", searchKeyWord);
                    bundle.putInt("cid", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    //分类
    public void initCategory() {
        RequestParams params = new RequestParams();
        params.put("pagesize", "3");
        params.put("image", "1");
        HttpRequest.getGoodCategory(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0;i < data.length(); i++) {
                        GoodCategory item = new GoodCategory();
                        item.setImage(data.getJSONObject(i).getString("image"));
                        item.setId(data.getJSONObject(i).getInt("id"));
                        categoryData.add(item);
                    }

                    categoryAdapter = new GoodCategoryAdapter(mContext, categoryData, R.layout.item_good_category_img);
                    gvCategory.setAdapter(categoryAdapter);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("failuer1=", e.getMessage());
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
                Log.e("failuer=", failuer.getEmsg());
            }
        });
    }

    //推荐
    public void initCommend() {
        RequestParams params = new RequestParams();
        params.put("pagesize", "4");
        params.put("iscommend", "1");
        HttpRequest.getGoods(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {

                try {

                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0;i < data.length(); i++) {
                        Good item = new Good();
                        item.setThumbnail(data.getJSONObject(i).getString("thumbnail"));
                        item.setTitle(data.getJSONObject(i).getString("title"));
                        item.setPrice(data.getJSONObject(i).getInt("price"));
                        item.setIntegral(data.getJSONObject(i).getInt("integral"));
                        item.setId(data.getJSONObject(i).getInt("id"));
                        goodData.add(item);
                    }

                    goodAdapter = new GoodAdapter(mContext, goodData, R.layout.item_good);
                    gvGoods.setAdapter(goodAdapter);

                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("failuer1=", e.getMessage());
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
                Log.e("failuer=", failuer.getEmsg());
            }
        });
    }

    public void initBanner() {
        RequestParams params = new RequestParams();
        params.put("pid", "7");
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initCategory();
        initBanner();
        initCommend();
        refreshLayout.finishRefresh();
    }
}
