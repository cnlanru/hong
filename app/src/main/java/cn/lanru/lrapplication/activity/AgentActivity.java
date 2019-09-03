package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.MainActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.AgentList;
import cn.lanru.lrapplication.bean.CardList;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class AgentActivity extends BaseActivity {

    private Context mContext;

    private GridView agentView;
    private List<AgentList> agentData = new ArrayList<>();
    private AgentAdapter agentAdapter;

    private int agentID;

    private ImageView ivThumbnail;
    private TextView tvTips;
    private TextView tvPrice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        mContext = getApplicationContext();

        ivThumbnail = findViewById(R.id.ivThumbnail);
        tvTips = findViewById(R.id.tvTips);
        tvPrice = findViewById(R.id.tvPrice);
        agentView = findViewById(R.id.gvAgent);

        initData();

        agentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                agentAdapter.setCurrentItem(position);
                agentAdapter.notifyDataSetChanged();

                AgentList item = agentData.get(position);
                if (item != null) {
                    agentID = item.getId();
                    tvPrice.setText("¥" + item.getPrice());
                    tvTips.setText(item.getTips() + " ");

                    RequestOptions options = new RequestOptions().error(R.mipmap.nopic)
                            .bitmapTransform(new RoundedCorners(100));
                    Glide.with(mContext)
                            .load(item.getThumbnail()).apply(options)
                            .into(ivThumbnail);
                }
            }
        });

    }

    public void btnSubmit(View view) {
        if (agentID > 0) {
            RequestParams params = new RequestParams();
            params.put("token", SharedHelper.getString(mContext, "token", ""));
            params.put("agent_id", agentID + "");

            HttpRequest.postUserAgent(params, new ResponseCallback() {
                @Override
                public void onSuccess(Object responseObj) {
                    try {
                        JSONObject res = new JSONObject((String) responseObj);

                        Toast.makeText(getApplicationContext(), res.getString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(OkHttpException failuer) {
                    Toast.makeText(getApplicationContext(), failuer.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "请选择会员卡类型", Toast.LENGTH_SHORT).show();
        }
    }


    private void initData() {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        HttpRequest.getUserAgent(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {

                try {

                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for(int i = 0;i < listArray.length(); i++) {
                        AgentList item = new AgentList();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setPrice(listArray.getJSONObject(i).getString("price"));
                        item.setDescriptione(listArray.getJSONObject(i).getString("description"));
                        item.setTips(listArray.getJSONObject(i).getString("tips"));

                        if (i == 0 && agentID == 0) {
                            agentID = listArray.getJSONObject(i).getInt("id");
                            tvTips.setText(listArray.getJSONObject(i).getString("tips"));
                            tvPrice.setText("¥" + listArray.getJSONObject(i).getString("price"));

                            RequestOptions options = new RequestOptions().error(R.mipmap.nopic)
                                    .bitmapTransform(new RoundedCorners(100));
                            Glide.with(mContext)
                                    .load(listArray.getJSONObject(i).getString("thumbnail")).apply(options)
                                    .into(ivThumbnail);
                        }

                        agentData.add(item);
                    }

                    agentAdapter = new AgentAdapter(mContext, agentData);
                    agentView.setAdapter(agentAdapter);

                } catch (Exception e) {

                    Log.e("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }
    public void back(View view) {
        this.finish();
    }


    public class AgentAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<AgentList> mCardData;
        private int mCurrentItem;

        public AgentAdapter(Context context, List<AgentList> mCardData) {
            this.mContext = context;
            this.mCardData = mCardData;
            this.mInflater = LayoutInflater.from(this.mContext);
            this.mCurrentItem = 0;
        }

        @Override
        public int getCount() {
            return mCardData.size();
        }

        @Override
        public Object getItem(int position) {
            return mCardData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_agent, null, true);
                viewHolder = new ViewHolder();

                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
                viewHolder.tvaLine = convertView.findViewById(R.id.tvLine);
                viewHolder.llBody = convertView.findViewById(R.id.llBody);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvTitle.setText(mCardData.get(position).getTitle());
            viewHolder.tvDescription.setText(mCardData.get(position).getDscription());
            if (mCurrentItem == position) {
                viewHolder.llBody.setBackgroundResource(R.drawable.button_blue);
                viewHolder.tvTitle.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.tvaLine.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.tvDescription.setTextColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.llBody.setBackgroundResource(R.drawable.block_light_blue);
                viewHolder.tvTitle.setTextColor(Color.parseColor("#49a0ff"));
                viewHolder.tvaLine.setBackgroundColor(Color.parseColor("#49a0ff"));
                viewHolder.tvDescription.setTextColor(Color.parseColor("#49a0ff"));
            }

            return convertView;
        }

        public void setCurrentItem(int currentItem) {
            this.mCurrentItem = currentItem;
        }

        public class ViewHolder{
            TextView tvTitle;
            TextView tvaLine;
            TextView tvDescription;
            LinearLayout llBody;
        }
    }
}
