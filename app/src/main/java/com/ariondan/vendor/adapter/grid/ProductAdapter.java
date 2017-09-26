package com.ariondan.vendor.adapter.grid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.ariondan.vendor.R;
import com.ariondan.vendor.model.ProductModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Dan on 7/9/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> items;
    private RequestQueue queue;
    private OnClickItemListener clickListener;

    public ProductAdapter(Activity activity, List<ProductModel> objects) {
        this.context = activity;
        items = objects;
        queue = Volley.newRequestQueue(activity);
        clickListener = (OnClickItemListener) activity;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ViewHolder holder, int position) {
        final ProductModel item = items.get(position);
        holder.textProduct.setText(item.getName());
        holder.textPrice.setText(new DecimalFormat("#.#").format(item.getPrice()));

        holder.imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.loading));
        ImageRequest request = new ImageRequest(item.getImageURL(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imageProduct.setImageBitmap(response);
                try {
                    ProductModel model = items.get(holder.getAdapterPosition());
                    model.setImage(response);
                    items.set(holder.getAdapterPosition(), model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

        holder.layoutProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnClickItemListener {
        void onClickItem(ProductModel product);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutProduct;
        TextView textProduct;
        TextView textPrice;
        ImageView imageProduct;

        ViewHolder(View view) {
            super(view);
            layoutProduct = (LinearLayout) view.findViewById(R.id.layout_item_product);
            textProduct = (TextView) view.findViewById(R.id.text_item_product_name);
            textPrice = (TextView) view.findViewById(R.id.text_item_product_price);
            imageProduct = (ImageView) view.findViewById(R.id.image_item_product);
        }
    }

}
