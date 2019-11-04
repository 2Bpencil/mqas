package com.tyf.mqas.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description: word工具
 * @Author: tyf
 * @Date: 2019/10/31 08:53
 */
public class PoiWordUtil {

    public static void main(String[] args) {
        // 创建Word文件
        XWPFDocument doc = new XWPFDocument();
        PoiWordUtil.addPicture(doc,new File("F:\\test\\doc\\aaa"),"jpg","aaa");
        PoiWordUtil.addPicture(doc,new File("F:\\test\\doc\\bbb"),"jpg","bbb");
        PoiWordUtil.addPicture(doc,new File("F:\\test\\doc\\bbb"),"jpg","ccc");
        PoiWordUtil.addPicture(doc,new File("F:\\test\\doc\\bbb"),"jpg","ddd");
        PoiWordUtil.addPicture(doc,new File("F:\\test\\doc\\bbb"),"jpg","eee");
        PoiWordUtil.addPicture(doc,new File("F:\\test\\doc\\bbb"),"jpg","fff");
        try{
            FileOutputStream out = new FileOutputStream("F:\\test\\doc\\test.docx");
            doc.write(out);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void  addPicture(XWPFDocument doc, File imgFile,String suffix,String title){
        // 新建一个段落
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        int format = 0;
        switch ("."+suffix){
            case ".wmf":
                format = XWPFDocument.PICTURE_TYPE_WMF;
                break;
            case ".pict":
                format = XWPFDocument.PICTURE_TYPE_PICT;
                break;
            case ".jpeg":
                format = XWPFDocument.PICTURE_TYPE_JPEG;
                break;
            case ".jpg":
                format = XWPFDocument.PICTURE_TYPE_JPEG;
                break;
            case ".png":
                format = XWPFDocument.PICTURE_TYPE_PNG;
                break;
            case ".dib":
                format = XWPFDocument.PICTURE_TYPE_DIB;
                break;
            case ".gif":
                format = XWPFDocument.PICTURE_TYPE_GIF;
                break;
            case ".tiff":
                format = XWPFDocument.PICTURE_TYPE_TIFF;
                break;
            case ".eps":
                format = XWPFDocument.PICTURE_TYPE_EPS;
                break;
            case ".bmp":
                format = XWPFDocument.PICTURE_TYPE_BMP;
                break;
            case ".wpg":
                format = XWPFDocument.PICTURE_TYPE_WPG;
                break;
        }
        r.setText(title);
        r.addBreak();
        try {
            r.addPicture(new FileInputStream(imgFile.getPath()), format, imgFile.getPath(), Units.toEMU(400), Units.toEMU(200));
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //另起一页
//        r.addBreak(BreakType.PAGE);
    }


}
    