<template>
  <div class="container">
    <div class="my-primary-navbar">
      <div class="my-primary-navbar-left" @click="goBack">
        <van-image :src="require('./../../assets/img/back.png')" class="navbar-icon" />
      </div>
      <div class="my-primary-navbar-center">{{ companyRecruit.shCareas!=null?companyRecruit.shCareas?.name:'职位详情' }}</div>
      <div class="my-primary-navbar-right">
        <van-row gutter="5">
          <van-col span="12"  @click="getCollect">
            <van-icon v-if="!collect" name="star-o" size="20" color="#8B8D92" />
            <van-icon v-else name="star" size="20" color="#1291fe" />
          </van-col>
          <van-col span="12"   @click="getLike">
            <van-icon v-if="!like" name="like-o"  size="20" color="#8B8D92" />
            <van-icon v-else name="like"  size="20" color="#1291fe" />
          </van-col>
        </van-row>
      </div>
    </div>
    <div class="part ">
      <div class="flex-between title">
        <div class="primary-title">{{ companyRecruit.shCareas!=null?companyRecruit.shCareas?.name:'' }}</div>
        <div class="primary-grey-text" v-if="companyRecruit.days">{{companyRecruit.days}}</div>
      </div>
      <div class="col-red front18" style="font-weight:600">{{ companyRecruit.shSalary!=null?companyRecruit.shSalary?.name+'元/月':'面议' }}</div>
      <div class="list-item-tabs">
        <div class="list-item-tab" v-if="companyRecruit.oneAndSecondArea">{{companyRecruit.oneAndSecondArea}}</div>
        <div class="list-item-tab" v-if="companyRecruit.shExperience">{{companyRecruit.shExperience.name}}</div>
        <div class="list-item-tab" v-if="companyRecruit.shEducation">{{companyRecruit.shEducation.name}}</div>
        <div class="list-item-tab" v-if="companyRecruit.socialSecurity">{{companyRecruit.socialSecurity}}</div>
        <div class="list-item-tab" v-if="companyRecruit.roomName">{{companyRecruit.roomName}}</div>
        <div class="list-item-tab" v-for="(item,index) in companyRecruit.tagsList" :key="index">{{item}}</div>
      </div>
    </div>
    <van-tabs v-model:active="active" scrollspy sticky @click-tab="getChanges" ref="tabs">
      <van-tab title="挣多少钱" name="a">
        <div class="part">
          <div class="part-item">
            <div class="default-title">挣多少钱</div>
            <ul class="table-info">
              <li >
                <label>综合薪资</label><span>{{ companyRecruit.shSalary!=null?companyRecruit.shSalary?.name+'元/月':'面议' }}</span>
              </li>
              <li >
                <label>发薪周期</label><span>{{companyRecruit.salaryCycle?companyRecruit.salaryCycle:'无'}}</span>
              </li>
              <li >
                <label>薪资结构</label><span>{{companyRecruit.salaryMake?companyRecruit.salaryMake:'无'}}</span>
              </li>
            </ul>
          </div>
        </div>
      </van-tab>
      <van-tab title="干什么活" name="b">
        <div class="part">
          <div class="part-item">
            <div class="default-title">干什么活</div>
            <ul class="table-info">
              <li >
                <label>工作内容</label><span>{{companyRecruit.request?companyRecruit.request:'无'}}</span>
              </li>
              <li >
                <label>工作时间</label><span>{{companyRecruit.workTime?companyRecruit.workTime:'无'}}</span>
              </li>
               <li >
                <label>工作地点</label><span>{{companyRecruit.address?companyRecruit.address:'无'}}</span>
              </li>
            </ul>
          </div>
        </div>
      </van-tab>
      <van-tab title="福利待遇" name="c">
        <div class="part">
          <div class="part-item">
            <div class="default-title">福利待遇</div>
            <ul class="table-info">
              <li >
                <label>伙食情况</label><span>{{companyRecruit.food?companyRecruit.food:'无'}}</span>
              </li>
              <li >
                <label>住宿情况</label><span>{{companyRecruit.room?companyRecruit.room:'无'}}</span>
              </li>
              <li >
                <label>社保情况</label><span>{{companyRecruit.socialSecurity?companyRecruit.socialSecurity:'无'}}</span>
              </li>
              <li >
                <label>公积金</label><span>{{companyRecruit.providentFund?companyRecruit.providentFund:'无'}}</span>
              </li>
            </ul>
          </div>
        </div>
      </van-tab>
      <van-tab title="招聘条件" name="d">
        <div class="part">
          <div class="part-item">
            <div class="default-title">招聘条件</div>
            <ul class="table-info">
              <li >
                <label>年龄要求</label><span>{{companyRecruit.age?companyRecruit.age:'无'}}</span>
              </li>
              <li >
                <label>性别要求</label><span v-if="companyRecruit.sex==='0'">男</span><span v-if="companyRecruit.sex==='1'">女</span><span v-if="companyRecruit.sex !=='0' && companyRecruit.sex!=='1'">男女不限</span>
              </li>
              <li >
                <label>经验要求</label><span>{{companyRecruit.shExperience!==null?companyRecruit.shExperience?.name:'不限'}}</span>
              </li>
              <li >
                <label>学历要求</label><span>{{companyRecruit.shEducation!==null?companyRecruit.shEducation?.name:'不限'}}</span>
              </li>
              <li >
                <label>其他要求</label><span>{{companyRecruit.other?companyRecruit.other:'无'}}</span>
              </li>
            </ul>
          </div>
        </div>
      </van-tab>
    </van-tabs>
    <div>
      <div class="part" id="company" style="margin-bottom:0;border-bottom:solid 2px #F5F5F5;">
        <div class="default-title">企业简介</div>
        <div class="flex-between" style="padding:15px 0;"   @click="toCompanyInfo">
          <van-image :src="img" width="56" height="56"></van-image>
          <div class="pos-relative info-right">
            <div class="info-title">{{ companyRecruit.hrId !== null?(companyRecruit.shCompanyUnderHr!=null?companyRecruit.shCompanyUnderHr.trueName:''):(companyRecruit.shCompany!=null?companyRecruit.shCompany.trueName:'') }}</div>
            <div class="primary-grey-text">{{companyRecruit.shFinanceLevel?.name}} · {{companyRecruit.shCompanyScale?.name}} · {{companyRecruit.shBranch?.name}} </div>
            <div class="img-arrow"><van-icon name="arrow" color="#BDBDBD" size="20" /></div>
          </div>
        </div>
        <div class="flex-between" style="padding-bottom:20px;">
          <van-image :src="require('./../../assets/img/icon_location.png')" width="14" height="14" class="icon-location"></van-image>
          <div class="location-name">{{companyRecruit.hrId !== null?companyRecruit.shCompanyUnderHr?.address:companyRecruit.shCompany?.address}}</div>
        </div>
      </div>
    </div>
    <div class="point">
      <div class="col21 front16" style="font-weight:600">
        <img src="../../assets/img/warning.png"  class="icon-warning"  />安全提示
      </div>
      <div class="col9e front13" style="padding: 10px 0;line-height: 2;">以担保或任何理由索取财物、扣押证件，均涉嫌违法，请提高警惕，谨防诈骗。</div>
    </div>

    <div class="footer">
      <div class="footer-content">
        <van-row gutter="10">
          <!-- <van-col span="12">
            <van-button type="default" size="normal" @click="toPhone" icon="phone-o">打电话</van-button>
          </van-col> -->
          <van-col span="24">
            <van-button type="primary" size="normal" @click="toChat" icon="chat-o">立即沟通</van-button>
          </van-col>
        </van-row>
      </div>
    </div>
  </div>
