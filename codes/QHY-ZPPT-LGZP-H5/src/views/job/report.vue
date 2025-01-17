<template>
  <div class="container2">
    <div class="box-content">
      <!-- TODO -->
      <van-image
        width="40"
        height="40"
        fit="cover"
        round
        src="https://img.yzcdn.cn/vant/apple-1.jpg"
        class="avatar"
      />
      <div class="box-content-right">
        <div class="flex-box">
          <div class="col3 front18">李艳梅</div>
        </div>
        <div class="flex-box" style="line-height: 20px">
          <div class="col9">好未来 · 产品运营</div>
        </div>
      </div>
    </div>
    <div class="flex-box list">
      <div>举报对方</div>
      <div><van-icon name="arrow" size="20" color="#999999" /></div>
    </div>
  </div>
</template>

<script>
import { Swipe, SwipeItem } from "vant";
export default {
  name: "JobListDetail",
  components: {
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
  },
  data() {
    return {
      images: [
        "https://img.yzcdn.cn/vant/apple-1.jpg", //TODO
        "https://img.yzcdn.cn/vant/apple-2.jpg", //TODO
      ],
      collect: false,
      like: false,
    };
  },
  methods: {
    getCollect() {
      this.collect = !this.collect;
    },
    getLike() {
      this.like = !this.like;
    },
    toChat(str) {
      this.$router.push({ path: "/chat", query: { type: str } });
    },
  },
};
</script>
<style scoped>
.container2 {
  padding: 0 0 50px 0;
}
.box-content {
  line-height: 24px;
  padding: 10px 15px 0;
  background: #fff;
  overflow: hidden;
  position: relative;
}
.avatar {
  float: left;
  margin-top: 10px;
}
.box-content-right {
  margin-left: 60px;
  margin-top: 10px;
  padding-bottom: 10px;
}
.box-content-right2 {
  margin-left: 90px;
  margin-top: 10px;
}
.list {
  margin-top: 10px;
  background: #fff;
  line-height: 24px;
  padding: 10px 15px;
}
</style>
