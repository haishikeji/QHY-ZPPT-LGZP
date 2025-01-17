<template>
  <div class="container">
    <div class="topPart" @click="toMyInfo">
      <div class="head-pic">
        <van-image
          v-if="this.avatar == null"
          width="70"
          height="70"
          round
          :src="require('../../assets/imgs/head.png')"
        />
        <van-image
          v-if="this.avatar != null"
          round
          width="70"
          height="70"
          :src=avatar
        />
      </div>
      <div class="top-rightpart">
        <div class="name">{{trueName}}</div>
        <div class="under-name">
          <div class="wechat">手机号: {{phone}}</div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
      </div>
    </div>
    <!-- <div class="part-main">
      <div class="item" @click="toVipResume">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/vip.png" />
            <span class="title">VIP简历</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
      </div>
    </div> -->
    <div class="part-main">
      <div class="item" @click="toOnlineResume">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/resume.png" />
            <span class="title">在线简历</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>
      <!-- <div class="item" @click="toAttachedResume">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/file.png" />
            <span class="title">附件简历</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div> -->

      <!-- <div class="item" @click="toVideoResume">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/comvideo.png" />
            <span class="title">视频简历</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div> -->
      <div class="item" @click="toSeek">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/seek.png" />
            <span class="title">求职意向</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>
      <div class="item" @click="toCollect">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/collection.png" />
            <span class="title">我的收藏</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
      </div>
    </div>

    <div class="part-main">
      <div class="item" @click="toHelp">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/help.png" />
            <span class="title">帮助反馈</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>

      <div class="item" @click="callPhone">
        <div class="flex-box">
          <div class="left-span" >
            <img src="../../assets/imgs/service.png" />
            <span class="title">我的客服</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>
      <div class="item" @click="toViolationList">
        <div class="flex-box">
          <div class="left-span" >
            <img src="../../assets/imgs/weigui.png" />
            <span class="title">违规公告</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>

      <div class="item"  @click="toSetting" >
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/setting.png" />
            <span class="title">设置</span>
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
      </div>
    </div>

    <!-- <div class="part-main">
      <div class="item">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/versions.png" /><span class="title"
              >版本更新</span
            >
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>
      <div class="item" @click="toSetting">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/setting.png" /><span class="title"
              >设置</span
            >
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
        <div class="line"><van-divider /></div>
      </div>
      <div class="item">
        <div class="flex-box">
          <div class="left-span">
            <img src="../../assets/imgs/resume.png" /><span class="title"
              >关于</span
            >
          </div>
          <div class="arrow"><van-icon name="arrow" /></div>
        </div>
      </div>
      <div style="height: 1rem"></div>
    </div> -->
  </div>
</template>
<script>
import { getUserResume } from '@/api/userResume'
export default {
  name: 'Center',
  components: {},
  data () {
    return {
      tabList: [
        { name: 'UI设计师', index: 0, checked: true },
        { name: '交互设计师', index: 1, checked: false },
        { name: 'APP设计师', index: 2, checked: false }
      ],
      tabList2: [
        { name: '推荐', index: 0, checked: true },
        { name: '附近', index: 1, checked: false },
        { name: '最新', index: 2, checked: false }
      ],
      loading: false,
      finished: false,
      list: [],
      // 真实名字
      trueName: '',
      // 手机号
      phone: '',
      // 图片
      avatar: null
    }
  },
  methods: {
    toVipResume () {
      this.$router.push({ path: '/vipResume' })
    },
    toOnlineResume () {
      this.$router.push({ path: '/onlineResume' })
    },
    toAttachedResume () {
      this.$router.push({ path: '/attachedResume' })
    },
    toVideoResume () {
      this.$router.push({ path: '/videoResume' })
    },
    toMyInfo () {
      this.$router.push({ path: '/myInfo' })
    },
    toSeek () {
      this.$router.push({ path: '/seek' })
    },
    toCollect () {
      this.$router.push({ path: '/collectList' })
    },
    toHelp () {
      this.$router.push({ path: '/help' })
    },
    callPhone () {
      window.location.href = 'tel:4008718711'
    },
    toViolationList () {
      this.$router.push({ path: '/violationList' })
    },
    toSetting () {
      this.$router.push({ path: '/setting' })
    },
    getUserResume () {
      getUserResume().then(response => {
        if (response.code === 200) {
          if (response.data != null) {
            const data = response.data
            if (data.resume != null) {
              const resumeList = data.resume
              if (resumeList[0] != null) {
                if (resumeList[0].seekUser != null) {
                  // 真实姓名
                  if (resumeList[0].seekUser.trueName != null) {
                    this.trueName = resumeList[0].seekUser.trueName
                  } else {
                    this.trueName = '未实名'
                  }
                }
                if (resumeList[0].appUser != null) {
                  // 手机号
                  this.phone = resumeList[0].appUser.phonenumber
                }

                // 头像
                if (resumeList[0].appUser.avatar != null) {
                  this.avatar = resumeList[0].appUser.avatar
                }
              }
            }
          }
        }
      })
    }
  },
  created () {
    this.getUserResume()
  }
}
</script>
<style scoped>
.container {
  font-size: 14px;
  padding: 0;
  background: #f7f7f7;
  padding-bottom: 60px;
  min-height: 84vh;
  overflow-x: hidden;
}
.topPart {
  width: 100%;
  background: #fff;
  padding: 15px 15px 15px 15px;
  display: flex;
}
.head-pic img {
  height: 60px;
  float: left;
}
.top-rightpart {
  width: 100%;
  margin-top: 10px;
  float: left;
  margin-left: 10px;
}
.top-rightpart .name {
  font-size: 17;
  color: #333333;
  font-weight: bold;
}
.under-name {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
.arrow {
  display: none;
  width: 30px;
  float: right;
  color: #999999;
  margin-right: 15px;
}
.wechat {
  float: left;
  color: #999999;
}
.flex-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.left-span {
  /* float: left; */
  display: flex;
  align-items: center;
  flex-direction: column;
}
.left-span img {
  display: block;
  float: left;
  width: 25px;
  height: 25px;
  margin-top: 3px;
  margin-bottom: 10px;
}
.left-span .title {
  float: left;
  font-size: 16px;
  color: #212121;
  margin-left: 10px;
}

.part-main {
  /* margin: 10px 0 10px 0; */
  padding: 15px 15px 0 15px;
  width: 100%;
  /* background: #fff; */
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  box-sizing: border-box;
}
.item {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  border-radius: 10px;
}

.line {
  display: none;
  margin-top: -4px;
  width: 90%;
}
</style>
