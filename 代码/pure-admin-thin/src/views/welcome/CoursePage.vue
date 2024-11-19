<template>
  <!-- 添加弹出框 -->
  <el-dialog title="课程信息" v-model="addVisible" width="35%">
    <el-form label-width="100px" :model="addForm">
      <el-form-item label="课程id" prop="id">
        <el-input v-model="addForm.id" />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="addForm.courseName" />
      </el-form-item>
      <el-form-item label="图片">
        <!-- add!!! (暂时把原来的el-upload上传组件替代)-->
        <Child v-model:fileList="fileList" />
        <el-button @click="submit">提交图片</el-button>
      </el-form-item>

      <el-form-item label="授课老师" prop="courseTeacher">
        <el-input v-model="addForm.courseTeacher" />
      </el-form-item>
      <el-form-item label="授课时间" prop="courseTime">
        <el-input v-model="addForm.courseTime" />
      </el-form-item>
      <el-form-item label="授课地点" prop="coursePlace">
        <el-input v-model="addForm.coursePlace" />
      </el-form-item>
      <el-form-item label="课程分类" prop="courseType" clearable>
        <el-input v-model="addForm.courseType" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addcancel">取 消</el-button>
        <el-button type="primary" @click="add(addForm)">确 定</el-button>
      </span>
    </template>
  </el-dialog>

  <el-row :gutter="20">
    <el-col :span="8">
      <el-input v-model="queryInfo.courseName" placeholder="请输入课程名">
        <template #append>
          <el-button @click="SelectByName()" :icon="Search" />
        </template>
      </el-input>
    </el-col>
    <el-button type="danger" :icon="User" @click="OnAddCourse()"
      >新增课程</el-button
    >
  </el-row>

  <el-table :data="CourseList" style="width: 100%">
    <el-table-column prop="id" label="课程id" width="165" />
    <el-table-column prop="courseName" label="课程名" width="165" />
    <el-table-column prop="img" label="图片" width="165">
      <!-- eslint-disable-next-line -->
      <template v-slot="scope">
        <el-image style="width: 50px; height: 50px" />
      </template>
    </el-table-column>
    <el-table-column prop="courseTeacher" label="授课老师" width="165" />
    <el-table-column prop="courseTime" label="授课时间" width="165" />
    <el-table-column prop="coursePlace" label="授课地点" width="165" />
    <el-table-column prop="courseType" label="课程分类" width="165" />
    <el-table-column prop="cz" label="操作" width="165">
      <!-- <template #default="scope">
        <el-button type="primary" circle plain :icon="Edit" />
        <el-button type="danger" plain circle :icon="Delete" />
      </template> -->
    </el-table-column>
  </el-table>

  <div class="block">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="params.page"
      :page-sizes="[2, 5, 10, 20]"
      :page-size="params.pageSize"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
// add
import Child from "./child.vue";
import { ref } from "vue";
import {
  PageCourseInfo,
  SelectByCourseName,
  addCourseInfo
} from "@/api/course.ts";
import { Search, Plus, User } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
// add!!!
import axios from "axios";

// import { getAssetsFile } from "../../utils/utils";
// add!!!
const fileList = ref();

// add!!!
const submit = () => {
  console.log(fileList.value);
  // 封装formData
  const data = new FormData();
  // forEach遍历的时fileList.value, 所有element不需要.value去除代理
  fileList.value.forEach(element => {
    data.append("imageList", element.raw);
  });

  axios.post("http://localhost:9999/upload/image2", data, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};

const CourseList = ref([]); //课程列表
const total = ref(0); //课程总条数

const params = ref({
  page: 1, //当前页
  pageSize: 10 //最大页码
});

//分页查询
const getCourseList = async () => {
  const res = await PageCourseInfo(params.value);
  console.log(res);
  CourseList.value = res.data.data.records;

  console.log(res.data.data.records);
  total.value = parseInt(res.data.data.total);
};

//分页逻辑
const handleSizeChange = size => {
  params.value.page = 1;
  params.value.pageSize = size;
  getCourseList();
};

const handleCurrentChange = page => {
  params.value.page = page;
  getCourseList();
};

getCourseList();

//条件查询
const queryInfo = ref({
  courseName: ""
});

const SelectByName = async () => {
  console.log(queryInfo.value);

  const ref = await SelectByCourseName(queryInfo.value);

  CourseList.value = ref.data.data;
};

const addVisible = ref(false);

//新增课程
const OnAddCourse = () => {
  addVisible.value = true;
};

const addForm = ref({
  id: "",
  courseName: "",
  img: "",
  courseTeacher: "",
  courseTime: "",
  coursePlace: "",
  courseType: ""
});

const addcancel = () => {
  addVisible.value = false;
};

const add = addForm => {
  addCourseInfo(addForm);
  addVisible.value = false;
  addForm.courseName = "";
  addForm.img = "";
  addForm.courseTeacher = "";
  addForm.courseTime = "";
  addForm.coursePlace = "";
  addForm.courseType = "";
  setTimeout(getCourseList, 50); //延迟执行，刷新页面

  ElMessage.success(`新增成功！`);
};

// const onSelectImg = (file) => {
//   console.log(file)
//   img.value = URL.createObjectURL(file.raw)
//   addForm.img = file.raw
//   uploadImage(file)
//   console.log(addForm.img)
// }

// const handleAvatarSuccess = (res) => {
//   addForm.img = res
// }

const img = ref();

const handleAvatarSuccess = (res, file) => {
  console.log(res);
  img.value = URL.createObjectURL(file.raw);
  addForm.value.img = res.data;
  console.log(img.value);
  console.log(addVisible.value);
  console.log(res);
};

// 图片上传成功的操作
// const handleAvatarSuccess = (res, file) => {
//   console.log(res.data)

//   addForm.value.img = URL.createObjectURL(file.raw)
// }

const beforeAvatarUpload = file => {
  const isJPG = file.type === "image/jpeg";
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG) {
    ElMessage.error("上传头像图片只能是 JPG 格式!");
  }
  if (!isLt2M) {
    ElMessage.error("上传头像图片大小不能超过 2MB!");
  }
  return isJPG && isLt2M;
};
</script>
<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
