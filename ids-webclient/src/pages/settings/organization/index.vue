<template>
   <org-com v-if="isShow" :data="treeData" @my-fresh="myFresh" :props="props"></org-com>
</template>

<script type="text/ecmascript-6">
  import OrgCom from './organiztionCom.vue'

  export default {
    components: {
      OrgCom
    },
    data () {
      return {
        treeData: [],
        props: {
          label: 'name'
        },
        isShow: true
      }
    },
    filters: {},
    mounted: function () {
      // this.getTreeData()
      this.$nextTick(function () {
      })
    },
    methods: {
      // getTreeData () { // 查询树节点的数据
      //   // TODO 先写死数据
      //   let arr = []
      //   let one
      //   for (let i = 0; i < 1; i++) {
      //     let chlds1 = []
      //     one = {
      //       id: i + 1,
      //       label: 'Qy',
      //       children: chlds1
      //     }
      //     arr.push(one)
      //     for (let j = 0; j < 3; j++) {
      //       let chlds2 = []
      //       one = {
      //         id: (i + 1) + '' + (j + 1),
      //         label: 'Qy_' + 'bm_' + j,
      //         children: chlds2
      //       }
      //       chlds1.push(one)
      //       for (let k = 0; k < 3; k++) {
      //         one = {
      //           id: (i + 1) + '' + (j + 1) + '' + (k + 1),
      //           label: 'Qy_' + 'bm_' + j + '_t' + k
      //         }
      //         chlds2.push(one)
      //       }
      //     }
      //   }
      //   this.$set(this, 'treeData', arr)
      // },
      myFresh(type, data) { // TODO 根据type确定如何刷新
        console.log(this.$t('organization.update'))
        let self = this
        this.isShow = false
        setTimeout(function () {
          self.isShow = true
        }, 10)
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>

