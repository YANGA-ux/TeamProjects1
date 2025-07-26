<template>
  <div class="arrange-container">
    <el-card class="arrange-card" shadow="hover">
      <div class="date-selection">
        <div class="selection-title">
          <i class="el-icon-date title-icon"></i>
          <span>请选择值班日期：</span>
        </div>
        <div class="date-buttons">
          <el-button
            v-for="monthDay in monthDays"
            :key="monthDay"
            type="primary"
            class="date-button"
            @click="dateClick(monthDay)"
          >
            <i class="el-icon-calendar button-icon"></i>
            {{ monthDay }}
          </el-button>
        </div>
      </div>
      <div class="router-view-container">
        <router-view></router-view>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getActivePath, setActivePath } from "@/utils/storage.js";
import { toLoad } from "@/utils/initialize.js";
const ARRANGEDATE = "arrangeDate";

export default {
  name: "ArrangeIndex",
  data() {
    return {
      monthDays: [],
      monthDay: "",
      activePath: "",
    };
  },
  methods: {
    dateClick(monthDay) {
      console.log(monthDay);
      const nowDate = new Date();
      let year = nowDate.getFullYear();
      let dateTime = year + "-" + monthDay;
      sessionStorage.setItem(ARRANGEDATE, dateTime);

      this.activePath = "sectionIndex";
      setActivePath("sectionIndex");
      if (this.$route.path !== "/sectionIndex")
        this.$router.push("sectionIndex");
    },
    nowDay(num) {
      var nowDate = new Date();
      nowDate.setDate(nowDate.getDate() + num);
      var month = nowDate.getMonth() + 1;
      var date = nowDate.getDate();
      if (date < 10) {
        date = "0" + date;
      }
      if (month < 10) {
        month = "0" + month;
      }
      var time = month + "-" + date;
      this.monthDays.push(time);
    },
  },
  mounted() {
    toLoad();
  },
  created() {
    for (var i = 0; i < 25; i++) {
      this.nowDay(i);
      this.activePath = getActivePath();
    }
  },
};
</script>

<style scoped lang="scss">
.arrange-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.arrange-card {
  border-radius: 8px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.date-selection {
  padding: 20px;
}

.selection-title {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 18px;
  color: #666;

  .title-icon {
    font-size: 24px;
    margin-right: 10px;
    color: #409EFF;
  }
}

.date-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.date-button {
  padding: 12px 20px;
  font-size: 16px;
  border-radius: 6px;
  transition: all 0.3s;

  &:hover {
    background-color: #66b1ff;
    border-color: #66b1ff;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 177, 255, 0.3);
  }

  .button-icon {
    margin-right: 8px;
    font-size: 18px;
  }
}

.router-view-container {
  margin-top: 30px;
  padding: 20px;
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.disabled {
  background-color: #ddd;
  border-color: #ddd;
  color: black;
  cursor: not-allowed;
  pointer-events: none;
}
</style>