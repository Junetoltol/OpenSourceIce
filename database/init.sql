-- =============================================
--  OpenSourceIce 프로젝트 DB 초기화 스크립트
--  실행: mysql -u root -p < database/init.sql
-- =============================================

-- DB 생성
CREATE DATABASE IF NOT EXISTS odbo
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE odbo;

-- 회원 테이블 생성
CREATE TABLE IF NOT EXISTS members (
  id          VARCHAR(16)  NOT NULL,
  passwd      VARCHAR(100) NOT NULL,
  email       VARCHAR(100) NOT NULL,
  signupTime  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

SELECT 'DB 초기화 완료!' AS 결과;
