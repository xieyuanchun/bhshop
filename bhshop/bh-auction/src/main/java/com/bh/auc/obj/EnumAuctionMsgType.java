package com.bh.auc.obj;

/**
 * Created by zhouyongyi on 2018/7/12.
 */

// 0-本期开拍;1-价格下降;2-流拍;3-有新出价;4-待支付
public enum EnumAuctionMsgType {
    BID_START("BID_START"),
    DOWN_PRICE("DOWN_PRICE"),
    AUCTION_LOSE("AUCTION_LOSE"),
    NEW_BID("NEW_BID"),
    WAIT_PAY("WAIT_PAY");

    private String auctionMsgType;

    private EnumAuctionMsgType(String auctionMsgType) {
        this.auctionMsgType = auctionMsgType;
    }

    public String getAuctionMsgType() {
        return this.auctionMsgType;
    }
}