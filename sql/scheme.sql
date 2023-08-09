ALTER TABLE `icms2`.`gs_superadmin` ADD COLUMN `team` VARCHAR(16) NULL AFTER `jsonobj`; --팀 추가

/* VMS 카메라 리스트 */
ALTER TABLE `icms2`.`gs_cctv` ADD COLUMN `origin` CHAR(2) NULL DEFAULT 'OR' AFTER `jsonobj`; --출처

/* 게시판 업데이트 */
ALTER TABLE gs_board_notice
CHANGE COLUMN `reg_date` `created_at` DATETIME NULL AFTER `smsflag`,
ADD COLUMN `updated_at` DATETIME NULL AFTER `created_at`;


/* 근무 일지 gs_dailyreport */
ALTER TABLE `gs_dailyreport`
DROP COLUMN `site`,
DROP COLUMN `notice_yn`,
DROP COLUMN `tel`,
DROP COLUMN `is_secret`,
DROP COLUMN `delflag`,
DROP COLUMN `passwd`,
DROP COLUMN `pos`,
DROP COLUMN `depth`,
DROP COLUMN `thread`,
DROP COLUMN `count`,
DROP COLUMN `body`,
DROP COLUMN `title`,
DROP COLUMN `home`,
DROP COLUMN `email`,
DROP COLUMN `category`,
DROP COLUMN `recommend`,
CHANGE COLUMN `submit_userid` `submit_member_id` VARCHAR(255) NULL DEFAULT NULL COMMENT '제출자id' AFTER `team`,
CHANGE COLUMN `reg_date` `created_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' AFTER `confirm_date`,
CHANGE COLUMN `stat_flag` `status` TINYINT NOT NULL DEFAULT '0' COMMENT '상태값(0:작성중, 1:제출, 2:승인)' ,
CHANGE COLUMN `work_jo` `team` VARCHAR(255) NULL DEFAULT NULL COMMENT '조' ,
CHANGE COLUMN `confirm_userid` `confirm_member_id` VARCHAR(255) NULL DEFAULT NULL COMMENT '결제자id' ,
ADD COLUMN `updated_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' AFTER `created_at`,
ADD COLUMN `summary` VARCHAR(255) NULL AFTER `user_ip`;

ALTER TABLE `gs_dailyreport_detail`
DROP COLUMN `action_code`,
DROP COLUMN `jochi_noaction_userid`,
DROP COLUMN `jochi_result_userid`,
DROP COLUMN `work_jochi_noaction_reg_date`,
DROP COLUMN `work_jochi_result_reg_date`,
DROP COLUMN `work_jochi_noaction`,
CHANGE COLUMN `work_cctv` `cctv_index` VARCHAR(255) NULL DEFAULT '' COMMENT 'cctv' ,
CHANGE COLUMN `work_date` `work_date` VARCHAR(10) NULL DEFAULT NULL AFTER `userid`;
CHANGE COLUMN `reg_date` `created_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' COMMENT '등록일자' AFTER `action_userid`,
CHANGE COLUMN `work_jochi` `action_code` VARCHAR(255) NULL DEFAULT 'R02' COMMENT '조치(R01:상활전파, R02:미조치, R03:조치완료, R04:미조치완료)' ,
CHANGE COLUMN `work_jochi_result` `action_result` VARCHAR(4000) NULL DEFAULT '' COMMENT '조치결과',
ADD COLUMN `updated_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' COMMENT '수정일자' AFTER `created_at` ,
ADD COLUMN `cctv_id` INT UNSIGNED NULL AFTER `work_dgubun`;

/* gs_superadmin */
ALTER TABLE gs_superadmin
DROP COLUMN `menunum`,
DROP COLUMN `namecha`,
DROP COLUMN `namejap`,
DROP COLUMN `nameeng`,
DROP COLUMN `jsonobj`,
CHANGE COLUMN `idx` `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT FIRST,
CHANGE COLUMN `regdate` `created_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' ,
ADD COLUMN `updated_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' AFTER `created_at`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`);

