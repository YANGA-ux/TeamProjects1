<template>
  <div class="doctor-order-container">
    <el-card class="order-card">
      <!-- 搜索栏 -->
      <el-row type="flex" justify="space-between" class="search-row">
        <el-col :span="8">
          <el-input
            v-model="query"
            placeholder="请输入患者ID查询"
            class="search-input"
            clearable
            @clear="requestOrders"
          >
            <el-button
              slot="append"
              type="primary"
              @click="requestOrders"
              class="search-btn"
            >
              <i class="el-icon-search"></i>
              查询
            </el-button>
          </el-input>
        </el-col>
      </el-row>

      <el-table
        :data="orderData"
        stripe
        border
        class="order-table"
        header-row-class-name="table-header"
        v-loading="loading"
      >
        <el-table-column
          prop="oId"
          label="挂号单号"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="dId"
          label="本人ID"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="pId"
          label="患者ID"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="oStart"
          label="挂号时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="oEnd"
          label="结束时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="oRecord"
          label="病因"
          width="300"
        ></el-table-column>
        <el-table-column
          prop="oDrug"
          label="药物"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="oCheck"
          label="检查项目"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="oTotalPrice"
          label="需交费用(元)"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="oPriceState"
          label="缴费状态"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              type="success"
              v-if="scope.row.oPriceState === 1"
              effect="dark"
            >已缴费</el-tag>
            <el-tag
              type="danger"
              v-if="scope.row.oPriceState === 0 && scope.row.oState === 1"
              effect="dark"
            >未缴费</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="oState"
          label="挂号状态"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              type="success"
              v-if="scope.row.oState === 1 && scope.row.oPriceState === 1"
              effect="dark"
            >已完成</el-tag>
            <el-tag
              type="danger"
              v-if="scope.row.oState === 0"
              effect="dark"
            >未完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="140"
          fixed="right"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="dealClick(scope.row.oId, scope.row.pId)"
              v-if="scope.row.oState === 1 && scope.row.oPriceState === 1"
              class="action-btn"
            >
              <i class="el-icon-refresh-right"></i>
              追诊
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNumber"
        :page-size="size"
        :page-sizes="[8, 16, 32, 64]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        class="pagination"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { toLoad } from "@/utils/initialize.js";
import { getToken } from "@/utils/storage.js";
export default {
  name: "DoctorOrder",
  data() {
    return {
      userId: 1,
      orderData: [],
      pageNumber: 1,
      size: 8,
      query: "",
      total: 3,
      loading: false
    };
  },
  methods: {
    //点击追诊按钮
    dealClick(oId, pId) {
      this.$router.push({
        path: "/dealOrderAgain",
        query: {
          oId: oId,
          pId: pId,
        },
      });
    },
    //页面大小改变时触发
    handleSizeChange(size) {
      this.size = size;
      this.requestOrders();
    },
    //页码改变时触发
    handleCurrentChange(num) {
      this.pageNumber = num;
      this.requestOrders();
    },

    //请求挂号信息
    requestOrders() {
      this.loading = true;
      request
        .get("order/findOrderByDid", {
          params: {
            dId: this.userId,
            pageNumber: this.pageNumber,
            size: this.size,
            query: this.query,
          },
        })
        .then((res) => {
          toLoad()
          if (res.data.status !== 200)
            this.$message.error("请求数据失败");
          this.orderData = res.data.data.orders;
          this.total = res.data.data.total;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    //token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
  },
  created() {
    this.userId = this.tokenDecode(getToken()).dId;
    this.requestOrders();
  },
};
</script>

<style lang="scss" scoped>
.doctor-order-container {
  padding: 20px;
}

.order-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;

  ::v-deep .el-card__body {
    padding: 20px;
  }
}

.search-row {
  margin-bottom: 20px;

  .search-input {
    ::v-deep .el-input-group__append {
      background-color: #409EFF;
      color: white;
      border-color: #409EFF;

      .el-button {
        color: white;
      }
    }
  }
}

.order-table {
  width: 100%;
  font-size: 14px;
  margin: 20px 0;

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

.action-btn {
  padding: 7px 12px;
  border-radius: 4px;

  i {
    margin-right: 5px;
    font-size: 14px;
  }
}

.pagination {
  margin-top: 20px;
  text-align: center;

  ::v-deep .el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color: #409EFF;
    color: white;
  }

  ::v-deep .el-pagination.is-background .el-pager li:not(.disabled):hover {
    color: #409EFF;
  }
}
</style>