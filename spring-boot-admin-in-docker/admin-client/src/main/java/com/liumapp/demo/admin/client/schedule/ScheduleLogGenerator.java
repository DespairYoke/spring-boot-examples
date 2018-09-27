package com.liumapp.demo.admin.client.schedule;

import com.liumapp.qtools.date.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class ScheduleLogGenerator {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleLogGenerator.class);

    @Scheduled(fixedRate = 3000)
    public void reportLogTime () {
        LOGGER.info(DateTool.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

}