</template>

<script>
import { Toast, Tab, Tabs, Dialog } from 'vant'
import { addOrDelIntrestIn, checkIntrestIn, checkIsCollect, collectR, getRecruitList, addLookUp, checkChatMsg } from '@/api/companyRecruit'

export default {
  name: 'JobListDetail',
  components: { [Tab.name]: Tab, [Tabs.name]: Tabs, [Dialog.name]: Dialog },
  data () {
    return {
      active: 'a',
      title: '设备技师岗',
      tabs: ['职位详情', '企业简介'],
      id: null,
      userId: null,
      type: null,
      companyId: null,
      form: {},
      welfareList: [],
      miaoshuList: [],
      companyRecruit: {},
      images: [],
      collect: false,
      like: false,
      img: ''
    }
  },
  created () {
    this.id = parseInt(this.$route.query.id)
    this.getParams(this.id)
    this.checkCollect(this.id)
  },
  methods: {
    goBack () {
      this.$router.go(-1)
    },
    getChanges (val) {
      console.log('val', val)
      // this.$refs.tabs.scrollTo(0)
    },
    toPhone () {
      window.location.href = 'tel:4008718711'
    },
    getParams (id) {
      this.form.id = id
      getRecruitList(this.form, null).then(response => {
        if (response.rows != null) {
          this.userId = response.rows[0].shAppUser.id
          this.addLookUps(this.userId)
          this.checkLike(response.rows[0].shAppUser.id)
          var skill = []
          if (response.rows[0].shExperience != null) {
            skill.push(response.rows[0].shExperience.name)
          }
          if (response.rows[0].shEducation != null) {
            skill.push(response.rows[0].shEducation.name)
          }
          if (response.rows[0].skillList != null) {
            for (const skillListElement of response.rows[0].skillList) {
              skill.push(skillListElement.name)
            }
          }
          if (response.rows[0].jobTypes != null) {
            for (const e of response.rows[0].jobTypes) {
              if (e === '0') {
                this.miaoshuList.push('全职')
              }
              if (e === '1') {
                this.miaoshuList.push('兼职')
              }
            }
          }
          if (response.rows[0].isSs === '0') {
            this.miaoshuList.push('缴纳社保')
          }
          if (response.rows[0].workType === '0') {
            this.miaoshuList.push('长白班')
          } else {
            this.miaoshuList.push('轮班')
          }
          response.rows[0].skills = skill
          if (response.rows[0].room) {
            const n = response.rows[0].room.indexOf('(')
            if (n >= 0) {
              response.rows[0].roomName = response.rows[0].room.substring(0, n)
            } else {
              response.rows[0].roomName = response.rows[0].room
            }

            console.log(response.rows[0].roomName)
            console.log(response.rows[0].room)
          } else {
            response.rows[0].roomName = ''
          }
          const tagsList = response.rows[0].tags.split(',')
          response.rows[0].tagsList = tagsList
          this.companyRecruit = response.rows[0]
          if (response.rows[0].hrId != null) {
            if (response.rows[0].hrHeadImages != null) {
              if (response.rows[0].hrHeadImages[0] != null) {
                this.img = response.rows[0].hrHeadImages[0].content
              }
            }
            this.type = '1'
            this.companyId = response.rows[0].hrId
            for (const e of response.rows[0].hrCompanyImages) {
              this.images.push(e.content)
            }
            for (const e of response.rows[0].hrWelfareList) {
              this.welfareList.push(e.welfareName)
            }
          } else {
            this.companyId = response.rows[0].shCompany.id
            this.type = '2'
            if (response.rows[0].headImages != null) {
              if (response.rows[0].headImages[0] != null) {
                this.img = response.rows[0].headImages[0].content
              }
            }
            for (const e of response.rows[0].companyImages) {
              this.images.push(e.content)
            }
            for (const e of response.rows[0].welfareList) {
              this.welfareList.push(e.welfareName)
            }
          }
        } else {
        }
      })
    },
    checkCollect (id) {
      checkIsCollect({ recruitId: id }).then(response => {
        if (response.code === 200) {
          this.collect = true
        } else {
          this.collect = false
        }
      })
    },
    checkLike (id) {
      checkIntrestIn({ userId: id }).then(response => {
        if (response.code === 200) {
          this.like = true
        } else {
          this.like = false
        }
      })
    },
    getCollect () {
      collectR({ recruitId: this.id }).then(response => {
        if (response.code === 200) {
          this.collect = !this.collect
        } else {
          Toast(response.msg)
        }
      })
    },
    getLike () {
      addOrDelIntrestIn({ userId: this.userId }).then(response => {
        if (response.code === 200) {
          this.like = !this.like
        } else {
          Toast(response.msg)
        }
      })
    },
    addLookUps (userId) {
      console.log('userid::', userId)
      addLookUp({ userId: userId }).then(response => {
        console.log('lookupres', response)
      })
    },
    toChat (str) {
      checkChatMsg({ senderId: this.companyRecruit.shAppUser?.id, receiverId: parseInt(localStorage.getItem('userId')), recruitId: this.companyRecruit.id }).then((res) => {
        if (res.code === 200) {
          const status = res.data.status
          // 没有聊天记录,发送简历推送
          if (status === '1') {
            this.$router.push({ path: '/chats', query: { id: this.companyRecruit.id, userId: this.companyRecruit.shAppUser?.id, isSendRecruit: true } })
          } else if (status === '2') {
            Dialog.confirm({
              title: '温馨提示',
              message: '您与该招聘人员已经沟通过，是否就新职位继续沟通?'
            })
              .then((isConfirm) => {
                this.$router.push({ path: '/chats', query: { id: this.companyRecruit.id, userId: this.companyRecruit.shAppUser?.id, isSendRecruit: true } })
              })
              .catch((isCancel) => {
                this.$router.push({ path: '/chats', query: { id: this.companyRecruit.id, userId: this.companyRecruit.shAppUser?.id, isSendRecruit: false } })
              })
          } else {
            this.$router.push({ path: '/chats', query: { id: this.companyRecruit.id, userId: this.companyRecruit.shAppUser?.id, isSendRecruit: false } })
          }
        }
      })
      // this.$router.push({ path: '/chats', query: { userId: this.userId } })
    },
    toCompanyInfo () {
      this.$router.push({ path: '/companyInfo', query: { companyId: this.companyId, type: this.type, pubUserId: this.userId } })
    }
  }
}
</script>
<style scoped>
.navbar-icon-bigger{
  margin-right: 10px;
}
.navbar-icon-bigger:last-child{
  margin-right: 0;
}
.part{
  /* margin-bottom: 10px; */
}
.title{
  margin-bottom: 20px;
}
.tabs{
  background: #fff;
  display: flex;
  padding-bottom: 10px;
}
.tab-item{
  width: 50%;
  line-height: 50px;
  text-align: center;
  font-size: 14px;
  color: #101010;
  font-weight: 600;
}
.tab-item.active{
  color: #1291fe;
  border-bottom: solid 2px #1291fe;
}
.default-title{
  padding: 20px 0 10px;
}
.table-info{
  line-height: 38px;
}
.table-info li{
  display: flex;
  align-items:center;
  background: #F5F5F5;
  margin-bottom: 1px;
}
.table-info label{
  display: block;
  width: 96px;
  height: 100%;
  text-align: center;
  background: #F5F5F5;
}
.table-info span{
  display: inline-block;
  line-height: 24px;
  background: #FAFAFA;
  width: calc(100% - 108px);
  border-left: solid 1px #fff;
  padding: 7px 10px;
}
.info-right{
  width: calc(100% - 66px);
}
.info-title{
  margin-bottom: 10px;
}
.img-arrow {
  position: absolute;
  top: 20px;
  right: 0;
}
.icon-location{
  margin-top: 2px;
}
.location-name{
  width: calc(100% - 24px);
  color: #424242;
  font-size: 14px;
}

