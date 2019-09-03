package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.Classes;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.CheckApkExist;

public class StoryActivity extends BaseActivity {

    private String keyword;
    private int cid;

    private List<Classes> lists = new ArrayList<>();
    private ListAdapter listAdapter;
    private GridView gridView;

    private Context mContext;
    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        mContext = getApplicationContext();
        super.menuInit("1");
        super.menuClick();

        Bundle bundle = this.getIntent().getExtras();
        try {
            keyword = bundle.getString("keyword");
            cid = bundle.getInt("cid");
        } catch (Exception e) {}

        gridView = findViewById(R.id.lists);
        initData();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = lists.get(position);
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

    public void back(View view) {
        StoryActivity.this.finish();
    }

    public void initData () {
        RequestParams params = new RequestParams();
        params.put("cid", cid + "");
        params.put("keyword", keyword);

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
                        lists.add(item);
                    }

                    listAdapter = new ListAdapter(mContext);
                    gridView.setAdapter(listAdapter);

                } catch (Exception e) {
                    Log.e("error=", e.getMessage());
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    public class ListAdapter extends BaseAdapter {

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
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
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

            RequestOptions options = new RequestOptions().error(R.mipmap.nopic)
                    .bitmapTransform(new RoundedCorners(100));
            Glide.with(getApplicationContext())
                    .load(lists.get(position).getThumbnail()).apply(options)
                    .into((ImageView) convertView.findViewById(R.id.ivThumbnail));

            return convertView;
        }
    }

}
