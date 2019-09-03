package cn.lanru.lrapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.Good;
import cn.lanru.lrapplication.bean.GoodCategory;

public class GoodAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Good> mDatas;
    private int mLayoutId;

    public GoodAdapter(Context context, List<Good> mDatas, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mDatas = mDatas;
        this.mLayoutId = layoutId;
    }
    public void setEmptyData () {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(mLayoutId, null, true);
            viewHolder = new ViewHolder();
            viewHolder.mThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvPrice = convertView.findViewById(R.id.tvPrice);
            viewHolder.tvIntegral = convertView.findViewById(R.id.tvIntegral);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(mDatas.get(position).getTitle());
        viewHolder.tvPrice.setText("¥" + mDatas.get(position).getPrice());
        String tvIntegralString = "";
        if (mDatas.get(position).getIntegral() > 0 &&  mDatas.get(position).getPrice() > 0) {
            tvIntegralString = " + " + mDatas.get(position).getIntegral() + "学豆";
        } else if (mDatas.get(position).getIntegral() > 0) {
            tvIntegralString = "" + mDatas.get(position).getIntegral() + "学豆";
        }
        viewHolder.tvIntegral.setText(tvIntegralString);

        if (mDatas.get(position).getThumbnail().length() > 0) {
            Glide.with(convertView)
                    .load(mDatas.get(position).getThumbnail()).placeholder(new ColorDrawable(Color.DKGRAY))
                    .error(new ColorDrawable(Color.DKGRAY))
                    .into((ImageView) convertView.findViewById(R.id.mThumbnail));
        }


        return convertView;
    }

    private final class ViewHolder
    {
        ImageView mThumbnail;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvIntegral;
    }
}
