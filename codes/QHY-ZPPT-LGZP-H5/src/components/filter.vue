<template>
  <van-nav-bar title="筛选" :border="false" left-arrow @click-left="onClickLeft" style="position: fixed;width: 100%;" />
  <div class="container3">
    <div class="part">
      <div class="title">期望薪资</div>
      <van-row gutter="10">
        <van-col span="8" v-for="(item,i) in salaryList" :key="i" @click="changeSalary(item)">
          <div :class="['span-cell',{ active: item.checked }]" >{{item.name}}</div>
        </van-col>
      </van-row>
    </div>
    <!-- <div class="part">
      <div class="title">工作经验</div>
      <van-row gutter="10">
        <van-col span="8" v-for="(item,i) in experinceList" :key="i" @click="changeExperience(item)">
          <div :class="['span-cell',{ active: item.checked }]">{{item.name}}</div>
        </van-col>
      </van-row>
    </div> -->
    <!-- <div class="part">
      <div class="title">学历要求</div>
      <van-row gutter="10">
        <van-col span="8" v-for="(item,i) in educationList" :key="i" @click="changeEducation(item)">
          <div :class="['span-cell',{ active: item.checked }]">{{item.name}}</div>
        </van-col>
      </van-row>
    </div> -->
    <div class="part">
      <div class="title">工作类型</div>
      <van-row gutter="10">
        <van-col span="8" v-for="(item,i) in workTypes" :key="i" @click="changeWorkTypes(item)">
          <div :class="['span-cell',{ active: item.checked }]">{{item.dictLabel}}</div>
        </van-col>
      </van-row>
    </div>
    <div class="part">
      <div class="title">缴纳社保</div>
      <van-row gutter="10">
        <van-col span="8" v-for="(item,i) in socials" :key="i" @click="changeSocials(item)">
          <div :class="['span-cell',{ active: item.checked }]">{{item.dictLabel}}</div>
        </van-col>
      </van-row>
    </div>
    <!-- <div class="part">
      <div class="title">工作性质</div>
      <van-row gutter="10">
        <van-col span="8" v-for="(item,i) in jobTypeList" :key="i" @click="changeJob(item)">
          <div :class="['span-cell',{ active: item.checked }]">{{item.dictLabel}}</div>
        </van-col>
      </van-row>
    </div> -->
    <div class="footer">
      <van-row>
        <van-col span="8">
          <div class="footer-btn-left" @click = 'reset'>重置</div>
        </van-col>
        <van-col span="16">
          <div class="footer-btn-right" @click = 'confirm'>确定</div>
        </van-col>
      </van-row>
    </div>
  </div>
</template>

