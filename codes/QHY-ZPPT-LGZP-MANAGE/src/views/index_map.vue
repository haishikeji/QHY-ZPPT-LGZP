<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <!--    左侧部分-->
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
        <div class="home-map-background">
          <div style="position:relative;">
            <div class="home-map-title-div">
              <div  v-for="item in tabTypes" :class="item.active?'home-map-title-active':'home-map-title'" @click="changeTabTypes(item)">
                <a>{{item.name}}</a>
              </div>
            </div>
            <div class="getJobs" v-if="showSelectJob == true">
              <el-select v-model="job" placeholder="请选择" @change="changeJobs">
                <el-option
                  v-for="item in careasOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </div>
          </div>


          <el-divider></el-divider>
          <div class="home-map">
            <div id="home-map-container" class="home-map-container"></div>

          </div>

        </div>

      </el-col>
      <!--    右侧部分-->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <!--招聘职位分布-->
        <div v-if="tabActive === 0">
          <!--今日职位新增-->
          <div class="home-tab-background">
            <!--左侧图片-->
            <div class="home-tab-background-img">
              <img src="./../assets/images/home_logo.png">
            </div>
            <!--右侧文字-->
            <div class="home-tab-background-text">
              <div class="home-tab-background-text-top">
                今日职位新增
              </div>
              <div class="home-tab-background-text-num">
                {{careaAddNum}}
              </div>
            </div>
          </div>

          <!--累计职位-->
          <div class="home-tab-background" style="margin-top:20px">
            <!--左侧图片-->
            <div class="home-tab-background-img">
              <img src="./../assets/images/home_logo.png">
            </div>
            <!--右侧文字-->
            <div class="home-tab-background-text">
              <div class="home-tab-background-text-top">
                职位累计
              </div>
              <div class="home-tab-background-text-num" style="color:#000000;">
                {{careaCountNum}}
              </div>
            </div>
          </div>

          <!--        热门职位-->
          <div class="home-tab-background-hot" style="margin-top:20px">
            <div class="home-tab-background-hot-title">
              热门职位(Top10)
            </div>
            <div class="home-tab-background-echarts-hot" id="hot-echarts">

            </div>
          </div>
        </div>

        <!--求职人数分布-->
        <div v-if="tabActive === 1">
          <!--今日职位新增-->
          <div class="home-tab-background">
            <!--左侧图片-->
            <div class="home-tab-background-img">
              <img src="./../assets/images/home_job_logo.png">
            </div>
            <!--右侧文字-->
            <div class="home-tab-background-text">
              <div class="home-tab-background-text-top">
                今日求职人数新增
              </div>
              <div class="home-tab-background-text-num">
                {{addJobNum}}
              </div>
            </div>
          </div>

          <!--累计职位-->
          <div class="home-tab-background" style="margin-top:20px">
            <!--左侧图片-->
            <div class="home-tab-background-img">
              <img src="./../assets/images/home_job_logo.png">
            </div>
            <!--右侧文字-->
            <div class="home-tab-background-text">
              <div class="home-tab-background-text-top">
                求职人数累计
              </div>
              <div class="home-tab-background-text-num" style="color:#000000;">
                {{countJobNum}}
              </div>
            </div>
          </div>

          <!--        热门职位-->
          <div class="home-tab-background-hot" style="margin-top:20px">
            <div class="home-tab-background-hot-title">
              热门职位(Top10)
            </div>
            <div class="home-tab-background-echarts-hot" id="hot-job-echarts">

            </div>
          </div>
        </div>

      </el-col>
    </el-row>

  </div>
</template>

