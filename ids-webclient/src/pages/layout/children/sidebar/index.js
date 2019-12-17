import { mapState } from 'vuex'

export default {
  name: 'sidebar',
  props: {
    menuList: {
      type: Array,
      default: () => []
    },
    isSubMenu: {
      type: Boolean,
      default: false
    },
  },
  data () {
    return {}
  },
  computed: {
    ...mapState([
      'permissionRoutes',
      'toggleSidebar',
    ]),
    menus () {
      return this.menuList
    },
    subMenuState () {
      return this.isSubMenu
    }
  },
  created () {
  },
  methods: {},
  watch: {},
}
