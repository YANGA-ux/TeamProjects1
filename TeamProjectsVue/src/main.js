import Vue from "vue";
import App from "./App.vue";
import './assets/font/iconfont.css';
import router from "./router";

Vue.config.productionTip = false; // 关闭生产环境提示
Vue.config.devtools = false;      // 关闭 Vue Devtools 检测

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
