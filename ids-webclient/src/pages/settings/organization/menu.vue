<!-- 菜单 -->
<template>
  <div>
    <div class="menu-parent" v-clickoutside="closeHandle">
      <ul class="my-ul">
        <li v-for="item in data" :key="item.id"
            @mouseenter="hoverItem(item)"
            @click="liClick(item)"
            :class="{'show-expend': !item.type}">
          <i :class="['my-start-icon',item.icon]"></i>
          <div class="li-text">{{$t(item.label)}}</div>
          <i v-if="!item.type" class="my-end-icon el-icon-arrow-right"></i>
        </li>
      </ul>
      <ul class="my-ul" v-for="tmpUl in ulDataList">
        <li v-for="item in tmpUl.data" :key="item.id"
            @mouseenter="hoverItem(item)"
            @click="liClick(item)"
            :class="{'show-expend': !item.type}">
          <i :class="['my-start-icon',item.icon]"></i>
          <div class="li-text">{{$t(item.label)}}</div>
          <i v-if="!item.type" class="my-end-icon el-icon-arrow-right"></i>
        </li>
      </ul>
    </div>
    <!-- 弹出框 -->
    <el-dialog v-if="dialogVisible" :title="$t('organization.newBuild')" :visible="dialogVisible" class="dialog" width="500px" :before-close="closeDialog" append-to-body :close-on-click-modal="false">
      <el-form :model="departmentModel" ref="departmentForm"  :rules="rules" label-width="140px" style="max-height:470px;overflow-y: auto;" status-icon>
        <el-form-item :label="$t('organization.depName')" prop="name">
          <el-col :span="18">
            <el-input v-model="departmentModel.name" clearable></el-input>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="18">
            <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import departmentService from '@/service/department'
  // 数组代表的意思   [添加子节点，上方添加，    下发添加，   删除，       移动到前一个， 移动到后一个， 向上移动]
  const fnTypeArr = ['addCld', 'addBefore', 'addAfter', 'deleteFn', 'moveBefore', 'moveAfter', 'moveUp'] // 功能的集合
  const defaultData = [
    {
      id: 1,
      label: 'organization.add',
      icon: 'el-icon-plus', // class路径
      children: [
        {
          id: 4,
          label: 'organization.addSub', // '新增子节点'
          type: fnTypeArr[0], // 添加子节点, 由type来具体确定功能,有type的就执行功能，没有的就显示下一级的菜单
          level: 1,
          icon: 'el-icon-circle-plus-outline'
        },
        {
          id: 5,
          label: 'organization.addAbo', // 'v'上方新增节点'
          type: fnTypeArr[1], // 上方添加节点
          level: 1, // 级别，根据级别确定要删除的数据
          icon: 'el-icon-arrow-up'
        },
        {
          id: 6,
          label: 'organization.addBel',
          type: fnTypeArr[2], // 下方添加节点
          level: 1,
          icon: 'el-icon-arrow-down'
        },
      ]
    },
    { // 需要修改移动的数据
      id: 2,
      label: 'organization.move',
      icon: 'el-icon-rank',
      children: [
        {
          id: 7,
          label: 'organization.moveUp',
          type: fnTypeArr[4], // 下方添加节点
          level: 1,
          icon: 'el-icon-arrow-up'
        },
        {
          id: 8,
          label: 'organization.moveDown',
          type: fnTypeArr[5], // 下方添加节点
          level: 1,
          icon: 'el-icon-arrow-down'
        },
        {
          id: 9,
          label: 'organization.sup',
          type: fnTypeArr[6], // 下方添加节点
          level: 1,
          icon: 'el-icon-upload2'
        },
      ]
    },
    {
      id: 3,
      label: 'organization.del',
      icon: 'el-icon-delete',
      type: fnTypeArr[3] // 删除的类型
    },
  ]
  const props = {
    data: {
      type: [Object, Array],
      default () {
        return Object.assign({}, defaultData)
      }
    },
    selectNode: { // 选择的节点
      type: Object,
      default: null
    },
    isShowMenu: { // 是否显示菜单
      type: Boolean,
      default: false
    }
  }
  export default {
    props: props,
    created () {
      this.ulDataList = []
    },
    components: {},
    data () {
      return {
        ulDataList: [], // 后面添加的数组 {key: id, level:级别, data: children}
        dialogVisible: false,
        departmentModel: {},
        rules: {
          name: [
            {required: true, message: this.$t('entPri.req'), trigger: 'blur'}
          ],
        }
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      closeDialog () {
        this.dialogVisible = false;
        this.departmentModel = {};
      },
      submitForm (){
        let self = this;
        this.$refs.departmentForm.validate(valid => { // 表单验证的回调函数
          if (valid) { // 验证成功
            let enterpriseId
            if (self.selectNode.level === 1) {
              enterpriseId = +((self.selectNode.data && self.selectNode.data.id && self.selectNode.data.id.split('_')[0]) || -1)
            } else {
              enterpriseId = self.selectNode.data.enterpriseId
            }
            self.departmentModel.enterpriseId = enterpriseId
            departmentService.insertDepartment(self.departmentModel).then(resp => {
              if (resp && resp.code === 1) {
                self.$message(this.$t('organization.saveSuc'));
                self.myFresh()
                self.closeDialog();
              } else {
                self.$message(resp.message);
              }
            });
          }
        });
      },
      hoverItem (item) {
        // 添加内容
        this.showChildrenUl(item)
      },
      liClick (item) { // 点击的事件
        console.log(this.$t('organization.perFun'))
        let type = item.type
        if (type) {
          this[type](item)
          this.clearData()
        }
      },
      // 添加下一级信息
      addCld (item) {
        this.dialogVisible = true;
        console.log(this.selectNode);
        if (this.selectNode.level === 1) {
          this.departmentModel.parentId = 0;
        } else {
          this.departmentModel.parentId = this.selectNode.data.id;
        }
        this.departmentModel.order_ = 0;
        console.log('addCld')
        this.closeHandle()
      },
      // 添加在同级的之前添加
      addBefore (item) {
        let self = this;
        if (this.selectNode.level === 1) {
          self.$message(this.$t('organization.conBuild'));
          return;
        }
        console.log(this.selectNode);
        this.dialogVisible = true;
        this.departmentModel.parentId = this.selectNode.data.parentId;
        this.departmentModel.id = this.selectNode.data.id;
        this.departmentModel.order_ = 1;
        console.log('addBefore')
      },
      // 添加在同级的之后添加
      addAfter (item) {
        let self = this;
        if (this.selectNode.level === 1) {
          self.$message(this.$t('organization.conBuild'));
          return;
        }
        this.dialogVisible = true;
        this.departmentModel.parentId = this.selectNode.data.parentId;
        this.departmentModel.order_ = -1;
        this.departmentModel.id = this.selectNode.data.id;
        console.log('addAfter')
      },
      // 删除当前节点
      deleteFn (item) {
        let self = this;
        if (this.selectNode.level === 1) {
          self.$message(this.$t('organization.notDel'));
          return;
        }
        this.$confirm(this.$t('organization.confirm_delte')).then(() => {
          departmentService.deleteDepartment({id: this.selectNode.data.id}).then(resp => {
            if (resp && resp.code === 1) {
              self.$message(this.$t('organization.delete_success'));
              self.myFresh(item)
            } else {
              self.$message(resp.message);
            }
          })
        });
        console.log('deleteFn')
      },
      // 移动到上一个节点
      moveBefore (item) {
        let self = this;
        if (this.selectNode.level === 1) {
          self.$message(this.$t('organization.motMove'));
          return;
        }
        departmentService.moveDepartment({id: self.selectNode.data.id, parentId: self.selectNode.data.parentId, order_: 1}).then(resp => {
          if (resp && resp.code === 1) {
            self.$message(this.$t('organization.movSuc'));
          } else {
            self.$message(resp.message);
          }
        });
        console.log('moveBefore')
        self.myFresh(item)
      },
      // 下移
      moveAfter (item) {
        let self = this;
        if (this.selectNode.level === 1) {
          self.$message(this.$t('organization.motMove'));
          return;
        }
        departmentService.moveDepartment({id: self.selectNode.data.id, parentId: self.selectNode.data.parentId, order_: -1}).then(resp => {
          if (resp && resp.code === 1) {
            self.$message(this.$t('organization.movSuc'));
          } else {
            self.$message(resp.message);
          }
        });
        console.log('moveAfter')
        self.myFresh(item)
      },
      // 上级
      moveUp (item) {
        let self = this;
        if (this.selectNode.level === 1) {
          self.$message(this.$t('organization.motMove'));
          return;
        }
        if (this.selectNode.data.parentId === 0) {
          self.$message(this.$t('organization.notSup'));
          return;
        }
        departmentService.moveDepartment({id: self.selectNode.data.id, parentId: self.selectNode.data.parentId, order_: 0}).then(resp => {
          if (resp && resp.code === 1) {
            self.$message(this.$t('organization.movSuc'));
            self.departmentModel = self.selectNode.data
            self.myFresh(item)
          } else {
            self.$message(resp.message);
          }
        })
        this.closeHandle()
      },
      showChildrenUl (item) { // 显示子节点的Ul
        let level = item.level
        if (!level) {
          this.$set(this, 'ulDataList', [])
        } else {
          let len = this.ulDataList.length
          let neArr = []
          for (let i = 0; i < len; i++) {
            let tmp = this.ulDataList[i]
            if (tmp.level >= level) { // 移除节点
              continue
            }
            neArr.push(tmp)
          }
          // 排序 按级别来排序的,保证显示的是一级一级的
          neArr.sort((a, b) => {
            let leva = a.level || 0
            let levb = b.level || 0
            return leva - levb
          })
          this.$set(this, 'ulDataList', neArr)
        }
        if (item.children && item.children.length > 0) {
          this.ulDataList.push({key: item.id, level: item.level, data: item.children})
        }
      },
      closeHandle () { // 点击之外就不显示这些
        this.clearData()
        this.$emit('update:isShowMenu', false)
      },
      clearData () { // 清除菜单数据
        this.$set(this, 'ulDataList', [])
      },
      myFresh(type) { // 执行完操作之后调用的事件 type 是fnTypeArr数组中的元素
        this.$emit('my-fresh', type, this.departmentModel)
      }
    },
    watch: {
      selectNode () {
        this.clearData()
      }
    },
    computed: {
    }
  }
</script>

<style lang="less" scoped>
  .menu-parent {
    background: #fff;
    .my-ul {
      width: 150px;
      float: left;
      border: 1px solid #aaa;
      border-left: none;
      max-height: 200px;
      overflow-y: auto;
      background: #fff;
      li {
        padding: 10px 5px;
        font-size: 16px;
        position: relative;
        &:hover{
          background: #1eb6f8;
        }
        .my-start-icon{
          display: block;
          position: absolute;
          top: 9px;
          left: 5px;
        }
        .li-text{
          padding-left: 23px;
        }
        &.show-expend{
          & > i.my-end-icon.el-icon-arrow-right{
            display: block;
            position: absolute;
            top: 9px;
            right: 5px;
          }
        }
      }
      &:first-child {
        border-left:1px solid #aaa;
      }
    }
  }
</style>

