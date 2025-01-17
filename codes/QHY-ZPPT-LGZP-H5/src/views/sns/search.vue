<template>
  <div class="container">
    <div class="choose-title2">
      <van-search
        class="clear-icon"
        v-model="cityName"
        shape="round"
        background="#1291fe"
        placeholder="请输入搜索职位、公司"
        :formatter="formatter"
        @update:model-value="onSearch"
        @search="onSearch"
      />
      <ul v-if="searchList&&searchList.length>0" class="search-list">
        <li v-for="(item, i) in searchList" :key="i" @click="getRecruitList(item)">{{item.name}}</li>
      </ul>
    </div>
    <div v-if="showRecord">
      <div class="record" v-if="searchRecordList.length">
        <div class="record-title front15">搜索记录</div>
        <ul class="record-item">
          <li v-for="(item, i) in searchRecordList" :key="i" @click="clickSeachRecord(item)">{{item.content}}</li>
        </ul>
        <div class="img-del" @click = 'delSearchRecord'>
          <img src="../../assets/imgs/del.png"/>清除记录
        </div>
      </div>
      <div class="record">
        <div class="record-title front15">职位推荐</div>
        <ul class="record-item">
          <li v-for="(item, i) in recommendList" :key="i" @click="clickRecommendRecord(item)"><img src="../../assets/imgs/hot.png" class="img-hot"/>{{item.name}}</li>
        </ul>
      </div>
    </div>

    <van-list
     v-if="list&&list.length>0"
        v-model:loading="loading"
        :finished="finished"
        :finished-text="finishedText"
        ref="vlist"
        @load="getRecruitList"
        :offset="10"
      >
        <div class="list">
          <div class="list-item" v-for="(item, i) in list" :key="i" @click="toDetail(item)">
            <div class="box">
              <van-image
                width="4.45rem"
                :height="i === 0 ? '7.25rem' : '8.86667rem'"
                :src="item.imgUrl"
              />

              <div class="model">
                <div>
                  <van-image
                    width="16"
                    height="16"
                    fit="cover"
                    round
                    :src="item.imageUrl ? item.imageUrl : imgUrl"
                    class="avatar"
                  />
                </div>
                <div class="span">{{ item.companyName }}</div>
                <div>{{ item.playNum }}次播放</div>
              </div>
            </div>
          </div>
        </div>
      </van-list>
  </div>
