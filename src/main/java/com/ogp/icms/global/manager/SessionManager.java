package com.ogp.icms.global.manager;


import javax.servlet.http.HttpSession;

import com.ogp.icms.global.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SessionManager {
    private final SqlUtil sqlUtil;
    public JSONParser jp;

    public SessionManager(SqlUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
        this.jp = new JSONParser();
    }

    // Invoked from SessionServlet
    public void set(HttpSession session, JSONObject jsonobj) throws Exception {
        session.setAttribute("sesobj", jsonobj.toJSONString());
        session.setMaxInactiveInterval(30 * 24 * 60 * 60);
    }

    // Invoked from SessionServlet
    public JSONObject get(HttpSession session, String id, String accessToken, String refreshToken) {
        session.setMaxInactiveInterval(30 * 24 * 60 * 60);
        String sess = (String) session.getAttribute("sesobj");

        try {
            if(sess == null) {
                // sess가 null이면 accessToken 체크 해서 db에서 가져옴
                /*
                String storedData = sqlUtil.pQuery("SELECT access_token FROM gs_superadmin WHERE userid=?", id);
                if(storedData.equals(accessToken)) {
                    return  (JSONObject) jp.parse(sqlUtil.pQuery(true, "SELECT jsonobj FROM gs_superadmin WHERE userid=?", id));
                }
                 */
                return null;
            }
            else {
                return (JSONObject) jp.parse(sess);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void init(HttpSession session, JSONObject jsonObject) {
        try {
            set(session, jsonObject);
        }
        catch (Exception e) {

        }
    }

    public void put(HttpSession session, String id, String accessToken, String refreshToken, String name, JSONObject jsonobj) throws Exception {
        if (jsonobj == null) return;

        JSONObject sesobj = get(session, id, accessToken, refreshToken);
        if (sesobj == null) {
            sesobj = new JSONObject();
        }
        sesobj.put(name, jsonobj);
        set(session, sesobj);
    }

    public void remove(HttpSession session, String name, String id, String accessToken, String refreshToken) throws Exception {
        JSONObject sesobj = get(session, id, accessToken, refreshToken);
        if (sesobj != null) {
            sesobj.remove(name);
            set(session, sesobj);
        }
    }

    public void remove(HttpSession session) throws Exception {
        session.invalidate();
    }
}
