package org.lrx.controller;

import org.junit.jupiter.api.Test;
import org.lrx.entity.Employee;
import org.lrx.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ControllerApplicationTests {

    @Autowired
    EmpService empService;


    @Test
    void contextLoads() {
        Employee employee = empService.selectEmpById(1);
        System.out.println(employee);
    }

}
