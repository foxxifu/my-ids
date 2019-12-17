export default {
  name: 'ContentArea',
  computed: {
    key() {
      return this.$route.name !== undefined ? this.$route.name + '_' + +new Date() : +new Date()
    },
  },
}
