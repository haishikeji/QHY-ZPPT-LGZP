<template>
  <div class="container2">
    <div class="title">
      {{createTime}}</div>
    <div class="content">{{ content }}</div>
  </div>
</template>

<script>

import { getViolationInfo, updateViolation } from '@/api/violation'

export default {
  name: 'VipResumeDetail',
  components: {},
  data () {
    return {
      form: {
      },
      createTime: null,
      content: ''
    }
  },
  created () {
    const id = parseInt(this.$route.query.id)
    this.getParams(id)
    this.updateViolation(id)
  },
  methods: {
    getParams (id) {
      getViolationInfo(id).then(response => {
        const time1 = response.data.createTime.substring(0, 10)
        const time2 = time1.replace('-', '年')
        this.createTime = time2.replace('-', '月') + '日 ' + response.data.createTime.substring(10, 16)
        this.content = response.data.content
      })
    },
    updateViolation (id) {
      this.form.id = id
      updateViolation(this.form).then(response => {
      })
    }
  }
}
</script>
<style scoped>
.container2 {
  padding: 0;
}
.title {
  text-align: center;
  color: #8b8d92;
  line-height: 44px;
}
.content{
  line-height: 20px;
  padding: 10px 15px;
  background: #fff;
  text-indent:2em;
}
</style>
