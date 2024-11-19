const { VITE_HIDE_HOME } = import.meta.env;
const Layout = () => import("@/layout/index.vue");

export default {
  path: "/",
  name: "Home",
  component: Layout,
  redirect: "/welcome",
  meta: {
    icon: "homeFilled",
    title: "首页",
    rank: 0
  },
  children: [
    {
      path: "/welcome",
      name: "Welcome",
      component: () => import("@/views/welcome/index.vue"),
      meta: {
        title: "首页",
        showLink: VITE_HIDE_HOME === "true" ? false : true
      }
    },
    {
      path: "/fullText",
      name: "fullText",
      component: () => import("@/views/welcome/full.vue"),
      meta: {
        title: "富文本",
        showLink: VITE_HIDE_HOME === "true" ? false : true
      }
    },
    {
      path: "/webrtc",
      name: "webrtc",
      component: () => import("@/views/welcome/webrtc.vue"),
      meta: {
        title: "webrtc",
        showLink: VITE_HIDE_HOME === "true" ? false : true
      }
    },
    {
      path: "/course",
      name: "webrtc",
      component: () => import("@/views/welcome/CoursePage.vue"),
      meta: {
        title: "course",
        showLink: VITE_HIDE_HOME === "true" ? false : true
      }
    }

  ]
} as RouteConfigsTable;
