<template>
<div>
<van-nav-bar title="填写信息" :border="false" :left-arrow="arrow" @click-left="onClickLeft" />
  <div class="container">
    <div class="choose-title">
      <p class="front20 front-weight">你需要填写基础信息完成注册，大约花费1~3分钟</p>
      <p class="col9 front13">所填信息将被妥善保护，后续你也可以修改更新</p>
    </div>
    <van-form>
      <van-cell-group :border="false">
        <label class="col3 front18">你首次参加工作的时间是</label>
        <van-field
          v-model="joinWorkTime"
          placeholder="请选择你首次参加工作的时间"
          readonly
          @click="show = true"
        />
        <van-popup
          v-model:show="show"
          position="bottom"
          :style="{ height: '35%' }"
        >
          <van-datetime-picker
            v-model="currentDate"
            type="year-month"
            :min-date="minDate"
            :max-date="maxDate"
            @cancel="show = false"
            @confirm="confirmPicker"
          />
        </van-popup>
      </van-cell-group>
    </van-form>
    <div class="footer" @click="toRecentlyJob">
      <van-button type="primary" block round>下一步</van-button>
    </div>
  </div>
  </div>
</template>

<script>
import { updateUserResume, addProgress } from '@/api/baseInfo'
import { ref } from 'vue'
export default {
  name: 'Experience',
  components: {},
  data () {
    const currentDate = ref(new Date())
    return {
      username: '',
      sexList: [
        { name: '男', value: '0', checked: true },
        { name: '女', value: '1', checked: false }
      ],
      minDate: new Date(1970, 0, 1),
      maxDate: new Date(2025, 10, 1),
      currentDate: currentDate,
      joinWorkTime: '',
      joinWorkTime2: '',
      show: false,
      arrow: true
    }
  },
  created () {
    if (this.$route.query.progress) {
      this.arrow = false
    } else {
      this.arrow = true
    }
  },
  methods: {
    onClickLeft () {
      this.$router.go(-1)
    },
    confirmPicker (val) {
      const year = val.getFullYear() // 年
      let month = val.getMonth() + 1 // 月
      if (month >= 1 && month <= 9) {
        month = `0${month}`
      }
      this.joinWorkTime = `${year}-${month}`
      this.joinWorkTime2 = `${year}-${month}-01`
      this.show = false
    },
    toRecentlyJob () {
      var that = this
      if (!that.joinWorkTime) {
        that.$toast({
          message: '请先选择参加工作时间',
          icon: 'none',
          duration: 2000
        })
        return false
      };
      updateUserResume({ joinWorkTime: that.joinWorkTime2 }).then(response => {
        if (response.code === 200) {
          addProgress({ progress: 3 }).then(add => {
            that.$router.push('/recentlyJob')
          })
        }
      })
    }
  }
}
</script>
<style scoped>
.choose-title {
  line-height: 28px;
  margin: 10px 0 40px 0;
}
label {
  display: block;
  margin-bottom: 10px;
}
.van-cell-group {
  margin-bottom: 10px;
}
.sex {
  display: flex;
  justify-content: flex-start;
  padding: 10px 0;
}
.sex .sex-people {
  width: 98px;
  margin-right: 20px;
}
.van-cell::after {
  bottom: -1px;
}
</style>
