<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="版本号" prop="versions">
        <el-input
          v-model="queryParams.versions"
          placeholder="请输入版本号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="下载地址" prop="downloadUrl">
        <el-input
          v-model="queryParams.downloadUrl"
          placeholder="请输入下载地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
<!--      <el-form-item label="是否启用此版本" prop="using">
        <el-input
          v-model="queryParams.isusing"
          placeholder="请输入是否启用此版本 (0不启用 1启用)"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
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
          v-hasPermi="['version:version:add']"
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
          v-hasPermi="['version:version:edit']"
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
          v-hasPermi="['version:version:remove']"
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
          v-hasPermi="['version:version:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="versionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="版本号" align="center" prop="versions" />
      <el-table-column label="下载地址" align="center" prop="downloadUrl" />
      <el-table-column label="是否启用此版本" align="center" prop="isusing" :formatter = 'isusingFormatter'></el-table-column>
      <el-table-column label="是否强制升级" align="center" prop="isForcibly" :formatter = 'isForciblyFormatter'></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['version:version:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['version:version:remove']"
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

    <!-- 添加或修改app版本管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="版本号" prop="versions">
          <el-input v-model="form.versions" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="下载地址" prop="downloadUrl">
          <el-input v-model="form.downloadUrl" placeholder="请输入下载地址" />
        </el-form-item>
        <el-form-item label="是否启用此版本" prop="isusing">
<!--          <el-input v-model="form.isusing" placeholder="请输入是否启用此版本 (0启用 1不启用)" />-->
          <el-select v-model="form.isusing" placeholder="请选择是否启用此版本">
          <el-option
            v-for="item in isUsingOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否强制升级" prop="isForcibly">
<!--          <el-input v-model="form.isForcibly" placeholder="请输入是否强制升级 (0不强制 1强制)" />-->
          <el-select v-model="form.isForcibly" placeholder="请选择是否强制升级">
          <el-option
            v-for="item in isForciblyOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVersion, getVersion, delVersion, addVersion, updateVersion, exportVersion } from "@/api/version/version";

export default {
  name: "Version",
  data() {
    return {
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
      // app版本管理表格数据
      versionList: [],
      //是否启用选项
      isUsingOptions: [{
        value: '0',
        label: '启用'
      }, {
        value: '1',
        label: '不启用'
      }],
      //是否强制xuanx
      isForciblyOptions: [{
        value: '0',
        label: '不强制'
      }, {
        value: '1',
        label: '强制'
      }],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        orderByColumn:'id',
        isAsc:'desc',
        pageNum: 1,
        pageSize: 10,
        versions: null,
        downloadUrl: null,
        isusing: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        versions: [
          { required: true, message: "版本号不能为空", trigger: "blur" }
        ],
        downloadUrl: [
          { required: true, message: "下载地址不能为空", trigger: "blur" }
        ],
        isusing: [
          { required: true, message: "是否启用不能为空", trigger: "blur" }
        ],
        isForcibly: [
          { required: true, message: "是否强制升级不能为空", trigger: "blur" }
        ],

      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询app版本管理列表 */
    getList() {
      this.loading = true;
      listVersion(this.queryParams).then(response => {
        this.versionList = response.rows;
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
        versions: null,
        downloadUrl: null,
        isusing: null
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
      this.title = "添加app版本管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getVersion(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改app版本管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateVersion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVersion(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除app版本管理编号为"' + ids + '"的数据项？').then(function() {
        return delVersion(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有app版本管理数据项？').then(() => {
        this.exportLoading = true;
        return exportVersion(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    isusingFormatter(row){
      switch(row.isusing){
        case '0':
          return '启用'
        case '1':
          return '不启用'
      }
    },
    isForciblyFormatter(row){
      switch(row.isForcibly){
        case '0':
          return '不强制'
        case '1':
          return '强制'
      }
    },
  }
};
</script>
