import fileUpload from '@/components/fileUpload/index.vue'
import licenseService from '@/service/license'

export default {
  components: {
    fileUpload
  },
  data () {
    return {
      fileName: null,
      licenseInfo: { }
    }
  },
  mounted () {
    let self = this;
    self.showLicenseInfo();
    this.$nextTick(function () {
    });
  },
  methods: {
    // 展示license基本信息
    showLicenseInfo: function(){
      licenseService.getLicenseInfo().then(resp => {
        if (resp && resp.code === 1){
          this.licenseInfo = resp.results || {};
          if (this.licenseInfo.hasOwnProperty('importDate')){
            this.licenseInfo.importDate = new Date(this.licenseInfo.importDate).format(this.$t('dateFormat.yyyymmddhhmmss'))
          }
        }
      });
    },
    // 上传文件验证通过后，回填文件名
    setInputText: function(file){
      this.fileName = file.name;
    },
    // 导入成功
    importSuccess: function(){
      this.$message(this.$t('licMan.import_success'));
      this.fileName = null;
      this.showLicenseInfo();
    },
    // 导入失败
    importFailed: function(){
      this.$message(this.$t('licMan.import_fail'));
    },
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
  }
}
