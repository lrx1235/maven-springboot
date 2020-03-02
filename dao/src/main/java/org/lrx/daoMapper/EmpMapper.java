package org.lrx.daoMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lrx.entity.Employee;

import java.util.List;

@Mapper
public interface EmpMapper {

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Employee selectEmpById(Integer id);

    /**
     * 根据账号密码查询用户
     * @param lastNmae
     * @param password
     * @return
     */
    Employee indexSelect(@Param("lastName")String lastNmae, @Param("password")String password);

    /**
     * 根据用户名查询
     * @param lastName
     * @return
     */
    Employee selectEmpByLastName(String lastName);

    /**
     * 用户注册
     * @param employee
     * @return
     */
    int insertEmp(Employee employee);

    /**
     * 用户更新
     * @param employee
     * @return
     */
    int updateEmp(Employee employee);

    /**
     * 查询全部用户
     * @return
     */
    List<Employee> selectAllEmp();
}
