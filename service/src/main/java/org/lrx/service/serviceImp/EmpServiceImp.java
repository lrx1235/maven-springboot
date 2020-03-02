package org.lrx.service.serviceImp;


import org.lrx.daoMapper.EmpMapper;
import org.lrx.entity.Employee;
import org.lrx.service.EmpService;
import org.lrx.until.CpachaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EmpServiceImp implements EmpService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    EmpMapper empMapper;

    /**
     * 根据id查询用户，修改用
     * @param id
     * @return
     */
    @Override
    //测试缓存，知道怎么用，此处不需要，简单的增删改查需要，比如对管理员的增删改查，用户的太过去复杂，不需要
    //@Cacheable(value = "emp",key = "#id")
    public Employee selectEmpById(Integer id) {
        System.out.println("进来了");
        Employee employee = empMapper.selectEmpById(id);
        return employee;
    }

    /**
     * 登录界面查询用户
     * @param lastNmae
     * @param password
     * @return
     */
    @Override
    public Map<String,Object> indexSelect(String lastNmae, String password) {

        Map<String,Object> map = new HashMap<>();
        Employee employee = empMapper.indexSelect(lastNmae, password);
        if(employee == null){
            map.put("type","fail");
            map.put("msg","用户不存在或密码错误");
            return map;
        }else {
            map.put("type","success");
            map.put("msg","欢迎登录");
            return map;
        }
    }

    /**
     * 根据姓名查询用户
     * @param lastName
     * @return
     */
    @Override
    public Employee selectEmpByLastName(String lastName) {
        Employee employee = empMapper.selectEmpByLastName(lastName);
        return employee;
    }

    /**
     * 用户注册
     * @param employee
     * @return
     */
    @Override
    public Map<String,Object> insertEmp(Employee employee) {
        Map<String,Object> map = new HashMap<>();
        Employee employee1 = empMapper.selectEmpByLastName(employee.getLastName());
        if(employee1 != null){
            map.put("type","fail");
            map.put("msg","该用户已存在");
            return map;
        }
        int i = empMapper.insertEmp(employee);
        if(i>0){
            map.put("type","success");
            map.put("msg","注册成功");
            return map;
        }else {
            map.put("type","fail");
            map.put("msg","注册失败");
            return map;
        }
    }

    /**
     * 用户的修改
     * @param employee
     * @return
     */
    @Override
    public Map<String, Object> updateEmp(Employee employee) {
        Map<String,Object> map = new HashMap<>();
        int i = empMapper.updateEmp(employee);
        if(i>0){
            map.put("type","success");
            map.put("msg","修改成功");
            return map;
        }else {
            map.put("type","fail");
            map.put("msg","修改失败");
            return map;
        }
    }

    /**
     * 查询所有用户
     * @return
     */
    @Override
    public List<Employee> selectAllEmp() {

        List<Employee> employees = empMapper.selectAllEmp();

        return employees;

    }

    /**
     * 验证码的生成
     * @param vl
     * @param w
     * @param h
     * @param response
     */
    @Override
    public void getCpacha(Integer vl, Integer w, Integer h, HttpServletResponse response) {
        //设置验证码的大小
        CpachaUtil cpachaUtil = new CpachaUtil(vl,w,h);
        //获取生成的验证码
        String generatorVCode = cpachaUtil.generatorVCode();
        //将生成的验证码保存在redis中，在登录页面要进行判断
        redisTemplate.opsForValue().set("loginCpacha",generatorVCode);
        redisTemplate.expire("loginCpacha",30,TimeUnit.SECONDS);
        //生成验证码的图片形式
        BufferedImage generatorRotateVCode = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
        try {
            ImageIO.write(generatorRotateVCode, "gif", response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
