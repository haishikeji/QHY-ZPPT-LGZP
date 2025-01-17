<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会话表id" prop="msgId">
        <el-input
          v-model="queryParams.msgId"
          placeholder="请输入会话表id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="回调命令" prop="callbackcommand">
        <el-input
          v-model="queryParams.callbackcommand"
          placeholder="请输入回调命令"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息发送者 UserID" prop="fromAccount">
        <el-input
          v-model="queryParams.fromAccount"
          placeholder="请输入消息发送者 UserID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息接收者 UserID" prop="toAccount">
        <el-input
          v-model="queryParams.toAccount"
          placeholder="请输入消息接收者 UserID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息序列号，用于标记该条消息" prop="msgseq">
        <el-input
          v-model="queryParams.msgseq"
          placeholder="请输入消息序列号，用于标记该条消息"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息随机数，用于标记该条消息" prop="msgrandom">
        <el-input
          v-model="queryParams.msgrandom"
          placeholder="请输入消息随机数，用于标记该条消息"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息的发送时间戳，单位为秒" prop="msgtime">
        <el-input
          v-model="queryParams.msgtime"
          placeholder="请输入消息的发送时间戳，单位为秒"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="该条消息的唯一标识" prop="msgkey">
        <el-input
          v-model="queryParams.msgkey"
          placeholder="请输入该条消息的唯一标识"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="在线消息，为1，否则为0" prop="onlineonlyflag">
        <el-input
          v-model="queryParams.onlineonlyflag"
          placeholder="请输入在线消息，为1，否则为0"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="该条消息的下发结果，0表示下发成功，非0表示下发失败" prop="sendmsgresult">
        <el-input
          v-model="queryParams.sendmsgresult"
          placeholder="请输入该条消息的下发结果，0表示下发成功，非0表示下发失败"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="To_Account 未读的单聊消息总数量" prop="unreadmsgnum">
        <el-input
          v-model="queryParams.unreadmsgnum"
          placeholder="请输入To_Account 未读的单聊消息总数量"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息自定义数据" prop="cloudcustomdata">
        <el-input
          v-model="queryParams.cloudcustomdata"
          placeholder="请输入消息自定义数据"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息内容" prop="msgbodyid">
        <el-input
          v-model="queryParams.msgbodyid"
          placeholder="请输入消息内容"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="该条消息下发失败的错误信息" prop="errorinfo">
        <el-input
          v-model="queryParams.errorinfo"
          placeholder="请输入该条消息下发失败的错误信息"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否首次发送" prop="isFirstSend">
        <el-input
          v-model="queryParams.isFirstSend"
          placeholder="请输入是否首次发送"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="读取状态" prop="readStatus">
        <el-select v-model="queryParams.readStatus" placeholder="请选择读取状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
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
          v-hasPermi="['imbean:imbean:add']"
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
          v-hasPermi="['imbean:imbean:edit']"
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
          v-hasPermi="['imbean:imbean:remove']"
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
          v-hasPermi="['imbean:imbean:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="imbeanList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="会话表id" align="center" prop="msgId" />
      <el-table-column label="回调命令" align="center" prop="callbackcommand" />
      <el-table-column label="消息发送者 UserID" align="center" prop="fromAccount" />
      <el-table-column label="消息接收者 UserID" align="center" prop="toAccount" />
      <el-table-column label="消息序列号，用于标记该条消息" align="center" prop="msgseq" />
      <el-table-column label="消息随机数，用于标记该条消息" align="center" prop="msgrandom" />
      <el-table-column label="消息的发送时间戳，单位为秒" align="center" prop="msgtime" />
      <el-table-column label="该条消息的唯一标识" align="center" prop="msgkey" />
      <el-table-column label="在线消息，为1，否则为0" align="center" prop="onlineonlyflag" />
      <el-table-column label="该条消息的下发结果，0表示下发成功，非0表示下发失败" align="center" prop="sendmsgresult" />
      <el-table-column label="To_Account 未读的单聊消息总数量" align="center" prop="unreadmsgnum" />
      <el-table-column label="消息自定义数据" align="center" prop="cloudcustomdata" />
      <el-table-column label="消息内容" align="center" prop="msgbodyid" />
      <el-table-column label="该条消息下发失败的错误信息" align="center" prop="errorinfo" />
      <el-table-column label="是否首次发送" align="center" prop="isFirstSend" />
      <el-table-column label="读取状态" align="center" prop="readStatus" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['imbean:imbean:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['imbean:imbean:remove']"
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

    <!-- 添加或修改回调信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会话表id" prop="msgId">
          <el-input v-model="form.msgId" placeholder="请输入会话表id" />
        </el-form-item>
        <el-form-item label="回调命令" prop="callbackcommand">
          <el-input v-model="form.callbackcommand" placeholder="请输入回调命令" />
        </el-form-item>
        <el-form-item label="消息发送者 UserID" prop="fromAccount">
          <el-input v-model="form.fromAccount" placeholder="请输入消息发送者 UserID" />
        </el-form-item>
        <el-form-item label="消息接收者 UserID" prop="toAccount">
          <el-input v-model="form.toAccount" placeholder="请输入消息接收者 UserID" />
        </el-form-item>
        <el-form-item label="消息序列号，用于标记该条消息" prop="msgseq">
          <el-input v-model="form.msgseq" placeholder="请输入消息序列号，用于标记该条消息" />
        </el-form-item>
        <el-form-item label="消息随机数，用于标记该条消息" prop="msgrandom">
          <el-input v-model="form.msgrandom" placeholder="请输入消息随机数，用于标记该条消息" />
        </el-form-item>
        <el-form-item label="消息的发送时间戳，单位为秒" prop="msgtime">
          <el-input v-model="form.msgtime" placeholder="请输入消息的发送时间戳，单位为秒" />
        </el-form-item>
        <el-form-item label="该条消息的唯一标识" prop="msgkey">
          <el-input v-model="form.msgkey" placeholder="请输入该条消息的唯一标识" />
        </el-form-item>
        <el-form-item label="在线消息，为1，否则为0" prop="onlineonlyflag">
          <el-input v-model="form.onlineonlyflag" placeholder="请输入在线消息，为1，否则为0" />
        </el-form-item>
        <el-form-item label="该条消息的下发结果，0表示下发成功，非0表示下发失败" prop="sendmsgresult">
          <el-input v-model="form.sendmsgresult" placeholder="请输入该条消息的下发结果，0表示下发成功，非0表示下发失败" />
        </el-form-item>
        <el-form-item label="To_Account 未读的单聊消息总数量" prop="unreadmsgnum">
          <el-input v-model="form.unreadmsgnum" placeholder="请输入To_Account 未读的单聊消息总数量" />
        </el-form-item>
        <el-form-item label="消息自定义数据" prop="cloudcustomdata">
          <el-input v-model="form.cloudcustomdata" placeholder="请输入消息自定义数据" />
        </el-form-item>
        <el-form-item label="消息内容" prop="msgbodyid">
          <el-input v-model="form.msgbodyid" placeholder="请输入消息内容" />
        </el-form-item>
        <el-form-item label="该条消息下发失败的错误信息" prop="errorinfo">
          <el-input v-model="form.errorinfo" placeholder="请输入该条消息下发失败的错误信息" />
        </el-form-item>
        <el-form-item label="是否首次发送" prop="isFirstSend">
          <el-input v-model="form.isFirstSend" placeholder="请输入是否首次发送" />
        </el-form-item>
        <el-form-item label="读取状态">
          <el-radio-group v-model="form.readStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
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
  </div>
</template>

<script>
import { listImbean, getImbean, delImbean, addImbean, updateImbean, exportImbean } from "@/api/imbean/imbean";

export default {
  name: "Imbean",
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
      // 回调信息表格数据
      imbeanList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        msgId: null,
        callbackcommand: null,
        fromAccount: null,
        toAccount: null,
        msgseq: null,
        msgrandom: null,
        msgtime: null,
        msgkey: null,
        onlineonlyflag: null,
        sendmsgresult: null,
        unreadmsgnum: null,
        cloudcustomdata: null,
        msgbodyid: null,
        errorinfo: null,
        isFirstSend: null,
        readStatus: null,
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
    /** 查询回调信息列表 */
    getList() {
      this.loading = true;
      listImbean(this.queryParams).then(response => {
        this.imbeanList = response.rows;
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
        msgId: null,
        callbackcommand: null,
        fromAccount: null,
        toAccount: null,
        msgseq: null,
        msgrandom: null,
        msgtime: null,
        msgkey: null,
        onlineonlyflag: null,
        sendmsgresult: null,
        unreadmsgnum: null,
        cloudcustomdata: null,
        msgbodyid: null,
        errorinfo: null,
        isFirstSend: null,
        readStatus: "0",
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
      this.title = "添加回调信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getImbean(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改回调信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateImbean(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addImbean(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除回调信息编号为"' + ids + '"的数据项？').then(function() {
        return delImbean(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有回调信息数据项？').then(() => {
        this.exportLoading = true;
        return exportImbean(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
