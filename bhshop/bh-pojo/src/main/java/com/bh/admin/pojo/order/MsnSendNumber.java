package com.bh.admin.pojo.order;

import java.util.Date;

public class MsnSendNumber {
    private Integer senmsnnumId;

    private Integer mId;

    private Integer apymsnId;

    private String phone;

    private Date addTime;

    private Date editTime;

    public Integer getSenmsnnumId() {
        return senmsnnumId;
    }

    public void setSenmsnnumId(Integer senmsnnumId) {
        this.senmsnnumId = senmsnnumId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getApymsnId() {
        return apymsnId;
    }

    public void setApymsnId(Integer apymsnId) {
        this.apymsnId = apymsnId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}