<template>
  <div class="login-container" :style="backgroundDiv">
    <div class="login-card">
      <div class="login-header">
        <h1>公共服务平台医生服务系统</h1>
        <p>公共服务平台 · 为医患提供便捷沟通桥梁</p>
      </div>

      <el-form
        :model="loginForm"
        :rules="loginRules"
        ref="ruleForm"
        class="login-form"
        label-position="top"
      >
        <el-form-item prop="id">
          <el-input
            v-model="loginForm.id"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
            size="medium"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="请输入密码"
            show-password
            prefix-icon="el-icon-lock"
            size="medium"
          ></el-input>
        </el-form-item>

        <!-- 角色单选框 -->
        <el-form-item class="role-radio">
          <el-radio-group v-model="role" size="medium">
            <el-radio-button label="患者"></el-radio-button>
            <el-radio-button label="医生"></el-radio-button>
            <el-radio-button label="管理员"></el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item class="login-buttons">
          <el-button
            type="primary"
            size="medium"
            @click="submitLoginForm('ruleForm')"
            class="login-btn"
          >
            <i class="el-icon-s-promotion"></i> 登 录
          </el-button>
          <el-button
            plain
            size="medium"
            @click="registerFormVisible = true"
            class="register-btn"
          >
            <i class="el-icon-user"></i> 注册新账号
          </el-button>
        </el-form-item>

        <div class="login-footer">
          <el-link type="info" @click="findFormVisible = true">忘记密码?</el-link>
        </div>
      </el-form>
    </div>

    <!-- 注册对话框 -->
    <el-dialog
      title="用户注册"
      :visible.sync="registerFormVisible"
      width="500px"
      custom-class="register-dialog"
      center
    >
      <el-form
        :model="registerForm"
        :rules="registerRules"
        ref="registerForm"
        label-width="100px"
        label-position="left"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账号" prop="pId">
              <el-input v-model.number="registerForm.pId" placeholder="请输入数字账号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="registerForm.pGender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="密码" prop="pPassword">
          <el-input v-model="registerForm.pPassword" show-password placeholder="4-50位字符"></el-input>
        </el-form-item>

        <el-form-item label="姓名" prop="pName">
          <el-input v-model="registerForm.pName" placeholder="2-8位字符"></el-input>
        </el-form-item>

        <el-form-item label="出生日期" prop="pBirthday">
          <el-date-picker
            v-model="registerForm.pBirthday"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="手机号" prop="pPhone">
          <el-input v-model="registerForm.pPhone" placeholder="11位手机号"></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="pEmail">
          <el-input v-model="registerForm.pEmail" placeholder="请输入有效邮箱"></el-input>
        </el-form-item>

        <el-form-item label="身份证号" prop="pCard">
          <el-input v-model="registerForm.pCard" placeholder="18位身份证号"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="registerFormVisible = false" size="medium">取 消</el-button>
        <el-button
          type="primary"
          @click="registerClick('registerForm')"
          size="medium"
          class="confirm-btn"
        >确 定</el-button>
      </div>
    </el-dialog>

    <!-- 找回密码对话框 -->
    <el-dialog
      title="找回密码"
      :visible.sync="findFormVisible"
      width="450px"
      custom-class="find-password-dialog"
    >
      <el-form :model="findForm" :rules="findRules" ref="findForm">
        <el-form-item label="角色" label-width="80px">
          <el-radio-group v-model="findRole" size="small">
            <el-radio label="患者"></el-radio>
            <el-radio label="医生"></el-radio>
            <el-radio label="管理员"></el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="邮箱" prop="pEmail">
          <el-input v-model="findForm.pEmail" placeholder="请输入注册邮箱"></el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <el-input v-model="findForm.code" placeholder="请输入验证码" class="code-input">
            <el-button
              slot="append"
              @click="sendEmail"
              :disabled="!canClick"
              class="code-btn"
            >{{content}}</el-button>
          </el-input>
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="findForm.newPassword" show-password placeholder="请输入新密码"></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="checkPassword">
          <el-input v-model="findForm.checkPassword" show-password placeholder="请再次输入密码"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="findFormVisible = false" size="medium">取 消</el-button>
        <el-button type="primary" @click="findPassword('findForm')" size="medium">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";
