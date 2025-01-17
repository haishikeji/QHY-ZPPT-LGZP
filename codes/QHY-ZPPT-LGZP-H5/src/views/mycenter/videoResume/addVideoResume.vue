<template>
  <div class="container">
    <van-row>
      <van-col span="24">
        <div class="col3 front18 title">添加标题</div>
      </van-col>
      <van-col span="24">
        <van-field
          v-model="evaluate"
          placeholder="请输入标题，让更多的人看到你~"
          :formatter="formatter"
        />
      </van-col>
    </van-row>
    <van-row>
      <van-col span="24">
        <div class="col3 front18 title">求职意向</div>
      </van-col>
      <van-col span="24">
        <van-field
          v-model="careasObj.name"
          placeholder="请选择您的求职意向"
          readonly
          @click="showJob = true"
        />
      </van-col>
    </van-row>
    <van-row v-if="!showPic" gutter="20">
      <van-col span="8">
        <div class="pos-box">
          <van-image
            width="100%"
            height="150"
            lazy-load
            :src="defaultImgUrl"
            @click="toDetail"
          />
          <div class="close" @click="delUserResume">×</div>
        </div>
      </van-col>
    </van-row>
    <div class="footer">
      <van-uploader
        v-if="showPic"
        accept="video/*"
        :before-read="beforeRead"
        :after-read="afterRead"
        id="bigBtn"
      >
        <van-button type="primary" block round>上传视频</van-button>
      </van-uploader>
      <van-button type="primary" v-else block round @click="toSave"
        >立即发布</van-button
      >
    </div>
    <van-popup
      v-model:show="showJob"
      position="bottom"
      round
      :style="{ height: '40%' }"
    >
      <div class="self-picker">
        <div class="self-picker__toolbar">
          <van-button class="self-picker__cancel" @click="closeJob"
            >取消</van-button
          >
          <van-button
            class="van-picker__confirm self-picker__confirm"
            @click="confirmJob"
            >确定</van-button
          >
        </div>
        <div class="self-wrapper">
          <van-radio-group v-model="checked">
            <ul>
              <li
                class="flex-box items"
                v-for="(item, i) in intentionList"
                :key="i"
              >
                <div>
                  <div>
                    {{ item.shCareas != null ? item.shCareas.name : "暂无" }}
                    {{ item.shSalary != null ? item.shSalary.name : "暂无" }}
                  </div>
                  <div>
                    {{ item.shArea != null ? item.shArea.name : "暂无" }} 丨
                    {{ item.shBranch != null ? item.shBranch.name : "暂无" }}
                  </div>
                </div>
                <div
                  @click="checkJob(item.id, item.shCareas.name)"
                  class="div-radio"
                >
                  <van-radio :name="item.id" />
                </div>
              </li>
            </ul>
          </van-radio-group>
        </div>
      </div>
    </van-popup>
    <!-- <div v-if="showVideo">

    </div> -->
    <van-popup
      v-model:show="showVideo"
      position="bottom"
      round
      :duration="0"
      :style="{ height: '100%' }"
    >
      <VideoPlayer
        v-if="showVideo"
        :url="this.form.url"
        @closeVideoPlayer="closeVideoPlayer"
      ></VideoPlayer>
    </van-popup>
  </div>
</template>

