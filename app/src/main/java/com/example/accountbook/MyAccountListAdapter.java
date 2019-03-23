package com.example.accountbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyAccountListAdapter extends RecyclerView.Adapter<MyAccountListAdapter.ViewHolder> {
    static List<MyAccount> mMyAccountList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView typeImage;
        TextView time;
        TextView price;
        TextView type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeImage=(ImageView)itemView.findViewById(R.id.imageView_item_image);
            time=(TextView)itemView.findViewById(R.id.textView_item_date);
            price=(TextView)itemView.findViewById(R.id.textView_item_money);
            type=(TextView)itemView.findViewById(R.id.textView_item_type);
        }
    }

    public MyAccountListAdapter(Context context,List<MyAccount> accountList){
        mContext=context;
        mMyAccountList=accountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder( view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MyAccount myAccount=mMyAccountList.get(i);
        switch(myAccount.getType()){
            case "food":
                viewHolder.typeImage.setImageResource(R.drawable.food);
                break;
            case "fruits":
                viewHolder.typeImage.setImageResource(R.drawable.fruit);
                break;
            case "communication":
                viewHolder.typeImage.setImageResource(R.drawable.communication);
                break;
            case "education":
                viewHolder.typeImage.setImageResource(R.drawable.education);
                break;
            case "trip":
                viewHolder.typeImage.setImageResource(R.drawable.trip);
                break;
            case "clothes":
                viewHolder.typeImage.setImageResource(R.drawable.clothes);
                break;
            case "medical":
                viewHolder.typeImage.setImageResource(R.drawable.medical);
                break;
            case "entertainment":
                viewHolder.typeImage.setImageResource(R.drawable.entertainment);
                break;
            case "income":
                viewHolder.typeImage.setImageResource(R.drawable.income);
                break;
            case "bonus":
                viewHolder.typeImage.setImageResource(R.drawable.bonus);
                break;
            case "investment":
                viewHolder.typeImage.setImageResource(R.drawable.investment);
                break;
            default:
                //都不属于上面的话
                if(!myAccount.getPhotoPath().equals("")){
                    try {
                        Uri imageUri;
                        File outputImage=new File(mContext.getExternalFilesDir(null),myAccount.getPhotoPath());
                        if(Build.VERSION.SDK_INT>=24){
                            imageUri= FileProvider.getUriForFile(mContext,"com.example.accountbook.fileprovider",outputImage);
                        }else{
                            imageUri=Uri.fromFile(outputImage);
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imageUri));
                        viewHolder.typeImage.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    viewHolder.typeImage.setImageResource(R.drawable.entertainment);
                }
                break;
        }

        viewHolder.type.setText(myAccount.getType());
        if(myAccount.getPriceType()==1){
            viewHolder.price.setText("-"+String.valueOf(myAccount.getPrice()));
        }else{
            viewHolder.price.setText("+"+String.valueOf(myAccount.getPrice()));
        }
        viewHolder.time.setText(myAccount.getTime());
    }


    @Override
    public int getItemCount() {
        return mMyAccountList.size();
    }

}
