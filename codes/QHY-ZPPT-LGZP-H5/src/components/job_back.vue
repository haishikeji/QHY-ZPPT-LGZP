<template>
  <div class="container2">
    <van-nav-bar title="期望职位" :border="false" left-arrow @click-left="onClickLeft" />
    <div class="treebox">
      <van-tree-select
        v-model:main-active-index="items.id"
        :items="items"
        @clickNav="getData"
      >
        <template #content>
          <div class="boxs">
            <div v-for="(item, i) in childrenList" :key="i">
              <div class="item-title col3">{{ item.name }}</div>
              <div class="overhid">
                <div
                  v-for="(info, index) in item.children"
                  :key="index"
                  :class="[{ active: item.checked }, 'item-box']"
                  @click="getInfo(info)"
                >
                  {{ info.name }}
                </div>
              </div>
            </div>
          </div>
        </template>
      </van-tree-select>
    </div>
  </div>
</template>

<script>
import { getFirstClassCarea, getChildCarea } from '@/api/baseInfo'
export default {
  name: 'Job',
  components: {},
  data () {
    return {
      title: '',
      isleftarrow: true,
      items: [],
      activeIndex: 0,
      childrenList: [],
      bid: 0
    }
  },
  created () {
    this.getFirstClassCarea()
  },
  methods: {
    onClickLeft () {
      this.$emit('goBack', false)
    },
    getFirstClassCarea () {
      var that = this
      getFirstClassCarea().then(response => {
        if (response.code === 200) {
          for (const item of response.data) {
            item.text = item.name
          }
          that.items = response.data
          that.getData(0)
        }
      })
    },
    getData (index) {
      var that = this
      that.bid = that.items[index].id
      getChildCarea({ id: that.items[index].id }).then(response => {
        if (response.code === 200) {
          that.childrenList = response.data[0].children
        }
      })
    },
    getInfo (item) {
      const info = { value: item.id, name: item.name, branchId: this.bid }
      this.$emit('getCareasObj', info)
    },
    goNext () {
      this.$router.push('/identity')
    }
  }
}
</script>
<style scoped>
.container2 {
  padding: 0;

}
.van-tree-select{
 min-height: calc(100vh - 52px);
}
.boxs {
  overflow: hidden;
}
.item-title {
  line-height: 44px;
  margin-left: 2%;
}
.item-box {
  width: 46%;
  height: 32px;
  line-height: 32px;
  text-align: center;
  background: #f7f7f7;
  float: left;
  margin: 0 2% 10px 2%;
}

</style>
