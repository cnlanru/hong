package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.Grade;
import cn.lanru.lrapplication.bean.NewCategory;
import cn.lanru.lrapplication.bean.NewList;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class NewsActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    private Context mContext;
    private GridView gridView;

    private List<NewCategory> category = new ArrayList<NewCategory>();
    private CategoryAdapter categoryAdapter;

    private List<NewList> newListsDb = new ArrayList<>();
    private ListAdapter listAdapter;
    private ListView newListView;

    private int categoryId = 0;
    private int page = 1;

    private EditText keyword;
    private String searchKeyword = "";

    SmartRefreshLayout smartRefreshLayout;
    private int categoryPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        try {
            categoryId = bundle.getInt("pid");
        } catch (Exception e) {}

        setContentView(R.layout.activity_news);
        smartRefreshLayout = findViewById(R.id.Main_SRLayout);

        mContext = getApplicationContext();
        gridView = findViewById(R.id.gvCategory);
        newListView = findViewById(R.id.newList);

        //分类
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    categoryId = category.get(position).getId();
                    page = 1;
                    newListsDb = new ArrayList<>();

                    categoryPosition = position;
                } catch (Exception e) {}

                categoryAdapter.setCurrentItem(position);
                categoryAdapter.notifyDataSetChanged();
                initList();

            }
        });

        keyword = findViewById(R.id.keyword);
        keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)  {
                    searchKeyword = String.valueOf(keyword.getText()).trim();
                    initList ();
                }
                return false;
            }
        });


        //例表
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewList item = newListsDb.get(position);
                if (item.getId() > 0) {
                    Intent intent = new Intent(mContext, NewShowActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id", item.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        super.menuInit("4");
        super.menuClick();

        initData();
        viewsAddListener();
    }

    public void viewsAddListener() {
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);
    }

    //新闻列表
    private void initList () {
        RequestParams options = new RequestParams();
        options.put("pid", categoryId + "");
        options.put("keyword", searchKeyword);
        options.put("page", page + "");

        HttpRequest.getNews(options, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    if (page == 1) {
                        newListsDb = new ArrayList<>();
                    }
                    JSONArray listArray = jsonObject.getJSONArray("data");

                    if (listArray.length() == 0 && page == 1) {
                        listAdapter.setEmptyData();
                    } else if (listArray.length() == 0 && page > 1) {
                        Toast.makeText(getApplicationContext(), "已经没有更多数据了...", Toast.LENGTH_SHORT).show();
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    else {
                        for (int i = 0; i < listArray.length(); i++) {
                            NewList item = new NewList();
                            item.setId(listArray.getJSONObject(i).getInt("id"));
                            item.setTitle(listArray.getJSONObject(i).getString("title"));
                            item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                            item.setOther(listArray.getJSONObject(i).getString("other"));
                            newListsDb.add(item);
                        }

                        listAdapter = new ListAdapter(mContext);
                        newListView.setAdapter(listAdapter);
                    }

                } catch (JSONException e) {
                    Log.e("eresult=", e.getMessage());
                    listAdapter.setEmptyData();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page = page + 1;
        initList();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        searchKeyword = "";
        initList();
        smartRefreshLayout.finishRefresh();
    }

    //新闻列表
    private class ListAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;

        public class ViewHolder{
            TextView textName;
            ImageView textThumbnail;
            TextView textOther;

            private ViewHolder(View view) {
                textName = view.findViewById(R.id.tvTitle);
                textThumbnail = view.findViewById(R.id.ivThumbnail);
                textOther = view.findViewById(R.id.tvOther);
            }
        }

        public void setEmptyData () {
            notifyDataSetChanged();
        }

        public ListAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(this.mContext);
        }


        @Override
        public int getCount() {
            return newListsDb.size();
        }

        @Override
        public Object getItem(int position) {
            return newListsDb.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_new_list, null, true);
                mHolder = new ViewHolder(convertView);
                mHolder.textName = (TextView) convertView.findViewById(R.id.tvTitle);
                mHolder.textThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
                mHolder.textOther = (TextView) convertView.findViewById(R.id.tvOther);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.textName.setText(newListsDb.get(position).getTitle());
            mHolder.textOther.setText(newListsDb.get(position).getOther());

            Glide.with(getApplicationContext())
                    .load(newListsDb.get(position).getThumbnail()).placeholder(new ColorDrawable(Color.DKGRAY))
                    .error(new ColorDrawable(Color.DKGRAY))
                    .into((ImageView) convertView.findViewById(R.id.ivThumbnail));

            return convertView;
        }
    }

    //分类
    private void initData () {
        HttpRequest.getNewCategory(null, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    category = new ArrayList<>();
                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        NewCategory newCategory = new NewCategory();
                        newCategory.setId(listArray.getJSONObject(i).getInt("id"));
                        newCategory.setName(listArray.getJSONObject(i).getString("name"));
                        category.add(newCategory);
                        if (categoryId == 0) categoryId = listArray.getJSONObject(i).getInt("id");
                    }

                    categoryAdapter = new CategoryAdapter(mContext);
                    gridView.setAdapter(categoryAdapter);

                    initList();

                } catch (JSONException e) {
                    Log.e("eresult=", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    //分类处理
    private class CategoryAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private int mCurrentItem = 0;

        public CategoryAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return category.size();
        }

        @Override
        public Object getItem(int position) {
            return category.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder{
            TextView textName;
            TextView textId;

            private ViewHolder(View view) {
                textName = view.findViewById(R.id.text_name);
                textId = view.findViewById(R.id.text_id);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_news_category, null, true);
                mHolder = new ViewHolder(convertView);
                mHolder.textName = (TextView) convertView.findViewById(R.id.text_name);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            NewCategory lt = category.get(position);
            String name = category.get(position).getName().toString();
            int id = category.get(position).getId();
            mHolder.textName.setText(name);

            if (mCurrentItem == position){
                categoryId = lt.getId();
                mHolder.textId.setBackgroundColor(Color.parseColor("#007aff"));
            } else {
                mHolder.textId.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            return convertView;
        }

        public void setCurrentItem(int currentItem) {
            this.mCurrentItem = currentItem;
        }
    }

}
