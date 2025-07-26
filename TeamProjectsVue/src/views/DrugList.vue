<template>
  <div class="drug-container">
    <!-- 卡片 -->
    <el-card class="drug-card" shadow="hover">
      <!-- 搜索栏及增加药物 -->
      <el-row type="flex" justify="space-between" align="middle" class="toolbar">
        <el-col :span="12">
          <el-input v-model="query" placeholder="请输入名称查询" class="search-input">
            <el-button slot="append" @click="requestDrugs" type="primary">
              <i class="el-icon-search"></i> 搜索
            </el-button>
          </el-input>
        </el-col>
        <el-col :span="4" class="text-right">
          <el-button type="primary" @click="addFormVisible = true" icon="el-icon-plus">
            增加药物
          </el-button>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table :data="drugData" stripe border class="drug-table">
        <el-table-column label="编号" prop="drId" align="center"></el-table-column>
        <el-table-column label="名称" prop="drName" align="center"></el-table-column>
        <el-table-column label="剩余数量" prop="drNumber" align="center"></el-table-column>
        <el-table-column label="单位" prop="drUnit" align="center"></el-table-column>
        <el-table-column label="单价" prop="drPrice" align="center">
          <template slot-scope="scope">
            <span>¥{{ scope.row.drPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column label="出版商" prop="drPublisher" align="center"></el-table-column>
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="modifyDialog(scope.row.drId)">
              编辑
            </el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteDialog(scope.row.drId)">
              删除
            </el-button>
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
        :page-sizes="[1, 2, 4, 8, 16]"
        :total="total"
        class="pagination">
      </el-pagination>
    </el-card>

    <!-- 增加药物对话框 -->
    <el-dialog title="增加药物" :visible.sync="addFormVisible" class="drug-dialog">
      <el-form :model="addForm" :rules="rules" ref="ruleForm" label-position="right" label-width="100px">
        <el-form-item label="编号" prop="drId">
          <el-input v-model.number="addForm.drId"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="drName">
          <el-input v-model="addForm.drName"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="drNumber">
          <el-input-number v-model="addForm.drNumber" :min="0" :max="1000"></el-input-number>
        </el-form-item>
        <el-form-item label="单位" prop="drUnit">
          <el-radio-group v-model="addForm.drUnit">
            <el-radio label="盒">盒</el-radio>
            <el-radio label="袋">袋</el-radio>
            <el-radio label="剂">剂</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="单价" prop="drPrice">
          <el-input v-model="addForm.drPrice">
            <template slot="prepend">¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="出版商" prop="drPublisher">
          <el-input v-model="addForm.drPublisher"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addDrug('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改药物对话框 -->
    <el-dialog title="修改药物" :visible.sync="modifyFormVisible" class="drug-dialog">
      <el-form :model="modifyForm" :rules="rules" ref="ruleForm" label-position="right" label-width="100px">
        <el-form-item label="编号" prop="drId">
          <el-input v-model.number="modifyForm.drId" disabled></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="drName">
          <el-input v-model="modifyForm.drName"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="drNumber">
          <el-input-number v-model="modifyForm.drNumber" :min="0" :max="1000"></el-input-number>
        </el-form-item>
        <el-form-item label="单位" prop="drUnit">
          <el-radio-group v-model="modifyForm.drUnit">
            <el-radio label="盒">盒</el-radio>
            <el-radio label="袋">袋</el-radio>
            <el-radio label="剂">剂</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="单价" prop="drPrice">
          <el-input v-model="modifyForm.drPrice">
            <template slot="prepend">¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="出版商" prop="drPublisher">
          <el-input v-model="modifyForm.drPublisher"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="modifyDrug('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
  name: "DrugList",
  data() {
    return {
      pageNumber: 1,
      size: 8,
      query: "",
      drugData: [],
      total: 3,
      addFormVisible: false,
      addForm: {},
      rules: {
        drId: [
          { required: true, message: "请输入编号", trigger: "blur" },
          {
            type: "number",
            message: "账号必须数字类型",
            trigger: "blur",
          },
        ],
        drName: [
          { required: true, message: "请输入名称", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "账号必须是1到50个字符",
            trigger: "blur",
          },
        ],
        drUnit: [
          { required: true, message: "请选择单位", trigger: "blur" },
        ],
        drPrice: [
          { required: true, message: "请输入单价", trigger: "blur" },
        ],
        drPublisher: [
          {
            required: true,
            message: "请输入出版商",
            trigger: "blur",
          },
          {
            min: 1,
            max: 50,
            message: "账号必须是1到50个字符",
            trigger: "blur",
          },
        ],
      },
      modifyFormVisible: false,
      modifyForm: {},
    };
  },
  methods: {
    //点击修改药物信息
    modifyDrug(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request
            .get("drug/modifyDrug", {
              params: {
                drId: this.modifyForm.drId,
                drName: this.modifyForm.drName,
                drNumber: this.modifyForm.drNumber,
                drPrice: this.modifyForm.drPrice,
                drUnit: this.modifyForm.drUnit,
                drPublisher: this.modifyForm.drPublisher,
              },
            })
            .then((res) => {
              if (res.data.status !== 200)
                return this.$message.error("修改信息失败！");
              this.modifyFormVisible = false;
              this.requestDrugs();
              this.$message.success("修改药物信息成功！");
              console.log(res);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //打开修改对话框
    modifyDialog(id) {
      request
        .get("drug/findDrug", {
          params: {
            drId: id,
          },
        })
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("请求数据失败");
          this.modifyForm = res.data.data;
          this.modifyFormVisible = true;
          console.log(res);
        });
    },
    //删除药物操作
    deleteDrug(id) {
      request
        .get("drug/deleteDrug", {
          params: {
            drId: id,
          },
        })
        .then((res) => {
          this.requestDrugs();
          console.log(res);
        });
    },
    //删除对话框
    deleteDialog(id) {
      this.$confirm("此操作将删除该药物信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteDrug(id);
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //点击增加确认按钮
    addDrug(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request
            .get("drug/addDrug", {
              params: {
                drId: this.addForm.drId,
                drName: this.addForm.drName,
                drNumber: this.addForm.drNumber,
                drPrice: this.addForm.drPrice,
                drUnit: this.addForm.drUnit,
                drPublisher: this.addForm.drPublisher,
              },
            })
            .then((res) => {
              if (res.data.status !== 200)
                return this.$message.error(
                  "编号不合法或已被占用！"
                );
              this.addFormVisible = false;
              this.requestDrugs();
              this.$message.success("增加医生成功！");
              console.log(res);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //页面大小改变时触发
    handleSizeChange(size) {
      this.size = size;
      this.requestDrugs();
    },
    //   页码改变时触发
    handleCurrentChange(num) {
      console.log(num);
      this.pageNumber = num;
      this.requestDrugs();
    },
    // 加载医生列表
    requestDrugs() {
      request
        .get("drug/findAllDrugs", {
          params: {
            pageNumber: this.pageNumber,
            size: this.size,
            query: this.query,
          },
        })
        .then((res) => {
          this.drugData = res.data.data.drugs;
          this.total = res.data.data.total;
          toLoad()
        });
    },
  },
  created() {
    this.requestDrugs();
  },
};
</script>

<style lang="scss" scoped>
.drug-container {
  padding: 20px;
  height: 100%;

  .drug-card {
    border-radius: 8px;
    border: none;
    background-color: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

    .toolbar {
      margin-bottom: 20px;

      .search-input {
        width: 300px;

        ::v-deep .el-input-group__append {
          background-color: #409EFF;
          color: white;
          border-color: #409EFF;
        }
      }

      .text-right {
        text-align: right;
      }
    }

    .drug-table {
      width: 100%;
      margin-bottom: 20px;
      font-size: 14px;

      ::v-deep th {
        background-color: #f8fafc !important;
        color: #333;
        font-weight: 600;
      }

      ::v-deep .el-button--mini {
        padding: 7px 12px;
        font-size: 12px;
      }
    }

    .pagination {
      margin-top: 20px;
      text-align: right;
    }
  }

  .drug-dialog {
    ::v-deep .el-dialog {
      border-radius: 8px;

      .el-dialog__header {
        border-bottom: 1px solid #eee;
        padding: 15px 20px;

        .el-dialog__title {
          font-size: 18px;
          color: #333;
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

    .el-form-item {
      margin-bottom: 22px;

      .el-input, .el-input-number {
        width: 100%;
      }

      .el-radio-group {
        width: 100%;
      }
    }

    .dialog-footer {
      text-align: right;
    }
  }
}

.el-message-box {
  border-radius: 8px;

  ::v-deep .el-message-box__header {
    background: linear-gradient(135deg, #409EFF, #66b1ff);

    .el-message-box__title {
      color: white;
    }

    .el-message-box__headerbtn .el-message-box__close {
      color: white;
    }
  }

  ::v-deep .el-message-box__btns {
    text-align: center;

    .el-button {
      min-width: 100px;
    }
  }
}

.el-message {
  min-width: 200px;
}
</style>