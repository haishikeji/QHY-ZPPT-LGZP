<template>
  <div class="container2">
    <van-nav-bar title="选择行业" :border="false" left-arrow @click-left="onClickLeft" />
    <div>
      <van-tree-select
        v-model:main-active-index="items.id"
        :items="items"
        @clickNav="getData"
      >
        <template #content>
          <div class="boxs">
            <div v-for="(item,i) in childrenList" :key="i" :class="[{ active: item.checked }, 'item-box']" @click="getInfo(item)">{{item.name}}</div>
          </div>
        </template>
      </van-tree-select>
    </div>
  </div>
</template>

<script>
import { getFirstClassBranch, getChildBranch } from '@/api/common'
export default {
  name: 'Branch',
  components: {},
  data () {
    return {
      items: [],
      activeIndex: 0,
      childrenList: []
    }
  },
  created () {
    this.getFirstClassBranch()
  },
  methods: {
    onClickLeft () {
      this.$emit('goBack', false)
    },
    getFirstClassBranch () {
      var that = this
      getFirstClassBranch().then(response => {
        if (response.code === 200) {
          for (const item of response.data) {
            item.text = item.name
          }
          that.items = response.data
          that.getData(0)
        }
      })
    },
    getData (id) {
      var that = this
      that.bid = that.items[id].id
      getChildBranch({ id: that.bid }).then(response => {
        if (response.code === 200) {
          that.childrenList = response.data[0].children
        }
      })
    },
    getInfo (item) {
      const info = { value: item.id, name: item.name }
      this.$emit('getBranchObj', info)
    }
  }
}
</script>
<style scoped>
.container{
  padding: 6px 0 0 0;
  background: #f7f7f7;
}
.boxs{
  overflow: hidden;
  padding-top: 10px;
}
.item-box{
  width: 46%;
  height: 32px;
  line-height: 32px;
  text-align: center;
  background: #f7f7f7;
  float: left;
  margin: 0 2% 10px 2%;
}
</style>
