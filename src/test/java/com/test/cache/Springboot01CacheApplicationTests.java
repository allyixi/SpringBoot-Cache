package com.test.cache;

import com.test.cache.bean.Department;
import com.test.cache.bean.Employee;
import com.test.cache.mapper.DepartmentMapper;
import com.test.cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Springboot01CacheApplication.class)
class Springboot01CacheApplicationTests {
    @Autowired
    EmployeeMapper employeeMapper;

    //操作字符串的
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //key-value 保存健值对象的
    @Autowired
    RedisTemplate redisTemplate;

//    @Autowired
//    RedisTemplate<Object, Employee> redisTemplate2;


    //Redis常见的五大数据类型：String(字符串)、List(列表)、Set(集合)、Hash(散列)、ZSet(有序集合)
    @Test
    public void test01(){
//        stringRedisTemplate.opsForValue().append("msg","hello");
//        System.out.println(stringRedisTemplate.opsForValue().get("msg"));
        Employee emp=employeeMapper.getEmpById(4);
        //默认如果保存对象，使用jdk序列化机制，序列化的数据保存到redis中，可视化差
        //解决方法：
        //1、将数据以json的方式保存：a.自己将对象转成json b.redisTemplate默认的序列化规则
        redisTemplate.opsForValue().set("emp01",emp);
        Employee emp2=(Employee) redisTemplate.opsForValue().get("emp01");
        System.out.println(emp2.toString());

    }


    @Test
    void contextLoads() {
        String lock="lock tests";
        stringRedisTemplate.opsForValue().set("giao::test",lock);
    }

}