<script>
var AMap = window.AMap;
import echarts from 'echarts'
import { getTodayAddNumAndCareasCountNum,getTopCareas,getCareaCountNumByMap,getTodayAddNumAndJobCountNum,getTopIntention,getJobCountNumByMap } from "@/api/indexmap";
import {selectFatherCareas} from "@/api/careas/careas";
export default {
  // mixins: [resize],
  name: "IndexMap",
  data() {
    return {
      //热门职位(职位)
      hotCharts:null,
      //热门职位(求职)
      hotJobCharts:null,
      //今日职位新增
      careaAddNum:0,
      //职位累计
      careaCountNum:0,
      //热门职位
      hotData:null,
      //区级点标记列表(职位)
      areaMarkerList:[],
      //市级点标记列表(职位)
      cityMarkerList:[],
      //省级(职位)
      provinMarkerList:[],
      //每个职位的点标记(职位)
      careasMarkerList:[],
      //区级点标记列表(求职)
      areaJobMarkerList:[],
      //市级点标记列表(求职)
      cityJobMarkerList:[],
      //省级(求职)
      provinJobMarkerList:[],
      // 地址对应的经纬度信息(德州的)
      //center: [116.357464,37.434092],
      // 地址解析（逆向）
      geocoder: '',
      //定位中心点的区级id
      areaId: null,
      //选中的tab 0 招聘职位分布  1求职人数分布
      tabActive:0,
      careasOptions:[],
      job:0,
      //地图内容
      content:'<div><div style="width: 122px;background-color:#6C63FF;font-size:14px;color:#FFFFFF;text-align:center;padding-top:6px; padding-bottom:6px">禹城 32个职位</div><div  class="marker-triangle">&#9660;</div></div>',
      //tab选择
      tabTypes:[
        {
          name:"招聘职位分布",
          value:0,
          active:true
        },
        {
          name:"求职人数分布",
          value:1,
          active:false
        },

      ],
      //今日求职人数新增
      addJobNum:0,
      //求职人数新增
      countJobNum:0,
      //是否显示搜索框
      showSelectJob:false
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initMap();
      this.initCareasData();
      this.initHotCareaData();
      this.initJobData();
      //适应屏幕
      this.initOnresize("0");
      this.initOnresize("1");
      //this.addAMapGeolocation();
      this.getTreeselectCareas();
    })
  },
  methods: {
    //初始化热门职位图标(职位)
    initHotCharts(data){
      this.hotCharts = echarts.init(document.getElementById('hot-echarts'));
      const option = {
        color: ['#1DE9B6'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '5%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.names
        },
        series: [
          {
            type: 'bar',
            data: data.datas
          }
        ]
      };
      this.hotCharts.setOption(option);
    },
    //初始化热门职位图标(求职)
    initHotJobCharts(data){
      this.hotJobCharts = echarts.init(document.getElementById('hot-job-echarts'));
      const option = {
        color: ['#7973F9'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '5%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.names
        },
        series: [
          {
            type: 'bar',
            data: data.datas
          }
        ]
      };
      this.hotJobCharts.setOption(option);
    },

    //初始化地图
    initMap() {
     /* var marker = new AMap.Marker({
        content: this.content,  // 自定义点标记覆盖物内容
        position:  this.position, // 基点位置
        offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
      });*/

      this.map = new AMap.Map('home-map-container', {
        resizeEnable: true,
        zoom: 10,
        //center:this.center
      });
      //this.addAMapGeolocation();
      this.initMaker("3",null);
      this.initMaker("1",null);
      this.initMaker("2",null);
      this.initJobMaker("3");
      this.initJobMaker("1");
      this.initJobMaker("2");
      this.initTool();

      this.map.on('zoomend',(e)=>{
        console.error(this.map.getZoom());
        if(5<this.map.getZoom() && this.map.getZoom()<= 10){
          if(this.tabActive === 0){
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.cityMarkerList);
          }else {
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.add(this.cityJobMarkerList);
          }
          console.error("123");
        } else if(0<this.map.getZoom() && this.map.getZoom()<= 6){
          if(this.tabActive === 0){
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.provinMarkerList);
          }else {
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.cityMarkerList);

            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.provinJobMarkerList);
          }
          console.error("333");
        } else if(13 >=this.map.getZoom()  &&this.map.getZoom() >= 11){
          console.error("444");
          if(this.tabActive === 0){
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.areaMarkerList);
          }else {
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.areaJobMarkerList);
          }
          //移动后获取中心点的坐标
          this.map.on('moveend',(e)=>{
            if(this.tabActive === 0){
              if(this.map.getZoom() >= 14){
                this.map.remove(this.areaMarkerList);
                this.initGeocoder(this.map.getCenter().lng,this.map.getCenter().lat);
              }
            }
          });

        }
      });

    },
    //添加点标记(职位)
    initMaker(type,areaId){
      let q = {
        type:type,
      }
      if(areaId != null){
        q.areaId = areaId;
      }
      getCareaCountNumByMap(q).then(response => {
        if(response.code === 200){
          if(response.data != null){
            if(type === "3"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.areaName != null && element.count != null){
                  content = '<div class="marker-content"><div class="job-num">'+element.areaName+element.count+'个职位</div><div class="marker-triangle">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.areaMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                  console.error("执行了",e.target.getExtData());
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
              this.map.add(this.areaMarkerList);
            }else if(type === "2"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.cityName != null && element.count != null){
                  content = '<div class="marker-content"><div class="job-num">'+element.cityName+element.count+'个职位</div><div  class="marker-triangle">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.cityMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                  console.error("执行了",e.target.getExtData());
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
            }else if(type === "1"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.provinceName != null && element.count != null){
                  content = '<div class="marker-content"><div class="job-num">'+element.provinceName+element.count+'个职位</div><div  class="marker-triangle">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.provinMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                  console.error("执行了",e.target.getExtData());
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
            }else {
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.careaName!= null && element.salaryName!= null){
                  content = '<div class="marker-content"><div class="job-num">职位:'+element.careaName+'<br/>薪水:'+element.salaryName+'元/月</div><div  class="marker-triangle">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.careasMarkerList.push(marker);
                this.map.add(this.careasMarkerList);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                  console.error("执行了",e.target.getExtData());
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
            }

          }
        }
      })
    },

    //添加点标记(求职),不去添加maker
    initJobMaker(type,careasId){
      let q = {
        type:type,
        careasId:careasId
      }
      getJobCountNumByMap(q).then(response => {
        if(response.code === 200){
          if(response.data != null){
            if(type === "3"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.areaName != null && element.count != null){
                  content = '<div class="marker-content2"><div class="job-num2">'+element.areaName+element.count+'人求职</div><div class="marker-triangle2">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.areaJobMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
            }else if(type === "2"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.cityName != null && element.count != null){
                  content = '<div class="marker-content2"><div class="job-num2">'+element.cityName+element.count+'人求职</div><div class="marker-triangle2">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.cityJobMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
            }else {
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.provinceName != null && element.count != null){
                  content = '<div class="marker-content2"><div class="job-num2">'+element.provinceName+element.count+'人求职</div><div class="marker-triangle2">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.provinJobMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }
            }

          }
        }
      })
    },

    //添加点标记(求职),添加maker
    initJobMakerAndAddMaker(type,careasId){
      this.areaJobMarkerList = [];
      this.cityJobMarkerList = [];
      this.provinJobMarkerList = [];
      let q = {
        type:type,
        careasId:careasId
      }
      getJobCountNumByMap(q).then(response => {
        if(response.code === 200){
          if(response.data != null){
            if(type === "3"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.areaName != null && element.count != null){
                  content = '<div class="marker-content2"><div class="job-num2">'+element.areaName+element.count+'人求职</div><div class="marker-triangle2">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.areaJobMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }

            }else if(type === "2"){
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.cityName != null && element.count != null){
                  content = '<div class="marker-content2"><div class="job-num2">'+element.cityName+element.count+'人求职</div><div class="marker-triangle2">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.cityJobMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }

            }else {
              for(var element of response.data){
                let position = [];
                let content;
                if(element.lng != null){
                  position.push(element.lng);
                }
                if(element.lat != null){
                  position.push(element.lat);
                }
                if(element.provinceName != null && element.count != null){
                  content = '<div class="marker-content2"><div class="job-num2">'+element.provinceName+element.count+'人求职</div><div class="marker-triangle2">&#9660;</div></div>'
                }
                var marker = new AMap.Marker({
                  content: content,  // 自定义点标记覆盖物内容
                  position:  position, // 基点位置
                  offset: new AMap.Pixel(0, -42) // 相对于基点的偏移位置
                });
                this.provinJobMarkerList.push(marker);
                marker.on('mouseover', function(e){
                  e.target.setTop(true);
                })
                marker.on("mouseout", function (e) {
                  e.target.setTop(false);
                });
              }

            }
            if(5<this.map.getZoom() && this.map.getZoom()<= 10){
              this.map.add(this.cityJobMarkerList);
            } else if(0<this.map.getZoom() && this.map.getZoom()<= 6){
              this.map.add(this.provinJobMarkerList);
              console.error("333");
            } else if(13 >=this.map.getZoom()  &&this.map.getZoom() >= 11){
              this.map.add(this.areaJobMarkerList);
            }else {
              if(this.tabActive === 1){
                this.map.remove(this.cityMarkerList);
                this.map.remove(this.careasMarkerList);
                this.map.remove(this.areaMarkerList);
                this.map.remove(this.provinJobMarkerList);
                this.map.remove(this.cityJobMarkerList);
                this.map.remove(this.areaJobMarkerList);
              }
            }
          }
        }
      })
    },
    //初始化职位新增和职位累计
    initCareasData(){
      getTodayAddNumAndCareasCountNum().then(response => {
        if(response.code === 200){
          if(response.data != null){
            if(response.data.addNum != null){
              this.careaAddNum = response.data.addNum;
            }
            if(response.data.careasCountNum != null){
              this.careaCountNum = response.data.careasCountNum;
            }
          }
        }
      })
    },

    //初始化今日求职人数和人数累计
    initJobData(){
      getTodayAddNumAndJobCountNum().then(response => {
        if(response.code === 200){
          if(response.data != null){
            if(response.data.addNum != null){
              this.addJobNum = response.data.addNum;
            }
            if(response.data.countNum != null){
              this.countJobNum = response.data.countNum;
            }
          }
        }
      })
    },

    //初始化热门热门职位(招聘职位)
    initHotCareaData(){
      getTopCareas().then(response => {
        if(response.code === 200){
          this.initHotCharts(response.data);
        }
      })
    },
    //初始化热门职位(求职)
    initHotJobData(){
      getTopIntention().then(response => {
        if(response.code === 200){
          this.initHotJobCharts(response.data);
        }
      })
    },
    // 添加自动定位
    addAMapGeolocation() {
      var that = this;
      console.error("jinlaile ");
      that.map.plugin('AMap.Geolocation', () => {
        const geolocation = new AMap.Geolocation({
          // 是否使用高精度定位，默认：true
          enableHighAccuracy: true,
          // 设置定位超时时间，默认：无穷大
          //timeout: 10000,
          // 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
          buttonOffset: new AMap.Pixel(10, 20),
          //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
          zoomToAccuracy: true,
          //  定位按钮的排放位置,  RB表示右下
          buttonPosition: 'RB',
          showCircle:false,
          panToLocation:true,
        });
        // 获取当前位置
        that.map.addControl(geolocation);
        geolocation.getCurrentePosition();
        // 添加定位当前城市成功监听
        AMap.event.addListener(
          geolocation,
          'complete',
          that.onCurrentPositionComplete
        );
        // 添加定位当前城市发生错误监听
        AMap.event.addListener(
          geolocation,
          'error',
          that.onCurrentPositionError
        );
      });
    },

    // 定位当前城市成功回调
    onCurrentPositionComplete(res) {
      console.error("jinlaile 222");
      const lnglat = [res.position.lng, res.position.lat];
      console.error(lnglat);
    },
    // 定位当前城市发生错误回调
    onCurrentPositionError(err) {
      console.log("error")
      console.log(err);
    },
    //添加比例尺
    initTool(){
      this.map.plugin(['AMap.ToolBar','AMap.Scale'],()=>{
        var toolBar = new AMap.ToolBar();
        var scale = new AMap.Scale();
        this.map.addControl(toolBar);
        this.map.addControl(scale);
      });
    },
    //逆地理编码
    initGeocoder(lng,lat){
      this.map.plugin('AMap.Geocoder',()=>{
        this.geocoder = new AMap.Geocoder({
          radius: 1000 // 范围，默认：500
        });
      });
      var lnglat = [lng, lat];
      var that = this;
      this.geocoder.getAddress(lnglat, function(status, result) {
        if (status === 'complete' && result.info === 'OK') {
          // result为对应的地理位置详细信息
          const res = result.regeocode;
          //市级编码
          const citycode = res.addressComponent.citycode;
            //区级编码
          const adcode = res.addressComponent.adcode;
          if(that.areaId != null && that.careasMarkerList.length > 0){
            //判断此的目的是防止在一个地区重复请求一个地区
            if(that.areaId == adcode){
              that.map.add(that.careasMarkerList);
              return;
            }
          }
          console.error("走进来了"+that.areaId);
          that.areaId = adcode;
          //that.careasMarkerList = [];
          that.map.remove(that.careasMarkerList);
          //放置职位marker
          that.initMaker("4",that.areaId);

        }
      })
    },
    //切换求职、招聘
    changeTabTypes(itemObj){
      for(let item of this.tabTypes){
        if(item.value === itemObj.value){
          item.active = true
        }else {
          item.active = false
        }
        if(item.active){
          this.tabActive = item.value;
          if(item.value === 0){
            console.error("进入了第一个");
            this.showSelectJob = false;
            this.initHotCareaData();
          }else {
            //选择了求职人数
            this.initHotJobData();
            this.showSelectJob = true;
          }
        }
        if(5<this.map.getZoom() && this.map.getZoom()<= 10){
          if(this.tabActive === 0){
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.cityMarkerList);
          }else {
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.add(this.cityJobMarkerList);
            console.log("hahah"+this.cityJobMarkerList.length);
          }
          console.error("123");
        } else if(0<this.map.getZoom() && this.map.getZoom()<= 6){
          if(this.tabActive === 0){
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.provinMarkerList);
          }else {
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.cityMarkerList);

            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.provinJobMarkerList);
          }
          console.error("333");
        } else if(13 >=this.map.getZoom()  &&this.map.getZoom() >= 11){
          console.error("444");
          if(this.tabActive === 0){
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaJobMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.areaMarkerList);
          }else {
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.add(this.areaJobMarkerList);
          }
        }else {
          if(this.tabActive === 1){
            this.map.remove(this.cityMarkerList);
            this.map.remove(this.careasMarkerList);
            this.map.remove(this.areaMarkerList);
            this.map.remove(this.provinJobMarkerList);
            this.map.remove(this.cityJobMarkerList);
            this.map.remove(this.areaJobMarkerList);
          }
        }
      }
    },
    //解决适应屏幕的问题 type 0 职位  1求职
    initOnresize(type){
      if(type === "0"){
        var that = this;
        window.onresize = function () {
          setTimeout(function () {
            that.hotCharts.resize();
          },10)
        }
      }else {
        var that = this;
        window.onresize = function () {
          setTimeout(function () {
            that.hotJobCharts.resize();
          },10)
        }
      }

    },
    /**查询职位*/
    getTreeselectCareas() {
      selectFatherCareas().then(response => {
        this.careasOptions = response.data;
        var data = {};
        data.id = 0;
        data.name = "全部";
        this.careasOptions.splice(0,0,data);
      });
    },
    /**
     * 职位变动
     */
    changeJobs(){
      console.log("123",this.job);
      if(this.job === 0){
        this.map.remove(this.provinJobMarkerList);
        this.map.remove(this.cityJobMarkerList);
        this.map.remove(this.areaJobMarkerList);
        //选择了全部
        this.initJobMakerAndAddMaker("3");
        this.initJobMakerAndAddMaker("1");
        this.initJobMakerAndAddMaker("2");
      }else {
        this.map.remove(this.provinJobMarkerList);
        this.map.remove(this.cityJobMarkerList);
        this.map.remove(this.areaJobMarkerList);
        this.initJobMakerAndAddMaker("3",this.job);
        this.initJobMakerAndAddMaker("1",this.job);
        this.initJobMakerAndAddMaker("2",this.job);

      }
    }
  },
};
</script>
<style>

