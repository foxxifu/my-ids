package com.interest.ids.common.project.support;

import com.interest.ids.common.project.constant.ResponseConstants;

/**
 * @Author: sunbjx
 * @Description: 前端返回标识支撑
 * @Date: Created in 上午10:15 18-2-1
 * @Modified By:
 */
public class Responses {

    public static Response SUCCESS() {
        return new Response(ResponseConstants.CODE_SUCCESS);
    }

    public static Response FAILED() {
        return new Response(ResponseConstants.CODE_FAILED);
    }

    public static Response AUTH() {
        return new Response(ResponseConstants.CODE_AUTH);
    }

}
