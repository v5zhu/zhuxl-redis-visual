//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.common.web;

import com.zhuxl.redis.visual.common.persistence.Page;
import com.zhuxl.redis.visual.common.utils.DateUtils;
import com.zhuxl.redis.visual.system.utils.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public BaseController() {
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                this.setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            public String getAsText() {
                Object value = this.getValue();
                return value != null ? value.toString() : "";
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                this.setValue(DateUtils.parseDate(text));
            }
        });
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                Date date = DateUtils.parseDate(text);
                this.setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }

    public <T> Page<T> getPage(HttpServletRequest request) {
        int pageNo = 1;
        int pageSize = 20;
        String orderBy = "";
        String order = "asc";
        if (StringUtils.isNotEmpty(request.getParameter("page"))) {
            pageNo = Integer.valueOf(request.getParameter("page"));
        }

        if (StringUtils.isNotEmpty(request.getParameter("rows"))) {
            pageSize = Integer.valueOf(request.getParameter("rows"));
        }

        if (StringUtils.isNotEmpty(request.getParameter("sort"))) {
            orderBy = request.getParameter("sort").toString();
        }

        if (StringUtils.isNotEmpty(request.getParameter("order"))) {
            order = request.getParameter("order").toString();
        }

        return new Page(pageNo, pageSize, orderBy, order);
    }

    public <T> Map<String, Object> getEasyUIData(Page<T> page) {
        Map<String, Object> map = new HashMap();
        map.put("rows", page.getResult());
        map.put("total", page.getTotalCount());
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        return map;
    }
}
