package com.amazon.notification.batch;

/**
 * Simple POJO to represent a product
 */
public class Product
{
    private int id;
    private String attrName;
    private String attrValue;

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
    

    public Product() {
    }

    public Product(int id, String attrName, String attrValue) {
        this.id = id;
        this.attrName = attrName;
        this.attrValue = attrValue;
       
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
