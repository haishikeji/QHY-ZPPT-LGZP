<template>
  <div class="container2">
    <div class="header-box" style="padding-top: 0; padding-bottom: 2px"></div>
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
    <van-list
      v-model:loading="loading"
      :finished="finished"
      :finished-text="finishedText"
      :immediate-check="false"
      @load="onloadMore"
      ref="vlist"
      :offset="10"
    >
      <van-row v-if="list.length>0">
        <van-col span="24" v-for="(item, i) in list" :key="i" @click="toDetail(item)">
          <van-cell>
            <div class="box van-clearfix">
              <div class="box-content">
                <div class="flex-box">
                  <div class="col3 front16">违规公告<span class="span-dot" v-if="item.isRead==='1'"></span></div>
                  <div class="col8C front12" >
                    {{item.createTime.substring(0, 10)}}
                  </div>
                </div>
                <div class="col8C front12 van-ellipsis" align="left">{{item.content}}</div>
              </div>
              <van-image width="50" height="50" fit="cover" :src="url" />
            </div>
          </van-cell>
        </van-col>
      </van-row>
      <div v-if="list.length==0&&loading==false&&refreshing==false">
          <img src="../../../assets/imgs/none.png" class="none-img" />
        </div>
    </van-list>
    </van-pull-refresh>
  </div>
</template>

<script>
import { listViolation } from '@/api/violation'
export default {
  name: 'VipResume',
  components: {},
  data () {
    return {
      query: {
        pageNum: 1,
        pageSize: 10
      },
      finishedText: '',
      refreshing: false,
      loading: false,
      finished: false,
      list: [{

      }],
      url: ''
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.onLoad()
    })
  },
  methods: {
    onRefresh () {
      this.query.pageNum = 1
      // 清空列表数据
      this.list = []
      this.finished = false
      this.refreshing = true
      // this.loading = true
      this.finishedText = ''
      this.onLoad()
    },
    onloadMore () {
      if (this.refreshing === true) {
        return false
      }
      this.onLoad()
    },
    onLoad () {
      listViolation(this.query).then(response => {
        this.loading = false
        this.refreshing = false
        if (response.code === 200) {
          console.log(response.rows)
          this.list = this.list.concat(response.rows)
          const num = Math.ceil(response.total / this.query.pageSize)
          if (this.query.pageNum >= num) {
            // 数据全部加载完成
            this.finished = true
          } else {
            this.query.pageNum++
          }
          this.list.length === 0 ? (this.finishedText = '') : (this.finishedText = '没有更多了')
        } else {
          this.finished = true
        }
      })
    },
    toDetail (item) {
      this.$router.push({ path: '/violationDetail', query: { id: item.id } })
    }
  }
}
</script>
<style scoped>
.container2 {
  padding: 0;
}
.box {
  overflow: hidden;
}
.van-col--24{
  background: #fff;
  margin-bottom: 10px;
  padding: 10px 15px;
}
.box .van-image {
  float: left;
}
.box-content {
  margin-left: 60px;
  line-height: 24px;
}
.span-dot{
  display: inline-block;
  width: 8px;
  height: 8px;
  background: #FF0000;
  border-radius: 50%;
  vertical-align: super;
  margin-left: 3px;
}
.van-list{
  background: #f7f7f7;
  padding: 0;
  height: calc(100vh - 50px)!important;
  overflow: scroll;
}

</style>
