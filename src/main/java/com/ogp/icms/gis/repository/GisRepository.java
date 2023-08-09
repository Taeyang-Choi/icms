package com.ogp.icms.gis.repository;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.gis.domain.CrimeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


@Slf4j
@Repository
public class GisRepository {
    private final DataSource dataSource;

    public GisRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("")
    public List<CrimeRequest> findByDate(String date1, String date2) {
        String sql = "select c.mgr_seq_no, c.reqst_dat, c.reqst_detail, c.crime_typ, c.doc_no, c.proc_stat_cd, c.req_gbn_cd, c.reqst_id, o.org_nm, m.depart_nm, m.user_nm" +
                " FROM xeus.crms_trans_rqst as c" +
                " LEFT JOIN xeus.mt_usr_desc as m" +
                " ON c.reqst_id = m.user_id" +
                " LEFT JOIN xeus.mt_orgz_desc as o" +
                " ON m.org_mgr_no = o.org_mgr_no" +
                " WHERE c.reqst_dat > ? AND c.reqst_dat < ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<CrimeRequest> list = new ArrayList<CrimeRequest>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date1 + "090000");
            pstmt.setString(2, date2 + "090000");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CrimeRequest crimeRequest = new CrimeRequest();
                crimeRequest.setId(rs.getLong("mgr_seq_no"));
                crimeRequest.setRequestDate(rs.getString("reqst_dat"));
                crimeRequest.setRequestDetail(rs.getString("reqst_detail"));
                crimeRequest.setCrimeType(rs.getString("crime_typ"));
                crimeRequest.setDocNo(rs.getString("doc_no"));

                //crimeRequest.setProcessStatCode(rs.getString("proc_stat_cd")); // 20230418 요청
                crimeRequest.setProcessStatCode(rs.getString("req_gbn_cd"));

                crimeRequest.setRequesterId(rs.getString("reqst_id"));
                crimeRequest.setRequesterName(rs.getString("user_nm"));
                crimeRequest.setOrgName(rs.getString("org_nm"));
                crimeRequest.setDept(rs.getString("depart_nm"));
                list.add(crimeRequest);
            }
        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return list;
    }

    public HashMap<String, Integer> getAllCount() {
        String sql = "SELECT crime_typ,count(crime_typ) FROM xeus.crms_trans_rqst" +
                " GROUP BY crime_typ";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        HashMap<String, Integer> countMap = new HashMap<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                countMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return countMap;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    /**
     * @TODO private으로 변경하기
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

    public String getCode(String code) {
        String sql = "select * FROM xeus.mt_cmm_cde WHERE cde_cde=?";
        String result = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, code);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd=  rs.getMetaData(); //rs의 정보를 얻어오는 메소드
            while (rs.next()) {
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    result += rs.getString(i) + ", ";
                }
                result += "<br>";
            }
        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return result;
    }

    public String getCodeGroup(String codeGroup) {
        String sql = "select * FROM xeus.mt_cmm_cde WHERE grp_cde=?";
        String result = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, codeGroup);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData(); //rs의 정보를 얻어오는 메소드
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    result += rs.getString(i) + ", ";
                }
                result += "<br>";
            }
        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return result;
    }

    public void dummy() {
        String sql = "INSERT INTO xeus.asset_cctv(" +
                " mgr_no, org_mgr_no, md_mgr_no, vms_mgr_no, site_mgr_no, cctv_nm, inst_dat, device_id, gbn_cd, ip_addr, use_yn, light_yn, infrd_yn, pan_yn, tilt_yn, zoom_yn, talk_yn, tour_yn, con_id, con_pwd, snmp_str, const_year, const_nm, loc_desc, rmark, cctv_desc, user_id, play_yn)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            for(int index = 1; index < 6; index ++) {
                pstmt.setString(1, index+"");

                for(int i = 2; i <= 28; i++) {
                    pstmt.setString(i, "a");
                }
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    // 모든 카메라를 가져온다.
    public ArrayList<Camera> getAllCameras() {
        ArrayList<Camera> list = new ArrayList<>();

        String sql = "SELECT ac.*,nc.addr,nc.cde_nm" +
                " FROM xeus.asset_cctv as ac" +
                " JOIN xeus.network_cctv as nc" +
                " ON ac.cctv_nm = nc.cctv_nm";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Camera camera = new Camera();

                try {
                    camera.setId(rs.getLong("device_id"));
                }
                catch (Exception e) {
                    continue;
                }
                camera.setCctvGubun(rs.getString("cde_nm")); // 구분
                camera.setDept(rs.getString("org_mgr_no"));

                camera.setJuso(rs.getString("addr"));
                camera.setDept("부서");
                camera.setInstallymd(rs.getString("const_year"));

                camera.setCctvIndex(rs.getString("cctv_nm"));
                camera.setLocation(rs.getString("loc_desc"));

                camera.setCameraCategory(rs.getString("cde_nm"));
                camera.setMovement("무브먼트");
                camera.setNightvision(rs.getString("infrd_yn"));
                camera.setShage("형태");
                camera.setInstallymd(rs.getString("const_year"));

                camera.setManufacturer("제조사");
                camera.setModel("모델");
                camera.setPixel("화소수");
                camera.setConnectCnt("연결대수");
                camera.setIntegrationCnt("1");

                camera.setConnectType("접속종류");
                camera.setConnectIp(rs.getString("ip_addr"));
                camera.setConnectPort(rs.getString("port_num"));
                camera.setConnectId(rs.getString("con_id"));
                camera.setConnectPw(rs.getString("con_pwd"));

                camera.setConnectModel(rs.getString("md_mgr_no"));
                camera.setConnectServerType(rs.getString("site_mgr_no"));
                camera.setSmCompany("유지보수업체");
                camera.setSmPerson("유지보수담당자");
                camera.setSmTel("번호");

                camera.setPtzUseyn(rs.getString("tilt_yn"));
                camera.setFallCamera("");
                camera.setFallDefinition("");
                camera.setFallEquipment("");
                camera.setFallNetwork("");

                //20230227 추가 위 경도
                camera.setAnnox(rs.getString("_annox"));
                camera.setAnnoy(rs.getString("_annoy"));
                list.add(camera);
            }
        } catch (Exception e) {
            log.error("db error {}", e);
        } finally {
            close(con, pstmt, rs);
        }

        return list;
    }
    
    // 그냥 모든 데이터 String으로 출력
    public String printTable(String tableName) {
        String sql = "select * FROM xeus." + tableName;
        String result = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd=  rs.getMetaData(); //rs의 정보를 얻어오는 메소드
            while (rs.next()) {
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    result += rs.getString(i) + ", ";
                }
                result += "<br>";
            }
        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return result;
    }

    public String getCamera(String name) {
        String sql = "select * FROM xeus.asset_cctv WHERE cctv_nm=?";
        String result = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData(); //rs의 정보를 얻어오는 메소드
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    result += rsmd.getColumnName(i) + "," +rs.getString(i) + "<br>";
                }
            }
        } catch (Exception e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return result;
    }
}