import { toLoad } from "@/utils/initialize.js";

export default {
  name: "Login",
  data() {
    var validateMoblie = (rule, value, callback) => {
      if (value === undefined) {
        callback(new Error("请输入手机号"));
      } else {
        let reg =
          /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
        if (!reg.test(value)) {
          callback(new Error("请输入合法的手机号"));
        }
        callback();
      }
    };
    var validateCard = (rule, value, callback) => {
      if (value === undefined) {
        callback(new Error("请输入身份证号"));
      } else {
        let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if (!reg.test(value)) {
          callback(new Error("请输入合法的身份证号码"));
        }
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.findForm.checkPassword !== "") {
          this.$refs.findForm.validateField("checkPassword");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.findForm.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      //背景图片
      backgroundDiv: {
        backgroundImage: "linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(" + require("../assets/doctor.jpg") + ")",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundPosition: "center"
      },
      loginForm: {
        id: "",
        password: "",
      },
      loginRules: {
        id: [
          {
            required: true,
            message: "请输入账号编号",
            trigger: "blur",
          },
          {
            min: 3,
            max: 50,
            message: "长度在 3到 50 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
        ],
      },
      role: "患者",
      findRole: "患者",
      //找回密码
      findFormVisible: false,
      findForm: {
        code: "",
        newPassword: "",
        checkPassword: "",
        pEmail: "",
      },
      findRules: {
        pEmail: [
          {
            required: true,
            message: "请输入邮箱地址",
            trigger: "blur",
          },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        code: [
          {
            required: true,
            message: "请输入验证码",
            trigger: "blur",
          },
        ],
        newPassword: [{ validator: validatePass, trigger: "blur" }],
        checkPassword: [{ validator: validatePass2, trigger: "blur" }],
      },
      totalTime: 60,
      content: "发送验证码",
      canClick: true,
      //注册
      registerFormVisible: false,
      registerForm: {
        pGender: "男",
      },
      registerRules: {
        pId: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            type: "number",
            message: "账号必须数字类型",
            trigger: "blur",
          },
        ],
        pPassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 4,
            max: 50,
            message: "长度在 4到 50 个字符",
            trigger: "blur",
          },
        ],
        pName: [
          { required: true, message: "请输入姓名", trigger: "blur" },
          {
            min: 2,
            max: 8,
            message: "长度在 2到 8 个字符",
            trigger: "blur",
          },
        ],
        pEmail: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        pPhone: [{ validator: validateMoblie }],
        pCard: [{ validator: validateCard }],
        pBirthday: [
          {
            required: true,
            message: "选择出生日期",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    toLoad()
  },
  methods: {
    //点击注册确认按钮
    registerClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request
            .get("patient/addPatient", {
              params: {
                pId: this.registerForm.pId,
                pName: this.registerForm.pName,
                pPassword: this.registerForm.pPassword,
                pGender: this.registerForm.pGender,
                pEmail: this.registerForm.pEmail,
                pPhone: this.registerForm.pPhone,
                pCard: this.registerForm.pCard,
                pBirthday: this.registerForm.pBirthday,
              },
            })
            .then((res) => {
              if (res.data.status !== 200)
                return this.$message.error(
                  "账号或邮箱已被占用！"
                );
              this.registerFormVisible = false;
              this.$message.success("注册成功！");
              console.log(res);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // 点击找回密码确认按钮
    findPassword(findForm) {
      this.$refs[findForm].validate((valid) => {
        if (valid) {
          //如果是选中患者
          if (this.findRole === "患者") {
            request
              .get("patient/findPassword", {
                params: {
                  pEmail: this.findForm.pEmail,
                  pPassword: this.findForm.newPassword,
                  code: this.findForm.code,
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error(
                    "验证码错误或者已过期！！！"
                  );
                this.$message.success("密码修改成功！！请登录");
                this.findFormVisible = false;
              });
          }
          //如果是选中管理员
          if (this.findRole === "管理员") {
            request
              .get("admin/findPassword", {
                params: {
                  aEmail: this.findForm.pEmail,
                  aPassword: this.findForm.newPassword,
                  code: this.findForm.code,
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error(
                    "验证码错误或者已过期！！！"
                  );
                this.$message.success("密码修改成功！！请登录");
                this.findFormVisible = false;
              });
          }
          //如果是选中患者
          if (this.findRole === "医生") {
            request
              .get("doctor/findPassword", {
                params: {
                  dEmail: this.findForm.pEmail,
                  dPassword: this.findForm.newPassword,
                  code: this.findForm.code,
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error(
                    "验证码错误或者已过期！！！"
                  );
                this.$message.success("密码修改成功！！请登录");
                this.findFormVisible = false;
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //点击发送验证码按钮
    sendEmail() {
      //倒计时
      if (!this.canClick) return; //改动的是这两行代码
      this.canClick = false;
      this.content = this.totalTime + "s后重新发送";
      let clock = window.setInterval(() => {
        this.totalTime--;
        this.content = this.totalTime + "s后重新发送";
        if (this.totalTime < 0) {
          window.clearInterval(clock);
          this.content = "重新发送验证码";
          this.totalTime = 10;
          this.canClick = true; //这里重新开启
        }
      }, 1000);

      //如果是选中患者
      if (this.findRole === "患者") {
        request
          .get("patient/sendEmail", {
            params: {
              pEmail: this.findForm.pEmail,
            },
          })
          .then((res) => {
            console.log(this.findForm.pEmail);
            console.log(res);
            if (res.data.status !== 200)
              return this.$message.error(
                "该邮箱暂未注册！请先注册！"
              );
            this.$message.success("验证码发送成功！");
          });
      }
      //如果是选中管理员
      if (this.findRole === "管理员") {
        request
          .get("admin/sendEmail", {
            params: {
              aEmail: this.findForm.pEmail,
            },
          })
          .then((res) => {
            console.log(this.findForm.pEmail);
            console.log(res);
            if (res.data.status !== 200)
              return this.$message.error(
                "该邮箱暂未注册！请先注册！"
              );
            this.$message.success("验证码发送成功！");
          });
      }
      //如果是选中医生
      if (this.findRole === "医生") {
        request
          .get("doctor/sendEmail", {
            params: {
              dEmail: this.findForm.pEmail,
            },
          })
          .then((res) => {
            console.log(this.findForm.pEmail);
            console.log(res);
            if (res.data.status !== 200)
              return this.$message.error(
                "该邮箱暂未注册！请先注册！"
              );
            this.$message.success("验证码发送成功！");
          });
      }
    },
    //提交表单
    submitLoginForm(formName) {
      if (!/^\d+$/.test(this.loginForm.id)) {
        this.$message.error("用户名有误");
        return;
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.role === "管理员") {
            var params = new URLSearchParams();
            params.append("aId", this.loginForm.id);
            params.append("aPassword", this.loginForm.password);

            request
              .post("admin/login", params)
              .then((res) => {
                console.log(res);
                if (res.data.status != 200)
                  return this.$message.error(
                    "用户名或密码错误"
                  );
                setToken(res.data.data.token);
                this.$router.push("/adminLayout");
              })
              .catch((e) => {
                console.log(e);
                if (
                  e.response == undefined ||
                  e.response.data == undefined
                ) {
                  this.$message({
                    showClose: true,
                    message: e,
                    type: "error",
                    duration: 5000,
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: e.response.data,
                    type: "error",
                    duration: 5000,
                  });
                }
              });
          }
          if (this.role === "医生") {
            var params1 = new URLSearchParams();
            params1.append("dId", this.loginForm.id);
            params1.append("dPassword", this.loginForm.password);

            request
              .post("doctor/login", params1)
              .then((res) => {
                console.log(res);
                if (res.data.status != 200)
                  return this.$message.error(
                    "用户名或密码错误"
                  );
                setToken(res.data.data.token);
                this.$router.push("/doctorLayout");
              })
              .catch((e) => {
                console.log(e);
                if (
                  e.response == undefined ||
                  e.response.data == undefined
                ) {
                  this.$message({
                    showClose: true,
                    message: e,
                    type: "error",
                    duration: 5000,
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: e.response.data,
                    type: "error",
                    duration: 5000,
                  });
                }
              });
          }
          if (this.role === "患者") {
            var params2 = new URLSearchParams();
            params2.append("pId", this.loginForm.id);
            params2.append("pPassword", this.loginForm.password);

            request
              .post("patient/login", params2)
              .then((res) => {
                console.log(res);
                if (res.data.status != 200)
                  return this.$message.error(
                    "用户名或密码错误"
                  );
                setToken(res.data.data.token);
                this.$router.push("/patientLayout");
              })
              .catch((e) => {
                console.log(e);
                if (
                  e.response == undefined ||
                  e.response.data == undefined
                ) {
                  this.$message({
                    showClose: true,
                    message: e,
                    type: "error",
                    duration: 5000,
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: e.response.data,
                    type: "error",
                    duration: 5000,
                  });
                }
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.login-card {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.18);
  animation: fadeIn 0.5s ease-in-out;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;

  h1 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 8px;
    font-weight: 500;
  }

  p {
    font-size: 14px;
    color: #909399;
    margin: 0;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 22px;
  }

  .el-input {
    ::v-deep .el-input__inner {
      height: 44px;
      line-height: 44px;
      padding-left: 40px;
      border-radius: 6px;
    }

    ::v-deep .el-input__prefix {
      left: 10px;
      i {
        color: #c0c4cc;
      }
    }
  }
}

.role-radio {
  ::v-deep(.el-radio-button) {
    margin-right: 40px;  // 调整这个值来控制间距

    &:last-child {
      margin-right: 0;  // 最后一个按钮不需要右边距
    }

    .el-radio-button__inner {
      width: 100%;
      border-radius: 4px;
      padding: 10px 0;
    }
  }
}

.login-buttons {
  margin-top: 30px;

  .el-button {
    width: 100%;
    height: 44px;
    font-size: 16px;
    margin-bottom: 12px;
    border-radius: 6px;
    transition: all 0.3s;
  }

  .login-btn {
    background: linear-gradient(90deg, #409EFF, #66b1ff);
    border: none;

    &:hover {
      opacity: 0.9;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
    }
  }

  .register-btn {
    &:hover {
      color: #409EFF;
      border-color: #c6e2ff;
      background-color: #ecf5ff;
      transform: translateY(-2px);
    }
  }
}

.login-footer {
  text-align: center;
  margin-top: 20px;

  .el-link {
    font-size: 13px;
  }
}

.register-dialog {
  border-radius: 12px;

  .el-dialog__header {
    border-bottom: 1px solid #ebeef5;
    padding: 20px 20px 15px;
  }

  .el-dialog__title {
    font-size: 18px;
    font-weight: 500;
  }

  .el-dialog__body {
    padding: 20px 30px;
  }

  .confirm-btn {
    background: linear-gradient(90deg, #409EFF, #66b1ff);
    border: none;

    &:hover {
      opacity: 0.9;
    }
  }
}

.find-password-dialog {
  border-radius: 12px;

  .code-input {
    ::v-deep .el-input-group__append {
      padding: 0 10px;
      background: #f5f7fa;
      border-left: 0;
    }

    .code-btn {
      border: none;
      background: transparent;
      color: #409EFF;
      padding: 0;

      &:disabled {
        color: #c0c4cc;
      }
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .login-card {
    width: 90%;
    padding: 30px 20px;
  }

  .register-dialog, .find-password-dialog {
    width: 90% !important;
  }
}
</style>