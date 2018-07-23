//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.web;

import com.zhuxl.redis.visual.system.utils.Constants;
import com.zhuxl.redis.visual.system.utils.DBUtil;
import com.zhuxl.redis.visual.system.utils.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping({"treesoft"})
public class LoginController {
    public static Map<String, String> loginUserMap = new HashMap();

    public LoginController() {
    }

    @RequestMapping(
            value = {"login"},
            method = {RequestMethod.GET}
    )
    public String login() {
        return "system/login";
    }

    @RequestMapping({"index"})
    public String treesoft(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String databaseType = Constants.DATABASETYPE;
        request.setAttribute("username", username);
        request.setAttribute("databaseType", databaseType);
        return "system/index";
    }

    @RequestMapping(
            value = {"loginVaildate"},
            method = {RequestMethod.POST}
    )
    public String loginVaildate(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username").toLowerCase();
        String password = request.getParameter("password").toLowerCase();
        String captcha = request.getParameter("captcha").toLowerCase();
        username = StringEscapeUtils.escapeHtml4(username.trim());
        HttpSession session = request.getSession(true);
        String cap = (String) session.getAttribute("KAPTCHA_SESSION_KEY");
        String message = "";
        HashMap<String, Object> map = new HashMap();
        if (username != "" && username != null) {
            String identifyingCode = "0";

            try {
                Resource resource = new ClassPathResource("application.properties");
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                identifyingCode = (String) props.get("identifyingCode");
            } catch (IOException var18) {
                var18.printStackTrace();
            }

            if (identifyingCode.equals("1") && !captcha.equals(cap)) {
                message = "验证码错误！";
                map.put("error", message);
                request.setAttribute("message", message);
                return "system/login";
            } else {
                new ArrayList();
                String sql = " select * from treesoft_users where  username='" + username + "'";
                DBUtil db = new DBUtil();

                List list;
                try {
                    list = db.executeQuery(sql);
                } catch (Exception var17) {
                    list = null;
                    var17.printStackTrace();
                }

                if (list.size() <= 0) {
                    message = "您输入的帐号或密码有误！";
                    map.put("error", message);
                    request.setAttribute("message", message);
                    return "system/login";
                } else {
                    String pas = (String) ((Map) list.get(0)).get("password");
                    if (!pas.equals(StringUtils.MD5(password + "treesoft" + username))) {
                        message = "您输入的帐号或密码有误！";
                        map.put("error", message);
                        request.setAttribute("message", message);
                        return "system/login";
                    } else {
                        message = "登录成功！";
                        session.setAttribute("LOGIN_USER_NAME", username);
                        loginUserMap.put(username, session.getId());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        System.out.println("TreeNMS login user " + username + " " + sdf.format(new Date()));
                        db.initDbConfig();
                        String databaseType = Constants.DATABASETYPE;
                        request.setAttribute("username", username);
                        request.setAttribute("databaseType", databaseType);
                        return "redirect:/treesoft/index";
                    }
                }
            }
        } else {
            message = "请输入帐号！";
            map.put("error", message);
            request.setAttribute("message", message);
            return "system/login";
        }
    }

    @RequestMapping({"logout"})
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("LOGIN_USER_NAME");
        request.getSession().invalidate();
        return "system/login";
    }
}
