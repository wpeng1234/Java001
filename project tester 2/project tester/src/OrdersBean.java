import java.io.Serializable;

public class OrdersBean implements Serializable {
    private int OrderID;
    private int BaristaID;
    private String OrderItems;
    private double OrderPrice;
    private String time;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getBaristaID() {
        return BaristaID;
    }

    public void setBaristaID(int baristaID) {
        BaristaID = baristaID;
    }

    public String getOrderItems() {
        return OrderItems;
    }

    public void setOrderItems(String orderItems) {
        OrderItems = orderItems;
    }

    public double getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {

        return OrderID + "," + BaristaID + "," + OrderItems + "," + OrderPrice + "," + time;
    }
}
