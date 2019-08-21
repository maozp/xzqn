package com.gangukeji.xzqn.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Author: hx
 * @Date: 2019/7/23 15:11
 * @Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthView {

    /**
     * showapi_res_error :
     * showapi_res_id : 8b83bad3588d466d971cecd391f58fc9
     * showapi_res_code : 0
     * showapi_res_body : {"birthday":"1995-11-18","sex":"M","ret_code":0,"address":"江西","code":0,"msg":"匹配"}
     */
    /**
     *                  {
     *                   "showapi_res_error": "",
     *                   "showapi_res_id": "c348b74c77564e22b400d12099407620",
     *                   "showapi_res_code": 0,
     *                   "showapi_res_body": {"ret_code":-1,"code":12,"msg":"身份证号码不合法!"}
     *                 }
     * */
    private String showapi_res_error;
    private String showapi_res_id;
    private int showapi_res_code;
    private ShowapiResBodyBean showapi_res_body;

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public String getShowapi_res_id() {
        return showapi_res_id;
    }

    public void setShowapi_res_id(String showapi_res_id) {
        this.showapi_res_id = showapi_res_id;
    }

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShowapiResBodyBean {
        /**
         * birthday : 1995-11-18
         * sex : M
         * ret_code : 0
         * address : 江西南丰县
         * code : 0
         * msg : 匹配
         */

        private String birthday;
        private String sex;
        private int ret_code;
        private String address;
        private int code;
        private String msg;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
