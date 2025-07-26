<template>
  <div class="check-list-container">
    <!-- 卡片 -->
    <el-card class="check-card" shadow="hover">
      <!-- 搜索栏及增加检查 -->
      <el-row type="flex" justify="space-between" align="middle" class="toolbar">
        <el-col :span="8">
          <el-input
            v-model="query"
            placeholder="请输入名称查询"
            class="search-input"
            clearable
            @clear="requestChecks"
            @keyup.enter.native="requestChecks"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="requestChecks"
            >搜索</el-button>
          </el-input>
        </el-col>
        <el-button
          type="primary"
          icon="el-icon-plus"
          @click="addFormVisible = true"
          class="add-btn"
        >新增项目</el-button>
      </el-row>

      <!-- 表格 -->
      <el-table
        :data="checkData"
        stripe
        style="width: 100%"
        border
        class="check-table"
        v-loading="loading"
      >
        <el-table-column label="编号" prop="chId" align="center"></el-table-column>
        <el-table-column label="项目" prop="chName" align="center"></el-table-column>
        <el-table-column label="价格(元)" prop="chPrice" align="center">
          <template slot-scope="scope">
            {{ scope.row.chPrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="modifyDialog(scope.row.chId)"
              class="action-btn"
            >编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="deleteDialog(scope.row.chId)"
              class="action-btn"
            >删除</el-button>
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
        class="pagination"
      ></el-pagination>
    </el-card>

    <!-- 增加检查项目对话框 -->
    <el-dialog
      title="新增检查项目"
      :visible.sync="addFormVisible"
      width="500px"
      class="form-dialog"
    >
      <el-form :model="addForm" :rules="rules" ref="addFormRef" label-width="100px">
        <el-form-item label="项目编号" prop="chId">
          <el-input v-model.number="addForm.chId" placeholder="请输入项目编号"></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="chName">
          <el-input v-model="addForm.chName" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item label="项目价格" prop="chPrice">
          <el-input v-model="addForm.chPrice" placeholder="请输入项目价格">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false" size="medium">取消</el-button>
        <el-button
          type="primary"
          @click="addCheck('addFormRef')"
          size="medium"
          :loading="addLoading"
        >确定</el-button>
      </div>
    </el-dialog>

    <!-- 修改检查项目对话框 -->
    <el-dialog
      title="修改检查项目"
      :visible.sync="modifyFormVisible"
      width="500px"
      class="form-dialog"
    >
      <el-form :model="modifyForm" :rules="rules" ref="modifyFormRef" label-width="100px">
        <el-form-item label="项目编号" prop="chId">
          <el-input v-model.number="modifyForm.chId" disabled></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="chName">
          <el-input v-model="modifyForm.chName" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item label="项目价格" prop="chPrice">
          <el-input v-model="modifyForm.chPrice" placeholder="请输入项目价格">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false" size="medium">取消</el-button>
        <el-button
          type="primary"
          @click="modifyCheck('modifyFormRef')"
          size="medium"
          :loading="modifyLoading"
        >确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";

export default {
  name: "CheckList",
  data() {
    return {
      pageNumber: 1,
      size: 8,
      query: "",
      checkData: [],
      total: 0,
      loading: false,

      addFormVisible: false,
      addForm: {},
      addLoading: false,

      modifyFormVisible: false,
      modifyForm: {},
      modifyLoading: false,

      rules: {
        chId: [
          { required: true, message: "请输入项目编号", trigger: "blur" },
          { type: "number", message: "编号必须为数字", trigger: "blur" }
        ],
        chName: [
          { required: true, message: "请输入项目名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在1到50个字符", trigger: "blur" }
        ],
        chPrice: [
          { required: true, message: "请输入项目价格", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "请输入正确的价格格式", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 请求检查项目列表
    requestChecks() {
      this.loading = true;
      request.get("check/findAllChecks", {
        params: {
          pageNumber: this.pageNumber,
          size: this.size,
          query: this.query
        }
      }).then(res => {
        this.checkData = res.data.data.checks;
        this.total = res.data.data.total;
        this.loading = false;
        toLoad();
      }).catch(() => {
        this.loading = false;
      });
    },

    // 添加检查项目
    addCheck(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addLoading = true;
          request.get("check/addCheck", {
            params: this.addForm
          }).then(res => {
            if (res.data.status !== 200) {
              this.$message.error("添加失败：" + res.data.message);
              return;
            }
            this.$message.success("添加成功");
            this.addFormVisible = false;
            this.addForm = {};
            this.requestChecks();
          }).finally(() => {
            this.addLoading = false;
          });
        }
      });
    },

    // 打开修改对话框
    modifyDialog(id) {
      request.get("check/findCheck", {
        params: { chId: id }
      }).then(res => {
        if (res.data.status !== 200) {
          this.$message.error("获取数据失败");
          return;
        }
        this.modifyForm = res.data.data;
        this.modifyFormVisible = true;
      });
    },

    // 修改检查项目
    modifyCheck(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.modifyLoading = true;
          request.get("check/modifyCheck", {
            params: this.modifyForm
          }).then(res => {
            if (res.data.status !== 200) {
              this.$message.error("修改失败：" + res.data.message);
              return;
            }
            this.$message.success("修改成功");
            this.modifyFormVisible = false;
            this.requestChecks();
          }).finally(() => {
            this.modifyLoading = false;
          });
        }
      });
    },

    // 删除检查项目
    deleteCheck(id) {
      request.get("check/deleteCheck", {
        params: { chId: id }
      }).then(res => {
        if (res.data.status !== 200) {
          this.$message.error("删除失败：" + res.data.message);
          return;
        }
        this.$message.success("删除成功");
        this.requestChecks();
      });
    },

    // 删除确认对话框
    deleteDialog(id) {
      this.$confirm("确定要删除该检查项目吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.deleteCheck(id);
      }).catch(() => {
        this.$message.info("已取消删除");
      });
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.size = size;
      this.requestChecks();
    },

    // 页码改变
    handleCurrentChange(num) {
      this.pageNumber = num;
      this.requestChecks();
    }
  },
  created() {
    this.requestChecks();
  }
};
</script>

<style lang="scss" scoped>
.check-list-container {
  padding: 20px;
  height: 100%;

  .check-card {
    min-height: calc(100% - 40px);
    display: flex;
    flex-direction: column;

    .toolbar {
      margin-bottom: 20px;

      .search-input {
        width: 300px;
      }

      .add-btn {
        padding: 10px 15px;
      }
    }

    .check-table {
      flex: 1;
      margin-bottom: 20px;

      .action-btn {
        padding: 7px 10px;
        font-size: 12px;
      }
    }

    .pagination {
      text-align: right;
      padding-top: 15px;
      border-top: 1px solid #eee;
    }
  }

  .form-dialog {
    .el-form-item {
      margin-bottom: 20px;

      .el-input {
        width: 100%;
      }
    }

    .dialog-footer {
      text-align: right;
    }
  }
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .check-list-container {
    padding: 10px;

    .check-card {
      .toolbar {
        flex-direction: column;

        .search-input {
          width: 100%;
          margin-bottom: 10px;
        }

        .add-btn {
          width: 100%;
        }
      }
    }
  }
}
</style>