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
        //插入图片
        // 新建一个段落
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();

        File imgFile = new File("F:\\test\\doc\\timg.jpg");
        int format = 0;

        switch (".jpg"){
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
        r.setText(imgFile.getPath());
        r.addBreak();
        try {
            r.addPicture(new FileInputStream(imgFile.getPath()), format, imgFile.getPath(), Units.toEMU(430), Units.toEMU(250));
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        r.addBreak(BreakType.PAGE);
        try{
            FileOutputStream out = new FileOutputStream("F:\\test\\doc\\test.doc");
            doc.write(out);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


}
    