.job-num{
  width: 122px;
  background-color:#6C63FF;
  font-size:14px;
  color:#FFFFFF;
  text-align:center;
  padding: 6px 10px;
  border-radius:5px;
}
.marker-triangle{
  padding-top:0px;
  margin-top:-5px;
  color:#6C63FF;
  text-align:center;
}
.marker-content:hover .job-num{
  background-color:#1DE9B6!important;
}
.marker-content:hover .marker-triangle{
  color:#1DE9B6!important;
}
.job-num2{
  width: 122px;
  padding: 6px 10px;
  background-color:#1DE9B6;
  font-size:14px;
  color:#FFFFFF;
  text-align:center;
  border-radius:5px
}
.marker-triangle2{
  padding-top:0px;
  margin-top:-5px;
  color:#1DE9B6;
  text-align:center;
}
.marker-content2:hover .job-num2{
  background-color:#6C63FF!important;
}
.marker-content2:hover .marker-triangle2{
  color:#6C63FF!important;
}
</style>
<style scoped lang="scss">

.home {
  background: #E0E0E0;
  min-height: calc(100vh - 84px);
}
.home-tab-background{
  border-radius: 6px;
  background:white;
  display:flex;
  flex-direction:row;
  padding:30px 0px 30px 20px
}

.home-tab-background-img{
  img{
    width: 65px;
    height: 65px;
  }

}
.home-tab-background-text{
  margin-left: 20px;
}
.home-tab-background-text-top{
  font-size:14px;
  color:#757575;
}
.home-tab-background-text-num{
  margin-top: 20px;
  font-size: 24px;
  color:#1DE9B6;
}
.home-tab-background-hot{
  border-radius: 6px;
  background:white;
  padding-left: 20px;
  padding-top:30px;
}
.home-tab-background-hot-title{
  font-size:18px;
  font-color:#101010;
  font-weight: bold;
}
.home-tab-background-echarts-hot{
  height:500px;
  width:100%;
}
.getJobs{
  position: absolute;
  top: 30px;
  right: 10px;
  width: 200px;
  background: #FFFFFF;
}
.home-map-background{
  border-radius: 6px;
  background:white;
}
.home-map-title-div{
  display:flex;
  flex-direction:row;
}
//标题选中
.home-map-title-active{
  padding-left: 20px;
  padding-top:30px;
  color:#101010;
  font-size:18px;
  font-weight: bold;
}
//标题未选中
.home-map-title{
  padding-left: 20px;
  padding-top:30px;
  color:#757575;
  font-size:18px;
}
.home-map{
  padding: 0px 15px 15px 15px;
}
.home-map-container{
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 735px;
  border-radius: 6px;
}

</style>

