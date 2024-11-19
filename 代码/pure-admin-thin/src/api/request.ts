import axios from "axios";
// import { useUserStore } from "@/stores";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

// const baseURL = "http://localhost:9999";
const baseURL = "http://localhost:9005/api_demo";

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000
});

//请求拦截器
instance.interceptors.request.use(
  config => {
    // TODO 2. 携带token
    // const useStore = useUserStore();
    // if (useStore.token) {
    //   // let user = JSON.parse(localStorage.getItem('big-user'))
    //   // console.log(user.token)
    //   config.headers.Authorization = useStore.token;
    // }
    return config;
  },
  err => Promise.reject(err)
);

//响应拦截器
instance.interceptors.response.use(
  res => {
    console.log(res);
    // TODO 4. 摘取核心响应数据
    if (res.data.code === 0) {
      return res;
    }

    //处理失败逻辑
    ElMessage.error(res.data.message);
    return Promise.reject(res.data);
  },
  err => {
    //错误的特殊情况  // TODO 5. 处理401错误
    console.log(err);
    if (err.response?.status === 401) {
      router.push("/login");
    }
    console.log(err);

    //错误的默认情况
    ElMessage.error(err.response);
    return Promise.reject(err);
  }
);

export default instance;
export { baseURL };
