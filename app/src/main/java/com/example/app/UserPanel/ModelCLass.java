package com.example.app.UserPanel;

public class ModelCLass {

    private String title,dic,image,pid;

    public ModelCLass(String title, String dic, String image, String pid) {
        this.title = title;
        this.dic = dic;
        this.image = image;
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
