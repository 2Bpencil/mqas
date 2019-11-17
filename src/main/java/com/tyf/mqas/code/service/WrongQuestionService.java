package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.*;
import com.tyf.mqas.code.entity.Classes;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.entity.WrongQuestion;
import com.tyf.mqas.config.ConfigData;
import com.tyf.mqas.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class WrongQuestionService extends PageGetter<WrongQuestion>{

    private final static Logger logger = LoggerFactory.getLogger(WrongQuestionService.class);

    @Autowired
    private WrongQuestionRepository wrongQuestionRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private ConfigData configData;
    @Autowired
    private WrongQuestionDao wrongQuestionDao;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassesRepository classesRepository;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<WrongQuestion> getDataPage(Map<String,String[]> parameterMap,Integer id,String startTime,String endTime){
        String sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id;
        String countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id;
        //开始结束时间都不为空
        if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
            sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time BETWEEN '"+startTime+"' AND '" +endTime+"'";
            countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time BETWEEN '"+startTime+"' AND '" +endTime+"'";
        }else if(StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)){
            //开始时间不为空
            sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time > '"+startTime+"'";
            countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time > '"+startTime+"'";
        }else if(StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)){
            //结束时间不为空
            sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time  < '" +endTime+"'";
            countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time < '" +endTime+"'";
        }
        return super.getPages(parameterMap,sql,countSql);
    }



    /**
     * 删除实体
     * @param id
     */
    public void deleteWrongQuestion(Integer id){
        WrongQuestion wrongQuestion = wrongQuestionRepository.getOne(id);
        logger.info(SecurityUtil.getCurUserName()+"-删除-错题 "+wrongQuestion.getName()+" 成功");
        //删除文件
        FileUtil.deleteFile(configData.getWrongQuestionDir()+"/"+wrongQuestion.getFileSaveName());
        wrongQuestionRepository.deleteById(id);
    }

    /**
     * 知识点错误次数数据
     * @param studentId
     * @return
     */
    public String getknowledgePointErrorNumJson(Integer studentId,String startDate,String endDate){
        Map<String,Object> dataMap = new HashMap<>();
        List<Map<String,Object>> knowledgeInfoList = wrongQuestionRepository.getAllKnowledgeInfoByStudentIdAndTime(studentId,startDate,endDate);
        List<String> xAxis = new ArrayList<>();
        List<Integer> wrongNumList = new ArrayList<>();
        knowledgeInfoList.forEach(map->{
            xAxis.add(map.get("knowledge_name").toString());
            wrongNumList.add(wrongQuestionRepository.getRongNumByStudentIdAndKnowledgeCode(studentId,map.get("knowledge_code").toString(),startDate,endDate));
        });
        dataMap.put("xAxis",xAxis);
        dataMap.put("data",wrongNumList);
        return JSONObject.toJSONString(dataMap);
    }

    /**
     * 获取该学生所有错误知识点
     * @return
     */
    public String getAllKnowledge(Integer studentId){
        List<Map<String,Object>> knowledgeInfoList = wrongQuestionRepository.getAllKnowledgeInfoByStudentId(studentId);
        return JSONArray.toJSONString(knowledgeInfoList);
    }


    /**
     * 知识点频次分析
     * @param studentId
     * @param code
     * @return
     */
    public String knowledgeFrequency(Integer studentId,String code){

        Map<String,Object> dataMap = new HashMap<>();
        List<String> xAxis = new ArrayList<>();
        List<String> dataList = new ArrayList<>();
        List<Map<String,String>> list = wrongQuestionRepository.knowledgeFrequency(studentId,code);
        list.forEach(map->{
            xAxis.add(map.get("time"));
            dataList.add(map.get("num"));
        });
        dataMap.put("xAxis",xAxis);
        dataMap.put("data",dataList);
        return JSONObject.toJSONString(dataMap);
    }

    /**
     *重点知识统计
     * @param studentId
     * @return
     */
    public String keyKnowledge(Integer studentId){
        Map<String,Object> dataMap = new HashMap<>();
        List<String> legend = new ArrayList<>();
        List<Map<String,String>> dataList = new ArrayList<>();
        List<Map<String,String>> keyKnowledgeInfoList = wrongQuestionRepository.getKeyKnowledgeWrongNum(studentId);
        Integer unKeyNum = wrongQuestionRepository.getUnKeyKnowledgeWrongNum(studentId);
        keyKnowledgeInfoList.forEach(map->{
            legend.add(map.get("name"));
            dataList.add(map);
        });
        legend.add("非重点知识");
        Map<String,String> unKeyMap = new HashMap<>();
        unKeyMap.put("value",unKeyNum.toString());
        unKeyMap.put("name","非重点知识");
        dataList.add(unKeyMap);
        dataMap.put("legend",legend);
        dataMap.put("data",dataList);
        return JSONObject.toJSONString(dataMap);
    }

    /**
     * 保存错题信息
     * @param request
     * @param wrongQuestion
     */
    public void saveWrongQuestion(HttpServletRequest request, WrongQuestion wrongQuestion){
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
            Iterator iter = mr.getFileMap().values().iterator();
            if (iter.hasNext()) {
                MultipartFile file = (MultipartFile) iter.next();
                String realFileName = file.getOriginalFilename();
                String suffix = realFileName.substring(realFileName.lastIndexOf(".") + 1);
                wrongQuestion.setFileSuffix(suffix);
                //保存路径
                String saveDir = configData.getWrongQuestionDir();
                String saveFileName = UUID.randomUUID().toString();
                wrongQuestion.setFileSaveName(saveFileName+"."+suffix);
                InputStream input = null;
                try {
                    input = file.getInputStream();
                    FileUtil.copyFile(input,saveDir+"/"+saveFileName+"."+suffix);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        wrongQuestion.setTime(sdf.format(new Date()));
        wrongQuestionRepository.save(wrongQuestion);
    }

    /**
     * 下载错题
     * @param response
     * @param id
     */
    public void downloadWrongQuestion(HttpServletResponse response, Integer id){
        WrongQuestion wrongQuestion = wrongQuestionRepository.getOne(id);
        logger.info(SecurityUtil.getCurUserName()+"-下载-错题 "+wrongQuestion.getName()+" 成功");
        File file = new File(configData.getWrongQuestionDir()+"/"+wrongQuestion.getFileSaveName());
        try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((System.currentTimeMillis()+"."+wrongQuestion.getFileSuffix()).getBytes(), "iso-8859-1"));
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
    /** 
    * @Description: 导出学生错题
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/10/12 14:16
    */ 
    public void exportStudentWrongQuestion(Integer studentId,String code,String level,String startTime,String endTime,HttpServletResponse response){
        String codes = getAllKnowledgeCodeByCode(code);
        List<WrongQuestion> wrongQuestionList = wrongQuestionDao.getWrongQuestionByStudentIdAndConditions(studentId,codes,level,startTime,endTime);
        // 创建Word文件
        XWPFDocument doc = new XWPFDocument();
        wrongQuestionList.forEach(q->{
            File file = new File(configData.getWrongQuestionDir()+"/"+q.getFileSaveName());
            PoiWordUtil.addPicture(doc,file,q.getFileSuffix(),q.getName());
        });
        Student student = studentRepository.getStudentById(studentId);
        try{
            String fileName = "学生--"+student.getName()+"--错题_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(Charset.defaultCharset().toString()), "ISO8859-1") + ".docx");
            OutputStream outputStream = response.getOutputStream();
            doc.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /** 
    * @Description:  导出年级班级错题
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/10/14 11:19
    */ 
    public void exportClassWrongQuestion(Integer classId,Integer type,String code,String level,String startTime,String endTime,HttpServletResponse response){
        //去重集合
        List<WrongQuestion> wrongQuestionDuplicateRemovalList = new ArrayList<>();
        Map<String,WrongQuestion>  wrongQuestionMap = new HashMap<>();
        String codes = getAllKnowledgeCodeByCode(code);
        List<WrongQuestion> wrongQuestionList = wrongQuestionDao.getWrongQuestionByClassIdAndConditions(classId,type,codes,level,startTime,endTime);
        wrongQuestionList.forEach(q->{
            wrongQuestionMap.put(q.getName(),q);
        });
        wrongQuestionMap.keySet().forEach(key->{
            wrongQuestionDuplicateRemovalList.add(wrongQuestionMap.get(key));
        });
        // 创建Word文件
        XWPFDocument doc = new XWPFDocument();
        wrongQuestionDuplicateRemovalList.forEach(q->{
            File file = new File(configData.getWrongQuestionDir()+"/"+q.getFileSaveName());
            PoiWordUtil.addPicture(doc,file,q.getFileSuffix(),q.getName());
        });
        Classes classes = classesRepository.getOne(classId);
        try{
            String fileName = classes.getName()+"--错题_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(Charset.defaultCharset().toString()), "ISO8859-1") + ".docx");
            OutputStream outputStream = response.getOutputStream();
            doc.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /** 
    * @Description: 向浏览器导出文件
    * @Param:
    * @return:
    * @Author: Mr.Tan 
    * @Date: 2019/10/16 14:23
    */ 
    private void exportFile(File file,String fileName,HttpServletResponse response){
        try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(), "iso-8859-1"));
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



    /** 
    * @Description: 判断是否有文件 
    * @Param:  
    * @return:
    * @Author: Mr.Tan 
    * @Date: 2019/10/12 17:17
    */ 
    public boolean hasStudentWrongQuestion(Integer studentId,String code,String level,String startTime,String endTime){
        String codes = getAllKnowledgeCodeByCode(code);
        List<WrongQuestion> wrongQuestionList = wrongQuestionDao.getWrongQuestionByStudentIdAndConditions(studentId,codes,level,startTime,endTime);
        if(wrongQuestionList.size()>0){
            return true;
        }else{
            return false;
        }
    }
    /** 
    * @Description:  判断年级班级是否有文件
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/10/14 11:20
    */ 
    public boolean hasClassWrongQuestion(Integer studentId,Integer type,String code,String level,String startTime,String endTime){
        String codes = getAllKnowledgeCodeByCode(code);
        List<WrongQuestion> wrongQuestionList = wrongQuestionDao.getWrongQuestionByClassIdAndConditions(studentId,type,codes,level,startTime,endTime);
        if(wrongQuestionList.size()>0){
            return true;
        }else{
            return false;
        }
    }
    private String getAllKnowledgeCodeByCode(String code){
        String codes = "";
        List<String> codeList = knowledgeRepository.findCodeByParentCode(code);
        for (String c:codeList  ) {
            if(StringUtils.isBlank(code)){
                code += "'"+c+"'";
            }else {
                code += ",'"+c+"'";
            }
        }
        return codes;
    }

    /**
     * 识别图片中的文字
     * @param request
     * @return
     */
    public String ocr(HttpServletRequest request){
        String content = "";
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
            Iterator iter = mr.getFileMap().values().iterator();
            if (iter.hasNext()) {
                MultipartFile file = (MultipartFile) iter.next();
                try {
                    content = Tess4jUtils.doOCR_BufferedImage(file.getInputStream());
                    content = content.replaceAll("\\s*|\t|\r|\n","");
                } catch (Exception e) {
                    content = "图片内容识别错误！";
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

}