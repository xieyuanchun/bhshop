package com.bh.user.pojo;

public class MemberUserAccountLog {
    private Integer id;

    private Integer mId;

    private Integer amount;

    private Long amountLog;

    private Byte event;

    private Integer addtime;

    private String adminUser;

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getAmountLog() {
        return amountLog;
    }

    public void setAmountLog(Long amountLog) {
        this.amountLog = amountLog;
    }

    public Byte getEvent() {
        return event;
    }

    public void setEvent(Byte event) {
        this.event = event;
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser == null ? null : adminUser.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}