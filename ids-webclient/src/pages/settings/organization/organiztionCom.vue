<!-- 组织结构的功能 -->
<template>
  <div class="org-tree-parent">
    <el-tree :props="myProps" @node-contextmenu="nodeContextmenuClick"
             :expand-on-click-node="false" class="tree"
             @node-click="isShowMenu = false"
             @node-expand="isShowMenu = false"
             @node-collapse="isShowMenu = false"
             @my-fresh="myFresh"
             highlight-current
             node-key="id"
             ref="orgTree"
             :load="getDepartmentData"
             lazy
    >
    </el-tree>
    <my-menu v-show="isShowMenu" :selectNode="currentNode" :isShowMenu.sync="isShowMenu"
             :style="{position:'absolute', top:top +'px', left: left + 'px'}"
             @my-fresh="myFresh"
    ></my-menu>
  </div>
</template>

<script type="text/ecmascript-6">
  import MyMenu from './menu.vue'
  import departmentService from '@/service/department'
  const prop = {
    label: 'label',
    children: 'children',
    isLeaf: 'leaf'
  }
  const props = {
    data: { // 树节点的数据
      type: [Object, Array],
      default() {
        return []
      }
    },
    props: {
      type: Object,
      default() {
        return prop
      }
    }
  }
  export default {
    props: props,
    created() {
      this.myProps = Object.assign({}, prop, this.props)
    },
    components: {
      MyMenu
    },
    data() {
      return {
        myProps: null,
        currentNode: null, // 当前编辑的节点
        isShowMenu: false, // 是否显示菜单
        top: 10,
        left: 150,
        qyMap: {}
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      nodeContextmenuClick(event, data, node, tree) { // 点击节点的事件
        this.$refs.orgTree.setCurrentNode(data) // 设置被选中的数据
        let self = this
        setTimeout(function () {
          let dom = document.querySelector('.el-tree-node.is-current.is-focusable span.el-tree-node__label')
          let menuDom = document.querySelector('div.menu-parent')
          let menuWidth
          let menuHeight
          if (!menuDom) {
            menuWidth = 120
            menuHeight = 200
          } else {
            menuWidth = menuDom.clientWidth
            menuHeight = menuDom.clientHeight
          }
          let left = dom.offsetLeft + dom.clientWidth + 10
          let top = dom.offsetTop
          // 119 是头部和导航栏的高度
          if (top + menuHeight + 119 >= document.documentElement.clientHeight) {
            let tmp = top - menuHeight
            top = tmp < 0 ? 0 : tmp
          }
          if (left + menuWidth >= document.documentElement.clientWidth) {
            let tmp = dom.offsetLeft - menuWidth
            left = tmp < 0 ? 0 : tmp
          }
          self.top = top
          self.left = left
        }, 10)
        self.isShowMenu = true
        self.currentNode = node
      },
      myFresh(type, data) {
        this.$emit('my-fresh', type, data)
      },
      getDepartmentData(node, resolve) {
        let self = this;
        if (node.level === 0) {
          departmentService.getEnterprise().then(resp => {
            if (resp && resp.code === 1) { // 查询的企业
              let data = resp.results.map(item => { // 企业的id
                item.id = item.id + '_ent'
                return item
              })
              resolve(data);
            } else {
              self.$message(resp.message);
            }
          });
          return
        }
        if (node.level === 1) { // 根据企业查部门
          let nodeId = node.data.id.split('_')[0]
          departmentService.getDepartmentByParentId({enterpriseId: nodeId, parentId: 0}).then(resp => {
            if (resp && resp.code === 1) {
              resolve(resp.results);
            } else {
              self.$message(resp.message);
            }
          });
          return
        }
        departmentService.getDepartmentByParentId({parentId: node.data.id}).then(resp => {
          if (resp && resp.code === 1) {
            resolve(resp.results);
          } else {
            self.$message(resp.message);
          }
        });
      },
    },
    watch: {},
  }
</script>

<style lang="less" scoped>
  .org-tree-parent {
    position: relative;
  }
</style>

