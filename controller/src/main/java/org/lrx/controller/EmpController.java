package org.lrx.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.lrx.entity.Employee;
import org.lrx.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Emp")
public class EmpController {

    @Autowired
    EmpService empService;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 根据姓名查询用户
     * @param lastName
     * @param map
     * @return
     */
    @GetMapping("/selectEmpByLastName")
    public String selectEmpByLastName(@RequestParam("lastName")String lastName,
                                      Map map){
        Employee employee = empService.selectEmpByLastName(lastName);
        map.put("employee",employee);
        return "select";
    }

    /**
     * 用户登录
     * @param lastName
     * @param password
     * @param vcode
     * @param request
     * @return
     */
    @PostMapping("/indexSelect")
    @ResponseBody
    public Map<String,Object> index(@RequestParam("lastName") String lastName,
                                    @RequestParam("password") String password,
                                    @RequestParam("vcode") String vcode,
                                    HttpServletRequest request){
        Map<String,Object> map1 = new HashMap<>();
        //判断缓存是否失效
        if(!redisTemplate.hasKey("loginCpacha")){
            map1.put("type","fail");
            map1.put("msg","验证码失效");
            return map1;
        }
        //从缓存中拿
        String loginCpacha = (String)redisTemplate.opsForValue().get("loginCpacha");
        //不区分大小写
        if (!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
            map1.put("type","fail");
            map1.put("msg","验证码错误");
            return map1;
        }
        Map<String, Object> map = empService.indexSelect(lastName, password);
        if (map.get("type") == "success"){
            Employee employee = empService.selectEmpByLastName(lastName);
            request.getSession().setAttribute("emp",employee);
        }
        return  map;
    }

    /**
     * 用户注册
     * @param employee
     * @return
     */
    @PostMapping("/insertEmp")
    @ResponseBody
    public Map<String,Object> insertEmp(Employee employee){
        Map<String, Object> map = empService.insertEmp(employee);
        return map;
    }

    /**
     * 根据id查询用户，修改用
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/selectByEmpId/{id}")
    public String selectByEmpId(@PathVariable("id") Integer id,
                                Map map){
        Employee employee = empService.selectEmpById(id);
        map.put("employee",employee);
        return "update";
    }

    /**
     * 用户的修改
     * @param employee
     * @return
     */
    @PostMapping("/updateEmp")
    @ResponseBody
    public Map<String,Object> updateEmp(Employee employee){

        Map<String, Object> map = empService.updateEmp(employee);
        return map;
    }

    /**
     * 查询所有用户分页查询
     * @param page
     * @param rows
     * @param map
     * @return
     */
    @GetMapping("/selectAllEmp/{page}/{rows}")
    public String selectAllEmp(@PathVariable("page") Integer page, @PathVariable("rows") Integer rows,Map map){
        Page<Employee> pages = PageHelper.startPage(page,rows).doSelectPage(()->empService.selectAllEmp());
        List<Employee> result = pages.getResult();
        map.put("employees",result);
        map.put("page", pages.getPageNum());  //当前页
        map.put("pages",pages.getPages());    //总页数
        map.put("total",pages.getTotal());     //总数据量
        return "selectAllEmp";

    }

    /**
     * 生成验证码 调用验证码函数CpachaUtil
     * @param vl
     * @param w
     * @param h
     * @param response
     */
    @RequestMapping(value = "/getCpacha",method=RequestMethod.GET)
    public void getCpacha(@RequestParam(value = "vl",defaultValue = "4",required = false) Integer vl,
                          @RequestParam(value = "w",defaultValue = "90",required = false) Integer w,
                          @RequestParam(value = "h",defaultValue = "33",required = false) Integer h,
                          HttpServletResponse response) {

        empService.getCpacha(vl,w,h,response);
    }
}
