<!-- 可以搜索的下拉框 这里不支持自定义树节点的事件，这里将事件写死了 具体最终选择的值是valueMode的值-->
<template>
  <div class="component-select-tree" v-clickoutside="closeHandle">
    <!--<div class="component-select-input" v-clickoutside="closeHandle">-->
    <div class="component-select-input">
      <el-input
        class="component-choose-input" :title="inputVale"
        v-model="inputVale"
        @focus.prevent="inputFocus"
        :placeHolder="placeholder" :disabled="disabled">
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
        <i v-if="isClearable" slot="suffix" @click="clearSelectHandler"
           class="el-input__icon el-icon-circle-close el-input__clear"></i>
      </el-input>
      <div class="compent-select-option" :class="{'is-show' : isShowSelect}" v-if="isShowSelect">
        <el-input v-model="filterVal" :placeHolder="$t('components.chooseTreeSelect.inputFilterOption')" clearable class="component-choose-input">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
        <!-- 1.判断是否是多选 -->
        <el-checkbox-group v-model="checkList" v-if="isMutiues">
          <el-tree :data="treeData" :props="props" :node-key="myProps.realValField"
                   :accordion="accordion"
                   :filter-node-method="filterNode" :ref="treeSelectTree">
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <el-checkbox :label="data[myProps.realValField]"
                             @change="mutihandleNodeClick(data, node)">{{data[myProps.label]}}</el-checkbox>
              </span>
          </el-tree>
        </el-checkbox-group>
        <!-- 单选的 -->
        <el-tree v-else :data="treeData" :props="props" :node-key="myProps.realValField"
                 @node-click="handleNodeClick"
                 :accordion="accordion"
                 :filter-node-method="filterNode" :ref="treeSelectTree">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span v-if="!isClickLeaf || (isClickLeaf && node.isLeaf)">
              <el-radio v-model="selectRadioModel" :label="data[myProps.realValField]">
              {{ node.label}}
             </el-radio></span>
            <span v-else>{{ node.label }}</span>
          </span>
        </el-tree>
      </div>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * TODO 需要添加传递一个URL去获取数据，或者提供一个方法获取数据
   * 弹出输入框选择一条记录，相当于select，只是这个select的选择项是树节点的形式
   * 1. 属性(Attributes)
   *      1) treeData：Array 提供的树节点的数据 格式 [{label:xxx,children:[{....},.....其他属性]}]
   *      2) props: 与elementui的tree组件的props属性一致
   *      3) isClearable: Boolean 默认 true ，是否有清除数据的按钮
   *      4) valueMode: 字符串或者数组类型的一个数据，就是相当于下拉列表的v-model属性
   *      5）placeholder: String 在选择框为空的时候的提示信息
   *      6) accordion: Boolean 默认 false，是否只展示一个同级，与elementui的tree组件的一致
   *      7) isClickLeaf: Boolean 默认 true, 是否只有点击叶子事件才发生 true：只点击叶子节点事件才生效 false:点击所有的节点事件都生效
   *      8) isMutiues: Boolean 默认 false, 是否多选 true：多选 ；false：单选
   * 2. 事件(Events)
   *      1) item-click: 参数：data 即当前点击树节点的满足isClickLeaf条件的节点的数据
   * 3. 使用例子
   *    1)在区域管理页面中选择上级区域的 关键代码如下
   *    <chooseMyRegin :treeData="data4" accordion :isClickLeaf="false" :value-mode.sync="domainForm.domainOwer"></chooseMyRegin>
   *    2)新增告警的弹出页面选择用户使用了这个组件 关键代码如下：
   *    <choose-user :treeData="allUsers" placeholder="请先选中告警后再选择用户" :valueMode.sync="formData.userId"
   *     @item-click="itemClick"></choose-user>
   * */

  const defaultProps = {
    children: 'children',
    label: 'label',
    icon: 'icon',
    disabled: 'disabled',
    realValField: 'id' //  真正的值取值的字段
  }
  const props = {
    treeData: { // 所有的选项 必须有这个属性 {text:显示名称, value:对应的值}
      type: Array,
      default () {
        return []
      },
      required: true
    },
    props: {
      default () {
        return defaultProps
      }
    },
    isClearable: { // 是否可以清除选中的内容
      type: Boolean,
      default: true
    },
    valueMode: { // 如果选中时候的值得模型,如果传递了就有值，否则没有选中的内容
      default: null
    },
    placeholder: {
      default: '请选择'
    },
    accordion: { // 是否每一次只打开一个同级的节点
      type: Boolean,
      default: false
    },
    isClickLeaf: { // 是否是点击叶子节点才触发事件
      type: Boolean,
      default: true
    },
    isMutiues: { // 是否是多选,如果是多选目前只支持一级的，不支持树结构的
      type: Boolean,
      default: false
    },
    disabled: false, // 是否可以修改
  }
  export default {
    components: {},
    created () {
      this.myProps = Object.assign({}, defaultProps, this.props)
      // 回填当前选中的内容, 只有在右树节点的情况下采取更新避免由于父节点是通过异步请求获取数据而这时候还没
      // 树节点的数据导致当前的选中的节点清除了
      // 为了解决回显这个情况就对树节点的变化做了一个watch的监听，变化后回显
      if (this.valueMode && this.treeData && this.treeData.length > 0) {
        if (!this.isMutiues) { // 单选创建组件的初始化
          this.updateValues(getSelectNode(this, this.valueMode, this.treeData))
        }
      }
    },
    props: props,
    data () {
      return {
        myProps: null,
        inputVale: null,
        isShowSelect: false,
        selectNodeData: null, // 当前选中的节点
        selectRadioModel: null, // 选中的选项
        treeSelectTree: 'tree' + Date.parse() + (Math.random() * 100).toFixed(),
        checkList: [], // 如果是多选选中的数据
        filterVal: '', // 过滤的数据
      }
    },
    methods: {
      inputFocus () {
        this.realTreeFilter('')
        this.isShowSelect = true
      },
      closeHandle (ev) { // 点击非弹出框区域的事件
        if (this.isMutiues) { // 如果是多选
          this.isShowSelect && (this.isShowSelect = false)
          return
        }
        // 单选的关闭事件
        if (this.isShowSelect) { // 如果是从弹出到没有弹出的状态，就需要去做这些操作
          if (this.selectNodeData) {
            this.inputVale = this.selectNodeData[this.myProps.label]
          } else {
            this.inputVale = '' // 清空
          }
          this.isShowSelect = false
        }
      },
      handleNodeClick (data, node, tree) {
        let isUpdate = this.isClickLeaf ? node.isLeaf : true // 判断是否是选择叶子节点才触发更新事件
        if (isUpdate && (!this.selectNodeData || this.selectNodeData !== data)) { // 如果点击的是叶子节点
          this.updateValues(data)
          this.$emit('item-click', data) // 如果调用了这个事件就执行这个事件
          this.isShowSelect = false
        } else if (this.selectNodeData === data) {
          this.isShowSelect = false
        }
      },
      clearSelectHandler (ev) { // 点击清除的按钮将选中的数据删除
        ev.stopPropagation()
        if (this.isMutiues) { // 如果是多选
          this.clearMutiValues()
        } else { // 如果是单选
          this.updateValues()
        }
      },
      clearMutiValues () { // 清空多选的数据
        this.checkList = []
        this.inputVale = ''
        this.$emit('update:valueMode', '') // 更新为没有数据
      },
      updateValues (data) { // 修改组件的基本信息，如果
        this.selectNodeData = data
        let val = (data && data[this.myProps.realValField]) || null
        this.selectRadioModel = val
        this.inputVale = (data && data[this.myProps.label]) || '' // 显示的信息
        this.$emit('update:valueMode', val) // 通知上级修改
      },
      realTreeFilter (val) { // 代码执行调用过滤统一执行这个方法
        if (this.isShowSelect) {
          this.$refs[this.treeSelectTree].filter(val) // 调用filterNode
        }
      },
      filterNode (value, data) { // 真正的执行数据过滤的方法
        if (!value) return true
        return data[this.myProps.label].indexOf(value) !== -1
      },
      // 如果是多选做的事情，改变选择做的事情
      mutihandleNodeClick (data, node) {
        let tmpVal = data[this.myProps.realValField]
        let index = this.checkList.indexOf(tmpVal)
        // 显示的内容
        let showTextArr = []
        if (this.inputVale) {
          showTextArr = this.inputVale.split(',')
        }
        if (index === -1) { // 如果没有就去掉显示的文本信息
          showTextArr.remove(data[this.myProps.label])
        } else { // 如果有就添加显示的内容
          showTextArr.push(data[this.myProps.label])
        }
        this.$set(this, 'inputVale', showTextArr.join(','))
        let val
        if (this.checkList.length > 0) {
          val = this.checkList.join(',')
        } else {
          val = ''
        }
        this.$emit('update:valueMode', val) // 通知上级修改
      }
    },
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    watch: {
      filterVal (newVal, oldVal) { // 监听input输入框的内容变化
        this.realTreeFilter(newVal)
      },
      valueMode (newVal, oldVal) { // 监听变化 如果是空就需要将显示的内容改变
        if (newVal) {
          if (this.treeData && this.treeData.length > 0) {
            if (this.isMutiues) { // 如果是多选选中的事件
              let selectIds = newVal.split(',')
              let len = selectIds.length
              let showTextArr = []
              let checkList = []
              let selectNodes = []
              for (let i = 0; i < len; i++) {
                let tmpNode = getSelectNode(this, selectIds[i], this.treeData)
                if (tmpNode) {
                  selectNodes.push(tmpNode)
                  checkList.push(tmpNode[this.myProps.realValField])
                  showTextArr.push(tmpNode[this.myProps.label])
                }
              }
              this.checkList = checkList
              this.$set(this, 'inputVale', showTextArr.join(','))
              this.selectNodeData = selectNodes
              if (len !== checkList.length) { // 如果有的没有对应的选项就移除掉,通知上更新
                this.$emit('update:valueMode', checkList.join(','))
              }
            } else { // 单选创建组件的初始化
              this.updateValues(getSelectNode(this, newVal, this.treeData))
            }
          } else {
            this.inputVale = ''
            this.selectNodeData = null
          }
        } else {
          this.inputVale = ''
          this.selectNodeData = null
        }
      },
      treeData (newVal, oldVal) { // 如果树节点的信息改变了，就要去检查是否需要显示和更新调用的位置
        if (newVal && newVal.length > 0) {
          if (!this.isMutiues) { // 如果不是多选
            this.updateValues(getSelectNode(this, this.valueMode, newVal))
          }
        }
      }
    },
  }
  /**
   * 递归调用获取当前选中的节点
   * @param vm 当前组件
   * @param key 一条数据的id
   * @param data 所有的数据
   * @returns {*}
   */
  const getSelectNode = (vm, key, data) => {
    let len = (data && data.length) || 0
    if (len === 0 || !key) {
      return null
    }
    for (let i = 0; i < len; i++) {
      let one = data[i]
      if (one[vm.myProps.realValField] == key) {
        return one
      }
      let clds = one[vm.myProps.children]
      if (clds) {
        let cld = getSelectNode(vm, key, clds) // 递归调用
        if (cld) {
          return cld
        }
      }
    }
    return null
  }
</script>
<style lang="less" scoped>
  .component-select-tree {
    .component-select-input {
      position: relative;
      width: 100%;
      .component-choose-input {
        width: 100%;
      }
      .compent-select-option {
        position: absolute;
        z-index: 1000;
        top: 40px;
        left: 0;
        display: none;
        width: 100%;
        border: 1px solid #08a8d8;
        &.is-show {
          display: block;
        }
        .el-tree {
          padding: 10px 0 0;
          max-height: 380px;
          overflow-y: auto;
        }
      }
    }
    /deep/ .el-radio__input.is-checked + .el-radio__label {
      color: #983c3c !important;
    }
  }

</style>
