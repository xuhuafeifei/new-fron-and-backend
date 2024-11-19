package com.fgbg.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgbg.common.utils.R;
import com.fgbg.demo.entity.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/course")
@Slf4j
@CrossOrigin
public class CourseControllerNew {

    //分页查询课程
    @GetMapping("/page")
//    public RestBean<Page> page(@RequestParam int page, @RequestParam int pageSize){
    public R page(@RequestParam int page, @RequestParam int pageSize){
        log.info("page = {},pageSize = {}",page,pageSize);

        //1.构造分页构造器
        Page page1 = new Page(page,pageSize);


        //2.添加排序条件
//        LambdaQueryWrapper<Course> queryWrapper=new LambdaQueryWrapper<>();

//        queryWrapper.orderByAsc(Course::getId);


//        return RestBean.success(page1);
        return R.ok().put("data", page1);
    }


    //根据条件查课程
    @PostMapping ("/list")
//    public RestBean<List<Course>> findByNameOrId(@RequestBody Course course){
    public R findByNameOrId(@RequestBody Course course){
        log.info("course:" +course);
//        List<Course> list = courseService.findByNameOrId(course);
        List<Course> list = new ArrayList<>();
//        return RestBean.success(list);
        return R.ok().put("data", list);
    }



    //添加课程   只有管理员可以添加
    @PostMapping(value = "/save")
    public Result<String> saveCourse(@RequestBody Course course){
        return Result.success("添加课程成功");
    }


    //删除课程
    @DeleteMapping("/delete")
    public Result<String> deleteCourse(Long [] ids){
        log.info("id: " +ids);
        List<Long> idslist = Arrays.asList(ids);
        return Result.success("删除成功！");
    }

    //修改课程
    @PutMapping("/update")
    public Result<String> updateCourse(@RequestBody Course course){
        return Result.success("更新成功！");
    }


    //回显修改数据根据id
    @GetMapping("ById")
    public Result<Course> findById(Long id){
        return Result.success(null);
    }

}
