import { mapState } from 'vuex'

export default {
  name: 'navBar',
  data () {
    return {
      levelList: null,
      // currentCompany: this.$store.state.company,
      currentCompany: '',
      currentDate: new Date(),
    }
  },
  computed: {
    ...mapState([
      'permissionRoutes',
    ]),
    formatCurrentDate: (vm) => {
      return vm.$moment(vm.currentDate).format('YYYY-MM-DD hh:mm:ss')
    },
  },
  created () {
    this.getBreadcrumb()
  },
  mounted: function () {
    const _this = this
    this.timer = setInterval(function () {
      _this.currentDate = new Date()
    }, 1000)
  },
  beforeDestroy: function () {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    getBreadcrumb () {
      this.levelList = this.$route.matched.filter(item => item.name)
    },
  },
  watch: {
    $route () {
      this.getBreadcrumb()
    },
  },
}
