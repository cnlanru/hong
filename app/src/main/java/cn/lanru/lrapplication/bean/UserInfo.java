package cn.lanru.lrapplication.bean;

import java.util.List;

public class UserInfo {

  /**
   * data : {"email":"","icon":"","id":12662,"password":"","token":"","type":0,"username":"15294792877"}
   * errorCode : 0
   * errorMsg :
   */

  private DataBean data;

  public DataBean getData() {
    return data;
  }

  public void setData(DataBean data) {
    this.data = data;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  private int code;
  private String msg;

  public static class DataBean {

    /**
     * email :
     * icon :
     * id : 12662
     * password :
     * token :
     * type : 0
     * username : 15294792877
     */

    private String email;
    private String avatar;
    private int id;
    private String password;
    private String token;
    private int type;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getAvatar() {
      return avatar;
    }

    public void setAvatar(String avatar) {
      this.avatar = avatar;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public int getGroup_id() {
      return group_id;
    }

    public void setGroup_id(int group_id) {
      this.group_id = group_id;
    }

    public String getNickname() {
      return nickname;
    }

    public void setNickname(String nickname) {
      this.nickname = nickname;
    }

    public String getMobile() {
      return mobile;
    }

    public void setMobile(String mobile) {
      this.mobile = mobile;
    }

    public int getGender() {
      return gender;
    }

    public void setGender(int gender) {
      this.gender = gender;
    }

    private String username;
    private int group_id;
    private String nickname;
    private String mobile;
    private int gender;



    @Override
    public String toString() {
      return "DataBean{" +
          "email='" + email + '\'' +
          ", avatar='" + avatar + '\'' +
          ", id=" + id +
          ", password='" + password + '\'' +
          ", token='" + token + '\'' +
          ", group_id=" + group_id +
          ", username='" + username + '\'' +
          ", nickname=" + nickname +
          ", mobile=" + mobile +
          ", gender=" + gender +
          '}';
    }
  }

  @Override
  public String toString() {
    return "UserInfo{" +
        "data=" + data +
        ", code=" + code +
        ", msg='" + msg + '\'' +
        '}';
  }
}
