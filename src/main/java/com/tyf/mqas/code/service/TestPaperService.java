package com.tyf.mqas.code.service;

import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.TestPaperRepository;
import com.tyf.mqas.code.entity.*;
import com.tyf.mqas.config.ConfigData;

import com.tyf.mqas.utils.FileUtil;
import com.tyf.mqas.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.util.*;

@Service
@Transactional
public class TestPaperService extends PageGetter<TestPaper> {

    private final static Logger logger = LoggerFactory.getLogger(TestPaperService.class);

    @Autowired
    private TestPaperRepository testPaperRepository;
    @Autowired
    private ConfigData configData;


    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<TestPaper> getDataPage(Map<String,String[]> parameterMap){
        return super.getPages(parameterMap);
    }


    /**
     * 判断重复
     * @param name
     * @return
     */
    public boolean verifyTheRepeat(String name){
        Integer num = null;
        num = testPaperRepository.getTestPaperByName( name);
        return num > 0?false:true;
    }

    /**
     * 保存试卷
     * @param testPaper
     * @return
     */
    public void saveOrEditTestPaper(TestPaper testPaper,HttpServletRequest request){
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
            Iterator iter = mr.getFileMap().values().iterator();
            if (iter.hasNext()) {
                MultipartFile file = (MultipartFile) iter.next();
                String realFileName = file.getOriginalFilename();
                String suffix = realFileName.substring(realFileName.lastIndexOf(".") + 1);
                //保存路径
                String saveDir = configData.getTestPaperDir();
                String saveFileName = UUID.randomUUID().toString();
                String fileName = testPaper.getName()+"."+suffix;
                testPaper.setSaveFileName(saveFileName);
                testPaper.setFileName(fileName);
                InputStream input = null;
                try {
                    input = file.getInputStream();
                    FileUtil.copyFile(input,saveDir+"/"+saveFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                testPaperRepository.save(testPaper);
            }
        }

    }




    public TestPaper getTestPaper(Integer id){
        return testPaperRepository.getOne(id);
    }
    /** 
    * @Description: 删除
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/10/10 19:14
    */ 
    public void deleteTestPaper(Integer id){
        TestPaper testPaper = testPaperRepository.getOne(id);
        logger.info(SecurityUtil.getCurUserName()+"-删除-试卷 "+testPaper.getName()+" 成功");
        //删除文件
        FileUtil.deleteFile(configData.getTestPaperDir()+"/"+testPaper.getSaveFileName());
        testPaperRepository.deleteById(id);
    }

    /**
     * 下载试卷
     * @param response
     * @param id
     */
    public void downloadTestPaper(HttpServletResponse response,Integer id){
        TestPaper testPaper = testPaperRepository.getOne(id);
        logger.info(SecurityUtil.getCurUserName()+"-下载-试卷 "+testPaper.getName()+" 成功");
        File file = new File(configData.getTestPaperDir()+"/"+testPaper.getSaveFileName());
        try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((testPaper.getFileName()).getBytes(), "iso-8859-1"));
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 读出文件到response
            // 这里是先需要把要把文件内容先读到缓冲区
            // 再把缓冲区的内容写到response的输出流供用户下载
            byte[] b = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(b);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(b);
            // 人走带门
            bufferedInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
