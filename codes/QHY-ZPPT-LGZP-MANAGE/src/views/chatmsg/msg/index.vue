<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="企业名称" prop="userId">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入企业名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="trueName">
        <el-input
          v-model="queryParams.trueName"
          placeholder="请输入姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="手机号" prop="phoneNumber">
        <el-input
          v-model="queryParams.phoneNumber"
          placeholder="请输入手机号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable size="small"
                        v-model="queryParams.startTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请输入开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable size="small"
                        v-model="queryParams.endTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请输入结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['chatmsg:msg:add']"
        >新增</el-button>
      </el-col>-->
<!--      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['chatmsg:msg:edit']"
        >修改</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['chatmsg:msg:remove']"
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
          v-hasPermi="['chatmsg:msg:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="msgList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="发送者" align="center" prop="sendShAppUser.phonenumber" />
      <el-table-column label="接收者" align="center" prop="reShAppUser.phonenumber" />
      <el-table-column label="内容" align="center" prop="msg" >
        <template slot-scope="scope">
          <!--文件类型-->
          <el-button
            v-if="scope.row.msgType == 4"
            size="mini"
            type="text"
            @click= "showFile(scope.row)"
          >{{ scope.row.msg }}
          </el-button>
          <!--图片类型-->
          <img v-if="scope.row.msgType == 2" :src="scope.row.msg" style="max-width:55px;max-height: 55px"/>
          <!--文字类型-->
          <div v-if="scope.row.msgType == 1">
            {{scope.row.msg}}
          </div>
          <!--声音类型-->
          <el-button
            type="primary"
            v-if="scope.row.msgType == 3"
            size="mini"
            icon="el-icon-video-play"
            @click= "showAudio(scope.row.msg)"
          >
            播放音频
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="是否签收" align="center" prop="signed" :formatter = 'signedFormatter'>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['chatmsg:msg:edit']"
          >修改</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['chatmsg:msg:remove']"
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

    <!-- 添加或修改聊天记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发送者" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入发送者" />
        </el-form-item>
        <el-form-item label="接收者" prop="toUserId">
          <el-input v-model="form.toUserId" placeholder="请输入接收者" />
        </el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

<!--    职位弹窗-->
    <el-dialog title="职位" :visible.sync="recruitOpen" width="1500px" append-to-body>
      <el-table v-loading="recruitLoading" :data="recruitList" >
        <el-table-column label="id" align="center" prop="id"/>
        <el-table-column label="招聘企业" align="center" prop="shCompany.trueName"/>
        <el-table-column label="企业类型" align="center" prop="shCompany.type">
          <template slot-scope="scope">
            <span>{{ scope.row.shCompany!=null?(scope.row.shCompany.type === "1" ? '人资企业' : '普通企业'):'未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="招聘者" align="center" prop="shCompanyUser.trueName"/>
        <el-table-column label="代招公司名称" align="center" prop="shCompanyUnderHr.trueName"/>
        <el-table-column label="联系电话" align="center" prop="shAppUser.phonenumber"/>
        <el-table-column label="岗位名称" align="center" prop="shCareas.name"/>
        <el-table-column label="招聘地点" align="center" prop="shArea.description"/>
        <el-table-column label="工作地点" align="center" prop="address"/>
        <el-table-column label="综合薪资" align="center" prop="shSalary.name"/>
        <el-table-column label="发薪周期" align="center" prop="salaryCycle"/>
        <el-table-column label="薪资结构" align="center" prop="salaryMake"/>
        <el-table-column label="伙食情况" align="center" prop="food"/>
        <el-table-column label="住宿情况" align="center" prop="room"/>
        <el-table-column label="社保情况" align="center" prop="socialSecurity"/>
        <el-table-column label="公积金" align="center" prop="providentFund"/>
        <el-table-column label="福利" align="center" prop="tags"/>
        <el-table-column label="工作内容" align="center" prop="request"
                         :show-overflow-tooltip="true"
        >
          <template slot-scope="score">
            <span>{{score.row.request}}</span>
          </template>

        </el-table-column>
        <el-table-column label="工作时间" align="center" prop="workTime"/>
        <el-table-column label="年龄要求" align="center" prop="age"/>
        <el-table-column label="性别要求" align="center" prop="sex">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex"/>
          </template>

        </el-table-column>
        <el-table-column label="学历要求" align="center" prop="shEducation.name"/>
        <el-table-column label="创建时间" align="center" prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <!--      <el-table-column label="是否被推荐" align="center" prop="isRecommend" width="180" >
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.isRecommend"
                    active-value="1"
                    inactive-value="0"
                    @change="handleRecommendStatusChange(scope.row)"
                  ></el-switch>
                </template>
              </el-table-column>-->
      </el-table>
    </el-dialog>




  </div>
</template>

<script>
import { listMsg, getMsg, delMsg, addMsg, updateMsg, exportMsg,getChatMsgContentList } from "@/api/chatmsg/msg";
import {
  listRecruit
} from "@/api/recruit/recruit";
export default {
  name: "Msg",
  dicts: ['sys_user_sex'],
  data() {
    return {
      // 遮罩层
      loading: true,
      //职位列表遮罩层
      recruitLoading: true,
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
      // 招聘记录表格数据
      recruitList: [],
      // 总条数
      total: 0,
      // 聊天记录表格数据
      msgList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否显示职位弹出层
      recruitOpen:false,
      // 查询参数
      queryParams: {
        orderByColumn:'m.create_time',
        isAsc:'desc',
        pageNum: 1,
        pageSize: 10,
        userId: null,
        toUserId: null,
        content: null,
        companyName:null,
        trueName:null,
        startTime:null,
        endTime:null
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
  },
  methods: {
    /** 查询聊天记录列表 */
    getList() {
      this.loading = true;
      getChatMsgContentList(this.queryParams).then(response => {
        this.msgList = response.rows;
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
        userId: null,
        toUserId: null,
        content: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        companyName:null,
        trueName:null,
        startTime:null,
        endTime:null
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
      this.title = "添加聊天记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMsg(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改聊天记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMsg(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMsg(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除聊天记录编号为"' + ids + '"的数据项？').then(function() {
        return delMsg(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有聊天记录数据项？').then(() => {
        this.exportLoading = true;
        return exportMsg(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    //打开文件下载
    showFile(val){
      //求职者
      if(val.recruitId == null){
        let routeUrl = this.$router.resolve({
          path: "/resume",
          query: {userId: val.senderId}
        });
        window.open(routeUrl.href, '_blank');
      }else {
        //企业
        let q = {
          id:val.recruitId
        }
        listRecruit(q).then(response => {
          this.recruitList = response.rows;
          this.recruitLoading = false;
          this.recruitOpen = true;
        });
      }
    },
    //播放音频
    showAudio(val){
      var audio = new Audio(val) // path to file
      audio.play()
    },
    signedFormatter(row){
      switch(row.signed){
        case '0':
          return '未签收'
        case '1':
          return '已签收'
      }
    },

  }
};
</script>
