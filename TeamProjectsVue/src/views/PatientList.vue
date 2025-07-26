<template>
  <!-- 卡片 -->
  <el-card class="patient-card" shadow="hover">
    <!-- 搜索栏 -->
    <el-row type="flex" justify="space-between" align="middle" class="search-bar">
      <el-col :span="8">
        <el-input
          v-model="query"
          placeholder="请输入患者姓名查询"
          clearable
          @clear="requestPatients"
          class="search-input"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="requestPatients"
            class="search-btn"
          >搜索</el-button>
        </el-input>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
      :data="patientData"
      stripe
      style="width: 100%"
      border
      class="patient-table"
      v-loading="loading"
    >
      <el-table-column prop="pId" label="账号" width="120" align="center">
      </el-table-column>
      <el-table-column prop="pName" label="姓名" width="100" align="center">
      </el-table-column>
      <el-table-column prop="pGender" label="性别" width="80" align="center">
      </el-table-column>
      <el-table-column prop="pAge" label="年龄" width="100" align="center">
      </el-table-column>
      <el-table-column prop="pCard" label="证件号" align="center"> </el-table-column>
      <el-table-column prop="pPhone" label="手机号" align="center"> </el-table-column>
      <el-table-column prop="pEmail" label="邮箱" width="200" align="center">
      </el-table-column>
      <el-table-column prop="pState" label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag
            type="success"
            effect="dark"
            v-if="scope.row.pState === 1"
          >正常</el-tag>
          <el-tag
            type="danger"
            effect="dark"
            v-else
          >已删除</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="danger"
            @click="deleteDialog(scope.row.pId)"
            icon="el-icon-delete"
            class="action-btn"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :current-page="pageNumber"
      :page-size="size"
      :page-sizes="[5, 8, 10, 15]"
      :total="total"
      class="pagination"
    >
    </el-pagination>
  </el-card>
</template>

<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
  name: "PatientList",
  data() {
    return {
      pageNumber: 1,
      size: 8,
      query: "",
      patientData: [],
      total: 0,
      loading: false
    };
  },
  methods: {
    //删除病人操作
    deletePatient(id) {
      this.loading = true;
      request
        .get("admin/deletePatient", {
          params: {
            pId: id,
          },
        })
        .then((res) => {
          this.requestPatients();
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1500
          });
        })
        .finally(() => {
          this.loading = false;
        });
    },
    //删除对话框
    deleteDialog(id) {
      this.$confirm("此操作将永久删除该患者信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        customClass: "delete-dialog"
      })
        .then(() => {
          this.deletePatient(id);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
            duration: 1500
          });
        });
    },
    //页面大小改变时触发
    handleSizeChange(size) {
      this.size = size;
      this.requestPatients();
    },
    //   页码改变时触发
    handleCurrentChange(num) {
      this.pageNumber = num;
      this.requestPatients();
    },
    // 加载患者列表
    requestPatients() {
      this.loading = true;
      request
        .get("admin/findAllPatients", {
          params: {
            pageNumber: this.pageNumber,
            size: this.size,
            query: this.query,
          },
        })
        .then((res) => {
          this.patientData = res.data.data.patients;
          this.total = res.data.data.total;
          toLoad();
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
  created() {
    this.requestPatients();
  },
};
</script>

<style lang="scss" scoped>
.patient-card {
  border-radius: 8px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .search-bar {
    margin-bottom: 20px;

    .search-input {
      ::v-deep.el-input__inner {
        border-radius: 4px;
        border-right: none;
        height: 36px;
        line-height: 36px;
      }

      ::v-deep.el-input-group__append {
        background-color: #409EFF;
        color: white;
        border: none;
        border-radius: 0 4px 4px 0;
        padding: 0 15px;

        &:hover {
          background-color: #66b1ff;
        }
      }
    }
  }

  .patient-table {
    margin: 20px 0;
    font-size: 14px;

    ::v-deep th {
      background-color: #f5f7fa !important;
      color: #333;
      font-weight: 600;
      height: 50px;
    }

    ::v-deep td {
      height: 50px;
      padding: 8px 0;
    }

    .action-btn {
      padding: 7px 12px;
      font-size: 12px;
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: center;

    ::v-deep.el-pagination.is-background .el-pager li:not(.disabled).active {
      background-color: #409EFF;
    }

    ::v-deep.el-pagination.is-background .el-pager li:not(.disabled):hover {
      color: #409EFF;
    }
  }
}

.delete-dialog {
  ::v-deep.el-message-box__header {
    background: linear-gradient(135deg, #409EFF, #66b1ff);

    .el-message-box__title {
      color: white;
    }

    .el-message-box__headerbtn .el-message-box__close {
      color: white;
    }
  }

  ::v-deep.el-message-box__btns {
    text-align: center;

    .el-button {
      min-width: 100px;
    }
  }
}
</style>