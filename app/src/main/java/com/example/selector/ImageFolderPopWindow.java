package com.example.selector;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 * 利用popupwindow弹出图片文件的类
 * Created by pbq on 2016/7/19.
 */
public class ImageFolderPopWindow extends PopupWindow {
    HashMap<String, ImageDir> dirMap;
    LayoutInflater inflaotor;
    ListView lvDir;
    Context context;

    public OnClickListener onItemClickListner;

    public void setOnPopClickListener(OnClickListener listener){
        onItemClickListner=listener;
    }

    public ImageFolderPopWindow(Context context,int width,int height) {
        inflaotor = LayoutInflater.from(context);
        setContentView(initView());
        setHeight(height);
        setWidth(width);
        setFocusable(true); // 设置PopupWindow可获得焦点
        setTouchable(true); // 设置PopupWindow可触摸
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        this.context=context;
    }

    private View initView() {
        View view = inflaotor.inflate(R.layout.popwindow_img_dir, null);
        lvDir = (ListView) view.findViewById(R.id.lv_img_dir);
        return view;
    }

    public void popWindow(HashMap<String, ImageDir> dirMap,View view) {
        this.dirMap = dirMap;
        lvDir.setAdapter(new ImageDirAdapter(dirMap));
        showAsDropDown(view);
    }

    class ImageDirAdapter extends BaseAdapter {
        ImageDir[] dirs;

        public ImageDirAdapter(HashMap<String, ImageDir> dirMaps) {
            dirs = dirMaps.values().toArray(new ImageDir[dirMaps.size()]);
        }

        @Override
        public int getCount() {
            return dirs.length;
        }

        @Override
        public ImageDir getItem(int position) {
            return dirs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageDir dir=getItem(position);
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView=inflaotor.inflate(R.layout.list_item_dir_pop_window, null);
                viewHolder=new ViewHolder();
                viewHolder.imgHeader=(ImageView) convertView.findViewById(R.id.img_dir_header);
                viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_dir_title);
                viewHolder.imgDot=(ImageView) convertView.findViewById(R.id.img_dir_dot);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(dir.getDirName()+" ("+dir.getFiles().size()+")");
            if(dir.type== ImageDir.Type.IMAGE){
                Glide.with(context).load(dir.firstImagePath).placeholder(R.drawable.default_image).into(viewHolder.imgHeader);
                Log.i("ImageFolderPopWindow","图片类型"+dir.firstImagePath);
            }else {
                Glide.with(context).load(dir.firstImagePath).placeholder(R.drawable.default_image).into(viewHolder.imgHeader);
                Log.i("ImageFolderPopWindow","视频类型"+dir.firstImagePath);
            }

            if(dir.selectedFiles.size()>0){
                //选中状态
                viewHolder.imgDot.setVisibility(View.VISIBLE);
            }else{
                //未选中
                viewHolder.imgDot.setVisibility(View.INVISIBLE);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //设置路径 Tag
                    v.setTag(dir);
                    onItemClickListner.onClick(v);
                }
            });

            return convertView;
        }

        public class ViewHolder{
            TextView tvTitle;
            ImageView  imgHeader;
            ImageView imgDot;
        }
    }
}
