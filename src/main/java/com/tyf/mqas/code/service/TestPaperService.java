package com.tyf.mqas.code.service;

import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.TestPaperRepository;
import com.tyf.mqas.code.dao.TestPaperTypeRepository;
import com.tyf.mqas.code.dao.TestQuestionRepository;
import com.tyf.mqas.code.entity.TestQuestion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<TestQuestion> getDataPage(Map<String,String[]> parameterMap){
        return super.getPages(parameterMap);
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

}
