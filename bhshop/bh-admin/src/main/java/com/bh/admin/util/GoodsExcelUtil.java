package com.bh.admin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bh.admin.pojo.goods.ExportGoods;
import com.bh.admin.pojo.order.Phone;

/**
 * excel工具类,支持批量导出
 * @author lizewu
 *
 */
public class GoodsExcelUtil {
    
    /**
     * <p>Description: 商品导出</p>
     *  @author scj  
     *  @date 2018年8月29日
     */
    public static void exportGoodsExcel(List<ExportGoods> list,ServletOutputStream out)
    {
        try{
            //1.创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //1.1创建合并单元格对象
            CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,4);//起始行,结束行,起始列,结束列
            //1.2头标题样式
            HSSFCellStyle headStyle = createCellStyle(workbook,(short)16);
            //1.3列标题样式
            HSSFCellStyle colStyle = createCellStyle(workbook,(short)13);
            //2.创建工作表
            HSSFSheet sheet = workbook.createSheet("sheet1");
            //2.1加载合并单元格对象
            sheet.addMergedRegion(callRangeAddress);
            //设置默认列宽
            sheet.setDefaultColumnWidth(15);
            //3.创建行
            //3.1创建头标题行;并且设置头标题
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
        
            //加载单元格样式
            cell.setCellStyle(headStyle);
            cell.setCellValue("滨惠商城商品excle数据");
            
            //3.2创建列标题;并且设置列标题
            HSSFRow row2 = sheet.createRow(1);
            String[] titles = TdTitle();
            for(int i=0;i<titles.length;i++)
            {
                HSSFCell cell2 = row2.createCell(i);
                //加载单元格样式
                cell2.setCellStyle(colStyle);
                cell2.setCellValue(titles[i]);
            }
            
            //4.操作单元格;将用户列表写入excel
            if(list != null)
            {
                for(int j=0;j<list.size();j++)
                {
                    //创建数据行,前面有两行,头标题行和列标题行
                    HSSFRow row3 = sheet.createRow(j+2);
                	ExportGoods exportGoods = list.get(j);
                	row3.createCell(0).setCellValue(exportGoods.getId());
                	row3.createCell(1).setCellValue(exportGoods.getId());
                	row3.createCell(2).setCellValue(exportGoods.getGoodsName());
                	row3.createCell(3).setCellValue(exportGoods.getSkuNo());
                    if (StringUtils.isBlank(exportGoods.getGoodsSkuName())) {
                    	row3.createCell(4).setCellValue(exportGoods.getGoodsName());
        			}else{
        				row.createCell(4).setCellValue(exportGoods.getGoodsSkuName());
        			}
                    row3.createCell(5).setCellValue(exportGoods.getValueOne());
                    row3.createCell(6).setCellValue(exportGoods.getValueTwo());
                    row3.createCell(7).setCellValue(exportGoods.getValueThree());
                    row3.createCell(8).setCellValue(exportGoods.getValueFour());
                    row3.createCell(9).setCellValue(exportGoods.getValueFive());
                    row3.createCell(10).setCellValue(exportGoods.getCategoryName());
                    row3.createCell(11).setCellValue(exportGoods.getMarketRealPrice());
                    row3.createCell(12).setCellValue(exportGoods.getRealSellPrice());
                    row3.createCell(13).setCellValue(exportGoods.getRealJdProtocolPrice());
                    row3.createCell(14).setCellValue(exportGoods.getRealTeamPrice());
                    row3.createCell(15).setCellValue(exportGoods.getRealStockPrice());
                    row3.createCell(16).setCellValue(exportGoods.getRealJdBuyPrice());
                    row3.createCell(17).setCellValue(exportGoods.getStoreNums());
                    row3.createCell(18).setCellValue(exportGoods.getHavedSale());
                    row3.createCell(19).setCellValue(exportGoods.getSale());
                    row3.createCell(20).setCellValue(exportGoods.getJdSkuNo());
                }
            }
            //5.输出
            workbook.write(out);
            //workbook.close();
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
        // TODO Auto-generated method stub
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        return style;
    }
    
