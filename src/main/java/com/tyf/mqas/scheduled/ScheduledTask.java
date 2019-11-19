package com.tyf.mqas.scheduled;

import com.tyf.mqas.code.dao.StudentRecordsRepository;
import com.tyf.mqas.code.dao.StudentRepository;
import com.tyf.mqas.code.entity.StudentRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
* @Description:  定时任务
* @Author: Mr.Tan
* @Date: 2019/10/8 9:16
*/
@EnableScheduling
@Component
public class ScheduledTask {
    private final static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentRecordsRepository studentRecordsRepository;


    /**
     * 每天保存当月的学生总数  每天下午六点
     */
    @Scheduled(cron="0 0 18 * * ?")
    public void saveStudentRecords(){
        LocalDate localDate = LocalDate.now();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        if(studentRecordsRepository.getAllNumByYear(year)==0){
            List<StudentRecords> list = new ArrayList<>();
            for (int i = 0; i <12 ; i++) {
                StudentRecords studentRecords = new StudentRecords();
                studentRecords.setMonth(i+1);
                studentRecords.setYear(year);
                studentRecords.setTotal(0);
                list.add(studentRecords);
            }
            studentRecordsRepository.saveAll(list);
        }
        StudentRecords studentRecords = studentRecordsRepository.findByYearAndMonth(year,month);
        Integer total =studentRepository.getAllStudentNum();
        studentRecords.setTotal(total);
        studentRecordsRepository.save(studentRecords);
        logger.info("定时任务---------》》保存学生总数");
    }

}
