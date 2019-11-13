package com.tyf.mqas.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

/**
 * @Description: 
 * @Author: tyf
 * @Date: 2019/11/13 09:26
 */
public class Tess4jUtils {
    private final static String testResourcesLanguagePath = "src/main/resources/tessdata";
    public static void main(String[] args) throws Exception{
        ITesseract instance = new Tesseract();
        File imageFile = new File("C:\\Users\\Administrator\\Pictures\\wx2.jpg");
        instance.setDatapath(testResourcesLanguagePath);
        instance.setLanguage("chi_sim");
        String result = instance.doOCR(imageFile);
        System.out.println(result.replaceAll(" ",""));
    }

}
    