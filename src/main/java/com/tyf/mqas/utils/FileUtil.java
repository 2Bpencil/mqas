package com.tyf.mqas.utils;

import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @Description: 文件工具
 * @Author: tyf
 * @Date: 2019/10/11 09:24
 */
public class FileUtil {

    
    
    
    /** 
    * @Description: 文件复制  （效率高）
    * @Param:
    * @return:
    * @Author: Mr.Tan 
    * @Date: 2019/10/11 9:27
    */ 
    public static void copyFileUsingFileChannels(File source,String pathNew) throws IOException {
        File fileNew = new File(pathNew);
        FileChannel  inputChannel = new FileInputStream(source).getChannel();
        FileChannel outputChannel = new FileOutputStream(fileNew).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        outputChannel.close();
        inputChannel.close();
    }

    /**
     * 输入流复制
     * @param inputStream 源文件输入流
     * @param targetFilePath 目标文件
     * @throws IOException
     */
    public static void copyFile(InputStream inputStream,String targetFilePath) throws IOException{
        File targetFile = new File(targetFilePath);
        OutputStream outStream = new FileOutputStream(targetFile);
        FileCopyUtils.copy(inputStream,outStream);
    }

    /**
     * 创建文件夹
     */
    public static void creatFoler(String path){
        File folder = new File(path);
        if(!folder.exists() && ! folder.isDirectory()){
            folder.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @author CRF
     * @date 2017年5月3日 下午4:57:09
     */
    public static void creatFile(String path){
        File folder = new File(path);
        if(!folder.exists()){
            try {
                folder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param path
     * @author tyf
     * @date 2017年5月3日 下午4:57:19
     */
    public static void deleteFile(String path){
        File folder = new File(path);
        if(folder.exists()){
            folder.delete();
        }
    }

    /**
     *  删除文件夹下所有文件
     * @param foler
     */
    public static void deleteFoler(File foler){
        if(foler.isDirectory()){
            File[] files = foler.listFiles();
            for(File file : files){
                file.delete();
            }
        }
    }

    /**
     * 删除文件下所有内容以及该文件夹
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            // 删除完里面所有内容
            delAllFile(folderPath);
            //不想删除文佳夹隐藏下面
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            // 删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹下所有内容
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                // 先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                // 再删除空文件夹
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

}
    