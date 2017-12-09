package com.techcoderz.ruchira.ModelClasses;

/**
 * Created by Shahriar on 10/16/2016.
 */

public class ProductList {
    String productId;
    String productName;
    int pricePerPiece;
    String flag;
    String promotionId;
    int pricePerCarton;
    String productSku;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public int getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(int pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int getPricePerCarton() {
        return pricePerCarton;
    }

    public void setPricePerCarton(int pricePerCarton) {
        this.pricePerCarton = pricePerCarton;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }
}
