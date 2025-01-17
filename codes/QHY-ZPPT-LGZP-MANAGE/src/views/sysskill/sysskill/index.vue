<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="职位" prop="careasId" class="careas_search">
        <treeselect
          v-model="queryParams.careasId"
          :options="careasOptions"
          :show-count="true"
          placeholder="选择职位"
        />
      </el-form-item>
      <el-form-item label="技能" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入技能"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择类别"
          filterable
          clearable
          size="small"
          allow-create
        >
          <el-option
            v-for="company in skillTypeOptions"
            :key="company.type"
            :label="company.type"
            :value="company.type"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['sysskill:sysskill:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['sysskill:sysskill:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sysskill:sysskill:remove']"
          >删除</el-button
        >
      </el-col>
      <!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['sysskill:sysskill:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="sysskillList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="职位" align="center" prop="shCareas.name" />
      <el-table-column label="类别" align="center" prop="type" />
      <el-table-column label="技能" align="center" prop="name" />
      <!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sysskill:sysskill:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sysskill:sysskill:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改系统技能对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="职位" prop="careasId">
          <treeselect
            v-model="form.careasId"
            :options="careasOptions"
            :show-count="true"
            placeholder="选择职位"
          />
        </el-form-item>
        <el-form-item label="类别" prop="type">
          <el-select
            v-model="form.type"
            placeholder="请选择类别"
            filterable
            clearable
            size="small"
            allow-create
          >
            <el-option
              v-for="company in skillTypeOptions"
              :key="company.type"
              :label="company.type"
              :value="company.type"
            />
          </el-select>
        </el-form-item>

        <el-form-item v-if="!form.id" label="技能" prop="skills">
          <el-select
            v-model="form.skills"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请选择技能"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          <!--          <el-input v-model="form.name" placeholder="请输入技能" />-->
        </el-form-item>
        <el-form-item v-else label="技能" prop="name">
          <el-input v-model="form.name" placeholder="请输入技能" />
        </el-form-item>
        <!--        <el-form-item label="备注" prop="remark">-->
        <!--          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addSysskill,
  delSysskill,
  exportSysskill,
  getSysskill,
  listSysskill,
  listSysskillType,
  updateSysskill,
} from "@/api/sysskill/sysskill";
import {
  listCareas,
  listCareasExcludeChild,
  treeselectCareas,
} from "@/api/careas/careas";
// import Treeselect from "_@riophae_vue-treeselect@0.4.0@@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Sysskill",
  components: { Treeselect },
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
      // 系统技能表格数据
      sysskillList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        orderByColumn: "m.create_time",
        isAsc: "desc",
        pageNum: 1,
        pageSize: 10,
        careasId: null,
        name: null,
        type: null,
      },
      // 树选项
      careasOptions: undefined,
      skillTypeOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        careasId: [
          { required: true, message: "职位信息不能为空", trigger: "blur" },
        ],
        type: [
          { required: true, message: "类别信息不能为空", trigger: "blur" },
        ],
        skills: [
          { required: true, message: "技能信息不能为空", trigger: "blur" },
        ],
        name: [
          { required: true, message: "技能信息不能为空", trigger: "blur" },
        ],
      },
      options: [],
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
    // this.getSkillTypeList();
    listCareas().then((response) => {
      this.careasOptions = this.handleTree(response.data, "id", "pcode");
    });
  },
  watch: {
    // 监听deptId
    "form.careasId": "getSkillTypeList",
  },
  methods: {
    getCareasId() {},
    /** 查询下拉树结构 */
    getTreeselect() {
      treeselectCareas().then((response) => {
        this.careasOptions = response.data;
      });
    },
    /** 查询企业基本信息 */
    getSkillTypeList(careasId) {
      let q = {
        pageNum: 1,
        pageSize: 10000,
        del: 0,
        careasId: careasId,
      };
      listSysskillType(q).then((response) => {
        this.skillTypeOptions = response.data;
      });
    },
    /** 查询系统技能列表 */
    getList() {
      this.loading = true;
      listSysskill(this.queryParams).then((response) => {
        this.sysskillList = response.rows;
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
        careasId: null,
        name: null,
        skills: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加系统技能";

      this.getTreeselect();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const id = row.id || this.ids;
      getSysskill(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改系统技能";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateSysskill(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSysskill(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除系统技能编号为"' + ids + '"的数据项？')
        .then(function () {
          return delSysskill(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有系统技能数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportSysskill(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>
<style>
.careas_search {
  /*display: inline-block;
  overflow: hidden;*/
}
.careas_search label {
  float: left;
}
.careas_search .el-form-item__content {
  width: 215px;
}
</style>
