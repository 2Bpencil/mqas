package com.tyf.mqas.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.util.ClassUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description: 
 * @Author: tyf
 * @Date: 2019/11/13 09:26
 */
public class Tess4jUtils {
    public static void main(String[] args) throws Exception{
        ITesseract instance = new Tesseract();
        FileInputStream fi = new FileInputStream("C:\\Users\\Administrator\\Pictures\\demo\\math.jpg");
        BufferedImage bi = ImageIO.read(fi);
        instance.setDatapath("F:\\mqas\\tessdata");
        instance.setLanguage("chi_sim+equ");
        String result = instance.doOCR(bi);
        System.out.println(result.replaceAll(" ",""));
    }


    public static String doOCR_BufferedImage(InputStream is,String tessdataPath) throws Exception{
        ITesseract instance = new Tesseract();
        BufferedImage bi = ImageIO.read(is);
        //set language
        instance.setDatapath(tessdataPath);
        instance.setLanguage("chi_sim");
        String result = instance.doOCR(bi);
        return result;
    }
    /** 
    * @Description:  识别中文
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/11/19 11:40
    */ 
    public static String doOCR_Chi(InputStream is,String tessdataPath)throws Exception{
        ITesseract instance = new Tesseract();
        BufferedImage bi = ImageIO.read(is);
        instance.setDatapath(tessdataPath);
        instance.setLanguage("chi_sim");
        String result = instance.doOCR(bi);
        result = result.replaceAll("\\s*|\t|\r|\n","");
        return result;
    }
    /** 
    * @Description:  识别英文
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/11/19 11:40
    */ 
    public static String doOCR_Eng(InputStream is,String tessdataPath)throws Exception{
        ITesseract instance = new Tesseract();
        BufferedImage bi = ImageIO.read(is);
        instance.setDatapath(tessdataPath);
        instance.setLanguage("eng");
        String result = instance.doOCR(bi);
        return result;
    }

}
    