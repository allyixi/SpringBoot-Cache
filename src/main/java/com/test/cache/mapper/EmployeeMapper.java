package com.test.cache.mapper;

import com.test.cache.bean.Employee;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT * FROM employee where id =#{id}")
    public Employee getEmpById(Integer id);

    @Update("update employee set lastName=#{lastName},email=#{email},gender=#{gender},dId=#{dId} where id=#{id}")
    public void updateEmp(Employee emp);

    @Delete("delete from employee where id=#{id}")
    public void deleteEmp(Integer id);

    @Insert("insert into employee(lastName,email,gender,dId) values(#{lastName},#{email},#{gender},#{dId})")
    public void insertEmp(Employee emp);

    @Select("SELECT * FROM employee where lastName =#{lastname}")
    Employee getEmpByLastName(String lastname);
}
