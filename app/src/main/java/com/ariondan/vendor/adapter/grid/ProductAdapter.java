package com.ariondan.vendor.adapter.grid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ariondan.vendor.R;
import com.ariondan.vendor.model.ProductModel;

import java.util.List;

/**
 * Created by Dan on 7/9/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> items;
    private RelativeLayout layoutCart;
    private int totalItems;

    public ProductAdapter(Context context, RelativeLayout layoutCart, List<ProductModel> objects) {
        this.context = context;
        this.layoutCart = layoutCart;
        items = objects;
        totalItems = 0;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ViewHolder holder, int position) {
        ProductModel item = items.get(position);
        holder.textProduct.setText(item.getName());
        holder.textPrice.setText(String.valueOf(item.getPrice()));

        //TODO: get image by name from stream (from server)
//        holder.imageProduct.setImageBitmap();

        //delete this part when introducing streaming
        holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.coca_cola));
        if (item.getName().equals("Mici")) {
            holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.mici));
        }

        View.OnClickListener clickAdd = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.textCount.setText(String.valueOf(Integer.parseInt(holder.textCount.getText().toString()) + 1));
                layoutCart.setVisibility(View.VISIBLE);
                totalItems++;
            }
        };
        holder.layoutProduct.setOnClickListener(clickAdd);
        holder.textMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.textCount.getText().toString()) > 0) {
                    holder.textCount.setText(String.valueOf(Integer.parseInt(holder.textCount.getText().toString()) - 1));
                    totalItems--;
                    if (totalItems == 0) {
                        layoutCart.setVisibility(View.GONE);
                    }
                }
            }
        });
        holder.textPlus.setOnClickListener(clickAdd);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutProduct;
        TextView textProduct;
        TextView textPrice;
        ImageView imageProduct;
        TextView textMinus;
        TextView textCount;
        TextView textPlus;

        ViewHolder(View view) {
            super(view);
            layoutProduct = (LinearLayout) view.findViewById(R.id.layout_item_product);
            textProduct = (TextView) view.findViewById(R.id.text_item_product_name);
            textPrice = (TextView) view.findViewById(R.id.text_item_product_price);
            imageProduct = (ImageView) view.findViewById(R.id.image_item_product);
            textMinus = (TextView) view.findViewById(R.id.text_item_product_minus);
            textCount = (TextView) view.findViewById(R.id.text_item_product_count);
            textPlus = (TextView) view.findViewById(R.id.text_item_product_plus);
        }
    }

}