    /**
     * 写入结果到excel文件
     *
     * @param path
     * @param fileName
     * @param fileType
     * @param list
     * @param pageTitle
     * @param colTitle
     * @throws IOException
     */
    public static void writer(String path, String fileName, String fileType, List<ExportGoods> list, String pageTitle, String[] colTitle) throws IOException {
        Workbook wb = null;
        String excelPath;
        if (StringUtils.isNotBlank(path)) {
            excelPath = path + File.separatorChar + fileName + "." + fileType;
        } else {
            excelPath = fileName + "." + fileType;
        }
        File file = new File(excelPath);
        Sheet sheet = null;
        //创建工作文档对象
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                try {
                    throw new Exception("文件格式不正确");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //创建sheet对象
            sheet = (Sheet) wb.createSheet("sheet1");
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } else {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();

            } else {
                try {
                    throw new Exception("文件格式不正确");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //创建sheet对象
        if (sheet == null) {
            sheet = (Sheet) wb.createSheet("sheet1");
        }
        Row row;
        Cell cell;
        CellStyle style = wb.createCellStyle(); // 样式对象
        // 设置单元格的背景颜色为淡蓝色
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);
        style.setFont(font);

        //添加表头
        int startLine = 0;
        if (StringUtils.isNotBlank(pageTitle)) {
            row = sheet.createRow(startLine++);
            cell = row.createCell(0);
            row.setHeight((short) 540);
            cell.setCellValue(pageTitle);    //创建第一行
            cell.setCellStyle(style); // 样式，居中
            // 单元格合并
            // 四个参数分别是：起始行，起始列，结束行，结束列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            sheet.autoSizeColumn(5200);
        }

        row = sheet.createRow(startLine++);    //创建第二行
        row.setHeight((short) 540);
        int num=colTitle.length;
        for (int i = 0; i <num; i++) {
            cell = row.createCell(i);
            cell.setCellValue(colTitle[i]);
            cell.setCellStyle(style); // 样式，居中
            sheet.setColumnWidth(i, 20*256);
        }

        //循环写入行数据
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + startLine);
            row.setHeight((short) 500);
            ExportGoods exportGoods = list.get(i);
            row.createCell(0).setCellValue(exportGoods.getId());
            row.createCell(1).setCellValue(exportGoods.getId());
            row.createCell(2).setCellValue(exportGoods.getGoodsName());
            row.createCell(3).setCellValue(exportGoods.getSkuNo());
            if (StringUtils.isBlank(exportGoods.getGoodsSkuName())) {
            	row.createCell(4).setCellValue(exportGoods.getGoodsName());
			}else{
				row.createCell(4).setCellValue(exportGoods.getGoodsSkuName());
			}
           
            row.createCell(5).setCellValue(exportGoods.getValueOne());
            row.createCell(6).setCellValue(exportGoods.getValueTwo());
            row.createCell(7).setCellValue(exportGoods.getValueThree());
            row.createCell(8).setCellValue(exportGoods.getValueFour());
            row.createCell(9).setCellValue(exportGoods.getValueFive());
            row.createCell(10).setCellValue(exportGoods.getCategoryName());
            row.createCell(11).setCellValue(exportGoods.getMarketRealPrice());
            row.createCell(12).setCellValue(exportGoods.getRealSellPrice());
            row.createCell(13).setCellValue(exportGoods.getRealJdProtocolPrice());
            row.createCell(14).setCellValue(exportGoods.getRealTeamPrice());
            row.createCell(15).setCellValue(exportGoods.getRealStockPrice());
            row.createCell(16).setCellValue(exportGoods.getRealJdBuyPrice());
            row.createCell(17).setCellValue(exportGoods.getStoreNums());
            row.createCell(18).setCellValue(exportGoods.getHavedSale());
            row.createCell(19).setCellValue(exportGoods.getSale());
            row.createCell(20).setCellValue(exportGoods.getJdSkuNo());
        }

        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }
    
    /**
     * <p>Description: 表头</p>
     *  @author scj  
     *  @date 2018年8月29日
     */
    public static String[] TdTitle(){
    	String[] str = new String[21];
    	ArrayList<String> fieldName =new ArrayList<>();  
		fieldName.add("商品ID"); //0商品ID
		fieldName.add("父级商品ID");  //1父级商品ID
		fieldName.add("商品名称");  //2商品名称
		
	    fieldName.add("skuNo");//3
        fieldName.add("商品(sku)名称");//4
        fieldName.add("属性一");//5
        fieldName.add("属性二");//6
        fieldName.add("属性三");//7
        fieldName.add("属性四");//8
        fieldName.add("属性五");//9
        fieldName.add("商品分类");//10
        fieldName.add("市场价");//11
        fieldName.add("单买价");//12
        fieldName.add("协议价");//13
        fieldName.add("团购价");//14
        fieldName.add("进货价");//15
        fieldName.add("用户购买价");//16
        fieldName.add("库存");//17
        fieldName.add("已售数量");//18
        fieldName.add("销量");//19
        fieldName.add("京东SKU编码");//20
        for(int i=0;i<fieldName.size();i++){  
        	str[i]=(String)fieldName.get(i);  
        }
        return str;
    }
    
    
    
    
    /**
     * 写入结果到excel文件
     *
     * @param path
     * @param fileName
     * @param fileType
     * @param list
     * @param pageTitle
     * @param colTitle
     * @throws IOException
     */
    public static void writer1(String path, String fileName, String fileType, List<Phone> list, String[] colTitle) throws IOException {
        Workbook wb = null;
        String excelPath;
        if (StringUtils.isNotBlank(path)) {
            excelPath = path + File.separatorChar + fileName + "." + fileType;
        } else {
            excelPath = fileName + "." + fileType;
        }
        File file = new File(excelPath);
        Sheet sheet = null;
        //创建工作文档对象
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                try {
                    throw new Exception("文件格式不正确");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //创建sheet对象
            sheet = (Sheet) wb.createSheet("sheet1");
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } else {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();

            } else {
                try {
                    throw new Exception("文件格式不正确");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //创建sheet对象
        if (sheet == null) {
            sheet = (Sheet) wb.createSheet("sheet1");
        }
        Row row;
        Cell cell;
        CellStyle style = wb.createCellStyle(); // 样式对象
        // 设置单元格的背景颜色为淡蓝色
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);
        style.setFont(font);

        //添加表头
        int startLine = 0;
       /* if (StringUtils.isNotBlank(pageTitle)) {
            row = sheet.createRow(startLine++);
            cell = row.createCell(0);
            row.setHeight((short) 540);
            cell.setCellValue(pageTitle);    //创建第一行
            cell.setCellStyle(style); // 样式，居中
            // 单元格合并
            // 四个参数分别是：起始行，起始列，结束行，结束列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            sheet.autoSizeColumn(5200);
        }*/

        row = sheet.createRow(startLine++);    //创建第二行
        row.setHeight((short) 540);
        int num=colTitle.length;
        for (int i = 0; i <num; i++) {
            cell = row.createCell(i);
            cell.setCellValue(colTitle[i]);
            cell.setCellStyle(style); // 样式，居中
           //sheet.setColumnWidth(i, 20*256);
        }

        //循环写入行数据
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + startLine);
            row.setHeight((short) 500);
            Phone phone = list.get(i);
            row.createCell(0).setCellValue(phone.getPhone());
        }

        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }
    
    
    /**
     * <p>Description: 表头</p>
     *  @author scj  
     *  @date 2018年8月29日
     */
    public static String[] TdTitle1(){
    	String[] str = new String[1];
    	ArrayList<String> fieldName =new ArrayList<>();  
		fieldName.add("手机号"); //手机号
        for(int i=0;i<fieldName.size();i++){  
        	str[i]=(String)fieldName.get(i);  
        }
        return str;
    }
}
