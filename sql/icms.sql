# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 11.0.2-MariaDB)
# Database: icms
# Generation Time: 2023-08-13 13:12:47 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table camera_license
# ------------------------------------------------------------

DROP TABLE IF EXISTS `camera_license`;

CREATE TABLE `camera_license` (
                                  `id` mediumint(8) unsigned NOT NULL,
                                  `index` varchar(128) DEFAULT NULL,
                                  `created_at` timestamp NULL DEFAULT NULL,
                                  `updated_at` timestamp NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
                          `name` varchar(16) NOT NULL,
                          `jsonobj` text DEFAULT NULL,
                          PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_asset
# ------------------------------------------------------------


DROP TABLE IF EXISTS `gs_asset`;
CREATE TABLE `gs_asset` (
                            `id` varchar(10) NOT NULL,
                            `purpose` varchar(16) DEFAULT NULL,
                            `dept` varchar(16) DEFAULT NULL,
                            `type` varchar(16) DEFAULT NULL,
                            `project_name` varchar(64) DEFAULT NULL,
                            `project_type` varchar(16) DEFAULT NULL,
                            `vms_id` varchar(8) DEFAULT NULL,
                            `name` varchar(255) DEFAULT NULL,
                            `emd` varchar(8) DEFAULT NULL,
                            `li` varchar(8) DEFAULT NULL,
                            `town` varchar(8) DEFAULT NULL,
                            `address` varchar(64) DEFAULT NULL,
                            `ref_id` varchar(16) DEFAULT NULL,
                            `lat` varchar(16) DEFAULT NULL,
                            `lng` varchar(16) DEFAULT NULL,
                            `direction` varchar(8) DEFAULT NULL,
                            `jsonobj` text DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;




# Dump of table gs_board_comment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_board_comment`;

CREATE TABLE `gs_board_comment` (
                                    `idx` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                    `id` int(10) unsigned DEFAULT 0,
                                    `tbl` varchar(30) NOT NULL DEFAULT '',
                                    `name` varchar(30) NOT NULL DEFAULT '',
                                    `userid` varchar(30) DEFAULT '',
                                    `passwd` varchar(50) NOT NULL DEFAULT '',
                                    `userip` varchar(50) NOT NULL DEFAULT '',
                                    `msg_text` text DEFAULT NULL,
                                    `regdate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
                                    `times` varchar(5) DEFAULT NULL,
                                    `place` varchar(50) DEFAULT NULL,
                                    `tel` varchar(30) DEFAULT NULL,
                                    `thread` int(11) DEFAULT 0,
                                    `depth` smallint(6) DEFAULT 0,
                                    `pos` int(11) DEFAULT 0,
                                    `thread2` int(11) DEFAULT 0,
                                    `referer_url` text DEFAULT NULL,
                                    `jsonobj` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                    PRIMARY KEY (`idx`),
                                    UNIQUE KEY `idx` (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_board_files
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_board_files`;

CREATE TABLE `gs_board_files` (
                                  `seq` int(11) NOT NULL AUTO_INCREMENT,
                                  `fid` int(11) DEFAULT 0,
                                  `tid` varchar(50) DEFAULT '',
                                  `num` int(11) NOT NULL DEFAULT 0,
                                  `realname` varchar(100) NOT NULL DEFAULT '',
                                  `title` varchar(200) NOT NULL DEFAULT '',
                                  `regdate` datetime DEFAULT '0000-00-00 00:00:00',
                                  `counts` int(11) DEFAULT 0,
                                  `down` int(11) DEFAULT 0,
                                  `jsonobj` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                  PRIMARY KEY (`seq`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_board_master
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_board_master`;

CREATE TABLE `gs_board_master` (
                                   `idx` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                   `use_flag` char(2) NOT NULL DEFAULT 'Y',
                                   `bbs_id` varchar(200) NOT NULL DEFAULT '',
                                   `bbs_name` varchar(200) NOT NULL DEFAULT '',
                                   `bbs_title` text DEFAULT NULL,
                                   `contents_img` varchar(250) DEFAULT '',
                                   `file_01` varchar(250) DEFAULT '',
                                   `file_02` varchar(250) DEFAULT '',
                                   `bbs_type` varchar(30) DEFAULT '',
                                   `bbs_kind` varchar(30) DEFAULT NULL,
                                   `bbs_notice` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_tel` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_email` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_cer` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_link` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_file` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_filenum` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_count` varchar(1) NOT NULL DEFAULT '0',
                                   `bbs_comment` varchar(1) NOT NULL DEFAULT '0',
                                   `cate_flag` char(1) DEFAULT 'N',
                                   `cate_code` varchar(30) DEFAULT NULL,
                                   `cate_name` varchar(50) NOT NULL DEFAULT '',
                                   `secret_flag` char(1) NOT NULL DEFAULT '',
                                   `file_num` varchar(4) NOT NULL DEFAULT '0',
                                   `counts` int(11) NOT NULL DEFAULT 0,
                                   `signdate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
                                   `jsonobj` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                   PRIMARY KEY (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_board_notice
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_board_notice`;

CREATE TABLE `gs_board_notice` (
                                   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                   `category` varchar(30) DEFAULT '',
                                   `userid` varchar(30) DEFAULT '',
                                   `username` varchar(50) DEFAULT '',
                                   `email` varchar(50) DEFAULT '',
                                   `home` varchar(40) DEFAULT '',
                                   `title` varchar(250) DEFAULT NULL,
                                   `body` text DEFAULT NULL,
                                   `notice_edate` datetime DEFAULT '0000-00-00 00:00:00',
                                   `count` int(11) DEFAULT 0,
                                   `thread` int(11) DEFAULT 0,
                                   `depth` smallint(6) DEFAULT 0,
                                   `pos` int(11) DEFAULT 0,
                                   `passwd` varchar(8) DEFAULT '',
                                   `user_file` varchar(512) DEFAULT '',
                                   `user_ip` varchar(20) DEFAULT '',
                                   `delflag` char(1) DEFAULT '',
                                   `is_secret` char(1) DEFAULT '0',
                                   `tel` varchar(20) DEFAULT '',
                                   `notice_yn` char(1) DEFAULT '0',
                                   `site` varchar(20) DEFAULT '',
                                   `recommend` int(11) DEFAULT 0,
                                   `smsflag` char(1) DEFAULT '',
                                   `created_at` datetime DEFAULT NULL,
                                   `updated_at` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_board_qna
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_board_qna`;

CREATE TABLE `gs_board_qna` (
                                `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                `category` varchar(30) DEFAULT '',
                                `userid` varchar(30) DEFAULT '',
                                `username` varchar(50) DEFAULT '',
                                `email` varchar(50) DEFAULT '',
                                `home` varchar(40) DEFAULT '',
                                `title` varchar(250) DEFAULT NULL,
                                `body` text DEFAULT NULL,
                                `notice_edate` datetime DEFAULT '0000-00-00 00:00:00',
                                `count` int(11) DEFAULT 0,
                                `thread` int(11) DEFAULT 0,
                                `depth` smallint(6) DEFAULT 0,
                                `pos` int(11) DEFAULT 0,
                                `passwd` varchar(8) DEFAULT '',
                                `user_file` varchar(512) DEFAULT '',
                                `user_ip` varchar(20) DEFAULT '',
                                `delflag` char(1) DEFAULT '',
                                `is_secret` char(1) DEFAULT '0',
                                `tel` varchar(20) DEFAULT '',
                                `notice_yn` char(1) DEFAULT '0',
                                `site` varchar(20) DEFAULT '',
                                `recommend` int(11) DEFAULT 0,
                                `smsflag` char(1) DEFAULT '',
                                `created_at` datetime DEFAULT NULL,
                                `updated_at` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_cctv
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_cctv`;

CREATE TABLE `gs_cctv` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `cctv_gubun` varchar(255) DEFAULT '' COMMENT 'CCTV구분',
                           `dept` varchar(255) DEFAULT '' COMMENT '담당부서',
                           `juso` varchar(255) DEFAULT '' COMMENT '설치읍면동',
                           `cctv_index` varchar(512) DEFAULT '' COMMENT '인덱스',
                           `location` varchar(255) DEFAULT '' COMMENT '위치',
                           `camera_category` varchar(255) DEFAULT '' COMMENT '카메라분류',
                           `movement` varchar(255) DEFAULT '' COMMENT '고정/회전',
                           `nightvision` varchar(255) DEFAULT '' COMMENT '야간식별',
                           `shage` varchar(255) DEFAULT '' COMMENT '형태',
                           `installymd` varchar(255) DEFAULT '' COMMENT '설치년도',
                           `manufacturer` varchar(255) DEFAULT '' COMMENT '제조사',
                           `model` varchar(255) DEFAULT '' COMMENT '모델명',
                           `pixel` varchar(255) DEFAULT '' COMMENT '화소수',
                           `connect_cnt` varchar(11) DEFAULT NULL COMMENT '카메라연결대수',
                           `camera_cnt` varchar(11) DEFAULT NULL COMMENT '카메라대수',
                           `integration_cnt` varchar(11) DEFAULT NULL COMMENT '통합대수',
                           `connect_type` varchar(255) DEFAULT NULL COMMENT '접속종류',
                           `connect_ip` varchar(255) DEFAULT NULL COMMENT '접속IP',
                           `connect_port` varchar(255) DEFAULT NULL COMMENT '접속포트',
                           `connect_id` varchar(255) DEFAULT NULL COMMENT '접속아이디',
                           `connect_pw` varchar(255) DEFAULT NULL COMMENT '접속비밀번호',
                           `connect_model` varchar(255) DEFAULT NULL COMMENT '접속모델',
                           `connect_server_type` varchar(255) DEFAULT NULL COMMENT '서버타입',
                           `sm_company` varchar(255) DEFAULT NULL COMMENT '유지보수업체명',
                           `sm_person` varchar(255) DEFAULT NULL COMMENT '유지보수담당자',
                           `sm_tel` varchar(255) DEFAULT NULL COMMENT '연락처',
                           `ptz_useyn` varchar(255) DEFAULT NULL COMMENT 'ptz_사용유무',
                           `preset_useyn` varchar(255) DEFAULT NULL COMMENT 'preset사용유무',
                           `fall_camera` varchar(255) DEFAULT NULL COMMENT '카메라고장여부',
                           `fall_definition` varchar(255) DEFAULT NULL COMMENT '화질상태',
                           `fall_equipment` varchar(255) DEFAULT NULL COMMENT '장비',
                           `fall_network` varchar(255) DEFAULT NULL COMMENT '네트워크',
                           `annox` varchar(50) DEFAULT NULL,
                           `annoy` varchar(50) DEFAULT NULL,
                           `direction` VARCHAR(8) DEFAULT NULL,
                           `lat` varchar(16) DEFAULT NULL,
                           `lng` varchar(16) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='CCTV관리';



# Dump of table gs_cctv_error
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_cctv_error`;

CREATE TABLE `gs_cctv_error` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `cctv_id` int(10) unsigned DEFAULT NULL,
                                 `cctv_index` varchar(128) DEFAULT NULL,
                                 `cctv_gubun` varchar(255) DEFAULT NULL,
                                 `juso` varchar(255) DEFAULT NULL,
                                 `ymd` varchar(16) DEFAULT NULL,
                                 `model` varchar(255) DEFAULT NULL,
                                 `data` varchar(255) DEFAULT '{}',
                                 `created_at` datetime DEFAULT NULL,
                                 `updated_at` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_dailyreport
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_dailyreport`;

CREATE TABLE `gs_dailyreport` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `userid` varchar(30) DEFAULT '',
                                  `username` varchar(50) DEFAULT '',
                                  `user_file` varchar(50) DEFAULT '',
                                  `user_ip` varchar(20) DEFAULT '',
                                  `summary` varchar(255) DEFAULT NULL,
                                  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '상태값(0:작성중, 1:제출, 2:승인)',
                                  `work_gubun` varchar(255) DEFAULT NULL COMMENT '근무구분(01:주간, 02:야간)',
                                  `work_monitoring` varchar(255) DEFAULT NULL COMMENT '모니터링PC',
                                  `work_date_from` varchar(20) DEFAULT NULL COMMENT '근무일_from',
                                  `work_date_to` varchar(20) DEFAULT NULL COMMENT '근무일_to',
                                  `work_time_from` varchar(255) DEFAULT NULL COMMENT '근무시간from',
                                  `work_time_to` varchar(255) DEFAULT NULL COMMENT '근무시간to',
                                  `team` varchar(255) DEFAULT NULL COMMENT '조',
                                  `submit_userid` varchar(255) DEFAULT NULL COMMENT '제출자id',
                                  `submit_date` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '제출시간',
                                  `confirm_userid` varchar(255) DEFAULT NULL COMMENT '결제자id',
                                  `confirm_date` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '결재일자',
                                  `created_at` datetime DEFAULT '0000-00-00 00:00:00',
                                  `updated_at` datetime DEFAULT '0000-00-00 00:00:00',
                                  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_dailyreport_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_dailyreport_detail`;

CREATE TABLE `gs_dailyreport_detail` (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
                                         `dailyreport_id` int(11) NOT NULL DEFAULT 0,
                                         `userid` varchar(30) DEFAULT '' COMMENT '근무자id',
                                         `work_date` varchar(10) DEFAULT NULL,
                                         `work_dtime_from` varchar(255) DEFAULT NULL COMMENT '상세근무시간from',
                                         `work_dtime_to` varchar(255) DEFAULT NULL COMMENT '상세근무시간to',
                                         `work_dgubun` varchar(255) DEFAULT NULL COMMENT '상세근무구분(D01:상황, D02:장애, D03:기타)',
                                         `work_monitoring` varchar(255) DEFAULT NULL COMMENT '모니터링PC',
                                         `cctv_id` int(10) unsigned DEFAULT NULL,
                                         `cctv_index` varchar(255) DEFAULT '' COMMENT 'cctv',
                                         `work_content` varchar(4000) DEFAULT '' COMMENT '근무내용',
                                         `action_code` varchar(255) DEFAULT 'R02' COMMENT '조치(R01:상활전파, R02:미조치, R03:조치완료, R04:미조치완료)',
                                         `action_result` varchar(4000) DEFAULT '' COMMENT '조치결과',
                                         `barrier_level` varchar(255) DEFAULT 'D' COMMENT '장애레벨',
                                         `action_regdate` datetime DEFAULT NULL,
                                         `action_userid` varchar(32) DEFAULT NULL,
                                         `created_at` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '등록일자',
                                         `updated_at` datetime DEFAULT '0000-00-00 00:00:00',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='근무일지상세';



# Dump of table gs_kindcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_kindcode`;

CREATE TABLE `gs_kindcode` (
                               `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                               `code` varchar(50) NOT NULL,
                               `name` varchar(50) NOT NULL,
                               `essential` tinyint(4) DEFAULT 0,
                               `code_format` varchar(10) NOT NULL DEFAULT '',
                               `seq` int(11) DEFAULT NULL,
                               `active` tinyint(4) DEFAULT 1,
                               `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
                               `updated_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
                               PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_schedule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_schedule`;

CREATE TABLE `gs_schedule` (
                               `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
                               `division` varchar(20) DEFAULT '',
                               `userwid` varchar(20) DEFAULT '',
                               `userwnm` varchar(30) DEFAULT '',
                               `datecode` char(6) DEFAULT NULL,
                               `workform` varchar(16) DEFAULT NULL,
                               `grade` varchar(8) DEFAULT NULL,
                               `team` varchar(16) DEFAULT NULL,
                               `sdate` datetime DEFAULT NULL,
                               `edate` datetime DEFAULT NULL,
                               `updated_at` datetime DEFAULT '0000-00-00 00:00:00',
                               `created_at` datetime DEFAULT '0000-00-00 00:00:00',
                               PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_selcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_selcode`;

CREATE TABLE `gs_selcode` (
                              `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                              `kind_code` varchar(32) NOT NULL,
                              `code` varchar(32) NOT NULL,
                              `name` varchar(32) NOT NULL DEFAULT '',
                              `active` tinyint(4) DEFAULT NULL,
                              `seq` int(11) NOT NULL DEFAULT 0,
                              `remarks` varchar(128) DEFAULT NULL,
                              `user_file` varchar(512) DEFAULT '',
                              `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
                              `updated_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
                              PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_superadmin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_superadmin`;

CREATE TABLE `gs_superadmin` (
                                 `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                 `userid` varchar(20) NOT NULL DEFAULT '',
                                 `userpwd` varchar(100) NOT NULL DEFAULT '',
                                 `dept` varchar(50) DEFAULT '',
                                 `monitor` varchar(4) DEFAULT '',
                                 `name` varchar(20) DEFAULT NULL,
                                 `team` varchar(16) DEFAULT 'NOTEAM',
                                 `lastlogin` datetime DEFAULT '0000-00-00 00:00:00',
                                 `lastlogout` datetime DEFAULT '0000-00-00 00:00:00',
                                 `ip_addr` varchar(15) DEFAULT NULL,
                                 `grade` char(2) DEFAULT NULL,
                                 `hp` varchar(30) DEFAULT NULL,
                                 `created_at` datetime DEFAULT '0000-00-00 00:00:00',
                                 `updated_at` datetime DEFAULT '0000-00-00 00:00:00',
                                 PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table gs_visiting
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_visiting`;

CREATE TABLE `gs_visiting` (
                               `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `username` varchar(100) DEFAULT '' COMMENT '¹æ¹®AU¸i',
                               `company_name` varchar(100) DEFAULT '' COMMENT 'E¸≫c¸i',
                               `hp_no` varchar(100) DEFAULT '' COMMENT '¿￢¶oA³',
                               `body` text DEFAULT NULL COMMENT '¹æ¹®³≫¿e',
                               `ent_date` datetime DEFAULT '0000-00-00 00:00:00' COMMENT 'AO½C½A°￡',
                               `out_date` datetime DEFAULT NULL COMMENT 'Að½C½A°￡',
                               `submit_date` datetime DEFAULT '0000-00-00 00:00:00' COMMENT 'A|Aa½A°￡',
                               `submit_user` varchar(255) DEFAULT NULL COMMENT 'A|AaAUid',
                               `status` varchar(1) DEFAULT NULL COMMENT '≫oAA',
                               `created_at` datetime DEFAULT '0000-00-00 00:00:00' COMMENT 'μi·IAIAU',
                               `updated_at` datetime DEFAULT NULL COMMENT '¼oA¤½A°￠',
                               PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=DYNAMIC;



# Dump of table leave_request
# ------------------------------------------------------------

DROP TABLE IF EXISTS `leave_request`;

CREATE TABLE `leave_request` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `type_id` int(10) unsigned NOT NULL,
                                 `type_name` varchar(16) NOT NULL,
                                 `type_type` varchar(16) DEFAULT NULL,
                                 `grade` char(2) DEFAULT NULL,
                                 `dept` varchar(8) DEFAULT NULL,
                                 `drafter_id` int(10) unsigned NOT NULL,
                                 `drafter_name` varchar(8) NOT NULL,
                                 `alternative_id` int(10) unsigned DEFAULT NULL,
                                 `alternative_name` varchar(8) DEFAULT NULL,
                                 `approver_id` int(10) unsigned NOT NULL,
                                 `approver_name` varchar(8) NOT NULL,
                                 `shift_code` varchar(8) DEFAULT NULL,
                                 `start_date` datetime NOT NULL,
                                 `end_date` datetime NOT NULL,
                                 `created_at` datetime NOT NULL,
                                 `updated_at` datetime NOT NULL,
                                 `status` tinyint(4) DEFAULT 0,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table leave_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `leave_type`;

CREATE TABLE `leave_type` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `type` varchar(16) DEFAULT NULL,
                              `name` varchar(16) DEFAULT NULL,
                              `fulltime` tinyint(3) unsigned DEFAULT 1,
                              `annual` tinyint(3) unsigned DEFAULT 1,
                              `active` tinyint(3) unsigned DEFAULT 1,
                              `created_at` datetime DEFAULT NULL,
                              `updated_at` datetime DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table part
# ------------------------------------------------------------

DROP TABLE IF EXISTS `part`;

CREATE TABLE `part` (
                        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                        `type` char(1) DEFAULT NULL COMMENT 'D : dept\nT : team',
                        `name` varchar(16) DEFAULT NULL,
                        `created_at` datetime DEFAULT NULL,
                        `updated_at` datetime DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table schedule_pattern
# ------------------------------------------------------------

DROP TABLE IF EXISTS `schedule_pattern`;

CREATE TABLE `schedule_pattern` (
                                    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                    `team` varchar(4) DEFAULT NULL,
                                    `name` varchar(45) DEFAULT NULL,
                                    `list` varchar(255) DEFAULT NULL,
                                    `size` tinyint(3) unsigned DEFAULT 8,
                                    `start_date` char(8) DEFAULT '20230101',
                                    `end_date` char(8) DEFAULT '20231231',
                                    `created_at` datetime DEFAULT NULL,
                                    `updated_at` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table schedule_shift
# ------------------------------------------------------------

DROP TABLE IF EXISTS `schedule_shift`;

CREATE TABLE `schedule_shift` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `name` varchar(32) NOT NULL,
                                  `shift_code` varchar(32) NOT NULL,
                                  `work_start` char(5) NOT NULL,
                                  `work_end` char(5) NOT NULL,
                                  `rest_start` char(5) DEFAULT '00:00',
                                  `rest_end` char(5) DEFAULT '00:00',
                                  `start_date` datetime DEFAULT NULL,
                                  `end_date` datetime DEFAULT NULL,
                                  `color` char(8) DEFAULT NULL,
                                  `created_at` datetime DEFAULT NULL,
                                  `updated_at` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;



# Dump of table schedule_team
# ------------------------------------------------------------

DROP TABLE IF EXISTS `schedule_team`;

CREATE TABLE `schedule_team` (
                                 `team` char(2) NOT NULL,
                                 `date` char(8) NOT NULL DEFAULT '20201010',
                                 `shift` varchar(4) DEFAULT NULL,
                                 `created_at` datetime DEFAULT NULL,
                                 `updated_at` datetime DEFAULT NULL,
                                 PRIMARY KEY (`team`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

# DB 통합 작업
# Dump of table gs_asset2
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_asset2`;
CREATE TABLE `gs_asset2` (
                             `asset_id` varchar(10) DEFAULT NULL,
                             `purpose` varchar(16) DEFAULT NULL,
                             `dept` varchar(16) DEFAULT NULL,
                             `type` varchar(16) DEFAULT NULL,
                             `project_name` varchar(64) DEFAULT NULL,
                             `project_type` varchar(16) DEFAULT NULL,
                             `vms_id` varchar(8) DEFAULT NULL,
                             `name` varchar(255) NOT NULL,
                             `emd` varchar(8) DEFAULT NULL,
                             `li` varchar(8) DEFAULT NULL,
                             `town` varchar(8) DEFAULT NULL,
                             `address` varchar(64) DEFAULT NULL,
                             `ref_id` varchar(16) NOT NULL,
                             `lat` varchar(16) DEFAULT NULL,
                             `lng` varchar(16) DEFAULT NULL,
                             `direction` varchar(8) DEFAULT NULL,
                             `jsonobj` text DEFAULT NULL,
                             PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

# ALTER TABLE `gs_asset` ADD COLUMN `direction` VARCHAR(8) DEFAULT NULL AFTER `lng`;
# ALTER TABLE `gs_dailyreport_detail` ADD COLUMN `work_monitoring` VARCHAR(255) DEFAULT NULL AFTER `work_dgubun`;
# ALTER TABLE `gs_cctv` ADD COLUMN `direction` VARCHAR(8) DEFAULT NULL AFTER `annoy`;
# ALTER TABLE `gs_cctv` ADD COLUMN `lat` VARCHAR(16) DEFAULT NULL AFTER `direction`;
# ALTER TABLE `gs_cctv` ADD COLUMN `lng` VARCHAR(16) DEFAULT NULL AFTER `lat`;
# ALTER TABLE `gs_selcode` ADD COLUMN `user_file` varchar(512) DEFAULT '' AFTER `remarks`,


