<template>
    <el-tabs v-model="activeName" tab-position="right" type="border-card" id="todo">
      <el-tab-pane :label="$t('todo.watingTask')" :name="activeNameArr[0]">
        <TaskList v-if="isPending" :taskName="activeNameArr[0]"></TaskList>
      </el-tab-pane>
      <el-tab-pane :label="$t('todo.dealingTask')" :name="activeNameArr[1]">
        <TaskList v-if="isProcessing" :taskName="activeNameArr[1]"></TaskList>
      </el-tab-pane>
      <el-tab-pane :label="$t('todo.finshTask')" :name="activeNameArr[2]">
        <TaskList v-if="isCompleted" :taskName="activeNameArr[2]"></TaskList>
      </el-tab-pane>
    </el-tabs>
</template>
<style lang='less' src='./index.less' scoped></style>
<script type="text/ecmascript-6">
  import TaskList from '@/pages/todo/taskList.vue'

  const activeNameArr = ['0', 'dealing', '3'];
  export default {
    components: {
      TaskList
    },
    data () {
      return {
        activeName: activeNameArr[0],
        activeNameArr: activeNameArr
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {},
    watch: {},
    computed: {
      isPending () { // 是否是待分配的标签
        return activeNameArr.indexOf(this.activeName) === 0;
      },
      isProcessing () { // 是否是处理中的标签
        return activeNameArr.indexOf(this.activeName) === 1;
      },
      isCompleted () { // 是否是已完成的标签
        return activeNameArr.indexOf(this.activeName) === 2;
      },
    }
  }
</script>
