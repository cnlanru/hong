package cn.lanru.lrapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.MainActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.GlideImageLoader;
import cn.lanru.lrapplication.bean.Classes;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.CheckApkExist;

public class EduActivity extends BaseActivity {

    private Context mContext;
    private Banner banner;
    private List<String> images = new ArrayList<>();

    private ImageView ivFirst;
    private String ivFirstString;

    private List<Classes> classes_first = new ArrayList<>();
    private List<Classes> classes_sec = new ArrayList<>();

    private ListAdapters listAdapterFist;
    private ListView listView_first;

    private ListAdapter listAdapterSec;
    private GridView listView_sec;

    private EditText iptKeyword;
    private String keyword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu);
        mContext = getApplicationContext();
        super.menuInit("1");
        super.menuClick();

        banner = (Banner) findViewById(R.id.banner);
        initBanner();


        listView_first = findViewById(R.id.lvFirst);
        listView_sec = findViewById(R.id.lvSec);

        ivFirst = findViewById(R.id.firstIv);
        initData();
        initNewData();

        iptKeyword = findViewById(R.id.iptKeyword);

        iptKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)  {
                    keyword = String.valueOf(iptKeyword.getText()).trim();
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keyword", keyword);
                    bundle.putInt("cid", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return false;
            }
        });

        listView_sec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = classes_sec.get(position);
                if (data.getTitle() != null && CheckApkExist.checkAppExist(mContext)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
                    intent.putExtra("tag", data.getTitle());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
                }


            }
        });

        listView_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = classes_first.get(position);
                if (data.getTitle() != null && CheckApkExist.checkAppExist(mContext)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
                    intent.putExtra("tag", data.getTitle());
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //最新
    public void initNewData () {
        RequestParams params = new RequestParams();
        params.put("cid", "1");
        params.put("limit", "5");
        params.put("flag", "0");

        HttpRequest.getClasses(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray listArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < listArray.length(); i++) {
                        Classes item = new Classes();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                        if (i < 2) {
                            classes_first.add(item);
                        } else {
                            classes_sec.add(item);
                        }
                    }

                    listAdapterFist = new ListAdapters(mContext);
                    listView_first.setAdapter(listAdapterFist);

                    listAdapterSec = new ListAdapter(mContext);
                    listView_sec.setAdapter(listAdapterSec);
                    Log.e("listAdapterSec=", String.valueOf(classes_sec.size()));

                } catch (Exception e) {
                    Log.e("error=", e.getMessage());
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    private class ListAdapters extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;

        public ListAdapters(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(this.mContext);
        }

        public class ViewHolder{
            ImageView ivThumbnail;

            private ViewHolder(View view) {
                ivThumbnail = view.findViewById(R.id.ivThumbnail);
            }
        }

        @Override
        public int getCount() {
            return classes_first.size();
        }

        @Override
        public Object getItem(int position) {
            return classes_first.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_classesx, null, true);
                mHolder = new ViewHolder(convertView);
                mHolder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(getApplicationContext())
                    .load(classes_first.get(position).getThumbnail()).placeholder(new ColorDrawable(Color.DKGRAY))
                    .error(new ColorDrawable(Color.DKGRAY))
                    .into((ImageView) convertView.findViewById(R.id.ivThumbnail));

            return convertView;
        }
    }


    private class ListAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;

        public ListAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(this.mContext);
        }

        public class ViewHolder{
            ImageView ivThumbnail;

            private ViewHolder(View view) {
                ivThumbnail = view.findViewById(R.id.ivThumbnail);
            }
        }

        @Override
        public int getCount() {
            return classes_sec.size();
        }

        @Override
        public Object getItem(int position) {
            return classes_sec.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_classes, null, true);
                mHolder = new ViewHolder(convertView);
                mHolder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(getApplicationContext())
                    .load(classes_sec.get(position).getThumbnail()).placeholder(new ColorDrawable(Color.DKGRAY))
                    .error(new ColorDrawable(Color.DKGRAY))
                    .into((ImageView) convertView.findViewById(R.id.ivThumbnail));

            return convertView;
        }
    }


    //推荐
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("cid", "1");
        params.put("limit", "1");
        params.put("flag", "1");
        HttpRequest.getClasses(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray data =  jsonObject.getJSONArray("data");
                    Glide.with(mContext).load(data.getJSONObject(0).getString("thumbnail")).into(ivFirst);
                    ivFirstString = data.getJSONObject(0).getString("title");
                } catch (Exception e) {
                    Log.e("error=", e.getMessage());
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    //Banner begin
    public void initBanner() {

        RequestParams params = new RequestParams();
        params.put("pid", "2");
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


    //Banner END

    //推荐
    public void onClickFirst (View view) {
        if (CheckApkExist.checkAppExist(mContext)) {
            Intent intent = new Intent();
            intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
            intent.putExtra("tag", ivFirstString);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
        }

    }

    public void smallSchool(View view) {
        Intent intent = new Intent(mContext, EduActivity.class);
        startActivity(intent);
        finish();
    }

    public void middleSchool(View view) {
        Intent intent = new Intent(mContext, SchoolActivity.class);
        startActivity(intent);
        finish();
    }

    public void bigSchool(View view) {
        Intent intent = new Intent(mContext, MiddleActivity.class);
        startActivity(intent);
        finish();
    }
}
