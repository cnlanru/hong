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
import android.widget.ListView;
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
import cn.lanru.lrapplication.bean.CardList;
import cn.lanru.lrapplication.bean.NewList;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class CardActivity extends BaseActivity {

    private Context mContext;

    private GridView cardView;
    private List<CardList> cardDb = new ArrayList<>();
    private CardAdapter cardAdapter;

    private TextView tvCard;
    private int cartID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mContext = getApplicationContext();
        cardView = findViewById(R.id.gvCard);
        tvCard = findViewById(R.id.tvCard);

        initData();

        cardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cardAdapter.setCurrentItem(position);
                cardAdapter.notifyDataSetChanged();
                cartID = cardDb.get(position).getId();
            }
        });
    }

    public void btnSubmit(View view) {
        if (cartID > 0) {
            RequestParams params = new RequestParams();
            params.put("token", SharedHelper.getString(mContext, "token", ""));
            params.put("cart_id", cartID + "");

            HttpRequest.postUserCard(params, new ResponseCallback() {
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

    public void back (View view) {
        this.finish();
    }

    private void initData() {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        HttpRequest.getUserCard(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {

                try {

                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    String tvCardString = jsonObject.getString("msg");

                    if (tvCardString.length() > 0) {
                        tvCard.setText("你已经是"  +tvCardString + "会员！");
                    }

                    JSONArray listArray = jsonObject.getJSONArray("data");
                   for(int i = 0;i < listArray.length(); i++) {
                       CardList item = new CardList();
                       item.setId(listArray.getJSONObject(i).getInt("id"));
                       item.setTitle(listArray.getJSONObject(i).getString("title"));
                       item.setPrice(listArray.getJSONObject(i).getString("price"));
                       item.setDescriptione(listArray.getJSONObject(i).getString("description"));

                       if (i == 0 && cartID == 0) {
                           cartID = listArray.getJSONObject(i).getInt("id");
                       }

                       cardDb.add(item);
                    }

                    cardAdapter = new CardAdapter(mContext, cardDb);
                    cardView.setAdapter(cardAdapter);

                } catch (Exception e) {

                    Log.e("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    class CardAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<CardList> mCardData;
        private int mCurrentItem;

        public CardAdapter(Context context, List<CardList> mCardData) {
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
                convertView = mInflater.inflate(R.layout.item_cartd, null, true);
                viewHolder = new ViewHolder();

                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.ivTitle);
                viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.ivDescription);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.ivPrice);
                viewHolder.llBody = convertView.findViewById(R.id.ivBody);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (mCurrentItem == position) {
                viewHolder.llBody.setBackgroundResource(R.drawable.block_card_on);

            } else {
                viewHolder.llBody.setBackgroundResource(R.drawable.block_card);
            }
            viewHolder.tvTitle.setText(mCardData.get(position).getTitle());
            viewHolder.tvPrice.setText(mCardData.get(position).getPrice());
            viewHolder.tvDescription.setText(mCardData.get(position).getDscription());

            return convertView;
        }

        public void setCurrentItem(int currentItem) {
            this.mCurrentItem = currentItem;
        }

        public class ViewHolder{
            TextView tvTitle;
            TextView tvPrice;
            TextView tvDescription;
            LinearLayout llBody;
        }

    }
}
