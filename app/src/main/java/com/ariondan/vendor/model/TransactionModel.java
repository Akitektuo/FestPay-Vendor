package com.ariondan.vendor.model;

import java.util.List;

/**
 * Created by AoD Akitektuo on 12-Sep-17 at 20:21.
 */

public class TransactionModel {

    private List<Integer> productIds;
    private int vendorId;
    private int customerId;

    public TransactionModel(List<Integer> productIds, int vendorId, int customerId) {
        setProductIds(productIds);
        setVendorId(vendorId);
        setCustomerId(customerId);
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
}
