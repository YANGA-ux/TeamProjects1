<template>
  <div class="my-order-container">
    <el-card class="order-card" shadow="hover">
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
          prop="pName"
          label="姓名"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="dName"
          label="医生姓名"
          width="120"
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
          prop="oTotalPrice"
          label="费用(元)"
          width="100"
          align="center"
        ></el-table-column>

        <el-table-column
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
              type="warning"
              size="small"
              icon="el-icon-money"
              @click="priceClick(scope.row.oId, scope.row.dId)"
              v-if="scope.row.oPriceState === 0 && scope.row.oState === 1"
              class="action-btn"
            >
              点击缴费
            </el-button>
          </template>
        </el-table-column>

        <el-table-column
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
              type="danger"
              effect="dark"
              v-if="scope.row.oState === 0"
            >
              未完成
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-document"
              @click="seeReport(scope.row.oId)"
              v-if="scope.row.oState === 1 && scope.row.oPriceState === 1"
              class="action-btn"
            >
              查看报告
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 评价对话框 -->
    <el-dialog
      title="用户评价"
      :visible.sync="starVisible"
      width="500px"
      class="rating-dialog"
    >
      <div class="dialog-content">
        <h3 class="rating-title">
          请对医生 <span class="doctor-name">{{ dName }}</span> (工号: {{ dId }}) 进行评价
        </h3>
        <div class="rating-stars">
          <el-rate
            v-model="star"
            show-text
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
          ></el-rate>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="starVisible = false"
          class="dialog-btn"
        >
          <i class="el-icon-close"></i> 取消
        </el-button>
        <el-button
          type="primary"
          @click="starClick"
          class="dialog-btn"
        >
          <i class="el-icon-check"></i> 确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
export default {
  name: "MyOrder",
  data() {
    return {
      userId: 1,
      orderData: [],
      star: 5,
      starVisible: false,
      dId: 1,
      dName: "",
      loading: true
    };
  },
  methods: {
    starClick() {
      request
        .get("doctor/updateStar", {
          params: {
            dId: this.dId,
            dStar: this.star,
          },
        })
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("评价失败");
          this.$message.success("谢谢您的评价");
          this.starVisible = false;
        });
    },
    seeReport(id) {
      window.location.href =
        "http://localhost:9282/patient/pdf?oId=" + id;
    },
    priceClick(oId, dId) {
      this.$confirm('确定要支付该挂号费用吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request
          .get("order/updatePrice", {
            params: {
              oId: oId,
            },
          })
          .then((res) => {
            if (res.data.status !== 200) {
              this.$message.error("请求数据失败");
              return;
            }
            this.$message.success("单号 " + oId + " 缴费成功！");
            request
              .get("admin/findDoctor", {
                params: {
                  dId: dId,
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error("请求数据失败");
                this.dId = res.data.data.dId;
                this.dName = res.data.data.dName;
              });
            this.starVisible = true;
            this.requestOrder();
          });
      }).catch(() => {
        this.$message.info('已取消支付');
      });
    },
    requestOrder() {
      this.loading = true;
      request
        .get("patient/findOrderByPid", {
          params: {
            pId: this.userId,
          },
        })
        .then((res) => {
          this.loading = false;
          if (res.data.status !== 200)
            this.$message.error("请求数据失败");
          this.orderData = res.data.data;
        })
        .catch(() => {
          this.loading = false;
          this.$message.error("请求数据失败");
        });
    },
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
  },
  created() {
    this.userId = this.tokenDecode(getToken()).pId;
    this.requestOrder();
  },
};
</script>

<style lang="scss" scoped>
.my-order-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);

  .order-card {
    border-radius: 8px;
    border: none;

    .order-table {
      font-size: 14px;

      .action-btn {
        padding: 7px 12px;
        border-radius: 4px;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
      }
    }
  }

  .rating-dialog {
    .dialog-content {
      text-align: center;
      padding: 0 20px;

      .rating-title {
        margin-bottom: 30px;
        color: #333;
        font-weight: normal;

        .doctor-name {
          color: #409EFF;
          font-weight: bold;
        }
      }

      .rating-stars {
        display: flex;
        justify-content: center;
        margin-bottom: 20px;
      }
    }

    .dialog-footer {
      text-align: center;

      .dialog-btn {
        padding: 10px 20px;
        border-radius: 4px;
        font-size: 14px;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .my-order-container {
    padding: 10px;

    .order-card {
      .order-table {
        font-size: 12px;
      }
    }
  }
}
</style>