package com.pay.admin.schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pay.admin.service.BuildService;

@Component
public class TaskService {
    @Autowired
    BuildService buildService;
    public void buildApk() {
        buildService.build();
    }
}
