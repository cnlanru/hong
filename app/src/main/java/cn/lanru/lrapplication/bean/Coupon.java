package cn.lanru.lrapplication.bean;

public class Coupon {

    public String title;
    public int type;
    public int id;
    public String money;
    public String min_money;
    public String tips;
    public String begin_validity;
    public String end_validity;
    public String status;
    public String EndTime;
    public String Times;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String Times) {
        this.Times = Times;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMin_money() {
        return min_money;
    }

    public void setMin_money(String min_money) {
        this.min_money = min_money;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getBegin_validity() {
        return begin_validity;
    }

    public void setBegin_validity(String begin_validity) {
        this.begin_validity = begin_validity;
    }

    public String getEnd_validity() {
        return end_validity;
    }

    public void setEnd_validity(String end_validity) {
        this.end_validity = end_validity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
