<template>
  <el-container class="admin-container">
    <!-- 头部 -->
    <el-header class="admin-header">
      <div class="header-left" @click="menuClick('adminLayout')">
        <img src="@/assets/hospital.png" alt="医院logo" class="logo">
        <h1 class="system-title">公共服务平台医生服务系统</h1>
      </div>
      <div class="header-right">
        <div class="user-info">
          <span class="welcome-text">欢迎您，</span>
          <span class="user-name">{{ userName }}</span>
          <span class="user-role">管理员</span>
        </div>
        <el-button
          type="danger"
          size="small"
          @click="logout"
          class="logout-btn"
          plain
        >
          <i class="el-icon-switch-button"></i> 退出
        </el-button>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="220px" class="admin-sidebar">
        <!-- 导航菜单 -->
        <el-menu
          class="admin-menu"
          background-color="#fff"
          text-color="#666"
          active-text-color="#409EFF"
          :default-active="activePath"
        >
          <!-- 首页 -->
          <el-menu-item
            index="adminLayout"
            @click="menuClick('adminLayout')"
            class="menu-item"
          >
            <i class="el-icon-s-home menu-icon"></i>
            <span>首页</span>
          </el-menu-item>

          <!-- 医生信息 -->
          <el-menu-item
            index="doctorList"
            @click="menuClick('doctorList')"
            class="menu-item"
          >
            <i class="el-icon-user menu-icon"></i>
            <span>医生信息</span>
          </el-menu-item>

          <!-- 患者信息 -->
          <el-menu-item
            index="patientList"
            @click="menuClick('patientList')"
            class="menu-item"
          >
            <i class="el-icon-user-solid menu-icon"></i>
            <span>患者信息</span>
          </el-menu-item>

          <!-- 挂号信息 -->
          <el-menu-item
            index="orderList"
            @click="menuClick('orderList')"
            class="menu-item"
          >
            <i class="el-icon-tickets menu-icon"></i>
            <span>挂号信息</span>
          </el-menu-item>

          <!-- 药物信息 -->
          <el-menu-item
            index="drugList"
            @click="menuClick('drugList')"
            class="menu-item"
          >
            <i class="el-icon-first-aid-kit menu-icon"></i>
            <span>药物信息</span>
          </el-menu-item>

          <!-- 检查项目 -->
          <el-menu-item
            index="checkList"
            @click="menuClick('checkList')"
            class="menu-item"
          >
            <i class="el-icon-document-checked menu-icon"></i>
            <span>检查项目</span>
          </el-menu-item>

          <!-- 病床信息 -->
          <el-menu-item
            index="bedList"
            @click="menuClick('bedList')"
            class="menu-item"
          >
            <i class="el-icon-office-building menu-icon"></i>
            <span>病床信息</span>
          </el-menu-item>
          <!-- 排班信息 -->
          <el-menu-item
            index="arrangeIndex"
            @click="menuClick('arrangeIndex')"
            class="menu-item"
          >
            <i class="el-icon-date menu-icon"></i>
            <span>值班信息</span>
          </el-menu-item>


          <!-- 数据统计 -->
          <el-menu-item
            index="dataExpore"
            @click="menuClick('dataExpore')"
            class="menu-item"
          >
            <i class="el-icon-data-analysis menu-icon"></i>
            <span>数据统计</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="admin-main">
        <!-- 子路由入口 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken, clearToken, getActivePath, setActivePath } from "@/utils/storage.js";

export default {
  name: "Admin",
  data() {
    return {
      userName: "",
      activePath: "",
    };
  },
  methods: {
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
    menuClick(path) {
      this.activePath = path;
      setActivePath(path);
      if (this.$route.path !== "/" + path) this.$router.push(path);
    },
    logout() {
      this.$confirm("确定要退出登录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
        customClass: "logout-confirm"
      })
        .then(() => {
          clearToken();
          this.$message({
            type: "success",
            message: "退出登录成功!",
            customClass: "logout-message"
          });
          this.$router.push("login");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
            customClass: "logout-message"
          });
        });
    },
  },
  created() {
    this.activePath = getActivePath();
    this.userName = this.tokenDecode(getToken()).aName;
  }
};
</script>

<style lang="scss" scoped>
.admin-container {
  height: 100vh;
  background-color: #f5f7fa;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  background: linear-gradient(135deg, #409EFF, #66b1ff);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 10;
  height: 60px !important;
  color: white;

  .header-left {
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      opacity: 0.8;
    }

    .logo {
      width: 36px;
      height: 36px;
    }

    .system-title {
      margin-left: 12px;
      font-size: 20px;
      font-weight: 600;
      color: white;
    }
  }

  .header-right {
    display: flex;
    align-items: center;

    .user-info {
      margin-right: 20px;
      font-size: 14px;

      .welcome-text {
        color: rgba(255, 255, 255, 0.8);
      }

      .user-name {
        font-weight: 600;
        margin: 0 4px;
      }

      .user-role {
        color: rgba(255, 255, 255, 0.8);
      }
    }

    .logout-btn {
      color: white;
      background: rgba(255, 255, 255, 0.1);
      border-color: rgba(255, 255, 255, 0.3);

      &:hover {
        background: rgba(255, 255, 255, 0.2);
      }
    }
  }
}

.main-container {
  height: calc(100vh - 60px);
}

.admin-sidebar {
  background-color: #fff;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
  transition: all 0.3s;
  border-right: none;

  .admin-menu {
    border-right: none;
    height: 100%;

    .menu-item {
      height: 56px;
      line-height: 56px;
      font-size: 14px !important;
      margin: 2px 0;
      transition: all 0.2s;
      display: flex;
      align-items: center;

      &:hover {
        background-color: #ecf5ff !important;
        color: #409EFF !important;
      }

      &.is-active {
        background-color: #ecf5ff !important;
        color: #409EFF !important;
        font-weight: 500;
        border-right: 3px solid #409EFF;
      }

      .menu-icon {
        font-size: 18px;
        margin-right: 12px;
        flex-shrink: 0;
      }

      span {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}

.admin-main {
  padding: 20px;
  background-color: #f5f7fa;
  background-image: url('../assets/doctor.jpg'); /* 添加背景图 */
  background-size: cover; /* 或 contain，根据需求调整 */
  background-position: center; /* 图片位置 */
  background-repeat: no-repeat; /* 防止重复 */
  background-attachment: fixed; /* 可选 - 固定背景 */
  overflow: auto;
}

@media screen and (max-width: 992px) {
  .admin-sidebar {
    width: 180px !important;
  }
}

@media screen and (max-width: 768px) {
  .admin-header {
    padding: 0 12px;

    .system-title {
      font-size: 16px;
    }

    .user-info {
      display: none;
    }
  }

  .admin-sidebar {
    width: 64px !important;

    .menu-item {
      justify-content: center;

      span {
        display: none;
      }

      .menu-icon {
        margin-right: 0;
        font-size: 20px;
      }
    }
  }
}
</style>

<style lang="scss">
.logout-confirm {
  .el-message-box__header {
    background: linear-gradient(135deg, #409EFF, #66b1ff);

    .el-message-box__title {
      color: white;
    }

    .el-message-box__headerbtn .el-message-box__close {
      color: white;
    }
  }

  .el-message-box__btns {
    text-align: center;

    .el-button {
      min-width: 100px;
    }
  }
}

.logout-message {
  min-width: 200px;
}
</style>