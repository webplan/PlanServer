package com.plan.server;/**
 * Created by snow on 15-6-7.
 */

import com.opensymphony.xwork2.ActionSupport;
import com.plan.data.PlanEntity;
import com.plan.data.UserEntity;
import com.plan.function.Config;
import com.plan.function.DataOperate;
import com.plan.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class View extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;
    private String account;
    private String token;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("View:" + account + "," + token);
        String ret = "";
        JSONObject obj = new JSONObject();
        JSONArray jsarray = new JSONArray();
        try {
            DataOperate dataop = new DataOperate();
            boolean istoken = Config.CheckToken(dataop, account, token);
            if (istoken) {//token正確
                String hql = "from PlanEntity p where p.planId = " +
                        "any(select planId from PeopleInPlanEntity pe where pe.account=:para1" +
                        " and pe.returnTime is not null)";
                List list = dataop.SelectTb(hql, account);
                //person 不知道該怎麼存與傳 invite,view
                for (int i = 0; i < list.size(); i++) {
                    PlanEntity pe = (PlanEntity) list.get(i);
                    if (pe.getTime() == null || pe.getLocation() == null)
                        continue;
                    JSONObject location = new JSONObject();
                    location.put("location", pe.getLocation());
                    location.put("lat", pe.getLocationLat());
                    location.put("lon", pe.getLocationLon());

                    JSONObject jsob = new JSONObject();
                    jsob.put("title", pe.getTitle());
                    jsob.put("info", pe.getInfo());

                    jsob.put("time", pe.getTime());
                    jsob.put("location", location);
                    jsob.put("plan_id", pe.getPlanId());
                    //查询person
                    hql = "from UserEntity user where user.account = " +
                            "any(select account from PeopleInPlanEntity as pp where pp.planId=:para1" +
                            " and pp.returnTime is not null)";
                    List listPerson = dataop.SelectTb(hql, pe.getPlanId());
                    JSONArray jsPerson = new JSONArray();
                    for (int j = 0; j < listPerson.size(); j++) {
                        UserEntity user = (UserEntity) listPerson.get(j);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("account", user.getAccount());
                        jsonObject.put("nickname", user.getNickname());
                        jsonObject.put("avatag", user.getAvatag());
                        jsPerson.put(jsonObject);
                    }
                    jsob.put("person", jsPerson);
                    jsarray.put(jsob);
                }
                obj.put("status", 1);
                obj.put("planlist", jsarray);
            } else {
                obj.put("status", 2);
            }
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}