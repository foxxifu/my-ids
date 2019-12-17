<template>
    <div>
      <span v-if="myData === '-'">
        -
      </span>
      <div v-else :title="myData | myTitleFilter">
        <span v-for="(item, index) in myData" :key="index" class="my-color">
          <span :class="getClassFilter(item)">
             {{item | itemFormat(index)}}
          </span>
          {{index === 2 ? '' : '|'}}
        </span>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  const props = {
    myData: []
  }
  let tempArr;
  let tempArr2;
  let nomal;

  export default {
    props: props,
    components: {},
    created () {
      tempArr = this.$t('comAna.lossType') //  ['故障:', '低效:', '遮挡:']
      tempArr2 = this.$t('comAna.lossTypeArr1') //  ['故障损失电量:', '低效损失电量:', '遮挡损失电量:']
      nomal = this.$t('comAna.normal') //  正常
    },
    data () {
      return {}
    },
    filters: {
      itemFormat (val, index) {
        if (val === '0|0') {
          return tempArr[index] + nomal
        }
        return tempArr[index] + (val.split('|')[0]).fixed(2)
      },
      myTitleFilter (dataArr) {
        let str = ''
        let len = dataArr.length;
        for (let i = 0; i < dataArr.length; i++) {
          str += dataArr[i] === '0|0' ? (tempArr[i] + nomal) : (tempArr2[i] + (dataArr[i].split('|')[0]).fixed(2))
          if (i < len - 1) {
            str += '|'
          }
        }
        return str
      }
    },
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      getClassFilter (val) {
        if (val === '0|0') {
          return ''
        }
        return 'color-' + val.split('|')[1]
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>

 .my-color {
   span.color-1{
     background: #FE3230;
   }
   span.color-2{
     background: #efc54d;
   }
   span.color-3{
     background: #2eb1e2;
   }
 }
</style>
