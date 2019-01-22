package sample.xml.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zwd
 * @date 2019/1/10 15:25
 * @Email stephen.zwd@gmail.com
 */

public class StandardEventListener implements ApplicationListener{
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("standardEventListener");
    }
}
