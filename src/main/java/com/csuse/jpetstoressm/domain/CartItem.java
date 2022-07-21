package com.csuse.jpetstoressm.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {
  private static final long serialVersionUID = 6620528781626504362L;

  private String itemId;

  private Item item;
  private int quantity;
  private boolean inStock;
  private int QTY;

  public int getQTY() {
    return QTY;
  }

  public void setQTY(int QTY) {
    this.QTY = QTY;
    setInStock(QTY);
  }

  private BigDecimal total;

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }



  public boolean isInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }

  public void setInStock(int quantity)
  {
    if(quantity>0)
      this.inStock=true;
    else this.inStock=false;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
    calculateTotal();
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
    calculateTotal();
  }

  public void incrementQuantity() {
    quantity++;
    calculateTotal();
  }

  private void calculateTotal() {
    if (item != null && item.getListPrice() != null) {
      total = item.getListPrice().multiply(new BigDecimal(quantity));
    } else {
      total = null;
    }
  }

}
