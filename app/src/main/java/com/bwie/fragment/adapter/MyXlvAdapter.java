package com.bwie.fragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bwie.fragment.R;
import com.bwie.fragment.bean.Data;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



/**
 * Created by hasee on 2017/8/14.
 */

public class MyXlvAdapter extends BaseAdapter {
    private Context context;
    private List<Data.DataBean> data;
    private LayoutInflater mLayoutInflater;
    private PopupWindow mPopupWindow;
    private TextView deleteView;
    private ImageView closeView;

    public MyXlvAdapter(Context context, List<Data.DataBean> data) {
        this.context = context;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(context);
        initPopView();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item, null);
            holder.name =  convertView.findViewById(R.id.name);
            holder.image_url =  convertView.findViewById(R.id.image_url);
            holder.moreView =  convertView.findViewById(R.id.more_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getTITLE());
        ImageLoader.getInstance().displayImage((String) data.get(position).getIMAGEURL(),holder.image_url);
        holder.moreView.setOnClickListener(new PopAction(position));
        return convertView;
    }

    class ViewHolder {
        private TextView name;
        private ImageView image_url,moreView;

    }

    class PopAction implements View.OnClickListener {

        private int position;

        public PopAction(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            //操作对应positiion的数据
            int[] array = new int[2];
            v.getLocationOnScreen(array);
            int x = array[0];
            int y = array[1];
            showPop(v, position, x, y);
        }
    }

    private void initPopView() {
        View popwindowLayout = mLayoutInflater.inflate(R.layout.popupwindow, null);
        mPopupWindow = new PopupWindow(popwindowLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        mPopupWindow.setAnimationStyle(R.style.popWindowAnimation);

        //知道popwindow中间的控件 ,去做点击
        deleteView = popwindowLayout.findViewById(R.id.delete_tv);
        closeView =  popwindowLayout.findViewById(R.id.close_iv);
    }

    private void showPop(View parent, final int position, int x, int y) {

        //根据view的位置显示popupwindow的位置
        mPopupWindow.showAtLocation(parent, 0, x, y);

        //根据view的位置popupwindow将显示到他的下面 , 可以通过x ,y 参数修正这个位置
        // mPopupWindow.showAsDropDown(parent,0,-50);

        //设置popupwindow可以获取焦点,不获取焦点的话 popupwiondow点击无效
        mPopupWindow.setFocusable(true);

        //点击popupwindow的外部,popupwindow消失
        mPopupWindow.setOutsideTouchable(true);

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });


    }
}
