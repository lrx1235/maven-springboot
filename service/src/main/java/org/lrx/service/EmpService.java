package org.lrx.service;

import org.lrx.entity.Employee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface EmpService {

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Employee selectEmpById(Integer id);

    /**
     * 根据用户名和密码查询用户
     * @param lastNmae
     * @param password
     * @return
     */
    Map<String,Object> indexSelect(String lastNmae, String password);

    /**
     * 根据用户名查询用户
     * @param lastName
     * @return
     */
    Employee selectEmpByLastName(String lastName);

    /**
     * 注册用户
     * @param employee
     * @return
     */
    Map<String,Object> insertEmp(Employee employee);

    /**
     * 用户信息跟新
     * @param employee
     * @return
     */
    Map<String,Object> updateEmp(Employee employee);

    /**
     * 分页查询所有用户
     * @return
     */
    List<Employee> selectAllEmp();

    void getCpacha(Integer vl,
                   Integer w,
                   Integer h,
                   HttpServletResponse response);
}
