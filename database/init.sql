-- =============================================
--  OpenSourceIce 프로젝트 DB 초기화 스크립트
--  실행: mysql -u root -p < database/init.sql
-- =============================================

-- DB 생성
CREATE DATABASE IF NOT EXISTS odbo
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE odbo;

-- 회원 테이블 생성 (role: 'admin' 또는 'user', 기본값 'user')
CREATE TABLE IF NOT EXISTS members (
  id          VARCHAR(16)           NOT NULL,
  passwd      VARCHAR(100)          NOT NULL,
  email       VARCHAR(100)          NOT NULL,
  role        ENUM('admin','user')  NOT NULL DEFAULT 'user',
  signupTime  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- 관리자 계정 등록 (이미 있으면 무시)
INSERT IGNORE INTO members (id, passwd, email, role)
  VALUES ('space', '123456', 'admin@odbo.com', 'admin');

-- =============================================
--  기존 DB에 role 컬럼이 없는 경우 아래 명령어를 MySQL에서 직접 실행:
--  ALTER TABLE odbo.members ADD COLUMN role ENUM('admin','user') NOT NULL DEFAULT 'user';
--  UPDATE odbo.members SET role='admin' WHERE id='space';
--  INSERT IGNORE INTO odbo.members (id,passwd,email,role) VALUES ('space','123456','admin@odbo.com','admin');
-- =============================================

SELECT 'DB 초기화 완료!' AS 결과;
