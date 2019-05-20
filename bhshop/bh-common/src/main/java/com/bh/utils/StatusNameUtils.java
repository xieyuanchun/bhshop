package com.bh.utils;

import org.apache.commons.lang.StringUtils;

public class StatusNameUtils {

	// 根据orderShop设置状态
	public static String getMyStatus(int status, int fz) {
		String str = null;
		switch (status) { // 商家订单状态：1待付，2待发货，3已发货，4已收货，5待评价、6已取消、7已评价、8已删除
		case 1:
			if (fz == 1) {
				str = "待付款";
			} else {
				str = "";
			}
			break;
		case 2:
			if (fz == 1) {
				str = "待发货";
			} else {
				str = "";
			}
			break;
		case 3:
			if (fz == 1) {
				str = "已发货";
			} else {
				str = "";
			}
			break;
		case 5:
			if (fz == 1) {
				str = "待评价";
			} else {
				str = "";
			}
			break;
		case 6:
			if (fz == 1) {
				str = "已取消";
			} else {
				str = "";
			}
			break;
		case 7:
			if (fz == 1) {
				str = "已评价";
			} else {
				str = "";
			}
			break;
		case 8:
			if (fz == 1) {
				str = "已删除";
			} else {
				str = "";
			}
			;
			break;
		case 9:
			// 用户端如果是备货中需要改为待发货
			if (fz == 1) {
				str = "待发货";
			} else {
				str = "";
			}
			break;
		case 10:
			if(fz == 1) {
				str = "已失效";
			}else {
				str = "";
			}
			break;
		default:
			str = "待付款";
			break;
		}
		return str;
	}

	public static String getRefundStatusName(Integer status, Integer refundType, String expressNo) throws Exception {
		String str = null;
		switch (status) {
		// 退款状态，0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中 8：换货成功 9：换货失败
		// 10客服通过退款审核 11收货客服审核通过',
		case 0:
			if (refundType == 1) {
				str = "退款中";
			}
			if (refundType == 2) {
				str = "换货中";
			}
			if (refundType == 3) {
				str = "退款退货中";
			}
			break;
		case 1:
			if (refundType == 1) {
				str = "退款失败";
			}
			if (refundType == 2) {
				str = "换货失败";
			}
			if (refundType == 3) {
				str = "退换失败";
			}
			break;
		case 2:
			if (refundType == 1) {
				str = "退款成功";
			}
			if (refundType == 2) {
				str = "换货成功";
			}
			if (refundType == 3) {
				str = "退换成功";
			}
			break;
		case 3:
			if (refundType == 1) {
				str = "退款已拒绝";
			}
			if (refundType == 2) {
				str = "换货已拒绝";
			}
			if (refundType == 3) {
				str = "拒绝退换";
			}
			break;
		case 5:
			if(expressNo!=null&&!"".equals(expressNo)) {
				str = "退款退货中";
			}else {
				str = "请填写物流";
			}
			break;
		case 6:
			str = "退货失败";
			break;
		case 7:
			if (StringUtils.isNotEmpty(expressNo)) {
				str = "换货中";
			} else {
				str = "请填写物流";
			}
			break;
		case 8:
			str = "换货成功";
			break;
		case 9:
			if (refundType == 3) {
				str = "退换失败";
			}
			if (refundType == 2) {
				str = "换货失败";
			}
			if (refundType == 1) {
				str = "退款失败";
			}
			break;
		case 10:
			if (refundType == 1) {
				str = "退款中";
			}
			if (refundType == 2) {
				str = "换货中";
			}
			if (refundType == 3) {
				str = "退款退货中";
			}
			break;
		case 11:
			if (refundType == 1) {
				str = "退款中";
			}
			if (refundType == 2) {
				str = "换货中";
			}
			if (refundType == 3) {
				str = "退款退货中";
			}
			break;
		default:
			str = "状态异常";
			break;
		}
		return str;
	}

	// 2018-5-22
	public static String getAdminStatusName(Integer shopStatus, Integer refundStatus, String reason) {
		String statusName = "";
		statusName = getAdminMyStatus(shopStatus, 1);
		if ((refundStatus!=-1)&&(StringUtils.isNotEmpty(reason))) {
			statusName=getAdminRefundStatusName(refundStatus, reason);
		}
		return statusName;
	}

	

	public static String getAdminRefundStatusName(Integer status, String reason) {
		String statusName = "";
		if ((status != null) && (StringUtils.isNotEmpty(reason))) {
			Integer refundStatus = status;
			// 退款状态，0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中 8：换货成功
			// 9：换货失败 10客服通过退款审核 11收货客服审核通过
			switch (refundStatus) {
			case 0:
				if (!reason.equals("拼单失败")) {
					statusName = "申请" + reason + "中";
				}
				break;
			case 1:
				statusName = "退款失败";
				break;
			case 2:
				statusName = "退款成功";
				break;
			case 3:
				statusName = reason + "已拒绝";
				break;
			case 5:
				statusName = "申请退货中";
				break;
			case 6:
				statusName = "申请退货失败";
				break;
			case 7:
				statusName = "换货中";
				break;
			case 8:
				statusName = "换货成功";
				break;
			case 9:
				statusName = "换货失败";
				break;
			case 10:
				statusName = "客服通过退款审核";
				break;
			case 11:
				statusName = "收货客服审核通过";
				break;
			default:
				if (!reason.equals("拼单失败")) {
					statusName = "申请" + reason + "中";
				}
				break;
			}
		}
		return statusName;
	}

	// 根据orderShop设置状态
	public static String getAdminMyStatus(int status, int fz) {
		String str = null;
		switch (status) { // 商家订单状态：1待付，2待发货，3已发货，4已收货，5待评价、6已取消、7已评价、8已删除
		case 1:
			if (fz == 1) {
				str = "待付款";
			} else {
				str = "";
			}
			break;
		case 2:
			if (fz == 1) {
				str = "待发货";
			} else {
				str = "";
			}
			break;
		case 3:
			if (fz == 1) {
				str = "已发货";
			} else {
				str = "";
			}
			break;
		case 5:

			if (fz == 1) {
				str = "待评价";
			} else {
				str = "";
			}

			break;
		case 6:
			if (fz == 1) {
				str = "已取消";
			} else {
				str = "";
			}
			break;
		case 7:
			if (fz == 1) {
				str = "已评价";
			} else {
				str = "";
			}
			break;
		case 8:
			if (fz == 1) {
				str = "已删除";
			} else {
				str = "";
			}
			;
			break;
		case 9:
			// 用户端如果是备货中需要改为待发货
			if (fz == 1) {
				str = "备货中";
			} else {
				str = "";
			}
			break;
		default:
			str = "待付款";
			break;
		}
		return str;
	}
}
