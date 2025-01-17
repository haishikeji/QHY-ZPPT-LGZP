<template>
  <div class="container2">
    <van-nav-bar :title=title :border="false" left-arrow @click-left="onClickLeft" />
    <van-form class="bgf">
      <van-cell-group inset>
        <van-field
          v-model="name"
          label="证书名称"
          placeholder="请输入获得证书名称"
          :label-width="60"
          :formatter="formatter"
        />
        <van-field
          v-model="startTime"
          label="获得时间"
          readonly="true"
          placeholder="请选择获得时间"
          :label-width="60"
          @click="startTimeShow = true"
        />
      </van-cell-group>
    </van-form>
    <div v-if="id" class="footer flex-box">
      <van-button type="default" block round @click="delCertificate" style="width:100px;margin-right:10px">删除</van-button>
      <van-button type="primary" block round @click="toSave">保存</van-button>
    </div>
    <div v-else class="footer">
      <van-button type="primary" block round @click="toSave">保存</van-button>
    </div>
    <van-popup
      v-model:show="startTimeShow"
      position="bottom"
      :style="{ height: '40%' }"
    >
      <van-datetime-picker
        v-model="currentDate"
        type="year-month"
        :min-date="minDate"
        :max-date="maxDate"
        @cancel="startTimeShow = false"
        @confirm="startTimeConfirm"
      />
    </van-popup>
  </div>
</template>

<script>
import { ref } from 'vue'
import { Dialog } from 'vant'
import { addCertificate, editCertificate, delCertificate } from '@/api/userResume'
import { regStr } from '@/utils/emoji'
export default {
  name: 'Certificate',
  components: {
    [Dialog.Component.name]: Dialog.Component
  },
  setup () {
    // 过滤输入的数字
    const formatter = (value) => value.replace(regStr, '')
    return {
      formatter
    }
  },
  data () {
    const currentDate = ref(new Date())
    return {
      title: '添加证书',
      id: null,
      info: {},
      name: '',
      startTimeShow: false,
      minDate: new Date(1970, 0, 1),
      maxDate: new Date(2055, 10, 1),
      startTime: '',
      currentDate: currentDate
    }
  },
  created () {
    if (this.$route.query.info) {
      this.info = JSON.parse(this.$route.query.info)
      this.init()
    }
  },
  methods: {
    init () {
      this.title = '编辑证书'
      this.id = this.info.id
      this.name = this.info.name
      this.startTime = this.info.obtainTime.substring(0, this.info.obtainTime.lastIndexOf('-'))
    },
    onClickLeft () {
      this.$router.go(-1)
    },
    startTimeConfirm (value) {
      const year = value.getFullYear() // 年
      let month = value.getMonth() + 1 // 月
      if (month >= 1 && month <= 9) {
        month = `0${month}`
      }
      this.startTime = `${year}-${month}`
      this.startTimeShow = false
    },
    toSave () {
      var that = this
      // 证书名称
      if (!this.name) {
        that.$toast({
          message: '请输入证书名称',
          icon: 'none',
          duration: 2000
        })
        return false
      }
      // 获得时间
      if (!this.startTime) {
        that.$toast({
          message: '请输入证书名称',
          icon: 'none',
          duration: 2000
        })
        return false
      }
      const paramer = {
        id: this.id ? this.id : undefined,
        name: this.name,
        obtainTime: this.startTime + '-01'
      }
      if (that.id) {
        editCertificate(paramer).then(response => {
          if (response.code === 200) {
            that.$toast({
              message: response.msg,
              icon: 'none',
              duration: 2000
            })
            this.$router.go(-1)
          } else {
            that.$toast({
              message: response.msg,
              icon: 'none',
              duration: 2000
            })
          }
        })
      } else {
        addCertificate(paramer).then(response => {
          if (response.code === 200) {
            that.$toast({
              message: response.msg,
              icon: 'none',
              duration: 2000
            })
            this.$router.go(-1)
          } else {
            that.$toast({
              message: response.msg,
              icon: 'none',
              duration: 2000
            })
          }
        })
      }
    },
    delCertificate () {
      Dialog.confirm({
        title: '提示',
        confirmButtonColor: '#1291fe',
        message:
          '此操作将永久删除该记录，是否继续?'
      })
        .then(() => {
          delCertificate(this.id).then(response => {
            if (response.code === 200) {
              this.$router.go(-1)
            } else {
              this.$toast({
                message: response.msg,
                icon: 'none',
                duration: 2000
              })
            }
          })
        })
        .catch(() => {
          // on cancel
        })
    }
  }
}
</script>
 <style scoped>
.container2 {
  background: #fff;
}
.van-field__label {
  width: 120px;
}
.list {
  line-height: 44px;
  background: #fff;
}
.van-image {
  vertical-align: sub;
}
.grey {
  color: #8b8d92;
  text-align: right;
}
.flex-box {
  border-bottom: solid 1px #f2f2f2;
}
.flex-box:last-child {
  border-bottom: none;
}
.van-field {
  border-bottom: solid 1px #f2f2f2;
}
.van-cell::after {
  bottom: -1px;
}
</style>
