package com.ogp.icms.global.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

@Slf4j
public class HttpClientTool {

    public String apiCall(String TAG, HttpURLConnection con) {
        try {
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            }
            else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }

            String line;
            StringBuffer sbuf = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sbuf.append(line);
            }
            br.close();
            return sbuf.toString();

        } catch (Exception ex) {
            return ex.getLocalizedMessage();
        }
    }
}
