package com.test.cache.service;

import com.test.cache.bean.Employee;
import com.test.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

//统一指定缓存组件的名称
@CacheConfig(cacheNames = "emp")
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的运行结果进行缓存，以后再要相同的数据，直接从缓存中获取，不用调用方法
     *
     * CacheManager管理多个Cache组件，对缓存进行增删改查的操作都在Cache组件中，每一个缓存组件有自己唯一一个名字
     * 几个属性：
     * cacheNames/value：指定缓存组件的名字,可以指定多个缓存
     * key：缓存数据使用的key，默认是使用方法参数的值，可以使用SpEL表达式
     * keyGenerator：key的生成器，可以自己指定key的生成器
     * key/keyGenerator：二选一
     * cacheManager：指定缓存管理器
     * cacheResolver：指定缓存解析器
     * cacheManager/cacheResolver：二选一
     * condition：指定符合条件下才进行缓存
     * unless：否定缓存，当unless指定的条件为true，方法不进行缓存 例如unless="#result==null"
     * sync：是否使用异步模式 如果使用sync则不支持unless
     *
     * 可用SpEL:
     * 名字             位置            示例                     描述
     * methodName      root object     #root.methodName        被调用方法的名字
     * method          root object     #root.method.name       被调用的方法的名字
     * target          root object     #root.target            被调用的目标对象
     * targetClass     root object     #root.targetClass       被调用的目标对象类
     * args            root object     #root.args[0]           被调用的参数列表
     * caches          root object     #root.caches[0].name    缓存组件的名字
     * argumentname    context         #iban、#a0、#p0         #id=#p0=#a0=#root.args[0]
     * result          context         #result                 执行方法的返回值
     **/

    //运行前先查key，再进行缓存策略是否执行方法
    @Cacheable(cacheNames = "emp", keyGenerator = "myKeyGenerator", unless = "#id==2")
    public Employee getEmp(Integer id) {
        System.out.println("查询" + id);
        return employeeMapper.getEmpById(id);
    }

    //先执行方法，再进行缓存
    @CachePut(cacheNames = "emp", key = "#result.id")
    public Employee updateEmp(Employee employee) {
        System.out.println("更新" + employee.getId());
        employeeMapper.updateEmp(employee);
        return employee;
    }

    //allEntries=true 删除缓存组件cacheName中的所有缓存
    //beforeInvocation=false：缓存的清楚是否再方法之前执行，默认为false，在方法执行完后执行缓存清楚
    @CacheEvict(cacheNames = "emp", allEntries = true)
    public void deleteEmp(Integer id) {
        System.out.println("删除" + id);
        employeeMapper.deleteEmp(id);
    }

    //添加多个缓存规则
    @Caching(
            cacheable = {
                    @Cacheable(value = "emp", key = "#lastname")
            },
            put = {
                    @CachePut(value = "emp", key = "#result.id"),
                    @CachePut(value = "emp", key = "#result.email")
            }
    )
    public Employee getEmpByLastName(String lastname) {
        return employeeMapper.getEmpByLastName(lastname);
    }
}
