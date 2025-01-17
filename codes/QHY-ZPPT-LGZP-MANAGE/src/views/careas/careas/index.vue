<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          @click="handleAdd('all')"
          v-hasPermi="['careas:careas:add']"
        >新增</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

<!--    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="careasList"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >-->
    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="careasList"
    >
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column prop="name" label="名称" align="center"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否被推荐" align="center" prop="isRecommend" width="180">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isRecommend"
            active-value="1"
            inactive-value="0"
            @change="handleRecommendStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['branch:branch:edit']"
          >修改
          </el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd('singe',scope.row)"
            v-hasPermi="['branch:branch:add']"
          >新增
          </el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['branch:branch:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改职业分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
<!--          <el-col :span="24" >
            <el-form-item label="是否顶级分类" prop="isTop" v-if="isTopShow">
              <el-select v-model="form.isTop" placeholder="是否顶级分类" @change="selectPcode(form.isTop)" >
                <el-option
                  v-for="state in StatusOptions"
                  :key="state.key"
                  :value="state.key"
                  :label="state.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="isShow !== '0'" >
            <el-form-item label="上级" prop="pcode">
              <treeselect v-model="form.pcode" :options="careasOptions" :normalizer="normalizer" placeholder="选择上级分类"/>
            </el-form-item>
          </el-col>-->
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCareas, getCareas, delCareas, addCareas, updateCareas, listCareasExcludeChild } from "@/api/careas/careas";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "Careas",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 职业分类表格数据
      careasList: [],
      // 数选项
      careasOptions:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        name: undefined,
      },
      // 表单参数
      form: {},
      StatusOptions : [
        { key: '0', name: '是' },
        { key: '1', name: '否' }
      ],
      // 表单校验
      rules: {
        /*pcode: [
          { required: true, message: "上级不能为空", trigger: "blur" }
        ],*/
        name: [
          { required:true,max: 20, message: "长度不能超过20且不能为空", trigger: "blur" }
        ],
      },
      isTopShow:true,
      isShow:'0'
    };
  },
  created() {
    this.getList();
    this.reset();
  },
  methods: {
    selectPcode(data){
      this.isShow = data
    },
    /** 查询职业分类列表 */
    getList() {
      this.loading = true;
      listCareas(this.queryParams).then(response => {
        this.careasList = this.handleTree(response.data, "id","pcode");
        this.loading = false;
      });
    },
    /** 转换数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        isTop: undefined,
        pcode: null,
        code: undefined,
        name: undefined,
        type: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
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
    handleAdd(str,row) {
      this.reset();
      if(str==='all'){
        this.isTopShow = true;
        this.isShow = '0';
        this.form.isTop = '0';
        if(this.form.isTop ==='0'){
          this.form.pcode = 0;
        }
      }else{
        this.isTopShow = false;
        this.isShow = '1';
        this.form.pcode = row.id;
      }
      this.open = true;
      this.title = "添加职业分类";
      listCareas().then(response => {
        this.careasOptions = this.handleTree(response.data, "id","pcode");
      });
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.isTopShow = false;
      getCareas(row.id).then(response => {
        this.form = response.data;
        if(this.form.pcode === 0){
          this.isShow = '0'
        }else {
          this.isShow = '1'
        }
        this.open = true;
        this.title = "修改职业分类";
      });
      listCareasExcludeChild(row.id).then(response => {
        this.careasOptions = this.handleTree(response.data, "id","pcode");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCareas(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.pcode = 0;
            addCareas(this.form).then(response => {
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
      this.$modal.confirm('如果节点下存在子节点，会全部删除，是否确认删除改数据项？').then(function() {
        return delCareas(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    handleRecommendStatusChange(row){
      console.error(row);
      let data = {
        "id":row.id,
        "isRecommend":row.isRecommend,
        "pcode":row.pcode
      }
      updateCareas(data).then(response => {
        this.$modal.msgSuccess("更新成功");
        this.getList();
      });
    }
  }
};
</script>
