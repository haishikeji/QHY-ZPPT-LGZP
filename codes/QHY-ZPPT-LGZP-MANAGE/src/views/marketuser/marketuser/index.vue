<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="trueName">
        <el-input
          v-model="queryParams.trueName"
          placeholder="请输入姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable size="small">
          <el-option
            v-for="dict in sexOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
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
          v-hasPermi="['marketuser:marketuser:add']"
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
          v-hasPermi="['marketuser:marketuser:edit']"
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
          v-hasPermi="['marketuser:marketuser:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['marketuser:marketuser:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="marketuserList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="手机号" align="center" prop="appUser.phonenumber" />
      <el-table-column label="姓名" align="center" prop="trueName" />
      <el-table-column label="账号状态" align="center" prop="appUser.status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.account_status" :value="scope.row.appUser.status"/>
        </template>
      </el-table-column>
      <el-table-column label="性别" align="center" prop="sex" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.appUser.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="头像" align="center" prop="appUser.avatar" width="180" >
        <template slot-scope="scope">
          <img :src="scope.row.appUser.avatar" style="max-width:55px;max-height: 55px"  />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['marketuser:marketuser:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['marketuser:marketuser:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleCompany(scope.row)"
            v-hasPermi="['marketuser:marketuser:edit']"
          >企业用户</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleJob(scope.row)"
            v-hasPermi="['marketuser:marketuser:edit']"
          >个人用户</el-button>
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

    <!-- 添加或修改营销用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="手机号" prop="appUser.phonenumber">
          <el-input v-model="form.appUser.phonenumber" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="密码" prop="appUser.password">
          <el-input v-model="form.appUser.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="姓名" prop="trueName">
          <el-input v-model="form.trueName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="appUser.sex">
          <el-select v-model="form.appUser.sex" placeholder="请选择性别">
            <el-option
              v-for="dict in sexOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="账号状态" prop="appUser.sex">
          <el-select v-model="form.appUser.status" placeholder="请选择账号状态">
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像" prop="appUser.avatar">
          <el-upload
            class="avatar-uploader"
            :action="uploadFileUrl"
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :show-file-list="false"
            :headers="headers"
            :on-error="handleUploadError"
            :on-success="handleAvatarSuccess"
            :on-exceed="handleExceed"
            :before-upload="beforeAvatarUpload">
            <img v-if="avatar" :src="avatar" class="avatar" >
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 此营销用户发展的企业list -->
    <el-dialog :title="companyListTitle" :visible.sync="companyListOpen" width="1500px" append-to-body>
      <el-button type="primary"  plain size="mini" style="float:right;margin-right: 65px;margin-bottom: 10px" icon="el-icon-plus" @click="handleCompanyAdd">新增</el-button>
      <el-table v-loading="loading" :data="companyList">
        <el-table-column label="id" align="center" prop="id" />
        <el-table-column label="企业名称" align="center" prop="trueName" />
        <el-table-column label="类型" align="center" prop="type" >
          <template slot-scope="scope">
           {{scope.type == 1 ? '人资企业' : '普通企业'}}
          </template>
        </el-table-column>
        <el-table-column label="是否开户" align="center" prop="isOpen" >
          <template slot-scope="scope">
            {{scope.isOpen == 1 ? '是' : '否'}}
          </template>
        </el-table-column>
        <el-table-column label="续费到期时间" align="center" prop="validity" width="180" />
        <el-table-column label="营业执照" align="center" prop="businessPermit" width="180" >
          <template slot-scope="scope">
            <img :src="scope.row.businessPermit" style="max-width:55px;max-height: 55px"  />
          </template>
        </el-table-column>
        <el-table-column label="人力资源许可证" align="center" prop="humanResourcePermit" width="180" >
          <template slot-scope="scope">
            <img :src="scope.row.humanResourcePermit" style="max-width:55px;max-height: 55px"  />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">

            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleCompanyDelete(scope.row)"
              v-hasPermi="['marketuser:marketuser:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="companyListCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 此营销用户发展的个人list -->
    <el-dialog :title="jobListTitle" :visible.sync="jobListOpen" width="1500px" append-to-body>
      <el-button type="primary"  plain size="mini" style="float:right;margin-right: 65px;margin-bottom: 10px" icon="el-icon-plus" @click="handleJobAdd">新增</el-button>
      <el-table v-loading="loading" :data="jobList">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="appUser.id" />
        <el-table-column label="手机号码" align="center" prop="appUser.phonenumber" />
        <el-table-column label="昵称" align="center" prop="appUser.nickName" />
        <el-table-column label="真实姓名" align="center" prop="trueName" />
        <el-table-column label="用户邮箱" align="center" prop="appUser.email" />
        <el-table-column label="帐号状态" align="center" prop="appUser.status" >
          <template slot-scope="scope">
            <dict-tag :options="dict.type.account_status" :value="scope.row.appUser.status"/>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="appUser.createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.appUser.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="头像" align="center" prop="appUser.avatar" width="180" >
          <template slot-scope="scope">
            <img :src="scope.row.appUser.avatar" style="max-width:55px;max-height: 55px"  />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">

            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleJobDelete(scope.row)"
              v-hasPermi="['job:user:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="jobListCancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 添加企业 -->
    <el-dialog :title="addCompanyTitle" :visible.sync="addCompanyOpen" width="500px" append-to-body>
      <el-form ref="addCompanyForm" :model="addCompanyForm" :rules="addCompanyRules" label-width="80px">
        <el-form-item label="企业名称" prop="companyId">
        <el-select v-model="addCompanyForm.companyId" placeholder="请选择企业" filterable clearable size="small">
            <el-option
              v-for="company in companyOptions"
              :key="company.id"
              :label="company.trueName"
              :value="company.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addCompanySubmitForm">确 定</el-button>
        <el-button @click="addCompanyCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加个人 -->
    <el-dialog :title="addJobTitle" :visible.sync="addJobOpen" width="500px" append-to-body>
      <el-form ref="addJobForm" :model="addJobForm" :rules="addJobRules" label-width="80px">
        <el-form-item label="求职者" prop="jobId">
          <el-select v-model="addJobForm.jobId" placeholder="请选择求职者" filterable clearable size="small">
            <el-option
              v-for="company in jobOptions"
              :key="company.id"
              :label="company.trueName"
              :value="company.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addJobSubmitForm">确 定</el-button>
        <el-button @click="addJobCancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import { listMarketuser, getMarketuser, delMarketuser, addMarketuser, updateMarketuser, exportMarketuser,delCompany,delUser,addCompany,addUser } from "@/api/marketuser/marketuser";
import {listCompany} from "@/api/company/company";
import {getToken} from "@/utils/auth";
import {listSeekuser} from "@/api/seekuser/seekuser";
export default {
  name: "Marketuser",
  dicts: ['sys_user_sex','account_status'],
  data() {
    return {
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
      // 营销用户表格数据
      marketuserList: [],
      //性别集合
      sexOptions:[],
      //账号状态
      statusOptions:[],
      //头像
      avatar: '',
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //发展的企业弹出层list
      companyList:[],
      //发展的企业弹出层标题
      companyListTitle:null,
      //发展的企业弹出层
      companyListOpen:null,
      //发展的个人list
      jobList:[],
      //发展的个人弹出层标题
      jobListTitle:null,
      //发展的个人弹出层
      jobListOpen:false,
      //当前选中的营销用户
      marketId:null,
      //添加企业标题
      addCompanyTitle:null,
      //添加企业弹出
      addCompanyOpen:false,
      //添加个人标题
      addJobTitle:null,
      //添加个人弹出
      addJobOpen:null,
      //企业基本信息选项
      companyOptions:[],
      //个人求职者信息
      jobOptions:[],
      //添加企业表单
      addCompanyForm:{},
      //添加求职者表达那
      addJobForm:{},
      // 查询参数
      queryParams: {
        orderByColumn:'m.id',
        isAsc:'desc',
        pageNum: 1,
        pageSize: 10,
        userId: null,
        sex: null,
        //姓名
        trueName: null,
        //手机号
        phonenumber : null,
        //账号状态
        status:null
      },
      // 表单参数
      form: {
        appUser:{

        }
      },
      // 表单校验
      rules: {
        'appUser.phonenumber': [
          {   pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,required: true, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        'appUser.status': [
          { required: true, message: "账号状态不能为空", trigger: "blur" }
        ],
        trueName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        'appUser.sex': [
          { required: true, message: "性别不能为空", trigger: "blur" }
        ]
      },
      //添加企业表单校验
      addCompanyRules:{
        companyId: [
          { required: true, message: "企业不能为空", trigger: "blur" }
        ],
      },
      //添加个人表单校验
      addJobRules:{
        jobId: [
          { required: true, message: "求职者不能为空", trigger: "blur" }
        ],
      },
    };
  },
  created() {
    this.getList();
    //获取性别类型
    this.getDicts("sys_user_sex").then(response => {
      this.sexOptions = response.data;
    });
    //获取账号状态
    this.getDicts("account_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getCompanyList();
    this.getJobList();
  },
  methods: {
    /** 查询营销用户列表 */
    getList() {
      this.loading = true;
      listMarketuser(this.queryParams).then(response => {
        this.marketuserList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    //点击企业取消
    companyListCancel(){
      this.companyListOpen = false;
      this.companyList = [];
      this.marketId = null;
    },
    //点击个人取消
    jobListCancel(){
      this.jobListOpen = false;
      this.jobList = [];
      this.marketId = null;
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        sex: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        appUser: {}
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
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
      this.avatar = null;
      this.open = true;
      this.title = "添加营销用户";
    },

    /** 查询企业基本信息 */
    getCompanyList() {
      let q = {
        del: 0,
        orderByColumn:'m.id',
        isAsc:'desc'
      };
      listCompany(q).then(response => {
        this.companyOptions = response.rows;
      });
    },

    /** 查询求职者基本信息 */
    getJobList() {
      let q = {
        delFlag:0,
        orderByColumn:'m.id',
        isAsc:'desc'
      };
      listSeekuser(q).then(response => {
        this.jobOptions = response.rows;
      });
    },


    /** 点击企业用户*/
    handleCompany(row){
      this.marketId = row.id;
      this.companyList = [];
      getMarketuser(row.id).then(response => {
        this.companyList = response.data.companyList;
        this.companyListOpen = true;
        this.companyListTitle = "企业用户";
      });
    },

    /** 点击个人用户*/
    handleJob(row){
      this.marketId = row.id;
      this.jobList = [];
      getMarketuser(row.id).then(response => {
        this.jobList = response.data.seekUserList;
        this.jobListOpen = true;
        this.jobListTitle = "个人用户";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMarketuser(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.avatar = response.data.appUser.avatar;
        this.title = "修改营销用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.appUser.avatar = this.avatar;
            updateMarketuser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appUser.avatar = this.avatar;
            addMarketuser(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除营销用户编号为"' + ids + '"的数据项？').then(function() {
        return delMarketuser(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有营销用户数据项？').then(() => {
        this.exportLoading = true;
        return exportMarketuser(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    // 图片上传失败
    handleUploadError(err) {
      this.$message.error("上传失败, 请重试");
    },
    // 图片上传成功回调
    handleAvatarSuccess(res, file) {
      this.$message.success("上传成功");
      this.avatar = res.url;
      this.form.appUser.avatar = res.url;
    },
    // 图片上传前校检格式和大小
    beforeAvatarUpload(file) {
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
    //添加企业
    handleCompanyAdd(){
      this.addCompanyTitle = "添加企业";
      this.addCompanyOpen = true;
      this.addCompanyForm={};
    },
    //删除企业
    handleCompanyDelete(row){
      const deleteCompanyData = {};
      deleteCompanyData.marketId = this.marketId;
      deleteCompanyData.companyId =  row.id;
      this.$modal.confirm('是否确认删除企业编号为"' + row.id + '"的数据项？').then(function() {
          return delCompany(deleteCompanyData);
      }).then(() => {
        getMarketuser(this.marketId).then(response => {
          this.companyList = response.data.companyList;
          this.loading = false;
        });
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        console.error("出错了");
      });
    },

    //添加企业关闭
    addCompanyCancel(){
      this.addCompanyOpen = false;
    },

    //添加企业确认
    addCompanySubmitForm(){
      this.$refs["addCompanyForm"].validate(valid => {
        if (valid) {
          this.addCompanyForm.marketId = this.marketId;
          addCompany(this.addCompanyForm).then(response => {
              if(response.code == 200){
                this.$modal.msgSuccess("添加成功");
               getMarketuser(this.marketId).then(response => {
                  this.companyList = response.data.companyList;
                  this.loading = false;
                });
                this.addCompanyOpen = false;
              }else {
                this.$modal.msgSuccess(response.msg);
              }
          }).catch(() => {
          });;
        }
      });

    },


    //添加个人
    handleJobAdd(){
      this.addJobTitle = "添加个人";
      this.addJobOpen = true;
      this.addJobForm={};
    },
    //删除个人
    handleJobDelete(row){
      const deleteJobData = {};
      deleteJobData.marketId = this.marketId;
      deleteJobData.jobId =  row.id;
      this.$modal.confirm('是否确认删除个人编号为"' + row.id + '"的数据项？').then(function() {
        return delUser(deleteJobData);
      }).then(() => {
        getMarketuser(this.marketId).then(response => {
          this.jobList = response.data.seekUserList;
          this.loading = false;
        });
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        console.error("出错了");
      });
    },
    //添加个人关闭
    addJobCancel(){
      this.addJobOpen = false;
    },
    //添加个人确认
    addJobSubmitForm(){
      this.$refs["addJobForm"].validate(valid => {
        if (valid) {
          this.addJobForm.marketId = this.marketId;
          addUser(this.addJobForm).then(response => {
            if(response.code == 200){
              this.$modal.msgSuccess("添加成功");
              getMarketuser(this.marketId).then(response => {
                this.jobList = response.data.seekUserList;
                this.loading = false;
              });
              this.addJobOpen = false;
            }else {
              this.$modal.msgError(response.msg);
            }
          });
        }
      });

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
</style>