</template>
<script>
import { Search, IndexBar, IndexAnchor } from 'vant'
import { getRecruiltVideoList } from '@/api/video'
import { getSearchRecordList, getReCommendCarea, delSearchRecord, addSearch, search } from '@/api/common'
import { regStr } from '@/utils/emoji'
export default {
  name: 'SnsSearch',
  components: {
    [Search.name]: Search,
    [IndexBar.name]: IndexBar,
    [IndexAnchor.name]: IndexAnchor
  },
  setup () {
    // 过滤输入的数字
    const formatter = (value) => value.replace(regStr, '')
    return {
      formatter
    }
  },
  data () {
    return {
      cityName: '',
      name: '',
      loading: false,
      finished: false,
      showRecord: true, // 历史记录
      list: [],
      // 搜索记录
      searchRecordList: [],
      // 职位推荐
      recommendList: [],
      searchList: [], // 监听输入框内容，调取接口
      params: {
        pageNum: 1,
        pageSize: 20
      }
    }
  },
  props: {
    title: String
  },
  created () {
    this.getSearchRecord()
    this.getReCommendList()
    console.log(this.list.length)
  },
  methods: {
    onSearch (val) {
      if (val) {
        this.showRecord = false
        this.onLoad(val)
      } else {
        this.list = []
        this.searchList = []
        this.showRecord = true
        this.getSearchRecord()
        this.getReCommendList()
      }
    },
    onLoad (val) {
      this.loading = false
      this.finished = true
      const parameter = {
        content: this.cityName,
        areaId: ''
      }
      search(parameter).then(response => {
        if (response.code === 200) {
          this.list = []
          this.searchList = response.data
          if (response.data.length < 1) {
            // this.showRecord = true
            this.$toast('暂无数据~')
          }
        }
      })
    },
    onBack () {
      history.back()
    },
    // 获取搜索记录
    getSearchRecord () {
      const data = {
        pageType: '2'
      }
      getSearchRecordList(data).then(response => {
        if (response.code === 200) {
          this.searchRecordList = response.rows
        }
      })
    },
    // 获取推荐职位
    getReCommendList () {
      getReCommendCarea().then(response => {
        if (response.code === 200) {
          this.recommendList = response.data
        }
      })
    },
    // 清空搜索记录
    delSearchRecord () {
      delSearchRecord().then(response => {
        if (response.code === 200) {
          this.searchRecordList = []
        }
      })
    },
    // 添加搜索记录
    addSearchRecord (data, type) {
      const parameter = {
        content: data.name,
        itemId: data.id,
        type: type,
        pageType: 2
      }
      addSearch(parameter).then(response => {
        if (response.code === 200) {
        }
      })
    },
    // 搜索
    getRecruitList (data) {
      this.cityName = data.name
      // 添加搜索记录
      const addData = {
        name: data.name,
        id: data.id
      }
      this.addSearchRecord(addData, data.type)
      // 职位
      if (data.type === 1) {
        const parameter = {
          shCareaId: data.id
        }
        getRecruiltVideoList(this.params, parameter).then(response => {
          if (response.code === 200) {
            this.searchList = []
            this.loading = false
            if (response.rows !== null) {
              if (response.rows.length > 0) {
                for (const item of response.rows) {
                  if (item.shCompanyRecruit != null) {
                    if (item.shCompanyRecruit.hrId == null) {
                      // 非人资下的企业
                      if (item.shCompany?.trueName != null) {
                        item.companyName = item.shCompany.trueName
                      } else {
                        item.companyName = '暂无'
                      }
                    } else {
                      // 人资下的企业
                      if (item.shCompanyUnderHr) {
                        if (item.shCompanyUnderHr.trueName) {
                          item.companyName = item.shCompanyUnderHr.trueName
                        }
                      } else {
                        item.companyName = '暂无'
                      }
                    }
                    if (item.companyName) {
                      if (item.companyName.length > 4) {
                        item.companyName = item.companyName.substring(0, 4) + '...'
                      }
                    }
                  }
                  if (item.headImages && item.headImages.length > 0) {
                    if (item.headImages[0].content != null) {
                      item.imageUrl = item.headImages[0].content
                    }
                  }
                  if (item.playNum) {
                    if (item.playNum > 10000) {
                      var playNum = item.playNum / 10000
                      item.playNum = playNum.toString() + 'w'
                    }
                  }
                }
                this.list = this.list.concat(response.rows)
                console.log(response.rows.length)
                this.loading = false
                this.finished = true
              }
              const num = Math.ceil(response.total / this.params.pageSize)
              if (this.params.pageNum >= num) {
              // 数据全部加载完成
                this.finished = true
              } else {
                this.params.pageNum++
              }
              this.list.length === 0 ? (this.$toast('暂无数据~')) : (this.finishedText = '没有更多了')
            } else {
              this.finished = true
              this.loading = false
              this.refreshing = false
            }
          }
        })
        // 公司
      } else if (data.type === 2) {
        const parameter = {
          companyId: data.id
        }
        getRecruiltVideoList(this.params, parameter).then(response => {
          if (response.code === 200) {
            this.searchList = []
            this.loading = false
            if (response.rows !== null) {
              if (response.rows.length > 0) {
                for (const item of response.rows) {
                  if (item.shCompanyRecruit != null) {
                    if (item.shCompanyRecruit.hrId == null) {
                      // 非人资下的企业
                      if (item.shCompany?.trueName != null) {
                        item.companyName = item.shCompany.trueName
                      } else {
                        item.companyName = '暂无'
                      }
                    } else {
                      // 人资下的企业
                      if (item.shCompanyUnderHr) {
                        if (item.shCompanyUnderHr.trueName) {
                          item.companyName = item.shCompanyUnderHr.trueName
                        }
                      } else {
                        item.companyName = '暂无'
                      }
                    }
                    if (item.companyName) {
                      if (item.companyName.length > 4) {
                        item.companyName = item.companyName.substring(0, 4) + '...'
                      }
                    }
                  }
                  if (item.headImages && item.headImages.length > 0) {
                    if (item.headImages[0].content != null) {
                      item.imageUrl = item.headImages[0].content
                    }
                  }
                  if (item.playNum) {
                    if (item.playNum > 10000) {
                      var playNum = item.playNum / 10000
                      item.playNum = playNum.toString() + 'w'
                    }
                  }
                }
                this.list = this.list.concat(response.rows)
                console.log(response.rows.length)
                this.loading = false
                this.finished = true
              }
              const num = Math.ceil(response.total / this.params.pageSize)
              if (this.params.pageNum >= num) {
              // 数据全部加载完成
                this.finished = true
              } else {
                this.params.pageNum++
              }
              this.list.length === 0 ? (this.$toast('暂无数据~')) : (this.finishedText = '没有更多了')
            }
          } else {
            this.finished = true
            this.loading = false
            this.refreshing = false
          }
        })
      } else {
        const parameter = {
          hrId: data.id
        }
        getRecruiltVideoList(this.params, parameter).then(response => {
          if (response.code === 200) {
            this.searchList = []
            this.list = response.rows
            this.loading = false
            if (response.rows !== null) {
              if (response.rows.length > 0) {
                for (const item of response.rows) {
                  if (item.shCompanyRecruit != null) {
                    if (item.shCompanyRecruit.hrId == null) {
                      // 非人资下的企业
                      if (item.shCompany?.trueName != null) {
                        item.companyName = item.shCompany.trueName
                      } else {
                        item.companyName = '暂无'
                      }
                    } else {
                      // 人资下的企业
                      if (item.shCompanyUnderHr) {
                        if (item.shCompanyUnderHr.trueName) {
                          item.companyName = item.shCompanyUnderHr.trueName
                        }
                      } else {
                        item.companyName = '暂无'
                      }
                    }
                    if (item.companyName) {
                      if (item.companyName.length > 4) {
                        item.companyName = item.companyName.substring(0, 4) + '...'
                      }
                    }
                  }
                  if (item.headImages && item.headImages.length > 0) {
                    if (item.headImages[0].content != null) {
                      item.imageUrl = item.headImages[0].content
                    }
                  }
                  if (item.playNum) {
                    if (item.playNum > 10000) {
                      var playNum = item.playNum / 10000
                      item.playNum = playNum.toString() + 'w'
                    }
                  }
                }
                this.list = this.list.concat(response.rows)
                console.log(response.rows.length)
                this.loading = false
                this.finished = true
              }
              const num = Math.ceil(response.total / this.params.pageSize)
              if (this.params.pageNum >= num) {
                // 数据全部加载完成
                this.finished = true
              } else {
                this.params.pageNum++
              }
              this.list.length === 0 ? (this.$toast('暂无数据~')) : (this.finishedText = '没有更多了')
            }
          }
        })
      }
    },
    toDetail (item) {
      const query = {
        jobType: ''
      }
      this.$router.push({ path: '/snsVideo', query: { id: item.id, query: JSON.stringify(query) } })
    },
    // 点击了搜索记录
    clickSeachRecord (data) {
      this.showRecord = false
      const infoData = {
        id: data.itemId,
        type: parseInt(data.type),
        name: data.content
      }
      this.getRecruitList(infoData)
    },
    // 点击了职位推荐
    clickRecommendRecord (data) {
      this.showRecord = false
      const infoData = {
        id: data.id,
        type: 1,
        name: data.name
      }
      this.getRecruitList(infoData)
    }
  }
}
</script>
<style>
.clear-icon{
  margin-right: 10px;
}
.container {
  padding: 0;
  height: calc(100vh - 46px);
}
.cell {
  text-align: center;
  line-height: 32px;
  margin-bottom: 10px;
  background: #f7f7f7;
  border-radius: 4px;
}
.choose-title2 {
  background: #1291fe;
  padding: 0 15px 10px;
  position: relative;
}
.search-list{
  position: absolute;
  top: 54px;
  left: 0;
  right: 0;
  line-height: 36px;
  font-size: 14px;
  padding-left: 15px;
}
.search-list li{
  border-bottom: solid 1px #f2f2f2;
}
.search-list li:last-child{
  border-bottom: none;
}
.record{
  position: relative;
  padding: 0 15px;
}
.img-del{
  position: absolute;
  top: 0;
  right: 15px;
  color: #b2b2b2;
}
.img-del img{
  width: 16px;
  height: 16px;
  vertical-align: sub;
  margin-right: 4px;
}
.record-title {
  margin: 20px 0 0;
  color: #414141;
  font-weight: bolder;
}
.record-item {
  overflow: hidden;
  margin-left: -6px;
}
.record-item li {
  float: left;
  padding: 6px 12px;
  background: #f5f5f5;
  border-radius: 10px;
  color: #615d64;
  margin: 6px 6px 0;
}
.img-hot{
  width: 16px;
  height: 16px;
  vertical-align: text-bottom;
}
.box {
  position: relative;
}
.model {
  width: 100%;
  height: 36px;
  line-height: 36px;
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  border-radius: 2px 2px 0px 0px;
  background-color: rgba(51, 51, 51, 0.4);
  display: flex;
  justify-content: space-around;
  color: #fff;
  font-size: 12px;
}
.avatar {
  vertical-align: sub;
  margin-bottom: 10px;
  margin-top: 10px;
  margin-right: 6px;
}
.span{
  text-align: center;
}
.van-list{
  padding: 10px 0;
}
.van-search input{
  border-radius: 30px;
}
.box {
  overflow: hidden;
}
.van-col--24{
  background: #fff;
  padding: 10px 15px;
  margin-bottom: 10px;
}
.box .van-image {
  float: left;
}
.box-content {
  margin-left: 90px;
  line-height: 24px;
}
.jobs {
  padding: 10px 0;
}
.job-tab {
  overflow: hidden;
}
.job-tab-item {
  font-size: 12px;
  padding: 4px 8px;
  float: left;
  background: #f7f7f7;
  margin: 0 6px;
  border-radius: 4px;
}
.job-tab-item:first-child {
  margin-left: 0;
}
.job-tab-item:last-child {
  margin-right: 0;
}
.van-list{
  background: #fff;
  padding: 0;
  height: calc(100vh - 100px) !important;
  padding: 0;
  overflow: scroll;
}
.list {
  overflow: hidden;
  padding-top: 10px;
}
.list-item {
  float: left;
  width: 4.45rem;
  /* height: 220px; */
}
.list-item:nth-child(odd) {
  margin: -60px 0 10px 15px;
}
.list-item:nth-child(even) {
  margin: 0 15px 10px 10px;
}
.list-item:first-child {
  /* height: 160px; */
  margin-top: 0;
}
</style>
