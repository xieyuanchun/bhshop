package com.bh.wxpayjsapi.notify;

/**
 * 
 * @author xxj
 *
 */
public interface PaySuccessCallBack {

    /**
     * 支付成功回调。 商户进行业务处理，如果处理失败，抛出异常。
     * @param payNotifyData
     * @return
     */
    public void onSuccess(PayNotifyData payNotifyData);
}
