package com.txm.teamprojects.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@TableName("doctor")
@ExcelTarget("doctor")
public class Doctor implements Serializable {
    @JsonProperty("dId") @TableId(value = "d_id") @Excel(name = "账号") private Integer dId;
    @JsonProperty("dPassword") @Excel(name = "密码") private String dPassword;
    @JsonProperty("dName") @Excel(name = "姓名") private String dName;
    @JsonProperty("dGender") @Excel(name = "性别") private String dGender;
    @JsonProperty("dCard") @Excel(name = "身份证号码") private String dCard;
    @JsonProperty("dEmail") @Excel(name = "邮箱") private String dEmail;
    @JsonProperty("dPhone") @Excel(name = "手机号") private String dPhone;
    @JsonProperty("dPost") @Excel(name = "职位") private String dPost;
    @JsonProperty("dIntroduction") @Excel(name = "简介") private String dIntroduction;
    @JsonProperty("dSection") @Excel(name = "科室") private String dSection;
    @JsonProperty("dState") @Excel(name = "是否在职", replace = {"在职_1","离职_0"}) private Integer dState;//必须为integer类型，为int类型的话更新时会自动更新为0
    @JsonProperty("dPrice") @Excel(name = "挂号价格") private Double dPrice;
    @JsonProperty("dPeople") @Excel(name = "评价人数") private Integer dPeople;//必须为integer类型，为int类型的话更新时会自动更新为0
    @JsonProperty("dStar") @Excel(name = "总分") private Double dStar;//必须为integer类型，为int类型的话更新时会自动更新为0
    @JsonProperty("dAvgStar") @Excel(name = "平均分") private Double dAvgStar;//必须为integer类型，为int类型的话更新时会自动更新为0

    /**
     * 是否已排班，排班id
     */
    @TableField(exist = false)
    private String arrangeId;

    public Doctor() {
    }

    public Doctor(Integer dId, String dPassword, String dName, String dGender, String dCard, String dEmail, String dPhone, String dPost, String dIntroduction, String dSection, Integer dState, Double dPrice, Integer dPeople, Double dStar, Double dAvgStar, String arrangeId) {
        this.dId = dId;
        this.dPassword = dPassword;
        this.dName = dName;
        this.dGender = dGender;
        this.dCard = dCard;
        this.dEmail = dEmail;
        this.dPhone = dPhone;
        this.dPost = dPost;
        this.dIntroduction = dIntroduction;
        this.dSection = dSection;
        this.dState = dState;
        this.dPrice = dPrice;
        this.dPeople = dPeople;
        this.dStar = dStar;
        this.dAvgStar = dAvgStar;
        this.arrangeId = arrangeId;
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }

    public String getdPassword() {
        return dPassword;
    }

    public void setdPassword(String dPassword) {
        this.dPassword = dPassword;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdGender() {
        return dGender;
    }

    public void setdGender(String dGender) {
        this.dGender = dGender;
    }

    public String getdCard() {
        return dCard;
    }

    public void setdCard(String dCard) {
        this.dCard = dCard;
    }

    public String getdEmail() {
        return dEmail;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getdPhone() {
        return dPhone;
    }

    public void setdPhone(String dPhone) {
        this.dPhone = dPhone;
    }

    public String getdPost() {
        return dPost;
    }

    public void setdPost(String dPost) {
        this.dPost = dPost;
    }

    public String getdIntroduction() {
        return dIntroduction;
    }

    public void setdIntroduction(String dIntroduction) {
        this.dIntroduction = dIntroduction;
    }

    public String getdSection() {
        return dSection;
    }

    public void setdSection(String dSection) {
        this.dSection = dSection;
    }

    public Integer getdState() {
        return dState;
    }

    public void setdState(Integer dState) {
        this.dState = dState;
    }

    public Double getdPrice() {
        return dPrice;
    }

    public void setdPrice(Double dPrice) {
        this.dPrice = dPrice;
    }

    public Integer getdPeople() {
        return dPeople;
    }

    public void setdPeople(Integer dPeople) {
        this.dPeople = dPeople;
    }

    public Double getdStar() {
        return dStar;
    }

    public void setdStar(Double dStar) {
        this.dStar = dStar;
    }

    public Double getdAvgStar() {
        return dAvgStar;
    }

    public void setdAvgStar(Double dAvgStar) {
        this.dAvgStar = dAvgStar;
    }

    public String getArrangeId() {
        return arrangeId;
    }

    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "dId=" + dId +
                ", dPassword='" + dPassword + '\'' +
                ", dName='" + dName + '\'' +
                ", dGender='" + dGender + '\'' +
                ", dCard='" + dCard + '\'' +
                ", dEmail='" + dEmail + '\'' +
                ", dPhone='" + dPhone + '\'' +
                ", dPost='" + dPost + '\'' +
                ", dIntroduction='" + dIntroduction + '\'' +
                ", dSection='" + dSection + '\'' +
                ", dState=" + dState +
                ", dPrice=" + dPrice +
                ", dPeople=" + dPeople +
                ", dStar=" + dStar +
                ", dAvgStar=" + dAvgStar +
                ", arrangeId='" + arrangeId + '\'' +
                '}';
    }
}
