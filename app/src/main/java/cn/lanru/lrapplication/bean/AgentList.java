package cn.lanru.lrapplication.bean;

public class AgentList {

    public int id;
    public String title;
    public String price;
    public String description;
    public String thumbnail;
    public String tips;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDscription() {
        return description;
    }

    public void setDescriptione(String price) {
        this.description = price;
    }
}
