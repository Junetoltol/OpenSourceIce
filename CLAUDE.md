# 프로젝트 컨텍스트

## 개요
- 오픈소스 수업 5조 팀 저장소
- 팀 프로젝트 코드, 과제 결과물, 활동 기록 관리

## 기술 스택 (예정)
- **백엔드**: Apache Tomcat 11 + MySQL (Java/JSP 기반)
- **프론트엔드**: Vue.js 3 + Vite + TailwindCSS (확정)
- **연동 방식**: REST API (백엔드는 JSON만 제공, 화면은 Vue가 담당)
- `npm run build` → dist/ 폴더를 Tomcat webapps/ROOT에 복사하는 방식으로 배포

## 개발 환경
- **에디터**: VS Code (팀 기준)
- **수업 강의자료 기준 도구**: Eclipse
- **Java**: JDK 17 또는 21 (추천: Java SE 25)
- **서버**: Tomcat 11
- **DB**: MySQL (root / 123456, DB명: odbo, 테이블: members)
- **참고 교재**: `pdf/JSP웹프로그래밍-362-408.pdf` (CHAPTER 13: JSP 프로젝트)

## 수업 과제 내용 (CHAPTER 13, p.362~408)
회원 관리 웹 애플리케이션 실습. 만들어야 할 JSP 파일:
- `dbConn.jsp` (DB 연결 공통), `main.jsp` (메인), `signup.jsp` (가입 폼)
- `insertDB.jsp` (DB 등록), `signupSuccess.jsp` (가입 성공)
- `membership.jsp` (회원 전용), `withdraw.jsp` + `drawCheck.jsp` (탈퇴)
- `drawSuccess.jsp`, `drawErr.jsp` (탈퇴 결과)
- `login.jsp`, `loginSuccess.jsp`, `loginErr.jsp` (관리자 로그인)
- `membersList.jsp` (전체 회원 조회), `logout.jsp` (로그아웃)
- 관리자 계정: space / 123456

## VS Code 설정
- `.vscode/settings.json`: 에디터 및 Java 설정
- `.vscode/extensions.json`: 팀원 권장 확장 프로그램 목록

## 주의사항
- 비전공자 팀원도 있으므로 설명은 쉽고 친절하게 작성
- README.md는 GitHub에서 보이는 안내 문서 (한국어)
- Merge는 팀원 합의 후 진행
