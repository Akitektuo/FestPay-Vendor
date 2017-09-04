package com.ariondan.vendor.network;

import com.ariondan.vendor.model.ProductModel;

import java.util.List;

/**
 * Created by AoD Akitektuo on 04-Sep-17 at 10:40.
 */

public interface ProductResponse {

    void loadProducts(List<ProductModel> productModels);

}
