package com.tyf.mqas.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.util.ClassUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * @Description: 
 * @Author: tyf
 * @Date: 2019/11/13 09:26
 */
public class Tess4jUtils {
    public static void main(String[] args) throws Exception{
        ITesseract instance = new Tesseract();
        File imageFile = new File("C:\\Users\\Administrator\\Pictures\\wx2.jpg");
        instance.setDatapath("F:\\mqas\\tessdata");
        instance.setLanguage("chi_sim");
        String result = instance.doOCR(imageFile);
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

}
    