.icon-job{
  width: 15px;
  height: 15px;
  padding-right: 4px;
  vertical-align: sub;
}
.container {
  padding: 0 0 50px 0;
}
.swipe-img {
  width: 100%;
  height: 160px;
}
.box-content {
  line-height: 24px;
  padding: 20px 15px 0;
  background: #fff;
  overflow: hidden;
  position: relative;
  margin-left: 0;
}
.avatar {
  float: left;
  margin-top: 10px;
}
.box-content-right {
  margin-left: 50px;
  margin-top: 10px;
  /* padding-bottom: 10px; */
}
.box-content-right2 {
  margin-left: 90px;
  margin-top: 20px;
}
.jobs {
  padding: 10px 0;
}
.job-tab{
  margin-top: 10px;
  margin-left: -6px;
}
.job-tab,
.reply-tab {
  overflow: hidden;
  padding-bottom: 10px;
}
.job-tab-item {
  font-size: 12px;
  padding: 4px 8px;
  float: left;
  background: #f7f7f7;
  margin: 2px 6px;
}
.tab-tab-item:first-child {
  margin-left: 0;
}
.job-tab-item:last-child,
.tab-tab-item:last-child {
  margin-right: 0;
}
.reply-tab .reply-tab-item {
  font-size: 12px;
  padding: 0 8px;
  float: left;
  background: #f9fafc;
  margin: 4px 6px 0;
  color: #a9abae;
}
.reply-tab-item:first-child {
  margin-left: 0;
}
.reply-tab-item:last-child {
  margin-right: 0;
}
.icon-warning{
  width: 16px;
  height: 16px;
  padding-right: 4px;
  vertical-align: text-top;
}
.connect {
  width: 85px;
  height: 28px;
  line-height: 28px;
  text-align: center;
  color: #fff;
  font-size: 14px;
  position: absolute;
  top: 34px;
  right: 10px;
  background: #1291fe;
  border-radius: 4px;
}

.tab-tab-item {
  font-size: 12px;
  color: #757575;
  padding: 0 8px;
  float: left;
  margin: 6px 6px 0;
  background: #F4F5F7;
  border-radius: 2px;
}
.describe {
  padding: 10px 0;
  color: #8b8d92;
  font-size: 12px;
  line-height: 20px;
}
.point {
  background: #fff;
  padding: 30px 15px 30px 15px;
  line-height: 20px;
}
.point span {
  padding-left: 4px;
}
.footer-content {
  text-align: center;
  color: #8b8d92;
  /* line-height: 42px; */
}
.footer-content .van-button--normal {
  padding: 0 36px;
  line-height: 42px;
  height: 42px;
  border-radius: 4px;
  display: block;
  float: left;
  margin-top: 4px;
}
.footer-content .van-button--normal:last-child {
  width: 100%;
}
.footer .van-icon {
  margin-top: 4px;
}
.list-item-tabs{
  display: flex;
  flex-wrap: wrap;
  margin: 20px 0 0;
}
.list-item-tab{
  display: inline-block;
  padding: 4px 6px;
  background: #F8F6F1;
  color: #97775D;
  font-size: 12px;
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 2px;
}
</style>
