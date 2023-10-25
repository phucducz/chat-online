package com.firebaseclound.chatonline;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

class AdapterMessage extends BaseAdapter{

    private Context context;
    private int layout;
    private List<Message> messageList;

    public AdapterMessage(Context context, int layout, List<Message> messageList) {
        this.context = context;
        this.layout = layout;
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder extends Application {
        private RelativeLayout relMessageReceive;
        private RelativeLayout relMessageSend;

        private ImageView imgAvatar;
        private TextView txtMessageReceive;
        private TextView txtTimeReceive;
        private TextView txtMessageSend;
        private TextView txtTimeSend;
        private TextView txtHoTen;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView==null){

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout,null);

                holder = new ViewHolder();

                holder.relMessageReceive = (RelativeLayout) convertView.findViewById(R.id.relMessageReceive);
                holder.relMessageSend = (RelativeLayout) convertView.findViewById(R.id.relMessageSend);
                holder.txtMessageReceive = (TextView) convertView.findViewById(R.id.txtMessageReceive);
                holder.txtMessageSend = (TextView) convertView.findViewById(R.id.txtMessageSend);
                holder.txtTimeReceive = (TextView) convertView.findViewById(R.id.txtTimeReceive);
                holder.txtTimeSend = (TextView) convertView.findViewById(R.id.txtTimeSend);
                holder.txtHoTen = (TextView) convertView.findViewById(R.id.txtHoTen);

                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
                holder.imgAvatar.setDrawingCacheEnabled(true);
                convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message message = messageList.get(position);

        holder.relMessageSend.setVisibility(View.VISIBLE);
        holder.imgAvatar.setVisibility(View.VISIBLE);
        holder.relMessageReceive.setVisibility(View.VISIBLE);
        holder.txtTimeReceive.setVisibility(View.VISIBLE);

        if (!message.getUsername().trim().equals(message.getTaikhoan().trim())){
            RootApplication.setHinhAnh(context,message.getAvatar(),holder.imgAvatar,R.drawable.cat,R.drawable.cat);
            holder.txtTimeReceive.setText(message.getTime());
            holder.txtMessageReceive.setText(message.getNoidung());
            holder.txtHoTen.setText(message.getName());
            holder.relMessageSend.setVisibility(View.GONE);
        }else {
            holder.txtMessageSend.setText(message.getNoidung());
            holder.txtTimeSend.setText(message.getTime());
            holder.imgAvatar.setVisibility(View.GONE);
            holder.relMessageReceive.setVisibility(View.GONE);
            holder.txtTimeReceive.setVisibility(View.GONE);
        }

        return convertView;
    }
}
