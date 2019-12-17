import ComponentAnalysis from './componentAnalysis/index.vue'
import ScatterAnalysis from './scatterAnalysis/index.vue'
import ScatterDcAnalysis from './scatterDCAnalysis/index.vue'

export default {
  components: {
    ComponentAnalysis,
    ScatterAnalysis,
    ScatterDcAnalysis,
  },
  created () {
  },
  data () {
    return {
      activeName: 'first',
    }
  },
  filters: {},
  mounted: function () {
    this.$nextTick(function () {
    })
  },
  methods: {
  },
  watch: {},
  computed: {}
}
