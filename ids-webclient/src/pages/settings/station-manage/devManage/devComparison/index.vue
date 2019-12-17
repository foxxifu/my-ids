<!-- 设备对比的界面的信息 -->
<template>
  <el-dialog :title="$t('devCom.devComparison')" :visible="isShowDialog" width="90%" height="90%" :before-close="handleClose"
             append-to-body :close-on-click-modal="false" class="dev-comparison-dialog">
    <el-tabs v-model="activeName" tab-position="right" type="border-card"  @tab-click="tabClick">
      <el-tab-pane :label="$t('devCom.listMode')" name="list" >
        <list-model></list-model>
      </el-tab-pane>
      <el-tab-pane :label="$t('devCom.graMode')" name="img">
        <img-model v-if="hasLoadImg || activeName ==='img'"></img-model>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer" style="margin: 0 auto">
      <el-button @click="handleClose">{{ $t('close') }}</el-button>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import ListModel from './listModel.vue'
  import ImgModel from './imgModel.vue'

  const props = {
    isShowDialog: {
      type: Boolean,
      default: false
    }
  }

  export default {
    props: props,
    components: {
      ListModel,
      ImgModel
    },
    data () {
      return {
        activeName: 'list',
        hasLoadImg: false
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      handleClose () { // 关闭对话框
        this.$emit('update:isShowDialog', false)
      },
      tabClick () {
        if (!this.hasLoadImg && this.activeName === 'img') {
          this.hasLoadImg = true
        }
      }
    },
    watch: {},
    computed: {}
  }
</script>
<style lang='less' scope>
.dev-comparison-dialog{ // 设备对比的弹出框的样式
  /deep/ .el-dialog {
    margin-top: 5vh !important;
    min-height: 300px;
    /deep/ .el-dialog__footer {
      text-align: center;
    }
    // tabs的样式 开始====>
    /deep/ .el-tabs--right {
      overflow: visible;
      &.el-tabs--border-card {
        background: transparent;
        box-shadow: none;
        border: none;
        /deep/ .el-tabs__header {
          background-color: transparent;
          &.is-right {
            width: 40px;
            margin-left: 0px;
            border-bottom: none;
          }
          .el-tabs__item {
            &.is-right {
              min-height: 120px;
              padding: 100% 8px;
              margin: 0 -1px 15px;
              color: #202e33;
              border-radius: 0 15px 15px 0;
              border: 1px solid #08A8D8;
              writing-mode: tb-rl;
              text-transform: capitalize;
              letter-spacing: 0.1em;
              &.is-active {
                color: white;
                background: #08A8D8;
              }
            }
          }
        }
      }
      /deep/ .el-tabs__content {
        height: 100%;
        overflow: auto;
        border: 1px solid #21b1dc;
      }

      /deep/ .el-tabs__header {
        &.is-right {
          /deep/ .el-tabs__nav {
            white-space: pre-wrap;
            /deep/ .el-tabs__item {
              height: auto;
              line-height: 20px;
            }
          }
        }
      }
    }
    // tabs的样式 结束 <====
  }
}
</style>

