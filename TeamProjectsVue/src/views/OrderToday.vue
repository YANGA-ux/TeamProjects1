<template>
  <el-card class="order-card">
    <el-table
      :data="orderData"
      stripe
      border
      class="order-table"
      header-row-class-name="table-header"
    >
      <el-table-column
        label="序号"
        type="index"
        width="60"
        align="center"
      >
      </el-table-column>
      <el-table-column
        label="挂号单号"
        prop="oId"
        align="center"
      ></el-table-column>
      <el-table-column
        label="患者id"
        prop="pId"
        align="center"
      ></el-table-column>
      <el-table-column
        label="患者姓名"
        prop="pName"
        align="center"
      ></el-table-column>
      <el-table-column
        label="医生姓名"
        prop="dName"
        align="center"
      ></el-table-column>
      <el-table-column
        label="挂号时间"
        prop="oStart"
        width="180"
        align="center"
      ></el-table-column>
      <el-table-column
        label="操作"
        fixed="right"
        width="150"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="dealClick(scope.row.oId, scope.row.pId)"
            class="deal-btn"
          >
            <i class="el-icon-edit"></i>
            处理
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import { toLoad } from "@/utils/initialize.js";
import request from "@/utils/request.js";
export default {
  name: "orderToday",
  data() {
    return {
      userId: 1,
      userName: "dada",
      today: "",

      orderData: [],
    };
  },
  methods: {
    //挂号处理//页面跳转传值
    dealClick(oId, pId) {
      this.$router.push({
        path: "/dealOrder",
        query: {
          oId: oId,
          pId: pId,
        },
      });
    },
    //获取挂号信息
    requestOrder() {
      console.log(this.today)
      request
        .get("doctor/findOrderByNull", {
          params: {
            dId: this.userId,
            oStart: this.today,
          },
        })
        .then((res) => {
          toLoad();
          if (res.data.status != 200)
            return this.$message.error("获取数据失败");
          this.orderData = res.data.data;
        });
    },
    //token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
    //获取当天日期
    nowDay() {
      const nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        date: nowDate.getDate(),
      };
      if (date.date < 10) {
        date.date = "0" + date.date;
      }
      if (date.month < 10) {
        date.month = "0" + date.month;
      }
      this.today = date.year + "-" + date.month + "-" + date.date;
    },
  },
  created() {
    //解码token信息
    this.userId = this.tokenDecode(getToken()).dId;
    this.userName = this.tokenDecode(getToken()).dName;
    console.log(this.userId);
    console.log(this.userName);
    //获取当天日期
    this.nowDay();
    //获取订单信息
    this.requestOrder();
  },
};
</script>

<style lang="scss" scoped>
.order-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;

  ::v-deep .el-card__body {
    padding: 20px;
  }
}

.order-table {
  width: 100%;
  font-size: 14px;

  ::v-deep .table-header {
    th {
      background-color: #f5f7fa;
      color: #666;
      font-weight: 600;
      padding: 12px 0;
    }
  }

  ::v-deep .el-table__row {
    td {
      padding: 10px 0;
    }

    &:hover {
      td {
        background-color: #ecf5ff !important;
      }
    }
  }
}

.deal-btn {
  padding: 7px 12px;
  border-radius: 4px;
  background-color: #409EFF;
  border-color: #409EFF;

  i {
    margin-right: 5px;
    font-size: 14px;
  }

  &:hover {
    background-color: #66b1ff;
    border-color: #66b1ff;
  }

  &:active {
    background-color: #3a8ee6;
    border-color: #3a8ee6;
  }
}
</style>