/*gs_kindcode*/
ALTER TABLE `gs_kindcode`
DROP COLUMN `use_yn`,
CHANGE COLUMN `kind_idx` `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `kind_code` `code` VARCHAR(50) NOT NULL ,
CHANGE COLUMN `kind_name` `name` VARCHAR(50) NOT NULL ,
CHANGE COLUMN `display_code` `seq` VARCHAR(50) NOT NULL ,
CHANGE COLUMN `regdate` `created_at` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' ,
CHANGE COLUMN `upddate` `updated_at` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' ,
ADD COLUMN `active` TINYINT NULL DEFAULT 1 AFTER `seq`,
ADD COLUMN `essential` TINYINT NULL AFTER `name`;

/* gs_selcode */
ALTER TABLE `gs_selcode`
DROP COLUMN `use_yn`,
CHANGE COLUMN `code_idx` `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `kind_code` `kind_code` VARCHAR(32) NOT NULL ,
CHANGE COLUMN `s_code` `code` VARCHAR(32) NOT NULL ,
CHANGE COLUMN `code_name` `name` VARCHAR(32) NOT NULL ,
CHANGE COLUMN `seq_no` `seq` INT(11) NOT NULL DEFAULT 0 ,
CHANGE COLUMN `remarks` `remarks` VARCHAR(128) NULL DEFAULT NULL ,
CHANGE COLUMN `regdate` `created_at` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
CHANGE COLUMN `upddate` `updated_at` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
ADD COLUMN `active` TINYINT NULL AFTER `code_name`;

UPDATE `gs_selcode` SET `kind_code`=''team'' WHERE `kind_code`=''team''

/* gs_schedule */
ALTER TABLE gs_schedule
DROP COLUMN `userenm`,
DROP COLUMN `usereid`,
DROP COLUMN `dayofweek`,
DROP COLUMN `webgbn`,
DROP COLUMN `pcode`,
DROP COLUMN `mcode`,
DROP COLUMN `bcode`,
CHANGE COLUMN `signdate` `created_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' ,
CHANGE COLUMN `editdate` `updated_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' ;

/* schedule_shift(근무 시간) 테이블 생성 */
CREATE TABLE schedule_shift (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`name` VARCHAR(32) NOT NULL,
`shift_code` VARCHAR(32) NOT NULL,
`work_start` CHAR(5) NOT NULL,
`work_end` CHAR(5) NOT NULL,
`rest_start` CHAR(5) NULL,
`rest_end` CHAR(5) NULL,
`created_at` DATETIME NULL,
`updated_at` DATETIME NULL,
`start_date` DATETIME NULL,
`end_date` DATETIME NULL,
PRIMARY KEY (`id`));

/* schedule_pattern(패턴) 테이블 생성 */
CREATE TABLE schedule_pattern (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`name` VARCHAR(45) NULL,
`list` VARCHAR(255) NULL,
`created_at` DATETIME NULL,
`updated_at` DATETIME NULL,
PRIMARY KEY (`id`));

/* 휴가 정책 */
CREATE TABLE `icms2`.`leave_code` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`type` VARCHAR(16) NULL,
`name` VARCHAR(16) NULL,
`annual` TINYINT UNSIGNED NULL,
`active` TINYINT UNSIGNED NULL,
`created_at` DATETIME NULL,
`updated_at` DATETIME NULL,
PRIMARY KEY (`id`));

/* 휴가 */
CREATE TABLE `icms2`.`leave` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`type_id` VARCHAR(16) NOT NULL,
`type_name` VARCHAR(8) NOT NULL,
`drafter_id` VARCHAR(16) NOT NULL,
`drafter_name` VARCHAR(8) NOT NULL,
`alternative_id` VARCHAR(16) NULL,
`alternative_name` VARCHAR(8) NULL,
`approver_id` VARCHAR(16) NOT NULL,
`approver_name` VARCHAR(8) NOT NULL,
`start_date` DATETIME NOT NULL,
`end_date` DATETIME NOT NULL,
`created_at` DATETIME NOT NULL,
`updated_at` DATETIME NOT NULL,
PRIMARY KEY (`id`));

/* 팀(부서) */
CREATE TABLE `icms2`.`part` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`type` CHAR(1) NULL COMMENT 'D : dept\nT : team',
`name` VARCHAR(16) NULL,
`created_at` DATETIME NULL,
`updated_at` DATETIME NULL,
PRIMARY KEY (`id`));

/* 방문자관리 */
ALTER TABLE `gs_visiting`
DROP COLUMN `jsonobj`,
DROP COLUMN `depth`,
DROP COLUMN `thread`,
DROP COLUMN `pos`,
CHANGE COLUMN `reg_date` `created_at` DATETIME NULL DEFAULT '0000-00-00 00:00:00' AFTER `status`,
CHANGE COLUMN `upd_date` `updated_at` DATETIME NULL DEFAULT NULL AFTER `created_at`;

