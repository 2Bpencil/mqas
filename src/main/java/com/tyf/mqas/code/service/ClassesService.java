package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.code.dao.*;
import com.tyf.mqas.code.entity.*;
import com.tyf.mqas.utils.PoiUtil;
import com.tyf.mqas.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class ClassesService {

    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WrongQuestionRepository wrongQuestionRepository;


    /**
     * 获取所有菜单
     * @return
     */
    public String getAllClassess(){
        List<Classes> list = classesRepository.findAllBySort();
        List<TreeTable> treeTables = new ArrayList<>();
        list.forEach(classes -> {
            TreeTable treeTable = new TreeTable();
            treeTable.setId(classes.getId()+"");
            treeTable.setpId(classes.getPid()==0?"":classes.getPid().toString());
            treeTable.setName(classes.getName());
            String[] td = {classes.getSort()==null?"":classes.getSort().toString()};
            treeTable.setTd(td);
            treeTables.add(treeTable);
        });
        return JSONArray.toJSONString(treeTables);
    }

    /**
     * ztree data
     * @return
     */
    public String getAllClassesTree(){
        List<Classes> list = classesRepository.findAllBySort();
        List<Tree> treeList = new ArrayList<>();
        list.forEach(classes -> {
            Tree tree = new Tree();
            tree.setId(classes.getId()+"");
            tree.setpId(classes.getPid()==0?"":classes.getPid().toString());
            tree.setName(classes.getName());
            if(classes.getPid()==0){//年级
                tree.setIcon("/img/icon/grade.png");
            }else{
                tree.setIcon("/img/icon/class.png");
            }
            treeList.add(tree);
        });
        return JSONArray.toJSONString(treeList);
    }


    /**
     * 保存
     * @param classes
     * @return
     */
    public Classes saveEntity(Classes classes){
        return classesRepository.save(classes);
    }

    /**
    * 删除菜单
    * @param ids
    */
    public void deleteClasses(String ids){
        Stream.of(ids.split(",")).forEach(id->{
            classesRepository.deleteById(Integer.parseInt(id));
        });
    }

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    public Classes getClassesById(Integer id){
        return classesRepository.getOne(id);
    }

    /**
     * 根据id获取编辑信息
     * @param id
     * @return
     */
    public String getEditInfo(Integer id){
        Map<String,Object> map = new HashMap<>();
        Classes classes = classesRepository.getOne(id);
        map.put("id",classes.getId());
        map.put("name",classes.getName());
        map.put("sort",classes.getSort());
        map.put("pid",classes.getPid());
        if(classes.getPid()!=0){
            map.put("parent",classesRepository.getOne(classes.getPid()).getName());
        }else{
            map.put("parent","");
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 判断重复
     * @param name
     * @param id
     * @return
     */
    public boolean verifyTheRepeat(String name,String id,String pid){
        Integer num = null;
        Integer pId = 0;
        if(StringUtils.isNotBlank(pid)){
            pId = Integer.parseInt(pid);
        }
        if(StringUtils.isNotBlank(id)){
            num = classesRepository.getClassesNumByIdAndName(Integer.parseInt(id), name,pId);
        }else{
            num = classesRepository.getClassesNumByName( name,pId);
        }
        return num > 0?false:true;
    }

    /**
    * 判断菜单是否被使用
    * @param ids
    * @return
    */
    public Boolean checkClassesUsed(String ids){
        for (String id : ids.split(",")) {
            Integer userNum = classesRepository.getUserRsNumByClassesId(Integer.parseInt(id));
            Integer studentNum = classesRepository.getStudentNumByClassesId(Integer.parseInt(id));
            if(userNum > 0){
                return false;
            }
            if(studentNum > 0){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param id
     * @return
     */
    public String getKnowledgeByClassId(Integer id){
        List<Knowledge> list = knowledgeRepository.findKnowledgeByClassesId(id);
        return JSONArray.toJSONString(list);
    }

    /**
     * 保存知识配置
     * @param classesId
     * @param knowledgeIds
     */
    public void saveKnowledgeSet(Integer classesId,String knowledgeIds){
        classesRepository.deleteKnowledgeSet(classesId);
        if(StringUtils.isNotBlank(knowledgeIds)){
            Stream.of(knowledgeIds.split(",")).forEach(id->{
                classesRepository.saveKnowledgeSet(classesId,Integer.parseInt(id));
            });
        }
    }

    /**
     * 学生管理班级树json
     * @return
     */
    public String getClassTreeJsonForStudent(){
        User user = userRepository.findUserByUsername(SecurityUtil.getCurUserName());
        List<Classes> list = classesRepository.getClassesByUserId(user.getId());
        List<Tree> trees = new ArrayList<Tree>();
        list.forEach(classes -> {
            Tree tree = new Tree(classes.getId().toString(),classes.getName(),classes.getPid().toString());
            if(classes.getPid()==0){//年级
                tree.setValue("0");
                tree.setIcon("/img/icon/grade.png");
            }else{
                tree.setValue("1");
                tree.setIcon("/img/icon/class.png");
            }
            trees.add(tree);
        });
        return JSONArray.toJSONString(trees);
    }

    /**
     * 获取所有年级班级信息
     * @return
     */
    public String getAllClassesInfo(){
        List<Map<String,Object>> dataList = new ArrayList<>();
        List<Classes> grades = classesRepository.getAllGrade();
        grades.forEach(grade -> {
            Map<String,Object> map = new HashMap<>();
            map.put("gradeName",grade.getName());
            List<Classes> classesList = classesRepository.findAllByPid(grade.getId());
            int studentsNum = 0;
            for (Classes classes:classesList ) {
                studentsNum += studentRepository.getStudentNumByClassesId(classes.getId());
            }
            map.put("studentsNum",studentsNum);
            map.put("classesNum",classesList.size());
            dataList.add(map);
        });
        return JSONArray.toJSONString(dataList);
    }

    /**
     * 获取各个年级老师信息
     * @return
     */
    public String getTeacherInfo(){
        List<Map<String,Object>> dataList = new ArrayList<>();
        List<Classes> grades = classesRepository.getAllGrade();
        String[] subjects = {"语文","数学","英语"};
        for(String subject:subjects){
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("name",subject);
            dataMap.put("type","bar");
            List<Integer> numList = new ArrayList<>();
            grades.forEach(grade->{
                numList.add(userRepository.getUserNumByClassPid(grade.getId(),subject));
            });
            dataMap.put("data",numList);
            dataList.add(dataMap);
        }
        return JSONArray.toJSONString(dataList);
    }

    /**
     * 各年级个科目学生数量情况
     */
    public String learningSituation(){
        Map<String,Object> resultMap = new HashMap<>();
        List<Map<String,Object>> dataList = new ArrayList<>();
        String[] subjects = {"语文","数学","英语"};
        List<Classes> grades = classesRepository.getAllGrade();
        List<Map<String,Object>> indicatorList = new ArrayList<>();
        grades.forEach(grade->{
            Map<String,Object> map = new HashMap<>();
            Integer num = studentRepository.getStudentNumByGradeId(grade.getId());
            map.put("name",grade.getName());
            map.put("max",num);
            indicatorList.add(map);
        });

        for (String subject:subjects){
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("name",subject);
            List<Integer> numList = new ArrayList<>();
            grades.forEach(grade->{
                numList.add(studentRepository.getStudentNumByClassesIdAndSubject(grade.getId(),subject));
            });
            dataMap.put("value",numList);
            dataList.add(dataMap);
        }
        resultMap.put("dataList",dataList);
        resultMap.put("indicatorList",indicatorList);
        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 班级知识点错误分布统计图
     * @return
     */
    public String wrongQuestionDistribution(Integer classId){
        Map<String,Object> dataMap = new HashMap<>();
        List<String> legendList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        List<Map<String,String>> wrongKnowledgeList = classesRepository.findWrongKnowledgeByClassId(classId);
        wrongKnowledgeList.forEach(map -> {
            String kName = map.get("name");
            String code = map.get("code");
            legendList.add(kName);
            List<Object> wrongKnowledgeDataList = new ArrayList<>();
            List<Student> studentList = studentRepository.findAllByClassesId(classId);
            studentList.forEach(student -> {
                //某个知识点的错题数
                Integer num = studentRepository.getWrongNumByStudentIdAndCode(student.getId(),code);
                if(num>0){
                    List<Object> studentData = new ArrayList<>();
                    //如果大于零则计入统计
                    //计算出错间隔时长
                    List<WrongQuestion> wrongQuestionList = wrongQuestionRepository.findAllByStudentIdAndKnowledgeCodeOrderByTimeDesc(student.getId(),code);
                    if(wrongQuestionList.size()==1){
                        studentData.add(0);
                    }else{
                        String firstDate = wrongQuestionList.get(wrongQuestionList.size()-1).getTime();
                        String lastDate = wrongQuestionList.get(0).getTime();
                        try {
                            Date last = PoiUtil.DATEFORMAT_DATE.parse(lastDate);
                            Date first =PoiUtil.DATEFORMAT_DATE.parse(firstDate);
                            long daysBetween=(last.getTime()-first.getTime()+1000000)/(60*60*24*1000);
                            studentData.add(daysBetween);
                        } catch (ParseException e) {
                            studentData.add(0);
                            e.printStackTrace();
                        }
                    }
                    studentData.add(num);
                    studentData.add(student.getName());
                    studentData.add(kName);
                    wrongKnowledgeDataList.add(studentData);
                }
            });
            dataList.add(wrongKnowledgeDataList);
        });
        dataMap.put("legend",legendList);
        dataMap.put("data",dataList);
        return JSONObject.toJSONString(dataMap);
    }

}