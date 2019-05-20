package com.bh.utils;

public class CoordinatesUtil {
	
	//赤道周长
	public final static int equatorialCircumference = 40075000;
	
	
	/**  
     * 生成以中心点为中心的四方形经纬度  
     * @param lat 纬度  
     * @param lon 精度  
     * @param raidus 半径（以米为单位）  
     * @return minLat 最小的纬度  minLng最小经度    maxLat最大的纬度 maxLng最大经度
     */    
    public static double[] getAround(Double lat, Double lon, int raidus) {    
        Double latitude = lat;    
        Double longitude = lon;    
        Double degree = (24901 * 1609) / 360.0;    
        double raidusMile = raidus;    
        Double dpmLat = 1 / degree;    
        Double radiusLat = dpmLat * raidusMile;    
        Double minLat = latitude - radiusLat;    
        Double maxLat = latitude + radiusLat;    
        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));    
        Double dpmLng = 1 / mpdLng;                 
        Double radiusLng = dpmLng * raidusMile;     
        Double minLng = longitude - radiusLng;      
        Double maxLng = longitude + radiusLng;
        return new double[] { minLat, minLng, maxLat, maxLng };    
              
    	
    	/*double r = 6371;//地球半径千米  
        double dis = 0.5;//0.5千米距离  
        double dlng =  2*Math.asin(Math.sin(raidus/(2*r))/Math.cos(lat*Math.PI/180));  
        dlng = dlng*180/Math.PI;//角度转为弧度  
        double dlat = raidus/r;  
        dlat = dlat*180/Math.PI;          
        double minlat =lat-dlat;  
        double maxlat = lat+dlat;  
        double minlng = lon -dlng;  
        double maxlng = lon + dlng;  
        return new double[] { minlat, minlng, maxlat, maxlng };    */
    }
    
    /** 
     * 计算中心经纬度与目标经纬度的距离（米） 
     *  
     * @param centerLon 
     *            中心精度 
     * @param centerLan 
     *            中心纬度 
     * @param targetLon 
     *            需要计算的精度 
     * @param targetLan 
     *            需要计算的纬度 
     * @return 米 
     */  
    private static double distance(double centerLon, double centerLat, double targetLon, double targetLat) {  
        double jl_jd = 102834.74258026089786013677476285;// 每经度单位米;  
        double jl_wd = 111712.69150641055729984301412873;// 每纬度单位米;  
        double b = Math.abs((centerLat - targetLat) * jl_jd);  
        double a = Math.abs((centerLon - targetLon) * jl_wd);  
        return Math.sqrt((a * a + b * b));  
    } 
    
    
    public static void main(String[] args) {
    	double[] T = getAround(10D, 10D, 100);
    	
    	System.out.println(distance(39.938454,116.377547,39.938347,116.379776)+" 1");
    	System.out.println(distance(39.938454,116.377547,39.938236,116.379709)+"  2");
    	System.out.println(distance(39.938454,116.377547,39.937884,116.379781)+"   4");

	}
    
	/** 
	 * 计算地球上任意两点(经纬度)距离 
	 *  
	 * @param long1 
	 *            第一点经度 
	 * @param lat1 
	 *            第一点纬度 
	 * @param long2 
	 *            第二点经度 
	 * @param lat2 
	 *            第二点纬度 
	 * @return 返回距离 单位：米 
	 */  
	public static double Distance(double long1, double lat1, double long2,double lat2) {  
	    double a, b, R;  
	    R = 6378137; // 地球半径  
	    lat1 = lat1 * Math.PI / 180.0;  
	    lat2 = lat2 * Math.PI / 180.0;  
	    a = lat1 - lat2;  
	    b = (long1 - long2) * Math.PI / 180.0;  
	    double d;  
	    double sa2, sb2;  
	    sa2 = Math.sin(a / 2.0);  
	    sb2 = Math.sin(b / 2.0);  
	    d = 2   * R   * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)* Math.cos(lat2) * sb2 * sb2));  
	    return d;  
	} 
	
	 /**
	  * 根据经纬度和距离返回一个矩形范围
	  * 
	  * @param lng
	  *  经度
	  * @param lat
	  *  纬度
	  * @param distance
	  *  距离(单位为米)
	  * @return [lng1,lat1, lng2,lat2] 矩形的左下角(lng1,lat1)和右上角(lng2,lat2)
	  */
	 public static double[] getRectangle(double lng, double lat, long distance) {
	  float delta = 111000;
	  if (lng != 0 && lat != 0) {
	   double lng1 = lng - distance
	     / Math.abs(Math.cos(Math.toRadians(lat)) * delta);
	   double lng2 = lng + distance
	     / Math.abs(Math.cos(Math.toRadians(lat)) * delta);
	   double lat1 = lat - (distance / delta);
	   double lat2 = lat + (distance / delta);
	   return new double[] { lng1, lat1, lng2, lat2 };
	  } else {
	   // TODO ZHCH 等于0时的计算公式
	   double lng1 = lng - distance / delta;
	   double lng2 = lng + distance / delta;
	   double lat1 = lat - (distance / delta);
	   double lat2 = lat + (distance / delta);
	   return new double[] { lng1, lat1, lng2, lat2 };
	  }
	 }
	 
	 /**
	  * 得到两点间的距离 米
	  * 
	  * @param lat1
	  * @param lng1
	  * @param lat2
	  * @param lng2
	  * @return
	  */
	 public static double getDistanceOfMeter(double lat1, double lng1,
	   double lat2, double lng2) {
	  double radLat1 = rad(lat1);
	  double radLat2 = rad(lat2);
	  double a = radLat1 - radLat2;
	  double b = rad(lng1) - rad(lng2);
	  double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
	    + Math.cos(radLat1) * Math.cos(radLat2)
	    * Math.pow(Math.sin(b / 2), 2)));
	  s = s * EARTH_RADIUS;
	  s = Math.round(s * 10000) / 10;
	  return s;
	 }
	  
	 private static double rad(double d) {
	  return d * Math.PI / 180.0;
	 }
	 /**
	  * 地球半径：6378.137KM
	  */
	 private static double EARTH_RADIUS = 6378.137;
}
