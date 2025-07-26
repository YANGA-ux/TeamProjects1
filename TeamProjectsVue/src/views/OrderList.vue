<template>
  <!-- 卡片 -->
  <el-card class="order-card">
    <!-- 搜索栏 -->
    <el-row type="flex" class="search-row">
      <el-col :span="6">
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
            <i class="el-icon-search"></i> 搜索
          </el-button>
        </el-input>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
      :data="orderData"
      stripe
      style="width: 100%"
      border
      class="order-table"
      v-loading="loading"
    >
      <el-table-column
        prop="oId"
        label="挂号单号"
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
        prop="dId"
        label="医生ID"
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
        width="250"
        show-overflow-tooltip
      ></el-table-column>

      <el-table-column
        prop="oDrug"
        label="药物"
        width="180"
        show-overflow-tooltip
      ></el-table-column>

      <el-table-column
        prop="oCheck"
        label="检查项目"
        width="180"
        show-overflow-tooltip
      ></el-table-column>

      <el-table-column
        prop="oTotalPrice"
        label="费用(元)"
        width="120"
        align="center"
      ></el-table-column>

      <el-table-column
        prop="oPriceState"
        label="缴费状态"
        width="150"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag
            type="success"
            effect="dark"
            v-if="scope.row.oPriceState === 1"
          >
            已缴费
          </el-tag>
          <el-button
            type="danger"
            size="mini"
            v-if="scope.row.oPriceState === 0 && scope.row.oState === 1"
            @click="priceClick(scope.row.oId)"
            class="pay-btn"
          >
            <i class="el-icon-wallet"></i> 缴费
          </el-button>
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
            effect="dark"
            v-if="scope.row.oState === 1 && scope.row.oPriceState === 1"
          >
            已完成
          </el-tag>
          <el-tag
            type="warning"
            effect="dark"
            v-if="scope.row.oState === 0"
          >
            进行中
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        width="120"
        fixed="right"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="deleteDialog(scope.row.oId)"
            class="delete-btn"
          >
            <i class="el-icon-delete"></i> 删除
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
      :page-sizes="[8, 16, 24, 32]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      background
      class="pagination"
    >
    </el-pagination>
  </el-card>
</template>

<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";

export default {
  name: "OrderList",
  data() {
    return {
      pageNumber: 1,
      size: 8,
      query: "",
      orderData: [],
      total: 0,
      loading: false
    };
  },
  methods: {
    //删除挂号操作
    deleteOrder(id) {
      this.loading = true;
      request
        .get("admin/deleteOrder", {
          params: {
            oId: id,
          },
        })
        .then((res) => {
          this.requestOrders();
          this.$message.success("删除成功");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    //删除对话框
    deleteDialog(id) {
      this.$confirm("此操作将永久删除该挂号信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        customClass: "delete-dialog"
      })
        .then(() => {
          this.deleteOrder(id);
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    //页面大小改变时触发
    handleSizeChange(size) {
      this.size = size;
      this.requestOrders();
    },
    // 页码改变时触发
    handleCurrentChange(num) {
      this.pageNumber = num;
      this.requestOrders();
    },
    // 加载订单列表
    requestOrders() {
      this.loading = true;
      request
        .get("admin/findAllOrders", {
          params: {
            pageNumber: this.pageNumber,
            size: this.size,
            query: this.query,
          },
        })
        .then((res) => {
          this.orderData = res.data.data.orders;
          this.total = res.data.data.total;
          toLoad();
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 缴费操作
    priceClick(id) {
      this.$confirm("确认要为此挂号单缴费吗?", "缴费确认", {
        confirmButtonText: "确认缴费",
        cancelButtonText: "取消",
        type: "warning",
        customClass: "pay-dialog"
      })
        .then(() => {
          this.loading = true;
          // 这里应该调用缴费API
          // 模拟缴费成功
          setTimeout(() => {
            this.$message.success("缴费成功");
            this.requestOrders();
          }, 800);
        })
        .catch(() => {
          this.$message.info("已取消缴费");
        });
    }
  },
  created() {
    this.requestOrders();
  },
};
</script>

<style lang="scss" scoped>
.order-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  background-color: #fff;

  .search-row {
    margin-bottom: 20px;

    .search-input {
      ::v-deep.el-input__inner {
        border-radius: 4px 0 0 4px;
        height: 40px;
        line-height: 40px;
      }

      ::v-deep.el-input-group__append {
        background-color: #409EFF;
        color: white;
        border-radius: 0 4px 4px 0;
        padding: 0 15px;

        .el-button {
          color: white;
          font-size: 14px;
        }
      }
    }
  }

  .order-table {
    margin: 20px 0;
    border-radius: 8px;
    overflow: hidden;

    ::v-deep th {
      background-color: #f5f7fa;
      color: #333;
      font-weight: 600;
      text-align: center;
    }

    ::v-deep td {
      padding: 12px 0;
    }

    .pay-btn {
      padding: 7px 12px;
      font-size: 12px;
    }

    .delete-btn {
      padding: 7px 12px;
      font-size: 12px;
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: center;

    ::v-deep.el-pagination.is-background .el-pager li:not(.disabled).active {
      background-color: #409EFF;
      color: white;
    }

    ::v-deep.el-pagination.is-background .el-pager li:not(.disabled):hover {
      color: #409EFF;
    }
  }
}

// 删除确认对话框样式
.delete-dialog {
  ::v-deep.el-message-box__header {
    background: linear-gradient(135deg, #409EFF, #66b1ff);
    padding: 15px 20px;

    .el-message-box__title {
      color: white;
      font-size: 16px;
    }

    .el-message-box__headerbtn .el-message-box__close {
      color: white;
    }
  }

  ::v-deep.el-message-box__content {
    padding: 20px;
  }

  ::v-deep.el-message-box__btns {
    text-align: center;
    padding: 0 20px 15px;

    .el-button {
      min-width: 80px;
      padding: 8px 15px;
    }
  }
}

// 缴费确认对话框样式
.pay-dialog {
  ::v-deep.el-message-box__header {
    background: linear-gradient(135deg, #67C23A, #85ce61);
    padding: 15px 20px;

    .el-message-box__title {
      color: white;
      font-size: 16px;
    }

    .el-message-box__headerbtn .el-message-box__close {
      color: white;
    }
  }

  ::v-deep.el-message-box__content {
    padding: 20px;
  }

  ::v-deep.el-message-box__btns {
    text-align: center;
    padding: 0 20px 15px;

    .el-button {
      min-width: 80px;
      padding: 8px 15px;
    }

    .el-button--primary {
      background-color: #67C23A;
      border-color: #67C23A;
    }
  }
}
</style>