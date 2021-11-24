package database.entities;

import java.util.ArrayList;
import java.util.List;

public class BookOrder {
    private String orderNum;
    private String ISBN;
    private String dateOut;
    private String quantityOrdered;
    private List<String> attributes ;
    public BookOrder(){
        attributes = new ArrayList<>();
        for(int i=0 ;i<4;i++)attributes.add("");
    }
    public BookOrder(List<String> attributes){
        this.attributes = new ArrayList<>(attributes);
        orderNum = this.attributes.get(0);
        ISBN = this.attributes.get(1);
        dateOut = this.attributes.get(2);
        quantityOrdered = this.attributes.get(3);
    }

    public List<String> getAttributes() {
        return attributes;
    }
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
        attributes.set(0,orderNum);
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
        attributes.set(1,ISBN);
    }

    public String getDateOut() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
        attributes.set(2,dateOut);
    }

    public String getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(String quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
        attributes.set(3,quantityOrdered);
    }
}