<script>
import { Slider } from 'vant'
import { getSalaryListNew, getExperienceList, getEducationList, getDicts } from '@/api/common'
export default {
  name: 'Filter',
  components: {
    [Slider.name]: Slider
  },
  data () {
    return {
      value: [20, 30],
      salaryList: [],
      experinceList: [],
      educationList: [],
      jobTypeList: [],
      workTypes: [],
      socials: [],
      // 选中的id集合
      chooeseExperinceIdList: [],
      chooeseEducationIdList: [],
      chooseJobTypeIdList: [],
      chooseSalaryIdList: [],
      chooseWorkTypes: [],
      social: []
    }
  },
  props: {
    data: Object
  },
  created () {
    // this.getExperienceList()
    // this.getSalaryList()
    // this.getEducationList()
    // this.getDicts()
    this.initData()
  },
  methods: {
    // 初始化选中的数据
    initData () {
      console.log()
      var that = this
      // 工作类型
      that.workTypes = that.data.get('workTypes')
      // 缴纳社保
      that.socials = that.data.get('socials')
      // 薪资
      var salaryList = new Promise(function (resolve, reject) {
        getSalaryListNew().then((response) => {
          if (response.code === 200) {
            for (const item of response.data) {
              item.checked = false
            }
            salaryList = response.data
            resolve(salaryList)
          } else {
            // eslint-disable-next-line prefer-promise-reject-errors
            reject()
          }
        })
      })
      // 经验
      // var experienceList = new Promise(function (resolve, reject) {
      //   getExperienceList().then((response) => {
      //     if (response.code === 200) {
      //       for (const item of response.data) {
      //         item.checked = false
      //       }
      //       experienceList = response.data
      //       resolve(experienceList)
      //     } else {
      //       // eslint-disable-next-line prefer-promise-reject-errors
      //       reject()
      //     }
      //   })
      // })
      // 学历
      // var educationList = new Promise(function (resolve, reject) {
      //   getEducationList().then((response) => {
      //     if (response.code === 200) {
      //       for (const item of response.data) {
      //         item.checked = false
      //       }
      //       educationList = response.data
      //       resolve(educationList)
      //     } else {
      //       // eslint-disable-next-line prefer-promise-reject-errors
      //       reject()
      //     }
      //   })
      // })
      // 工作性质
      // var jobTybeList = new Promise(function (resolve, reject) {
      //   getDicts('job_type').then(response => {
      //     if (response.code === 200) {
      //       for (const item of response.data) {
      //         item.checked = false
      //       }
      //       jobTybeList = response.data
      //       resolve(jobTybeList)
      //     } else {
      //       reject()
      //     }
      //   })
      // })
      Promise.all([salaryList]).then(function (results) {
        // that.educationList = educationList
        that.salaryList = salaryList
        // that.experinceList = experienceList
        // that.jobTypeList = jobTybeList
        if (that.data !== null) {
          // 工作性质
          // if (that.data.get('jobType') != null && that.data.get('jobType').length > 0) {
          //   that.chooseJobTypeIdList = that.data.get('jobType')
          //   for (const data of that.chooseJobTypeIdList) {
          //     for (const item of that.jobTypeList) {
          //       if (data === item.dictValue) {
          //         item.checked = true
          //       }
          //     }
          //   }
          // }
          // if (that.data.get('education') != null) {
          //   that.chooeseEducationIdList = that.data.get('education')
          //   for (const data of that.chooeseEducationIdList) {
          //     for (const item of that.educationList) {
          //       if (data === item.id) {
          //         item.checked = true
          //       }
          //     }
          //   }
          // }
          // if (that.data.get('experience') != null) {
          //   that.chooeseExperinceIdList = that.data.get('experience')
          //   for (const data of that.chooeseExperinceIdList) {
          //     for (const item of that.experinceList) {
          //       if (data === item.id) {
          //         item.checked = true
          //       }
          //     }
          //   }
          // }
          if (that.data.get('salary') != null) {
            that.chooseSalaryIdList = that.data.get('salary')
            for (const data of that.chooseSalaryIdList) {
              for (const item of that.salaryList) {
                if (data === item.id) {
                  item.checked = true
                }
              }
            }
          }
        }
      })
    },
    // 薪资
    getSalaryList () {
      getSalaryListNew().then((response) => {
        if (response.code === 200) {
          for (const item of response.data) {
            item.checked = false
          }
          this.salaryList = response.data
        }
      })
    },
    changeSalary (info) {
      info.checked = !info.checked
      if (info.checked) {
        this.chooseSalaryIdList.push(info.id)
      } else {
        var index = this.chooseSalaryIdList.indexOf(info.id)
        this.chooseSalaryIdList.splice(index, 1)
      }
    },
    // 经验
    getExperienceList () {
      getExperienceList().then((response) => {
        if (response.code === 200) {
          for (const item of response.data) {
            item.checked = false
          }
          this.experinceList = response.data
        }
      })
    },
    changeExperience (info) {
      info.checked = !info.checked
      if (info.checked) {
        this.chooeseExperinceIdList.push(info.id)
      } else {
        var index = this.chooeseExperinceIdList.indexOf(info.id)
        this.chooeseExperinceIdList.splice(index, 1)
      }
    },
    // 学历
    getEducationList () {
      getEducationList().then((response) => {
        if (response.code === 200) {
          for (const item of response.data) {
            item.checked = false
          }
          this.educationList = response.data
        }
      })
    },
    changeEducation (info) {
      info.checked = !info.checked
      if (info.checked) {
        this.chooeseEducationIdList.push(info.id)
      } else {
        var index = this.chooeseEducationIdList.indexOf(info.id)
        this.chooeseEducationIdList.splice(index, 1)
      }
    },
    // 求职状态
    getDicts () {
      getDicts('job_type').then(response => {
        if (response.code === 200) {
          for (const item of response.data) {
            item.checked = false
          }
          this.jobTypeList = response.data
        }
      })
    },
    // 工作类型 0：长白班，1：轮班
    changeWorkTypes (info) {
      info.checked = !info.checked
    },
    // 缴纳社保 0：是 1：否
    changeSocials (info) {
      info.checked = !info.checked
      // for (const item of this.socials) {
      //   item.checked = false
      // }
      // info.checked = true
    },
    changeJob (info) {
      info.checked = !info.checked
      if (info.checked) {
        this.chooseJobTypeIdList.push(info.dictValue)
      } else {
        var index = this.chooseJobTypeIdList.indexOf(info.dictValue)
        this.chooseJobTypeIdList.splice(index, 1)
      }
    },
    onClickLeft () {
      this.$emit('goBack', false)
    },
    reset () {
      this.chooeseExperinceIdList = []
      this.chooeseEducationIdList = []
      this.chooseSalaryIdList = []
      this.chooseJobTypeIdList = []
      for (const item of this.salaryList) {
        item.checked = false
      }
      for (const item of this.experinceList) {
        item.checked = false
      }
      for (const item of this.educationList) {
        item.checked = false
      }
      for (const item of this.jobTypeList) {
        item.checked = false
      }
      for (const item of this.workTypes) {
        item.checked = false
      }
      for (const item of this.socials) {
        item.checked = false
      }
    },
    confirm () {
      this.chooseWorkTypes = []
      for (const item of this.workTypes) {
        if (item.checked === true) {
          this.chooseWorkTypes.push(item.value)
        }
      }
      this.social = []
      for (const item of this.socials) {
        if (item.checked === true) {
          this.social.push(item.value)
        }
      }
      var result = new Map()
      result.set('jobType', this.chooseJobTypeIdList)
      result.set('salary', this.chooseSalaryIdList)
      result.set('experience', this.chooeseExperinceIdList)
      result.set('education', this.chooeseEducationIdList)
      result.set('workTypes', this.chooseWorkTypes)
      result.set('social', this.social)
      this.$emit('chooesData', result)
    }
  }
}
</script>
<style scoped>
.container3{
  padding:60px 15px;
  height: calc(100vh -140px)!important;
  /* padding-bottom: 50px; */
  overflow-y:auto;
}
  .part .title{
    color: #333;
    line-height: 44px;
    font-weight: bolder;
  }
  .part .span-cell{
    text-align: center;
    border: solid 1px #666;
    color: #666;
    border-radius: 4px;
    line-height: 24px;
  }
  .footer{
    text-align: center;
    line-height: 46px;
    padding: 0;
  }
  .footer .footer-btn-left{
    background: #f7f7f7;
    color: #666;
  }
   .footer .footer-btn-right{
    background: #05c160;
    color: #fff;
  }
   .van-col--8{
     margin-bottom: 10px;
   }
   .span-cell.active{
     background: #05c160;
     color: #ffffff;
     border: solid 1px #05c160;
   }
</style>
