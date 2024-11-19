<template>
  <el-row>
    <el-upload
      v-model:file-list="fileList"
      class="upload-demo"
      multiple
      :auto-upload="false"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      span="10"
      style="margin-right: 20px"
    >
      <el-button type="primary">选择上传的文件</el-button>
    </el-upload>
    <el-button type="success" @click="submit">上传文件</el-button>
  </el-row>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { uploadBatch } from "/src/api/file.ts";

import type { UploadProps, UploadUserFile } from "element-plus";

const fileList = ref<UploadUserFile[]>([]);

const handleRemove: UploadProps["onRemove"] = (file, uploadFiles) => {
  console.log(file, uploadFiles);
};

const handlePreview: UploadProps["onPreview"] = uploadFile => {
  console.log(uploadFile);
};

const submit = () => {
  console.log(fileList.value);
  // 封装formData
  const data = new FormData();
  fileList.value.forEach(element => {
    data.append("files", element.raw);
  });
  uploadBatch(data).then(res => {
    console.log(res);
    if (res.code === 0) {
      ElMessage.success("上传成功");
    } else {
      ElMessage.error("上传失败: " + res.msg);
    }
  });
};
</script>
