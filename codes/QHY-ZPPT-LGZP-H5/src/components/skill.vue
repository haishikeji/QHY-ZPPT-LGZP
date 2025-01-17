<template>
    <van-nav-bar title="选择拥有的技能" :border="false" left-arrow @click-left="onClickLeft" style="position: fixed;width: 100%;" />
  <div class="container3">
    <div class="choose-title">
      <p class="front20">拥有技能</p>
      <p class="col9 front13">最多选择5个，被选中的标签将突出展示</p>
      <div class="top-box">
        <div class="fl">已选</div>
        <ul class="tabs">
          <li v-for="(chooseItem, i) in chooseSkillList" :key="i" @click="delChooeseItem(i)">{{chooseItem.name}}<van-icon name="cross"/></li>
        </ul>
      </div>
    </div>
    <div class="list">
      <div class="list-item"  v-for="(item, i) in allSkillList" :key="i">
        <div class="col3 front16">{{item.type}}</div>
        <ul class="grey-tabs">
          <li v-for="(childSkill, i) in item.skillList" :key="i"  @click="chooeseItem(childSkill)">{{childSkill.name}}</li>
        </ul>
      </div>
    </div>
    <!-- style="position: absolute;bottom: 0;left: 0;right: 0;background:#fff;border-top:solid 1px #f2f2f2;" -->
    <div class="footer2">
      <div style="padding:2px 15px;">
        <!-- 保存 -->
        <van-button type="primary" block round @click="toSave">保存</van-button>
      </div>
    </div>

  </div>
</template>

<script>
import { getSystemSkillList } from '@/api/userResume'
export default {
  name: 'Skill',
  components: {},
  data () {
    return {
      // 所有的系统技能项
      allSkillList: [],
      // 选择的系统技能项id
      chooseSkillList: []
    }
  },
  props: {
    id: Number
  },
  created () {
    this.getSystemSkillList(this.id)
  },
  methods: {
    // 根据职位id查询系统技能
    getSystemSkillList (id) {
      getSystemSkillList({ careasId: id }).then(response => {
        if (response.code === 200) {
          this.allSkillList = response.data
        }
      })
    },
    onClickLeft () {
      this.$emit('goBack', false)
    },
    // 点击了选项
    chooeseItem (value) {
      var that = this
      if (this.chooseSkillList.length > 4) {
        that.$toast({
          message: '最多只能选择5个',
          icon: 'none',
          duration: 2000
        })
        return
      }
      for (const item of this.chooseSkillList) {
        if (item.id === value.id && item.type === value.type) {
          return false
        }
      }
      this.chooseSkillList.push(value)
    },
    // 删除选项
    delChooeseItem (value) {
      this.chooseSkillList.splice(value, 1)
    },
    toSave () {
      this.$emit('chooseSkillList', this.chooseSkillList)
    }
  }
}
</script>
 <style scoped>
 .container3{
  padding:50px 0;
  height: calc(100vh -140px)!important;
  /* padding-bottom: 50px; */
  overflow-y:auto;
}
.choose-title {
  line-height: 28px;
  margin: 10px 0;
  padding: 0 15px;
}
.container2 {
  background: #fff;
}
.fl{
  float: left;
}
.top-box{
 overflow: hidden;
}
.tabs {
  margin-left: 30px;
  background: #fff;
  color: #05c160;
}
.tabs li {
  padding: 0 10px;
  line-height: 24px;
  height: 24px;
  float: left;
  margin: 4px 6px;
  background: #f7f7f7;
  border-radius: 4px;
}
.list {
  line-height: 44px;
  background: #fff;
  padding: 0 16px 10px;
}

.van-image {
  vertical-align: sub;
}
.grey-tabs {
  color: #000000;
  overflow: hidden;
  margin-left: -6px;
}
.grey-tabs li{
  padding: 0 10px;
  background: #f2f2f2;
  float: left;
  line-height: 24px;
  margin: 4px 6px;
}
.flex-box {
  border-bottom: solid 1px #f2f2f2;
}
.flex-box:last-child {
  border-bottom: none;
}
</style>
