import systemParam from '@/service/systemParam'
export default {
  data () {
    return {
      activeName: 'first',
      systemForm: {},
      dialogImageUrl: '',
      dialogVisible: false,
      tableData3: [],
      rules: {
        systemName: [
          {required: true, message: this.$t('sysPar.req'), trigger: 'blur'}
        ],
        description: [
          {required: true, message: this.$t('sysPar.req'), trigger: 'blur'}
        ]
      }
    }
  },
  filters: {},
  mounted: function () {
    this.getSystemParem();
    this.$nextTick(function () {
    })
  },
  methods: {
    cellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },
    getSystemParem() {
      systemParam.getSystemParam().then(resp => {
        if (resp && resp.code === 1) {
          this.systemForm = resp.results || {}
        }
      })
    },
    convertDateFromString(dateString) {
      if (dateString) {
        var arr1 = dateString.split('.');
        var date = arr1[0].concat(' ').concat(arr1[1]);
        var formatDate = Date.parse(date).format(this.$t('dateFormat.yyyymmddhhmmss'));
        return formatDate;
      }
    },
    submitForm() {
      this.$refs.systemForm.validate((valid) => {
        if (valid) {
          systemParam.saveSystemParam(this.systemForm).then(resp => {
            if (resp && resp.code === 1) {
              this.$message(this.$t('sysPar.saveSuc'));
            } else {
              this.$message(resp.message);
            }
          });
          return true;
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  },
  watch: {},
  computed: {}
}
