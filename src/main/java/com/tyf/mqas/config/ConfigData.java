package com.tyf.mqas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description: 获取配置文件参数
 * @Author: tyf
 * @Date: 2019/10/10 10:35
 */
@Component
@PropertySource(value = { "classpath:config.properties" },ignoreResourceNotFound = true)
public class ConfigData {
    @Value("${testPaperDir}")
    private String testPaperDir;
    @Value("${wrongQuestionDir}")
    private String wrongQuestionDir;
    @Value("${tessdataPath}")
    private String tessdataPath;

    public String getTestPaperDir() {
        return testPaperDir;
    }

    public void setTestPaperDir(String testPaperDir) {
        this.testPaperDir = testPaperDir;
    }

    public String getWrongQuestionDir() {
        return wrongQuestionDir;
    }

    public void setWrongQuestionDir(String wrongQuestionDir) {
        this.wrongQuestionDir = wrongQuestionDir;
    }

    public String getTessdataPath() {
        return tessdataPath;
    }

    public void setTessdataPath(String tessdataPath) {
        this.tessdataPath = tessdataPath;
    }
}
    