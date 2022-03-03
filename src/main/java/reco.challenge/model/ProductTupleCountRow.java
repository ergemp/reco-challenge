package reco.challenge.model;

import java.io.Serializable;

public class ProductTupleCountRow implements Serializable {
    private String product1 ;
    private String product2 ;
    private Integer count;

    public String getProduct1() {
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public String getProduct2() {
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ProductTupleCountRow (Integer gCount, String gProduct1, String gProduct2){
        this.count = gCount;
        this.product1 = gProduct1;
        this.product2 = gProduct2;
    }
}
