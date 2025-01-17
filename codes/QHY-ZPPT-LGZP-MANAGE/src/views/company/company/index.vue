<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="企业名称" prop="id">
        <el-select v-model="queryParams.id" placeholder="请选择用户" filterable clearable size="small">
          <el-option
            v-for="company in companyOptions"
            :key="company.id"
            :label="company.trueName"
            :value="company.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户账号" prop="id">
        <el-input v-model="queryParams.phonenumber" clearable  placeholder="请输入用户账号" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['company:company:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['company:company:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['company:company:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['company:company:export']"
        >导出</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleStartOpen"
        >一键开启缴纳开户费</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleStopOpen"
        >一键关闭缴纳开户费</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="companyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="用户名称" align="center" prop="shCompanyUser.trueName"/>
      <el-table-column label="用户账号" align="center" prop="shAppUser.phonenumber"/>
      <el-table-column label="用户头像" align="center" prop="shAppUser.avatar">
        <template slot-scope="scope">
          <img :src="scope.row.shAppUser.avatar" style="max-width:55px;max-height: 55px"
               @click="showDetail(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="用户账号状态" align="center" prop="shAppUser.status" >
        <template slot-scope="scope">
          {{scope.row.shAppUser.status==='0'?'正常':'停用'}}
        </template>
      </el-table-column>
      <el-table-column label="续费到期时间" align="center" prop="shCompanyUser.childvalidity" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shCompanyUser.childvalidity, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="企业名称" align="center" prop="trueName" />
      <el-table-column label="是否开启缴纳开户费" align="center" prop="isOpen" width="180" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isOpen"
            active-value="0"
            inactive-value="1"
            @change="handleIsOpenStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>

      <el-table-column label="营业执照" align="center" prop="businessPermit" >
        <template slot-scope="scope">
          <img :src="scope.row.businessPermit" style="max-width:55px;max-height: 55px"  @click="showDetail(scope.row)" />
        </template>
      </el-table-column>
<!--      <el-table-column label="粉丝" align="center" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-folder"
            @click="seeFans(scope.row)"
          >
            查看粉丝
          </el-button>
        </template>
      </el-table-column>-->
      <el-table-column label="审核状态" align="center" prop="shAppUser.status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.auth_status" :value="scope.row.shAppUser.authStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-folder"
            @click="seeMain(scope.row)"
          >子账号管理</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['company:company:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['company:company:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-folder"
            @click="handleOpen(scope.row)"
          >续费</el-button>
          <el-button v-if="scope.row.shAppUser.authStatus!=='0'"
                     size="mini"
                     type="text"
                     icon="el-icon-edit"
                     @click="handleAuthStart(scope.row)"
          >审核
          </el-button>
          <el-button v-if="scope.row.shAppUser.authStatus!=='0'"
                     size="mini"
                     type="text"
                     icon="el-icon-edit"
                     @click="handleAuthStop(scope.row)"
          >拒审
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改企业对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="企业名称" prop="trueName">
          <el-input v-model="form.trueName" placeholder="请输入企业名称" />
        </el-form-item>

        <el-form-item label="公司人数" prop="scaleId">
          <el-select v-model="form.scaleId" placeholder="请选择公司人数" filterable clearable style="width:580px">
            <el-option
              v-for="scale in scaleOptions"
              :key="scale.id"
              :label="scale.name"
              :value="scale.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="公司性质" prop="financeLevelId">
          <el-select v-model="form.financeLevelId" placeholder="请选择公司性质" filterable clearable style="width:580px">
            <el-option
              v-for="financeLevel in financeLevelOptions"
              :key="financeLevel.id"
              :label="financeLevel.name"
              :value="financeLevel.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属行业" prop="branchId">
          <treeselect v-model="form.branchId" :options="branchOptions" :show-count="true" placeholder="选择行业"/>
        </el-form-item>
        <el-form-item label="所属地区" prop="areaId">
          <treeselect v-model="form.areaId" :options="areaOptions" :show-count="true" placeholder="选择地区"/>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <template >
            <amap :default-value="aMapDefaultValue" @getPosition="getPosition" ref="child"/>
          </template>
        </el-form-item>
        <el-form-item label="公司福利" prop="welfareIds">
          <el-select v-model="form.welfareIds" placeholder="请选择拥有技能" multiple style="width:580px">
            <el-option
              v-for="welfare in welfareOptions"
              :key="welfare.id"
              :label="welfare.name"
              :value="welfare.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="公司介绍" prop="introduction" >
          <el-input  type="textarea" v-model="form.introduction" placeholder="请输入公司介绍" />
        </el-form-item>
        <el-form-item label="是否开启缴纳开户费" prop="isOpen" >
          <el-select v-model="form.isOpen" placeholder="请选择是否开启缴纳开户费" filterable clearable size="small">
            <el-option label="是" value="0"/>
            <el-option label="否" value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item label="公司logo" prop="logoUrl">
          <el-upload
            class="avatar-uploader"
            :action="uploadFileUrl"
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :show-file-list="false"
            :headers="headers"
            :on-error="handleUploadError"
            :on-success="handleLogoUrlSuccess"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload">
            <img v-if="form.logoUrl" :src="form.logoUrl" class="avatar" >
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="公司环境" prop="enwireUrls">
          <el-upload
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :action="uploadFileUrl"
            :headers="headers"
            :file-list= "enwireList"
            :on-success="handleEnwireUrlsSuccess"
            list-type="picture-card"
            :limit="limit"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
        <el-form-item label="营业执照" prop="businessPermit">
          <el-upload
            class="avatar-uploader"
            :action="uploadFileUrl"
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :show-file-list="false"
            :headers="headers"
            :on-error="handleUploadError"
            :on-success="handleBusinessPermitSuccess"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload">
            <img v-if="form.businessPermit" :src="form.businessPermit" class="avatar" >
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="增值电信业务许可证" prop="telecomUrl">
          <el-upload
            class="avatar-uploader"
            :action="uploadFileUrl"
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :show-file-list="false"
            :headers="headers"
            :on-error="handleUploadError"
            :on-success="handleTelecomUrlSuccess"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload">
            <img v-if="form.telecomUrl" :src="form.telecomUrl" class="avatar" >
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
       <el-form-item label="主账号姓名" prop="shCompanyUser.trueName">
        <el-input v-model="form.shCompanyUser.trueName" placeholder="请输入用户名称"/>
      </el-form-item>
        <el-form-item label="性别" prop="shAppUser.sex">
          <el-select v-model="form.shAppUser.sex" placeholder="请选择性别">
            <el-option label="男" value="0"/>
            <el-option label="女" value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item label="主账号电话" prop="shAppUser.phonenumber">
          <el-input v-model="form.shAppUser.phonenumber" placeholder="请输入主账号电话"/>
        </el-form-item>
        <el-form-item label="身份证号" prop="shCompanyUser.idcard">
          <el-input v-model="form.shCompanyUser.idcard" placeholder="请输入身份证号"/>
        </el-form-item>
       <el-form-item label="微信号" prop="shAppUser.wxNumber">
          <el-input v-model="form.shAppUser.wxNumber" placeholder="请输入微信号"/>
        </el-form-item>
        <el-form-item label="账号状态" prop="shAppUser.status">
          <el-select v-model="form.shAppUser.status" placeholder="请选择账号状态">
            <el-option label="正常" value="0"/>
            <el-option label="停用" value="1"/>
          </el-select>
        </el-form-item>
       <el-form-item label="头像" prop="avatar">
          <el-upload
            class="avatar-uploader"
            :action="uploadFileUrl"
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :show-file-list="false"
            :headers="headers"
            :on-error="handleUploadError"
            :on-success="handleAvatarSuccess"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload">
            <img v-if="avatar" :src="avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 续费对话框 -->
    <el-dialog :title="xufeiTitle" :visible.sync="xufeiOpen" width="500px" append-to-body>
      <el-form ref="xufeiForm" :model="xufeiForm" :rules="rules" label-width="80px">
        <el-form-item label="" prop="payType">
          <el-select v-model="xufeiForm.payType" placeholder="请选择续费时长" filterable clearable>
            <el-option label="一个月" value="1"/>
            <el-option label="一个季度" value="2"/>
            <el-option label="一年" value="3"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitXufeiForm">确 定</el-button>
        <el-button @click="cancelXufei">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 拒绝审核原因对话框 -->
    <el-dialog :title="title" :visible.sync="remarkOpen" width="500px" append-to-body>
      <el-form ref="remarkForm" :model="remarkForm" :rules="rules" label-width="80px">
        <el-form-item label="原因" prop="remark">
          <el-input v-model="remarkForm.remark" placeholder="请输入拒绝原因"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRemarkForm">确 定</el-button>
        <el-button @click="cancelRemark">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 显示粉丝 -->
    <el-dialog :title="fansTitle" :visible.sync="fansOpen" width="1000px" append-to-body>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAddFans"
          >新增
          </el-button>
        </el-col>
      </el-row>
      <div>
        <el-table :data="fansList" style="margin-top: 10px">
          <el-table-column label="id" align="center" prop="id"/>
          <el-table-column label="粉丝" align="center" prop="shAppUser.phonenumber" width="180">
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDeleteFans(scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <pagination
        v-show="fansTotal>0"
        :total="fansTotal"
        :page.sync="queryParamsFans.pageNum"
        :limit.sync="queryParamsFans.pageSize"
        @pagination="seeFans"
      />
    </el-dialog>

    <!-- 添加粉丝对话框 -->
    <el-dialog :title="titleFans" :visible.sync="openFans" width="500px" append-to-body>
      <el-form ref="fansForm" :model="fansForm" :rules="rules" label-width="80px">
        <el-form-item label="手机号" prop="phonenumber">
          <el-select v-model="fansForm.fansUserId" placeholder="请选择用户" filterable clearable>
            <el-option
              v-for="user in userOptions"
              :key="user.id"
              :label="user.phonenumber"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFansForm">确 定</el-button>
        <el-button @click="cancelFans">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addCompany, changeIsOpenById,
  delCompany,
  exportCompany,
  getCompany,
  listCompany,
  startCompany, startOpenCompany,
  stopCompany, stopOpenCompany,
  updateCompany
} from "@/api/company/company";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {listUser} from "@/api/job/user";
import {getToken} from "@/utils/auth";
import Amap from '@/components/Amap'
import {listFinanceLevel} from "@/api/financeLevel/financeLevel";
import {listCompnayscale} from "@/api/compnayscale/compnayscale";
import {treeselectBranch} from "@/api/branch/branch";
import {treeselectArea} from "@/api/area/area";
import {listWelfare} from "@/api/welfare/welfare";
import {changeRecommendById} from "@/api/recruit/recruit";
import {createOrderNew} from "@/api/trrecord/trrecord";
import {authStartCompanyuser, authStopCompanyuser, getCompanyuser} from "@/api/companyuser/companyuser";
import {addCompanyfans, delCompanyfans, listCompanyfans} from "@/api/companyfans/companyfans";

export default {
  name: "Company",
  dicts: ['account_status','auth_status'],
  components: {
    Amap,Treeselect
  },
  data() {
    return {
      companyId:null,
      // 是否显示弹出层
      openFans: false,
      titleFans: "",
      // 查询参数
      queryParamsFans: {
        pageSize: 10,
        pageNum: 1,
        delFlag: "0",
        companyId: null,
        fansUserId: null
      },
      // 总条数
      fansTotal: 0,
      fansForm: {},
      //粉丝list
      fansList: [],
      //粉丝标题
      fansTitle: "",
      //粉丝标题
      fansOpen: false,
      //头像
      avatar: '',
      remarkOpen: false,
      xufeiForm: {},
      //续费标题
      xufeiTitle: "",
      xufeiOpen: false,
      //地图的默认参数
      aMapDefaultValue:{},
      // 表单参数
      remarkForm: {},
      enwireList:[],
      limit:3,
      dialogImageUrl: '',
      dialogVisible: false,
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/appreq/oss/uploadBigFile", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 企业表格数据
      companyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        orderByColumn:'m.create_time',
        isAsc:'desc',
        delFlag:'0',
        pageNum: 1,
        pageSize: 10,
        userId: null,
        lat: null,
        lng: null,
        type: 2,
        isOpen: null,
        validity: null,
        trueName:null
      },
      // 表单参数
      form: {
        shCompanyUser:{},
        shAppUser:{},
        shBranch:{},
        shCompanyImage:{},
        shCompanyWelfare:{},
        shArea: {},
        shCompanyScale:{},
        shFinanceLevel:{},
        openAccount:{

        },
        usageFee:{

        },
        type:2
      },
      //用户基本信息
      userOptions:[],
      //企业基本信息
      companyOptions:[],
      scaleOptions: [],
      financeLevelOptions: [],
      // 树选项
      branchOptions: [],
      welfareOptions:[],
      areaOptions: [],
      //企业类型字典
      companyTypeOptions:[],
      // 表单校验
      rules: {
        trueName: [
          { required: true, message: "企业名称不能为空", trigger: "blur" }
        ],
        scaleId: [
          { required: true, message: "公司人数不能为空", trigger: "blur" }
        ],
        financeLevelId: [
          { required: true, message: "公司性质不能为空", trigger: "blur" }
        ],
        branchId: [
          { required: true, message: "所属行业不能为空", trigger: "blur" }
        ],
        areaId: [
          { required: true, message: "所属地区不能为空", trigger: "blur" }
        ],
        welfareIds: [
          { required: true, message: "公司福利不能为空", trigger: "blur" }
        ],
        introduction: [
          { required: true, message: "公司介绍不能为空", trigger: "blur" }
        ],
        logoUrl: [
          { required: true, message: "公司logo不能为空", trigger: "blur" }
        ],
        businessPermit: [
          { required: true, message: "营业执照不能为空", trigger: "blur" }
        ],
        isOpen: [
          { required: true, message: "是否开户不能为空", trigger: "blur" }
        ],
        "shCompanyUser.trueName": [
          { required: true, message: "主账号姓名不能为空", trigger: "blur" }
        ],
        "shAppUser.phonenumber": [
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, required: true, message: "请输入正确的手机号码", trigger: "blur"}
        ],
        "shCompanyUser.idcard": [
          {pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,required: true, message: "请输入正确的身份证号", trigger: "blur"}
        ],
        'shAppUser.status': [
          { required: true, message: "账号状态不能为空", trigger: "blur" }
        ],
        payType: [
          { required: true, message: "续费时长不能为空", trigger: "blur" }
        ]



      }
    };
  },
  created() {
    this.getDicts("sh_company_type").then(response => {
      this.companyTypeOptions = response.data;
    });
    this.getList();
    this.getUserList();
    this.getCompanyList();
    this.getScaleList();
    this.getFinanceList();
    this.getSkillTypeList();
  },
  methods: {
    // 取消按钮
    cancelFans() {
      this.openFans = false;
      this.resetFans();
    },
    // 表单重置
    resetFans() {
      this.fansForm = {
        id: null,
        companyId: null,
        fansUserId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("fansForm");
    },
    /** 提交按钮 */
    submitFansForm() {
      this.$refs["fansForm"].validate(valid => {
        if (valid) {
          this.fansForm.companyId = this.companyId
          addCompanyfans(this.fansForm).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.openFans = false;
            listCompanyfans(this.queryParamsFans).then(response => {
              this.fansList = response.rows;
              this.fansTotal = response.total;
            });
          });
        }
      });
    },
    /** 新增按钮操作 */
    handleAddFans() {
      this.resetFans();
      this.openFans = true;
      this.titleFans = "添加粉丝";
    },
    /** 删除fans按钮操作 */
    handleDeleteFans(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除粉丝编号为"' + ids + '"的数据项？').then(function () {
        return delCompanyfans(ids);
      }).then(() => {
        listCompanyfans(this.queryParamsFans).then(response => {
          this.fansList = response.rows;
          this.fansTotal = response.total;
        });
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    //打开粉丝
    seeFans(val) {
      this.getUserList()
      this.queryParamsFans.companyId = val.id
      this.companyId = val.id
      listCompanyfans(this.queryParamsFans).then(response => {
        this.fansList = response.rows;
        this.fansTotal = response.total;
        this.fansTitle = "粉丝";
        this.fansOpen = true;
      });
    },
    // 图片上传成功回调
    handleAvatarSuccess(res, file) {
      this.$message.success("上传成功");
      this.avatar = res.url;
      this.form.shAppUser.avatar = res.url;
    },
    submitRemarkForm() {
      this.$refs["remarkForm"].validate(valid => {
        if (valid) {
          authStopCompanyuser(this.remarkForm).then(response => {
            this.$modal.msgSuccess("审核未通过");
            this.remarkOpen = false;
            this.getList();
          });
        }
      });
    },
    // 取消按钮
    cancelRemark() {
      this.open = false;
      this.remarkOpen = false;
      this.reset();
    },
    // 表单重置
    resetRemark() {
      this.remarkForm = {
        id: null,
        userId: null,
        remark: null
      }
      this.resetForm("form");
    },
    /** 审核按钮操作 */
    handleAuthStart(row) {
      if(row.shAppUser != null){
        if(row.shAppUser.authStatus != null){
          if(row.shAppUser.authStatus === "3"){
            this.$modal.msgError("该企业未完善信息，不能进行审核");
            return;
          }
        }
      }
      const ids = row.shCompanyUser.id
      this.$modal.confirm('是否确认通过企业用户编号为"' + ids + '"的数据项的审核？').then(function () {
        return authStartCompanyuser(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("审核成功");
      }).catch(() => {
      });
    },
    /** 审核按钮操作 */
    handleAuthStop(row) {
      if(row.shAppUser != null){
        if(row.shAppUser.authStatus != null){
          if(row.shAppUser.authStatus === "3"){
            this.$modal.msgError("该企业未完善信息，不能进行审核");
            return;
          }
        }
      }
      this.resetRemark();
      this.remarkForm.id = row.shCompanyUser.id
      this.remarkForm.userId = row.shAppUser.id;
      const ids = row.shCompanyUser.id
      getCompanyuser(ids).then(response => {
        this.remarkOpen = true;
        this.title = "拒审原因";
        this.remarkForm.remark = response.data.remark;
      });

    },
    //打开续费
    handleOpen(val) {
      this.resetXufei()
      this.xufeiTitle = "续费";
      this.xufeiOpen = true;
      this.xufeiForm.userId=val.shAppUser.id
    },
    // 取消按钮
    cancelXufei() {
      this.xufeiOpen = false;
      this.resetXufei();
    },
    // 表单重置
    resetXufei() {
      this.xufeiForm = {
        id: null,
        userId: null,
        payType: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("xufeiForm");
    },
    /** 提交按钮 */
    submitXufeiForm() {
      this.$refs["xufeiForm"].validate(valid => {
        if (valid) {
          createOrderNew(this.xufeiForm).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.xufeiOpen = false;
            this.getList()
          });
        }
      });
    },
    //打开子账号
    seeMain(row) {
      const companyId = row.id;
      this.$router.push("/job/qiye/companyUser/" + companyId);
    },
    handleIsOpenStatusChange(row){
      changeIsOpenById(row.id).then(response => {
        this.getList();
      });
    },
    handlePictureCardPreview(file){
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file, fileList) {
      this.form.enwireUrls.map((val,i)=>{
        if(val===file.url){
          this.form.enwireUrls.splice(i,1)
        }
      })
    },
    //跳转
    seeShare(row){

      let routeUrl = this.$router.resolve({
        path: "/companyuser",
        query: {companyId:row.id}
      });
      window.open(routeUrl.href, '_blank');
    },
    getSkillTypeList() {
      let params = {
        delFlag:0
      }
      listWelfare(params).then(response => {
        this.welfareOptions = response.rows;
      });
    },
    /** 查询下拉树结构 */
    getTreeselectBranch() {
      treeselectBranch().then(response => {
        this.branchOptions = response.data;
      });
    },
    /** 查询下拉树结构 */
    getTreeselectArea() {
      treeselectArea().then(response => {
        this.areaOptions = response.data;
      });
    },
    //获取企业规模
    getScaleList(){
      let q = {
        delFlag:0
      };
      listCompnayscale(q).then(response => {
        this.scaleOptions = response.rows;
      });
    },
    //获取融资阶段
    getFinanceList() {
      let q = {
        delFlag:0
      };
      listFinanceLevel(q).then(response => {
        this.financeLevelOptions = response.rows;
      });
    },
    // 获取地址信息
    getPosition({ address, lat, lng }) {
      this.form.address = address;
      this.form.lng = lng;
      this.form.lat = lat;
    },
    /** 查询企业基本信息 */
    getCompanyList() {
      let q = {
        delFlag:0
      };
      listCompany(q).then(response => {
        this.companyOptions = response.rows;
      });
    },
    /** 查询用户基本信息 */
    getUserList() {
      let q = {
        delFlag:0
      };
      listUser(q).then(response => {
        this.userOptions = response.rows;
      });
    },
    /** 查询企业列表 */
    getList() {
      this.loading = true;
      listCompany(this.queryParams).then(response => {
        this.companyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        shCompanyUser:{},
        shAppUser: {},
        id: null,
        userId: null,
        branchId: null,
        areaId: null,
        lat: null,
        lng: null,
        type: null,
        isOpen: null,
        businessPermit: null,
        validity: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        openAccount: {},
        usageFee: {},
        shCompanyScale:{},
        shFinanceLevel:{},
        shBranch:{},
        shCompanyWelfare:{},
        shArea: {},
        logoUrl:null,
        enwireUrls:[],
        telecomUrl:null,
        welfareIds:[]
      };
      this.resetForm("form");
    },
    format(value){
      if(value == "0"){
        return "正常";
      }else{
        return "停用";
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.phonenumber = null;
      this.queryParams.id = null;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加企业";
      this.getTreeselectBranch();
      this.getTreeselectArea();
      this.avatar=null
      this.enwireList=[]
      this.aMapDefaultValue = {};
      this.$nextTick(() => {
        this.$refs.child.parentMsg("0");
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCompany(id).then(response => {
        this.avatar = response.data.shAppUser.avatar;
        this.form = response.data;
        this.enwireList=[]
        var index=0
        for (const e of response.data.enwireUrls) {
          var en={

          };
          en.url=e
          en.uid=index
          this.enwireList.push(en)
          index++
        }
        if(this.form.address != null){
          this.aMapDefaultValue.default = this.form.address;
        }
        if(this.form.lat != null){
          this.aMapDefaultValue.lat = this.form.lat;
        }
        if(this.form.lng != null){
          this.aMapDefaultValue.lng = this.form.lng;
        }
        if(this.form.lat == null){
          this.aMapDefaultValue = {};
          this.$nextTick(() => {
            this.$refs.child.parentMsg("0");
          })
        }else {
          this.$nextTick(() => {
            this.$refs.child.parentMsg("1");
          })
        }
        this.open = true;
        this.title = "修改企业";
      });
      this.getTreeselectBranch();
      this.getTreeselectArea();
    },

    /** 提交按钮 */
    submitForm() {
      let that = this;
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            if(this.form.shAppUser != null){
              if(this.form.shAppUser.wxNumber != null && this.form.shAppUser.wxNumber != ""){
                var wxPattern = /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$/;
                var bool = wxPattern.test(this.form.shAppUser.wxNumber);
                console.error(bool);
                if(!bool){
                  this.$modal.msgError("请输入正确的微信号");
                  return;
                }
              }
            }
            updateCompany(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.type=2;
            this.form.shCompanyUser.isMain="0"
            if(this.form.shAppUser != null){
              if(this.form.shAppUser.wxNumber != null  && this.form.shAppUser.wxNumber != ""){
                var wxPattern = /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$/;
                var bool = wxPattern.test(this.form.shAppUser.wxNumber);
                console.error(bool);
                if(!bool){
                  this.$modal.msgError("请输入正确的微信号");
                  return;
                }
              }
            }
            addCompany(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleStartOpen() {
      this.$modal.confirm('是否确认一键开启所有普通企业缴纳开户费？').then(function() {
        return startOpenCompany(2);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("开启成功");
      }).catch(() => {});
    },
    handleStopOpen() {
      this.$modal.confirm('是否确认一键关闭所有普通企业缴纳开户费？？').then(function() {
        return stopOpenCompany(2);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("关闭成功");
      }).catch(() => {});
    },
    /** 启用按钮操作 */
    handleStart(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认启用企业编号为"' + ids + '"的数据项？').then(function() {
        return startCompany(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("启用成功");
      }).catch(() => {});
    },
    /** 停用按钮操作 */
    handleStop(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认停用企业编号为"' + ids + '"的数据项？').then(function() {
        return stopCompany(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("停用成功");
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除企业编号为"' + ids + '"的数据项？').then(function() {
        return delCompany(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有企业数据项？').then(() => {
        this.exportLoading = true;
        return exportCompany(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    // 图片上传失败
    handleUploadError(err) {
      this.$message.error("上传失败, 请重试");
    },
    // 图片文件个数超出
    handleExceeds() {
      this.$message.error(`最多上传3个文件`);
    },
    // 营业执照上传成功回调
    handleBusinessPermitSuccess(res, file) {
      this.$message.success("上传成功");
      this.form.businessPermit = res.url;
    },
    // 营业执照上传成功回调
    handleLogoUrlSuccess(res, file) {
      this.$message.success("上传成功");
      this.form.logoUrl = res.url;
    },
    // 营业执照上传成功回调
    handleEnwireUrlsSuccess(res, file,fileList) {
      this.$message.success("上传成功");
      if(this.form.enwireUrls.indexOf(res.url)<0){
        this.form.enwireUrls.push(res.url)
      }
    },
    // 营业执照上传成功回调
    handleTelecomUrlSuccess(res, file) {
      this.$message.success("上传成功");
      this.form.telecomUrl = res.url;
    },
    // 图片上传前校检格式和大小
    beforeUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    // 图片文件个数超出
    handleExceed() {
      this.$message.error(`只允许上传单个文件`);
    },

  }
};
</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.list-avatar{
  width: 178px;
  height: 178px;
  display: block;
  float: left;
  margin-right: 10px;
}
</style>
