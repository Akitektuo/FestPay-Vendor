package com.ariondan.vendor.adapter.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariondan.vendor.R;
import com.ariondan.vendor.model.HistoryModel;

import java.util.List;

/**
 * Created by alexandrucopindean on 24/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryModel> items;

    public HistoryAdapter(Context context, List<HistoryModel> objects) {
        this.context = context;
        items = objects;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        HistoryModel item = items.get(position);
        holder.textNumber.setText(String.format("%d.", item.getId()));

        //TODO: get image by name from internal storage
        holder.imageProduct.setImageBitmap(item.getImage());

        //delete this part when introducing local db
//        holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.coca_cola));
//        if (item.getProduct().equals("Mici")) {
//            holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.mici));
//        }

        holder.textProduct.setText(item.getProduct());
        holder.textPrice.setText(String.valueOf(item.getPrice()));
        holder.textQuantity.setText(String.format("x%d", item.getQuantity()));
        holder.textTotalPrice.setText(String.valueOf(item.getTotalPrice()));
        holder.textCustomer.setText(item.getCustomer());
        holder.textTime.setText(item.getTimeAsString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNumber;
        ImageView imageProduct;
        TextView textProduct;
        TextView textPrice;
        TextView textQuantity;
        TextView textTotalPrice;
        TextView textCustomer;
        TextView textTime;

        ViewHolder(View view) {
            super(view);
            textNumber = (TextView) view.findViewById(R.id.text_history_number);
            imageProduct = (ImageView) view.findViewById(R.id.image_history_product);
            textProduct = (TextView) view.findViewById(R.id.text_history_product);
            textPrice = (TextView) view.findViewById(R.id.text_history_price);
            textQuantity = (TextView) view.findViewById(R.id.text_history_quantity);
            textTotalPrice = (TextView) view.findViewById(R.id.text_history_total_price);
            textCustomer = (TextView) view.findViewById(R.id.text_history_customer);
            textTime = (TextView) view.findViewById(R.id.text_history_time);
        }
    }
}
