<!-- 树节点的导航栏，将预留的页面分为两部分，一部分是树的导航位置，另一部分是 -->
<template>
  <div class="tree-nav-parent">
    <div class="tree-nav">
      <h3 class="title-text" v-if="titleText">{{titleText}}</h3>
      <el-input v-if="isFilter" prefix-icon="el-icon-search" class="mySelectInput"
                :placeholder="$t('components.treeNav.enterKeyWordFil')"
                v-model="filterText" >
      </el-input>
      <el-tree
        :props="myProps"
        :load="getTreeDatas"
        :lazy="lazy"
        :data="list"
        :node-key="lazySearchKey"
        :accordion="accordion"
        @node-click="nodeClick"
        :default-expanded-keys="defaultExpendKeys"
        :expand-on-click-node="isLeafClick"
        :filter-node-method="filterNode"
        :class="{'tree':true,'is-search': isFilter, isShowText: !!titleText}"
        ref="navTree"
      >
      </el-tree>
    </div>

    <div class="content">
      <!-- 将选中的selectNode数据传递给调用的节点 ，子节点调用的地方使用slot-scope来调用就可以了-->
      <slot :item="selectNode"></slot>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * 上传图片的插件说明
   * 1. 属性(Attributes)
   *      1) data：Array 如果不异步请求由父级传递给子节点的组件树节点的数据一般lazy===true,就需要传递这个参数
   *      2) props: Object 属性与element-ui的树节点组件的props意义相同, 默认{label: 'label',children: 'children',isLeaf: 'isLeaf' // 叶子节点取数据的属性名称}
   *      3) lazy: Boolean 是否启动懒加载，如果启动懒加载需要有查询的url等数据 默认true 意义： true->使用懒加载，false->不使用懒加载
   *      4）lazySearchUrl：String 如果是懒加载获取数据的url 默认null
   *      5) lazySearcData: Object 懒加载的情况下查询数据额外需要的参数信息 默认空对象{}
   *      6) lazySearchKey: String 如果懒加载的情况下需要传递给后台的key的字符串，默认为'id'
   *      7) accordion: Boolean 是否启用手风琴的效果,默认false; 参数意义 true:使用，false:不使用
   *      8) isLeafClick: Boolean 是否点击叶子节点才生效 默认false; 参数意义 true:点击叶子节点才触发事件，false:点击任何树节点都生效
   *      9) isFilter: Boolean 是否使用过滤的功能 默认false; 参数意义 true:有过滤的输入框  false：没有过滤的输入框
   *      10) titleText: Sring 树节点头上的标题 默认为null; 参数意义：如果设置了值就显示标题
   *      11）isDefaultSelect： 是否加载节点后自动选中
   * 2. 事件(Events)
   *      1) node-click 点击有效的树节点的回调函数 参数(data点击节点的数据, node点击的节点)
   * 3. 使用例子
   *      1) 在电站管理页面station/index.vue中的
   *      关键代码：<TreeNav
   *                 @node-click="treeNodeClick"
   *                 :lazy="false"
   *                 :data="treeData"
   *                 :props="props"
   *                 isFilter
   *                 titleText="电站组织结构">
   *      <file-upload :fileId.sync="formData.fileId"></file-upload>
   *      2) 在设备管理界面的例子，关键代码
   *     <TreeNav lazySearchUrl="/biz/domain/getDomainsByParentId"
   *        @node-click="treeNodeClick"
   *        titleText="电站组织结构">
   * */
  import fetch from '@/utils/fetch' // 异步请求的时候需要组织发送请求
  const prop = {
    label: 'label',
    children: 'children', // 树节点中的下一级使用的字段名称
    isLeaf: 'isLeaf' // 叶子节点取数据的属性名称
  }
  const props = {
    data: { // 树节点的数据
      type: [Object, Array],
      default () {
        return []
      }
    },
    props: {
      type: Object,
      default () {
        return prop
      }
    },
    lazy: { // 是否是使用异步加载 true：使用异步  ，false：使用同步,就不会去调用查询的方法了
      type: Boolean,
      default: true
    },
    lazySearchUrl: { // 懒加载的时候点击查询的url路径
      type: String,
      default: null
    },
    // 异步请求需要的其他参数，如果有其他的参数就需要从这个地方传入，
    // 这里对这些查询参数不做修改，只会对查询数据添加一个点击的节点的信息
    // 比如有的查询需要登录用户的id，这里就可以设置lazySearcData= {userId: xxxx,...}
    // 后面真正查询的时候会传递的查询参数 {lazySearchKey:node.data[lazySearcData], userId: xxxx,...}
    lazySearcData: {
      type: Object,
      default () {
        return {}
      }
    },
    lazySearchKey: { // 如果是异步查询数据，传递给后台的点击节点的key默认使用这个这条数据的id，可以自定义修改
      type: String,
      default: 'id'
    },
    accordion: { // 是否只能展开一个同级 默认 true， 意义：true：只能一个同级 false: 可以同时打开多个同级
      type: Boolean,
      default: true
    },
    isLeafClick: { // 是否叶子点击才生效, 如果这个为true，就点击展开节点
      type: Boolean,
      default: false
    },
    isFilter: { // 是否搜索,最好异步的请求的不执行搜索的功能
      type: Boolean,
      default: false
    },
    titleText: { // 显示的title
      type: String,
      default: null
    },
    isDefaultSelect: { // 是否默认选中
      type: Boolean,
      default: true
    },
    lazySearchFun: { // 懒加载数据可以由外部提供 这个通常必须是返回树节点的子节点的数组的形式(是否是叶子结点等等)
      type: Function, // 获取数据的回调方法，有三个参数(node, resolve【查询成功后调用这个方法】, reject)
      default: null
    }
  }
  export default {
    props: props,
    components: {},
    created () {
      this.myProps = Object.assign({}, prop, this.props)
      if (!this.lazy) {
        this.list = this.data
        // 自动选中节点
        this.defaultSelectNode()
      }
    },
    data () {
      return {
        myProps: null,
        list: [], // 加载表格的数据
        selectNode: null,
        filterText: '',
        defaultExpendKeys: []
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      getTreeDatas (node, resolve1) { // lazySearchUrl查询数据, 这个方法调用只在异步的情况下才会去调用
        if (!this.lazySearchUrl && !this.lazySearchFun) {
          return
        }
        let self = this
        // 执行查询
        if (this.lazySearchFun) { // 通过外部来调用
          let self = this
          new Promise(function(resolve, reject){ // 等待异步查询到的数据后的回调
            self.lazySearchFun(node, resolve, reject)
          }).then(function (result1) { // 查询到数据之后的回调函数
            result1 = result1 || []
            resolve1(result1)
            if (self.isDefaultSelect && node.level === 0 && !self.selectNode && result1.length > 0) { // 懒加载的情况下去做默认的选中的情况
              self.defaultSelectNode(result1[0])
            }
          }).catch(function (e) {
            self.$message.warning(this.$t('components.treeNav.getDataErr'))
          })
        } else {
          // 1.构建查询参数
          let data = {}
          data[this.lazySearchKey] = (node.data && node.data[this.lazySearchKey]) || null
          Object.assign(this.lazySearcData || {}, data)
          fetch(this.lazySearchUrl, this.lazySearcData, 'POST').then(resp => {
            let resutls = (resp.code === 1 && resp.results) || []
            resolve1(resutls)
            if (self.isDefaultSelect && node.level === 0 && !self.selectNode && resutls.length > 0) { // 懒加载的情况下去做默认的选中的情况
              self.defaultSelectNode(resutls[0])
            }
          })
        }
      },
      nodeClick (data, node, tree) { // 点击树节点的回调函数
        let isUpdate = this.isClickLeaf ? node.isLeaf : true;
        if (isUpdate) { // 调用数据
          this.selectNode = data
          this.$emit('node-click', data, node) // 调用父组件的方法
        } else if (this.selectNode){
          this.realSelectNode(this.selectNode)
        }
      },
      filterNode(value, data) { // 节点过滤
        if (!value) return true;
        return data[this.props.label].indexOf(value) !== -1;
      },
      defaultSelectNode (lazyNode) {
        // 目前只解决是所有节点都可以选中的时候的默认选中的情况
        if (!this.selectNode && !this.lazy && this.list && this.list.length > 0) {
          let tmpSelectNode
          if (!this.isLeafClick) {
            tmpSelectNode = this.list[0]
          } else {
            tmpSelectNode = this.findFristLeaf(this.list[0])
          }
          this.realSelectNode(tmpSelectNode)
        } else if (!this.selectNode && !this.isLeafClick && this.lazy && lazyNode) { // 如果是懒加载的情况
          this.realSelectNode(lazyNode)
        }
      },
      realSelectNode (node) { // 真正去执行选中的操作
        let self = this
        setTimeout(() => {
          self.$refs.navTree.setCurrentNode(node)
          self.nodeClick(node, self.$refs.navTree.getCurrentNode(), self.$refs.navTree)
        }, 10)
      },
      findFristLeaf(node) { // 找到第一个叶子节点
        this.defaultExpendKeys.push(node[this.lazySearchKey]) // 默认展开的节点
        if (node[this.myProps.isLeaf]) {
          return node
        }
        let cld = node[this.myProps.children]
        if (cld && cld.length > 0) {
          return this.findFristLeaf(cld[0])
        } else {
          return node
        }
      }
    },
    watch: {
      filterText(newVal) {
        this.$refs.navTree.filter(newVal)
      },
      data (newData) { // 改变的时候重新赋值
        if (!this.lazy) {
          this.list = newData
          this.defaultSelectNode()
        }
      }
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
  .tree-nav-parent {
    width: 100%;
    height: 100%;
    .tree-nav {
      width: 300px;
      height: 100%;
      border: 1px solid #C6E9EF;
      float: left;
      h3.title-text{
        height: 50px;
        line-height: 50px;
        text-align: center;
        background: #E4F4F4;
        font-size: 18px;
        font-weight: 500;
        color: #1E9FBF;
        border-bottom:1px solid #C6E9EF;
      }
      .tree {
        width: 100%;
        height: 100%;
        max-height: ~'calc(100%)';
        overflow: auto;
        /deep/ .el-tree-node > .el-tree-node__children{ // 使得可以在父节点添加滚动条
          overflow: visible !important;
        }
        /deep/ span.is-leaf ~ span.el-tree-node__label{ // 叶子节点后的label元素添加右边的padding
          padding-right: 5px;
        }
        &.isShowText,&.is-search {
          height:  ~'calc(100% - 50px)';
          max-height: ~'calc(100% - 50px)';
        }
        &.is-search.isShowText{
          height:  ~'calc(100% - 100px)';
          max-height: ~'calc(100% - 100px)';
        }
      }
    }
    .content{
      float: left;
      margin-left: 10px;
      width: ~'calc(100% - 312px)';
      height: 100%;
    }
    // 选中的节点的样式
    /deep/ .el-tree-node.is-current > .el-tree-node__content {
      background: #C6E9EF;
      /deep/ span.el-tree-node__label{
        color: #1E9FBF;
      }
    }
    /deep/ .mySelectInput .el-input__inner {
      background-color: #F5F4F2;
      border: none;
      border-radius: 0;
      border-bottom: 1px solid #CDCCCA;
      margin-bottom: 5px;
    }
  }
</style>

