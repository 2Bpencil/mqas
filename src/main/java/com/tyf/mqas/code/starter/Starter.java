package com.tyf.mqas.code.starter;

import com.tyf.mqas.config.ConfigData;
import com.tyf.mqas.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description: 启动执行服务
 * @Author: tyf
 * @Date: 2019/10/11 09:21
 */
@Component
@Order(value = 1)
public class Starter implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(Starter.class);
    @Autowired
    private ConfigData configData;

    @Override
    public void run(ApplicationArguments args) {
        FileUtil.creatFoler(configData.getTestPaperDir());
        FileUtil.creatFoler(configData.getWrongQuestionDir());
        FileUtil.creatFoler(configData.getWrongQuestionDir()+"/temp");
        logger.info("------完成初始化存储文件夹");
    }

}
    