<template>
  <div class="in-bed-container">
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
          label="挂号单号"
          prop="oId"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column
          label="患者ID"
          prop="pId"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          label="医生ID"
          prop="dId"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          label="挂号时间"
          prop="oStart"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          label="结束时间"
          prop="oEnd"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          label="挂号状态"
          prop="oState"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.oState === 1"
              type="success"
              effect="dark"
            >已完成</el-tag>
            <el-tag
              v-if="scope.row.oState === 0"
              type="danger"
              effect="dark"
            >未完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="180"
          fixed="right"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="BedDiag(scope.row.pId, scope.row.dId)"
              class="action-btn"
            >
              <i class="el-icon-office-building"></i>
              申请住院
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
        :page-sizes="[4, 8, 16, 32]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        class="pagination"
      ></el-pagination>
    </el-card>

    <!-- 住院对话框 -->
    <el-dialog
      title="申请住院"
      :visible.sync="BedFormVisible"
      width="600px"
      class="bed-dialog"
    >
      <el-form
        :model="bedForm"
        label-position="right"
        label-width="100px"
      >
        <el-form-item label="患者账号：">
          <el-input v-model="bedForm.pId" disabled></el-input>
        </el-form-item>
        <el-form-item label="医生账号：">
          <el-input v-model="bedForm.dId" disabled></el-input>
        </el-form-item>
        <el-form-item label="申请原因：">
          <el-input
            v-model="bedForm.bReason"
            type="textarea"
            :rows="4"
            placeholder="请输入详细的住院原因"
          ></el-input>
        </el-form-item>
        <el-form-item label="病床号：">
          <el-select
            v-model="bedForm.bId"
            placeholder="请选择病床号"
            style="width: 100%"
          >
            <el-option
              v-for="item in nullBed"
              :key="item.bId"
              :label="item.bId"
              :value="item.bId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="BedFormVisible = false"
          class="dialog-btn"
        >
          取消
        </el-button>
        <el-button
          type="primary"
          @click="bedClick"
          class="dialog-btn"
        >
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import { toLoad } from "@/utils/initialize.js";
import request from "@/utils/request.js";
export default {
  name: "InBed",
  data() {
    return {
      userId: 1,
      userName: "",
      pageNumber: 1,
      size: 4,
      query: "",
      total: 3,
      orderData: [],
      loading: false,
      //申请住院对话框
      BedFormVisible: false,
      bedForm: {
        pId: "",
        dId: "",
        bId: "",
        bReason: ""
      },
      nullBed: [],
    };
  },
  methods: {
    //点击申请床位确认按钮
    bedClick() {
      if (!this.bedForm.bId) {
        return this.$message.warning("请选择病床号");
      }
      if (!this.bedForm.bReason) {
        return this.$message.warning("请输入住院原因");
      }

      this.loading = true;
      request
        .get("bed/updateBed", {
          params: {
            bId: this.bedForm.bId,
            dId: this.bedForm.dId,
            pId: this.bedForm.pId,
            bReason: this.bedForm.bReason,
          },
        })
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("来晚了...该床位已被占用");
          this.BedFormVisible = false;
          this.$message.success("申请住院成功！");
          this.requestOrders();
        })
        .finally(() => {
          this.loading = false;
        });
    },

    //请求所有空床位
    requestBeds() {
      request
        .get("bed/findNullBed")
        .then((res) => {
          toLoad();
          if (res.data.status !== 200)
            return this.$message.error("数据请求失败");
          this.nullBed = res.data.data;
        })
        .catch((err) => {
          console.error(err);
          this.$message.error("获取床位数据失败");
        });
    },
    //打开申请住院对话框
    BedDiag(pId, dId) {
      this.bedForm = {
        pId: pId,
        dId: dId,
        bId: "",
        bReason: ""
      };
      this.BedFormVisible = true;
      this.requestBeds();
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
    //获取已完成的订单信息
    requestOrders() {
      this.loading = true;
      request
        .get("order/findOrderFinish", {
          params: {
            dId: this.userId,
            pageNumber: this.pageNumber,
            size: this.size,
            query: this.query,
          },
        })
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("数据请求失败");
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
    //解码token信息
    this.userId = this.tokenDecode(getToken()).dId;
    this.userName = this.tokenDecode(getToken()).dName;
    //获取订单信息
    this.requestOrders();
  },
};
</script>

<style lang="scss" scoped>
.in-bed-container {
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

.bed-dialog {
  ::v-deep .el-dialog {
    border-radius: 8px;

    .el-dialog__header {
      border-bottom: 1px solid #eee;
      padding: 15px 20px;

      .el-dialog__title {
        color: #409EFF;
        font-weight: 600;
      }
    }

    .el-dialog__body {
      padding: 20px;
    }

    .el-dialog__footer {
      border-top: 1px solid #eee;
      padding: 15px 20px;
    }
  }
}

.dialog-btn {
  padding: 10px 20px;
  border-radius: 4px;
  min-width: 100px;
}

::v-deep .el-form-item__label {
  color: #666;
  font-weight: 500;
}
</style>