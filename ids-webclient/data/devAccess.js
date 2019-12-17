module.exports.R = {
  // 获取数采设备
  getSNDevList: function (params) {
    return {
      "code": 1,
      "message": "SUCCESS",
      "results": [
        {
          "devAlias": "数采",
          "id": 1,
          "protocolCode": "SNMODBUS",
          "signalVersion": "GPRS_2.0",
          "snCode": "GPRSV2"
        }
      ]
    }
  },
  // 获取上能协议的版本信息
  getSNSignalVersionList: function (params) {
    return {
      "code": 1,
      "message": "SUCCESS",
      "results": [
        {
          "devTypeId": 1,
          "devTypeName": "组串式逆变器",
          "name": "GPRSV2版本",
          "protocolCode": "SNMODBUS",
          "signalVersion": "GPRS_2.0"
        }
      ]
    }
  },
  // 验证设备的sn号的正确性
  checkAndQuerySnInfo: function (params) {
    return {
      "code": 1,
      "message": 1,
      "results": {
        "data": {
          "latitudes": 1,
          "longitudes": 1,
          "parentSn": 'GPRSV2',
          "sn": 1,
          "unitId": 1,
          "vender": 1
        },
        "type": 1
      }
    }
  },
  insertDevInfo: function (params) {
    return {
      code: 1,
      results: null
    }
  }
}
