package cn.lanru.lrapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.Classes;
import cn.lanru.lrapplication.bean.GoodCategory;

public class GoodCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<GoodCategory> mDatas;
    private int mLayoutId;
    private int mCurrentItem = 0;

    public GoodCategoryAdapter(Context context, List<GoodCategory> mDatas, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mDatas = mDatas;
        this.mLayoutId = layoutId;
    }

    public GoodCategoryAdapter(Context context, List<GoodCategory> mDatas, int layoutId, int position) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mDatas = mDatas;
        this.mLayoutId = layoutId;
        this.mCurrentItem = position;

        Log.e("position", position + "");
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
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvId = convertView.findViewById(R.id.tvId);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.tvName != null) {
            viewHolder.tvName.setText(mDatas.get(position).getName());
        }

        if (viewHolder.tvName != null) {
            if (this.mCurrentItem == position){
                viewHolder.tvId.setBackgroundColor(Color.parseColor("#007aff"));
            } else {
                viewHolder.tvId.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        if (viewHolder.mThumbnail != null && mDatas.get(position).getImage().length() > 0) {
            Glide.with(convertView)
                    .load(mDatas.get(position).getImage()).placeholder(new ColorDrawable(Color.DKGRAY))
                    .error(new ColorDrawable(Color.DKGRAY))
                    .into((ImageView) convertView.findViewById(R.id.ivThumbnail));
        }


        return convertView;
    }

    public void setCurrentItem(int currentItem) {
        this.mCurrentItem = currentItem;
    }

    private final class ViewHolder
    {
        ImageView mThumbnail;
        TextView tvName;
        TextView tvId;
    }
}
