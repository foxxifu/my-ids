module.exports = {
    queryUserRolSrc:function(params){
        var resultData={};//最终返回结果
        var templateA={"success":true,"data":{"expireTime":"1501084800000","DEVICENUM":"500","USERS":"50","licenseStatus":"ABOUT_EXPIRED","SENCE":"0","CAPACITY":"100"},"failCode":0,"params":null};

        params && params.body && defaultFunc(); //有传递参数
        function defaultFunc(param){
            switch (param){
                case "":
                    resultData = templateA;
                    break;
                default:
                    resultData = templateA;
            }
        }
        return resultData;
    }
}