package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.GoodAdapter;
import cn.lanru.lrapplication.adapter.GoodCategoryAdapter;
import cn.lanru.lrapplication.bean.Good;
import cn.lanru.lrapplication.bean.GoodCategory;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.MyGridView;

public class GoodListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    Context mContext;

    String keyword = "";
    int cid = 0;
    int page = 1;

    EditText etKeyword;
    SmartRefreshLayout smartRefreshLayout;

    GridView gvCategory;
    List<GoodCategory> categoryData = new ArrayList<>();
    GoodCategoryAdapter categoryAdapter;

    MyGridView gvGoods;
    List<Good> goodData = new ArrayList<>();
    GoodAdapter goodAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_list);
        mContext = getApplicationContext();
        super.menuInit("2");
        super.menuClick();

        Bundle bundle = this.getIntent().getExtras();
        try {
            keyword = bundle.getString("keyword") != null ? bundle.getString("keyword") : "";
            cid = bundle.getInt("cid");
        } catch (Exception e) {}

        gvCategory = findViewById(R.id.category);
        initCategory();
        smartRefreshLayout = findViewById(R.id.Main_SRLayout);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        //搜索
        etKeyword = findViewById(R.id.etKeyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)  {
                    keyword = String.valueOf(etKeyword.getText()).trim();
                    initData();
                }
                return false;
            }
        });
        gvGoods = findViewById(R.id.myGoodList);
        initData();

        //分类
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodCategory item = categoryData.get(position);
                if (item.getId() > 0) {
                    categoryAdapter.setCurrentItem(position);
                    categoryAdapter.notifyDataSetChanged();
                    cid = item.getId();
                    page = 1;
                    keyword = "";
                    initData();
                    smartRefreshLayout.setNoMoreData(false);
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

    //分类
    public void initCategory() {
        RequestParams params = new RequestParams();
        params.put("pagesize", "3");
        HttpRequest.getGoodCategory(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {

                    int position = 0;
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0;i < data.length(); i++) {
                        GoodCategory item = new GoodCategory();
                        item.setName(data.getJSONObject(i).getString("name"));
                        item.setId(data.getJSONObject(i).getInt("id"));
                        categoryData.add(item);
                        if (cid > 0 && cid == data.getJSONObject(i).getInt("id")) position = i;
                    }

                    int size = categoryData.size();
                    int length = 80;
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    float density = dm.density;
                    int gridviewWidth = (int) (size * (length + 4) * density);
                    int itemWidth = (int) (length * density);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
                    gvCategory.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                    gvCategory.setColumnWidth(itemWidth); // 设置列表项宽
                    gvCategory.setHorizontalSpacing(5); // 设置列表项水平间距
                    gvCategory.setStretchMode(GridView.NO_STRETCH);
                    gvCategory.setNumColumns(size); // 设置列数量=列表集合数
                    gvCategory.setSelector(new ColorDrawable(Color.TRANSPARENT));

                    categoryAdapter = new GoodCategoryAdapter(mContext, categoryData, R.layout.item_good_category_txt, position);
                    gvCategory.setAdapter(categoryAdapter);

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

    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pagesize", "8");
        params.put("page", page + "");
        params.put("cid", cid + "");
        params.put("keyword", keyword + "");
        HttpRequest.getGoods(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {

                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length() == 0 && page == 1) {
                        goodData = new ArrayList<>();
                        Log.e("length=", "00000");
                        goodAdapter = new GoodAdapter(mContext, goodData, R.layout.item_good);
                        gvGoods.setAdapter(goodAdapter);
                    } else if (data.length() == 0 && page > 1) {
                        Toast.makeText(getApplicationContext(), "已经没有更多数据了...", Toast.LENGTH_SHORT).show();
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    else {
                        if (page == 1) goodData = new ArrayList<>();
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
                    }


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

    //更多
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page = page + 1;
        initData();
        smartRefreshLayout.finishLoadMore();
    }
    //刷新
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        initData();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.setNoMoreData(false);
    }
}
