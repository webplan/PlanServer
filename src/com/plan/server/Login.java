package com.plan.server;/**
 * Created by snow on 15-5-31.
 */

import com.opensymphony.xwork2.ActionSupport;
import com.plan.data.UserEntity;
import com.plan.function.Config;
import com.plan.function.DataOperate;
import com.plan.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Login extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    private String account;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }


    private String password_md5;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("Login:" + account + "," + password_md5);
        String ret = "";
        JSONObject obj = new JSONObject();
        try {
            DataOperate dataop = new DataOperate();
            List it = dataop.SelectTb("from UserEntity as user where user.account =:para1", account);
            if (it.size() == 1) {
                UserEntity user = (UserEntity) it.get(0);
                if (user.getPassword().equals(password_md5)) {//判斷密碼是否正確
                    int x = (int) (Math.random() * 100);
                    String token = Config.MD5(x + "");
                    user.setToken(token);
                    dataop.UpdataTb(user);
                    obj.put("status", 1);
                    obj.put("token", user.getToken());
                }else
                    obj.put("status", 0);
            } else
                obj.put("status", 0);
        } catch (Exception e) {
            try {
                obj.put("status", 0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        ret = obj.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword_md5() {
        return password_md5;
    }

    public void setPassword_md5(String password_md5) {
        this.password_md5 = password_md5;
    }
}