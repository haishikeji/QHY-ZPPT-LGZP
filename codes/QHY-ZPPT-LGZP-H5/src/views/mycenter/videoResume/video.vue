<template>
  <div class="container2">
    <div class="header" @click="goBack">
      <van-icon name="arrow-left" color="#fff" size="16" />
    </div>
    <div class="video-box">
      <video
        id="videoOnly"
        ref="videoOnly"
        class="video-js vjs-default-skin vjs-big-play-centered vjs-fill"
        preload="auto"
        data-setup="{}"
      ></video>
    </div>

  </div>
</template>

<script>
import { ActionSheet, ShareSheet, Swipe, SwipeItem, Toast } from 'vant'
import videojs from 'video.js'
export default {
  name: 'SnsVideo',
  components: {
    [ActionSheet.name]: ActionSheet,
    [ShareSheet.name]: ShareSheet,
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [Toast.name]: Toast
  },
  data () {
    return {
      info: {},
      player: undefined
    }
  },
  mounted () {
    this.info = this.$route.query.url
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    init () {
      this.player = videojs('videoOnly', {
        // 视频源
        sources: [{
          src: this.info,
          type: 'video/mp4'
        }],
        // 是否显示播放按钮
        bigPlayButton: true,
        playbackRates: [0.5, 1, 1.5, 2],
        controls: true,
        autoplay: true, // 如果true,浏览器准备好时开始播放。
        muted: true, // 默认情况下将会消除任何音频。
        loop: true, // 导致视频一结束就重新开始。
        preload: 'auto', // auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN', // 汉化
        // fluid: true, // 当true时，播放器将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        // 设置小喇叭音量键
        controlBar: {
          fullscreenToggle: true,
          // 音量条的横向还是纵向显示
          volumePanel: {
            inline: false
          }
        }
      },
      function onPlayerReady () {
        this.on('click', function (e) {
          if (this.paused()) {
            this.play()
          } else {
            this.pause()
          }
        })
      }
      )
    },
    goBack () {
      this.player.dispose()
      this.$router.go(-1)
    }

  }
}
</script>
<style>
.video-js .vjs-big-play-button {
  font-size: 2.5em;
  line-height: 1.7em !important;
  height: 2.5em;
  width: 2.5em;
  -webkit-border-radius: 2.5em;
  -moz-border-radius: 2.5em;
  border-radius: 2.5em;
  background-color: #73859f;
  background-color: rgba(115, 133, 159, 0.5);
  border-width: 0.15em;
  margin-top: -1.25em;
  margin-left: -1.0225em;
}
/* 中间的播放箭头 */
.vjs-big-play-button .vjs-icon-placeholder {
  font-size: 1.63em;
}
/* 加载圆圈 */
.vjs-loading-spinner {
  font-size: 2.5em;
  width: 2em;
  height: 2em;
  border-radius: 1em;
  margin-top: -1em;
  margin-left: -1.5em;
}
</style>
<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 2;
  padding-left: 10px;
}
.container2 {
  padding: 0;
}
video {
  width: 100%;
  height: 100vh;
}
/* .video-js .vjs-big-play-button {
  background-color: rgba(0,0,0,0.6);
  font-size: 4em;
  border-radius: 50%;
  height: 2em !important;
  line-height: 2em !important;
  width: 2em !important;
  left: calc(103%/2);
  top: calc((100%)/2);
  transition: transform 0.5s ease;
}

 .video-js .vjs-big-play-button:hover{
  transform: scale(1.2,1.2);
} */

.video-box {
  width: 100%;
  height: 100vh;
  position: relative;
}
video::-webkit-media-controls {
  overflow: hidden !important;
}
video::-webkit-media-controls-enclosure {
  width: calc(100% + 32px);
  margin-left: auto;
}

.video-box-right {
  position: absolute;
  right: 10px;
  bottom: 35%;
  z-index: 5;
  color: #fff;
}
.video-js .vjs-big-play-button {
  left: 40% !important;
  top: 40% !important;
  width: 20%;
  height: 20%;
}

.video-js .vjs-play-control:before {
  top: 20% !important;
  content: "\f101";
  font-size: 48px;
}
.logo-img {
  position: relative;
}
.guanzhu {
  position: absolute;
  top: 30px;
  left: 13px;
}
.focus-img {
  width: 40px;
  margin: 20px auto;
  text-align: center;
  font-size: 12px;
}
.focus-img img {
  width: 30px;
  height: 30px;
}
.focus-img span{
  display: block;
}
.video-box-bottom {
  position: absolute;
  bottom: 26px;
  left: 15px;
  right: 15px;
  color: #fff;
}
.video-box-bottom .title {
  line-height: 44px;
}
.video-box-bottom ul {
  overflow: hidden;
}
.video-box-bottom ul li {
  float: left;
  margin: 0 10px 6px 0;
  padding: 4px 10px;
  background: #796b61;
}
.action-sheet {
  padding: 15px 0;
}
.action-sheet .title {
  font-size: 18px;
  line-height: 44px;
  color: #595959;
  padding-left: 15px;
}
.action-sheet .second-title {
  line-height: 33px;
  background: #f3f3f3;
  font-size: 15px;
  color: #595959;
  padding-left: 15px;
}
.icon-zan {
  width: 20px;
  height: 20px;
  vertical-align: text-bottom;
}
.content-list {
  padding: 15px 0;
}
.content-list li {
  overflow: hidden;
  margin-bottom: 10px;
}
.front15 {
  font-weight: bolder;
}
.pos {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 6px 0;
  border-top: solid 1px #f2f2f2;
  background: #fff;
}
.pos .span {
  position: absolute;
  top: 20px;
  right: 12px;
  z-index: 9;
}
.van-swipe {
  height: 100vh;
  /* height: calc(100vh - 46px); */
  overflow: hidden;
}
.content-list-right{
  width: calc(100% - 50px);
  line-height: 16px;
}
.van-list{
  height: 36vh;
  overflow-y: auto;
}
.cell{
  margin-bottom: 10px;
}
</style>