<script>
import { addUserVideoResume } from "@/api/videoResume";
import { Toast, Uploader, Dialog } from "vant";
import { getUserJobintentionList } from "@/api/video";
import { uploadFiles } from "@/api/feedback";
import { regStr } from "@/utils/emoji";
import VideoPlayer from "@/components/VideoPlayer";
export default {
  name: "addVideoResume",
  components: {
    VideoPlayer,
    [Uploader.name]: Uploader,
  },
  setup() {
    // 过滤输入的数字
    const formatter = (value) => value.replace(regStr, "");
    return {
      formatter,
    };
  },
  data() {
    return {
      checked: null,
      showJob: false,
      evaluate: "",
      careas: "",
      showPic: true,
      careasObj: {
        value: null,
        name: "",
      },
      intentionList: [],
      form: {},
      defaultImgUrl:
        "https://shuahuo-oss.oss-cn-beijing.aliyuncs.com/20220226/1645846098643543.png", //TODO
      showVideo: false,
    };
  },
  created() {
    this.getUserIntention();
  },
  methods: {
    closeVideoPlayer() {
      this.showVideo = false;
    },
    afterRead(file, detail) {
      this.form = {};
      var formData = new FormData();
      formData.append("file", file.file);
      uploadFiles(formData)
        .then((response) => {
          if (response.code === 200) {
            this.form.url = response.url;
            this.form.fileName = file.file.name;
            this.showPic = false;
          }
        })
        .catch((e) => {});
    },
    beforeRead(file) {
      console.log("file");
      return true;
    },
    getUserIntention() {
      getUserJobintentionList().then((response) => {
        if (response.code === 200) {
          this.intentionList = response.data;
          this.checked = null;
        } else {
        }
      });
    },
    checkJob(id, name) {
      this.careasObj.value = id;
      this.careasObj.name = name;
    },
    closeJob() {
      this.careasObj = {};
      this.showJob = false;
    },
    confirmJob() {
      this.checked = this.careasObj.value;
      this.showJob = false;
    },
    delUserResume() {
      Dialog.confirm({
        title: "提示",
        message: "此操作将永久删除该记录，是否继续?",
      })
        .then(() => {
          this.form.url = "";
          this.form.fileName = "";
          this.showPic = true;
          // delUserResumeVideo().then((response) => {
          //   if (response.code === 200) {
          //     this.getCommentList(this.list[this.currentIndex])
          //     this.list[this.currentIndex].commentNum -= 1
          //   }
          // })
        })
        .catch(() => {
          // on cancel
        });
    },
    toDetail() {
      // this.form.url = 'https://shuahuo-oss.oss-cn-beijing.aliyuncs.com/20220228/1646011108177791.mp4'
      this.showVideo = true;
    },
    toSave() {
      if (!this.form.url) {
        this.showPic = true;
        this.$toast("请先上传视频~");
        return false;
      }
      const data = {
        url: this.form.url,
        imgUrl: this.defaultImgUrl,
        evaluate: this.evaluate,
        intentionId: this.careasObj.value,
      };
      addUserVideoResume(data).then((response) => {
        Toast(response.msg);
      });
      this.$router.go(-1);
    },
  },
};
</script>
<style>
#bigBtn {
  width: 100%;
  display: block;
}
#bigBtn .van-uploader__wrapper {
  display: block;
}
</style>
<style scoped>
.van-row {
  margin-bottom: 10px;
}
.title {
  line-height: 44px;
}
.van-image {
  vertical-align: sub;
}
.grey {
  color: #8b8d92;
  text-align: right;
}
.flex-box {
  border-bottom: solid 1px #f2f2f2;
}
.flex-box:last-child {
  border-bottom: none;
}
.van-cell::after {
  bottom: -1px;
}
.items {
  padding: 6px 15px;
  line-height: 26px;
}
.self-picker {
  position: relative;
}
.self-picker__toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 42px;
}
.self-picker__cancel {
  color: #969799;
}
.self-picker__cancel,
.self-picker__confirm {
  height: 100%;
  padding: 0 15px;
  font-size: 14px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  border: none;
}
.self-wrapper {
  height: 220px;
  overflow-y: auto;
}
.div-radio {
  margin-top: 16px;
}
.van-uploader__input-wrapper,
.bigBtn {
  width: 100%;
}
.pos-box {
  position: relative;
}
.pos-box .close {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: #999;
  color: #fff;
  line-height: 30px;
  text-align: center;
  font-size: 18px;
  position: absolute;
  right: -6px;
  top: -6px;
  z-index: 99;
}
</style>
