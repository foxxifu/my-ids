<!-- 任务处理流程节点的人员信息 -->
<template>
    <div class="workflow-user-parent">
      <div v-for="item in list" :key="item[myprop.id]">
        <div v-if="item[myprop.id] === -2" class="myfloat workflow-user">
          <div class="circle">
            {{ item[myprop.name]}}
            <div class="user-text">
              {{ myprop.format(item[myprop.status], vm) }}
            </div>
          </div>
        </div>
        <div v-else  class="myfloat workflow-user">
          <div class="myfloat jt">
          </div>
          <div class="myfloat circle">
            {{ item[myprop.name]}}
            <div class="user-text myfloat">
              {{ myprop.format(item[myprop.status], vm) }}
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  const prop = {
    id: 'id', // 列表主键
    name: 'name', // 展示在中间需要去的属性,默认取name属性
    status: 'status', // 流程状态的名称
    format: function (value, vm) { // 对属性格式化的方法
      if (value === '-1') {
        // 任务创建人
        return vm.$t('io.defect.userTaskArr')[0]
      }
      if (value === '0') {
        // 任务接受人
        return vm.$t('io.defect.userTaskArr')[1]
      }
      if (value === '1') {
        // 任务消缺人
        return vm.$t('io.defect.userTaskArr')[2]
      }
      if (value === '2') {
        // 任务审核人
        return vm.$t('io.defect.userTaskArr')[3]
      }
      if (value === '3') {
        // 任务已完成
        return vm.$t('io.defect.userTaskArr')[4]
      }
      // '未知'
      return vm.$t('io.unKnow')
    }
  }
  const props = {
    list: { // 当前要显示的人员的数据, 当前展示的数据就根据这个来显示 这个里面至少要包含的属性 {用户名称，流程名称, id}
      type: [Object, Array],
      default: null
    },
    prop: { // 用于判断具体区什么数据
      type: Object,
      default () {
        return prop
      }
    }
  }
  export default {
    props: props,
    created() {
      this.myprop = Object.assign(prop, this.prop)
    },
    components: {},
    data () {
      return {
        myprop: null,
        vm: this
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {},
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
  .workflow-user-parent{
    max-height: 200px;
    line-height: 100px;
    div.circle {
      width: 80px;
      height: 80px;
      line-height: 80px;
      text-align: center;
      border: 1px solid #aaa;
      border-radius: 50%;
      position: relative;
      color: #aaa;
    }
    .myfloat {
      float: left;
      &.workflow-user {
        height: 100px;
      }
    }
    .user-text{
      position: absolute;
      width: 80px;
      text-align: center;
      bottom: -52px;
      color: #333;
    }
    .jt {
      width: 80px;
      height: 80px;
      margin: 0 10px;
      position: relative;
      &::after{
        position: absolute;
        content: '';
        display: block;
        top: 35px;
        right: 0;
        width: 0px;
        height: 0px;
        border-top: 5px solid transparent;
        border-left: 10px solid #aaa;
        border-bottom: 5px solid transparent;
      }
      &::before{
        position: absolute;
        display: block;
        content: '';
        top: 40px;
        right: 0;
        width: 80px;
        height: 1px;
        border-bottom: 1px solid #aaa;
      }
    }
  }
</style>

