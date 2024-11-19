import request from "./request";

//分页查询
export const PageCourseInfo = params => {
  return request.get("/course/page", { params });
};

//根据条件查询
export const SelectByCourseName = couserName => {
  return request.post("/course/list", couserName);
};

//新增课程
export const addCourseInfo = data => {
  return request.post("/course/save", data);
};

//上传图片
export const uploadImage = file => {
  return request.post("/upload/image", file);
};
