<template>
  <div class="order-operate-container">
    <el-card class="section-card" shadow="hover">
      <!-- 面包屑 -->
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/orderOperate' }">科室选择</el-breadcrumb-item>
        <el-breadcrumb-item>日期选择</el-breadcrumb-item>
        <el-breadcrumb-item>挂号</el-breadcrumb-item>
      </el-breadcrumb>

      <el-divider></el-divider>

      <!-- 内科类 -->
      <div class="section-header">
        <i class="el-icon-first-aid-kit section-icon"></i>
        <span class="section-title">内科类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="inter in inters"
          :key="inter"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(inter)"
        >
          {{ inter }}
        </el-button>
      </div>

      <el-divider></el-divider>

      <!-- 外科类 -->
      <div class="section-header">
        <i class="el-icon-first-aid-kit section-icon"></i>
        <span class="section-title">外科类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="out in outs"
          :key="out"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(out)"
        >
          {{ out }}
        </el-button>
      </div>

      <el-divider></el-divider>

      <!-- 妇产科类 -->
      <div class="section-header">
        <i class="el-icon-female section-icon"></i>
        <span class="section-title">妇产科类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="woman in women"
          :key="woman"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(woman)"
        >
          {{ woman }}
        </el-button>
      </div>

      <el-divider></el-divider>

      <!-- 儿科类 -->
      <div class="section-header">
        <i class="el-icon-child section-icon"></i>
        <span class="section-title">儿科类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="kid in kids"
          :key="kid"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(kid)"
        >
          {{ kid }}
        </el-button>
      </div>

      <el-divider></el-divider>

      <!-- 五官科类 -->
      <div class="section-header">
        <i class="el-icon-view section-icon"></i>
        <span class="section-title">五官科类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="five in fives"
          :key="five"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(five)"
        >
          {{ five }}
        </el-button>
      </div>

      <el-divider></el-divider>

      <!-- 中医类 -->
      <div class="section-header">
        <i class="el-icon-orange section-icon"></i>
        <span class="section-title">中医类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="chinese in chineses"
          :key="chinese"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(chinese)"
        >
          {{ chinese }}
        </el-button>
      </div>

      <el-divider></el-divider>

      <!-- 其他类 -->
      <div class="section-header">
        <i class="el-icon-more section-icon"></i>
        <span class="section-title">其他类</span>
      </div>
      <div class="section-buttons">
        <el-button
          v-for="orther in orthers"
          :key="orther"
          type="primary"
          plain
          class="section-btn"
          @click="sectionClick(orther)"
        >
          {{ orther }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { toLoad } from "@/utils/initialize.js";
export default {
  name: "orderOperate",
  data() {
    return {
      inters: [
        "神经内科",
        "呼吸与危重症医学科",
        "内分泌科",
        "消化内科",
        "心血管内科",
        "肾内科",
        "发热门诊",
      ],
      outs: [
        "手足外科",
        "普通外科",
        "肛肠外科",
        "神经外科",
        "泌尿外科",
        "骨科",
        "烧伤整形外科",
      ],
      women: ["妇科", "产科"],
      kids: ["儿科", "儿童保健科"],
      fives: ["耳鼻咽喉科", "眼科", "口腔科"],
      chineses: ["中医科"],
      orthers: ["康复医学科", "急诊科", "皮肤性病科", "功能科"],
    };
  },
  methods: {
    sectionClick(sectionOpt) {
      this.$router.push({
        path: "/sectionMessage?sectionOpt=" + sectionOpt,
      });
    },
  },
  mounted() {
    toLoad()
  }
};
</script>

<style lang="scss" scoped>
.order-operate-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);

  .section-card {
    border-radius: 8px;
    border: none;

    .breadcrumb {
      padding: 8px 0;
      font-size: 14px;
    }

    .section-header {
      margin: 16px 0;
      display: flex;
      align-items: center;

      .section-icon {
        font-size: 22px;
        color: #409EFF;
        margin-right: 10px;
      }

      .section-title {
        font-size: 18px;
        font-weight: 600;
        color: #333;
      }
    }

    .section-buttons {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;

      .section-btn {
        margin: 0;
        padding: 12px 18px;
        border-radius: 6px;
        transition: all 0.3s;

        &:hover {
          background-color: #409EFF;
          color: white;
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
        }
      }
    }

    .el-divider {
      margin: 24px 0;
      background-color: #ebeef5;
    }
  }
}

@media screen and (max-width: 768px) {
  .order-operate-container {
    padding: 10px;

    .section-card {
      .section-buttons {
        gap: 8px;

        .section-btn {
          padding: 10px 12px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>