// 告警详细信息的页面js
import DevAlarm from '@/pages/station/alarm/devAlarm/index.vue';
import SmartAlarm from '@/pages/station/alarm/smartAlarm/index.vue'

export default {
  name: 'alarm',
  components: {
    DevAlarm,
    SmartAlarm
  },
  filters: {},
  data () {
    return {
      activeName: 'DevAlarm'
    }
  },

  computed: {
  },

  methods: {
  },
  mounted () {
  }
}
