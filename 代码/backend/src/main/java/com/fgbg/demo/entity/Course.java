package com.fgbg.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {

    private Long id;
    //课程名
    private String courseName;
    //图片
    private String img;
    //课程老师
    private String courseTeacher;
    //授课时间
    private Date courseTime;
    //授课地点
    private String coursePlace;
    //课程类型
    private String courseType;




}
