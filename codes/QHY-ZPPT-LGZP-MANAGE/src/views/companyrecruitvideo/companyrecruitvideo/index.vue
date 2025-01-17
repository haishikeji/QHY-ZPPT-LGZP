<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="企业" prop="companyId">
        <el-select v-model="queryParams.companyId" placeholder="请选择企业" filterable clearable size="small">
          <el-option
            v-for="company in companyOptions"
            :key="company.id"
            :label="company.trueName"
            :value="company.id"
          />
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
          v-hasPermi="['companyrecruitvideo:companyrecruitvideo:add']"
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
          v-hasPermi="['companyrecruitvideo:companyrecruitvideo:edit']"
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
          v-hasPermi="['companyrecruitvideo:companyrecruitvideo:remove']"
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
          v-hasPermi="['companyrecruitvideo:companyrecruitvideo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="companyrecruitvideoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="招聘企业" align="center" prop="shCompany.trueName" />
      <el-table-column label="客户企业" align="center" prop="shCompanyUnderHr.trueName" />
<!--      <el-table-column label="招聘记录id" align="center" prop="recruitId" />-->
      <el-table-column label="播放量" align="center" prop="playNum" />
      <el-table-column label="视频" align="center" prop="url" >
        <template slot-scope="scope">
          <el-button type="primary" @click="playVideo(scope.row)" ref="btn" size="mini">
            播放视频
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="在线简历是否被推荐" align="center" prop="isRecommend" width="180" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isRecommend"
            active-value="1"
            inactive-value="0"
            @change="handleRecommendStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['companyrecruitvideo:companyrecruitvideo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['companyrecruitvideo:companyrecruitvideo:remove']"
          >删除</el-button>
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

    <!-- 添加或修改企业招聘记录视频对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="企业id" prop="companyId">
          <el-input v-model="form.companyId" placeholder="请输入企业id" />
        </el-form-item>-->
        <el-form-item label="播放量" prop="playNum">
          <el-input v-model="form.playNum" placeholder="请输入播放量" />
        </el-form-item>
        <el-form-item label="视频地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入视频地址" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--    视频地址-->
    <el-dialog title = "播放视频" :visible.sync="dialogPlay" width="30%" @close="closeDialog">
      <video :src="videoUrl" controls autoplay class="video"
             width="100%"></video>
    </el-dialog>
  </div>
</template>

<script>
import { listCompanyrecruitvideo, getCompanyrecruitvideo, delCompanyrecruitvideo, addCompanyrecruitvideo, updateCompanyrecruitvideo, exportCompanyrecruitvideo,changeRecommendById, } from "@/api/companyrecruitvideo/companyrecruitvideo";
import {listCompany} from "@/api/company/company";

export default {
  name: "Companyrecruitvideo",
  data() {
    return {
      //企业基本信息
      companyOptions: [],
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
      //视频播放弹窗
      dialogPlay:false,
      //视频地址
      videoUrl:null,
      // 总条数
      total: 0,
      // 企业招聘记录视频表格数据
      companyrecruitvideoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        orderByColumn:'m.id',
        isAsc:'desc',
        pageNum: 1,
        pageSize: 10,
        delFlag:0,
        companyId: null,
        recruitId: null,
        playNum: null,
        url: null,
        imgUrl: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getCompanyList();
  },
  methods: {
    /** 查询企业基本信息 */
    getCompanyList() {
      let q = {
        pageNum: 1, pageSize: 10000, delFlag: 0,
      };
      listCompany(q).then(response => {
        this.companyOptions = response.rows;
      });
    },
    //更改推荐状态
    handleRecommendStatusChange(row){
      changeRecommendById(row.id).then(response => {
        this.getList();
      });
    },
    // 视频播放
    playVideo(row) {
      this.dialogPlay = true;
      this.videoUrl = row.url;
    },

    closeDialog() {
      this.videoUrl = null; //清空数据 关闭视频播放
    },
    /** 查询企业招聘记录视频列表 */
    getList() {
      this.loading = true;
      listCompanyrecruitvideo(this.queryParams).then(response => {
        this.companyrecruitvideoList = response.rows;
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
        id: null,
        companyId: null,
        recruitId: null,
        playNum: null,
        url: null,
        imgUrl: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.open = true;
      this.title = "添加企业招聘记录视频";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCompanyrecruitvideo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业招聘记录视频";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCompanyrecruitvideo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCompanyrecruitvideo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除企业招聘记录视频编号为"' + ids + '"的数据项？').then(function() {
        return delCompanyrecruitvideo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有企业招聘记录视频数据项？').then(() => {
        this.exportLoading = true;
        return exportCompanyrecruitvideo(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
