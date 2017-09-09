package com.ariondan.vendor.adapter.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariondan.vendor.R;
import com.ariondan.vendor.model.CartModel;

import java.util.List;

/**
 * Created by AoD Akitektuo on 09-Aug-17 at 21:56.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> items;

    public CartAdapter(Context context, List<CartModel> objects) {
        this.context = context;
        items = objects;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, int position) {
        CartModel item = items.get(position);
        //TODO: get image by name from network
        holder.imageProduct.setImageBitmap(item.getImage());

        //delete this part when introducing stream data
//        holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.coca_cola));
//        if (item.getName().equals("Mici")) {
//            holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.mici));
//        }
        holder.textProduct.setText(item.getName());
        holder.textPrice.setText(String.valueOf(item.getPrice()));
        holder.textQuantity.setText(String.format("x%d", item.getQuantity()));
        holder.textTotalPrice.setText(String.valueOf(item.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProduct;
        TextView textPrice;
        TextView textQuantity;
        TextView textTotalPrice;

        ViewHolder(View view) {
            super(view);
            imageProduct = (ImageView) view.findViewById(R.id.image_item_cart);
            textProduct = (TextView) view.findViewById(R.id.text_item_cart_name);
            textPrice = (TextView) view.findViewById(R.id.text_item_cart_price);
            textQuantity = (TextView) view.findViewById(R.id.text_item_cart_quantity);
            textTotalPrice = (TextView) view.findViewById(R.id.text_item_cart_total_price);
        }
    }

}
