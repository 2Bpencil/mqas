package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.KnowledgeRepository;
import com.tyf.mqas.code.dao.TestPaperRepository;
import com.tyf.mqas.code.dao.TestPaperTypeRepository;
import com.tyf.mqas.code.dao.TestQuestionRepository;
import com.tyf.mqas.code.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TestPaperService extends PageGetter<TestQuestion> {

    @Autowired
    private TestPaperRepository testPaperRepository;
    @Autowired
    private TestPaperTypeRepository testPaperTypeRepository;
    @Autowired
    private TestQuestionRepository testQuestionRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;


    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<TestQuestion> getDataPage(Map<String,String[]> parameterMap,String testPaperTypeId){

        String sql = "SELECT test_question.* FROM test_question   WHERE test_question.test_paper_type_id = "+testPaperTypeId;
        String countSql = "SELECT COUNT(*) FROM test_question   WHERE test_question.test_paper_type_id = "+testPaperTypeId;

        return super.getPages(parameterMap,sql,countSql);
    }


    /**
     * 判断重复
     * @param name
     * @param id
     * @return
     */
    public boolean verifyTheRepeat(String name,String id){
        Integer num = null;
        if(StringUtils.isNotBlank(id)){
            num = testPaperRepository.getTestPaperByIdAndName(Integer.parseInt(id), name);
        }else{
            num = testPaperRepository.getTestPaperByName( name);
        }
        return num > 0?false:true;
    }

    /**
     * 保存试卷
     * @param testPaper
     * @return
     */
    public TestPaper saveOrEditTestPaper(TestPaper testPaper){
        return testPaperRepository.save(testPaper);
    }

    public TestPaperType saveOrEditTestPaperType(TestPaperType testPaperType){
        return testPaperTypeRepository.save(testPaperType);
    }

    public TestQuestion saveOrEditTestQuestion(TestQuestion testQuestion){
        Knowledge knowledge = knowledgeRepository.findByCode(testQuestion.getKnowledgeCode());
        testQuestion.setKnowledgeName(knowledge.getName());
        return testQuestionRepository.save(testQuestion);
    }

    /**
     *
     * @return
     */
    public String getTestPaperTreeJson(){
        List<TestPaper> paperList = testPaperRepository.findAll();
        List<Tree> treeList = new ArrayList<>();
        paperList.forEach(testPaper -> {
            Tree tree = new Tree();
            tree.setName(testPaper.getName());
            tree.setId("testPaper"+testPaper.getId());
            tree.setValue("0");
            tree.setpId("");
            treeList.add(tree);
            List<TestPaperType> testPaperTypeList = testPaperTypeRepository.findAllByTestPaperId(testPaper.getId());
            testPaperTypeList.forEach(testPaperType -> {
                Tree tree2 = new Tree();
                tree2.setName(testPaperType.getName());
                tree2.setId(testPaperType.getId().toString());
                tree2.setValue("1");
                tree2.setpId("testPaper"+testPaper.getId());
                treeList.add(tree2);
            });
        });
        return JSONArray.toJSONString(treeList);
    }

    public TestPaper getTestPaper(Integer id){
        return testPaperRepository.getOne(id);
    }

    public TestPaperType getTestPaperType(Integer id){
        return testPaperTypeRepository.getOne(id);
    }

    public TestQuestion getTestQuestion(Integer id){
        return testQuestionRepository.getOne(id);
    }

    public void deleteTestQuestion(Integer id){
        testQuestionRepository.deleteById(id);
    }

    public void deleteTestPaperType(Integer id){
        testQuestionRepository.deleteByTestPaperTypeId(id);
        testPaperTypeRepository.deleteById(id);
    }
    public void deleteTestPaper(Integer id){
        List<TestPaperType> typeList = testPaperTypeRepository.findAllByTestPaperId(id);
        typeList.forEach(testPaperType -> {
            testQuestionRepository.deleteByTestPaperTypeId(testPaperType.getId());
            testPaperTypeRepository.deleteById(testPaperType.getId());
        });
        testPaperRepository.deleteById(id);
    }

}
