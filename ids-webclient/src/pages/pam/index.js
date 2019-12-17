import assetsOfManufacturer from './assetsOfManufacturer/index.vue'
import exchangeOfManufacturer from './exchangeOfGoodsOfManufacturer/index.vue'
import statisticsOfManufacturer from './statisticsOfManufacturer/index.vue'
import projectOfManufacturer from './projectOfManufacturer/index.vue'

import assetsOfepc from './assetsOfepc/index.vue'
import exchangeOfepc from './exchangeOfGoodsOfepc/index.vue'
import statisticsOfepc from './statisticsOfepc/index.vue'
import projectOfepc from './projectOfepc/index.vue'
import { cookie } from 'cookie_js'

export default {
  data() {
    return {
      isEpc: false,
      reportTabName: "assets"
    };
  },
  created() {
    let userInfo = JSON.parse(cookie.get('userInfo'));
    if (userInfo.username === "epc"){
      this.$data.isEpc = true;
    }
  },
  methods: {
    tabClick(tab) {
      if (tab.index === "2"){
        if (document.getElementById("shipments").children[0].style.width === "100px"){
          document.getElementById("tab-shipments").click();
        }
      }
    }
  },
  components: {
    assetsOfManufacturer,
    exchangeOfManufacturer,
    statisticsOfManufacturer,
    projectOfManufacturer,
    assetsOfepc,
    exchangeOfepc,
    statisticsOfepc,
    projectOfepc,
  },
};
