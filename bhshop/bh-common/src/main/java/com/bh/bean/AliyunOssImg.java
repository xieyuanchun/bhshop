package com.bh.bean;

import com.bh.utils.StringUtil;

/**
 * 
 * @author xxj
 *
 */
public class AliyunOssImg {
	// 图片宽度
	private int width;
	// 图片高度
	private int height;
	// x坐标轴
	private int x;
	// y坐标轴
	private int y;
	// 旋转角度
	private int angle;
	// 锐化值
	private int sharpenVal;
	// 水印文本
	private String waterTxt;
    //图片样式
	private ImgStyle imgStyle;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getSharpenVal() {
		return sharpenVal;
	}

	public void setSharpenVal(int sharpenVal) {
		this.sharpenVal = sharpenVal;
	}

	public ImgStyle getImgStyle() {
		return imgStyle;
	}

	public void setImgStyle(ImgStyle imgStyle) {
		this.imgStyle = imgStyle;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getWaterTxt() {
		return new String(StringUtil.decodeBase64(waterTxt));
	}

	public void setWaterTxt(String waterTxt) {
		this.waterTxt = StringUtil.encodeBase64(waterTxt.getBytes());
	}

	public static enum ImgStyle {
		RESIZE("image/resize", "缩放"), CROP("image/crop", "剪裁"), ROTATE("image/rotate", "旋转"), SHARPER("image/sharpen",
				"锐化"), WATERMARK("image/watermark", "水印");
		private String type;
		private String desc;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		private ImgStyle(String type, String desc) {

		}
	}

}
