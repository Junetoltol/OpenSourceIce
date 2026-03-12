# 🛠️ 개발 환경 설정 트러블슈팅

> 실제로 개발하면서 막혔던 문제들을 정리했어요.
> 비슷한 증상이 나타나면 여기서 먼저 찾아보세요!

---

## 💻 처음 세팅하는 법 (VS Code 기준)

처음 이 프로젝트를 받았다면 아래 순서대로 설치하세요.

### 설치 순서 체크리스트

- [ ] **1. Java (JDK) 설치**
  - [Oracle JDK 다운로드](https://www.oracle.com/java/technologies/downloads/) → Windows x64 Installer
  - 설치 완료 후 PowerShell에서 확인:
    ```
    java -version
    ```

- [ ] **2. JAVA_HOME 환경변수 설정** ← 안 하면 Maven 빌드 안 됨
  - Windows 검색 → **"시스템 환경 변수 편집"**
  - 하단 **환경 변수** → 시스템 변수 → **새로 만들기**
  - 변수 이름: `JAVA_HOME`
  - 변수 값: `C:\Program Files\Java\jdk-25` (본인 버전에 맞게)
  - 확인 → VS Code 재시작

- [ ] **3. Tomcat 11 설치**
  - [Apache Tomcat 11 다운로드](https://tomcat.apache.org/download-11.cgi) → Windows Service Installer
  - 설치 중 포트 번호는 기본값 **8080** 그대로 두기

- [ ] **4. MySQL 8.0 설치**
  - [MySQL Installer 다운로드](https://dev.mysql.com/downloads/installer/)
  - Accounts and Roles 화면에서 Root 비밀번호: `123456`
  - 설치 완료 후 DB 초기화:
    ```sql
    -- MySQL 8.0 Command Line Client 열고
    source C:/Users/사용자이름/Documents/GitHub/OpenSourceIce/database/init.sql
    ```

- [ ] **5. Maven 설치** ← 없으면 백엔드 빌드 안 됨
  - PowerShell에서 실행:
    ```powershell
    Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile "$env:TEMP\maven.zip"
    Expand-Archive -Path "$env:TEMP\maven.zip" -DestinationPath "$env:LOCALAPPDATA\Programs\" -Force
    ```
  - `.vscode/settings.json`에 경로 추가:
    ```json
    "maven.executable.path": "C:\\Users\\사용자이름\\AppData\\Local\\Programs\\apache-maven-3.9.6\\bin\\mvn.cmd"
    ```
  > ⚠️ `사용자이름`은 본인 Windows 사용자 이름으로 바꾸세요. 이 줄은 **커밋하지 말고** 로컬에서만 설정하세요.

- [ ] **6. VS Code 확장 프로그램 설치**
  - `Extension Pack for Java` (Oracle)
  - `Community Server Connectors` (Red Hat)
  - `Vue - Official` (Volar)
  - `Prettier - Code formatter`

- [ ] **7. 프론트엔드 설정**
  - VS Code 터미널에서:
    ```bash
    cd frontend
    npm install
    npm run dev
    ```
  - 브라우저에서 `http://localhost:5173` 접속 확인

- [ ] **8. 백엔드 빌드 + 배포**
  1. VS Code 왼쪽 패널 **MAVEN** → Lifecycle → **package** 더블클릭
     → `backend/server/target/ROOT.war` 생성됨
  2. VS Code **SERVERS** 패널 → Tomcat **Stop**
  3. 생성된 `ROOT.war`를 Tomcat webapps 폴더에 복사:
     ```
     backend/server/target/ROOT.war
     → C:/Program Files/Apache Software Foundation/Tomcat 11.0/webapps/ROOT.war
     ```
  4. 기존 `webapps/ROOT/` 폴더 삭제 (있으면)
  5. Tomcat **Start** → ROOT.war 자동으로 풀림
  - `http://localhost:8081` 접속 확인 (포트는 본인 Tomcat 설정에 따라 다름)

> 막히는 단계가 있으면 아래 **트러블슈팅** 섹션을 확인하세요!

---

## 목차

1. [MySQL 설치 — Accounts and Roles 화면](#1-mysql-설치--accounts-and-roles-화면)
2. [MySQL Connector/J가 목록에 없어요](#2-mysql-connectorj가-목록에-없어요)
3. [DB 초기화 — init.sql은 어디서 실행해요?](#3-db-초기화--initsql은-어디서-실행해요)
4. [Maven 빌드 오류 — JAVA_HOME not found](#4-maven-빌드-오류--java_home-not-found)
5. [Maven wrapper 오류 — MavenWrapperMain 클래스 없음](#5-maven-wrapper-오류--mavenwrappermain-클래스-없음)
6. [로그인이 안 돼요 — 404 오류 (Tomcat 포트 문제)](#6-로그인이-안-돼요--404-오류-tomcat-포트-문제)
7. [ROOT.war 배포했는데 API가 404예요](#7-rootwar-배포했는데-api가-404예요)

---

## 1. MySQL 설치 — Accounts and Roles 화면

**증상**
MySQL 설치 중 아래 화면이 나타나고 뭘 입력해야 할지 모르겠어요.

```
Accounts and Roles
MySQL Root Password: [         ]
Repeat Password:    [         ]
```

**원인**
MySQL 관리자(root) 비밀번호를 설정하는 화면이에요.

**해결**
- `MySQL Root Password` 칸에 `123456` 입력
- `Repeat Password` 칸에 `123456` 다시 입력
- **Next >** 클릭

> ⚠️ 팀 공통 비밀번호는 `123456`이에요. 다른 걸로 설정하면 DB 연결이 안 될 수 있어요.

---

## 2. MySQL Connector/J가 목록에 없어요

**증상**
MySQL Installer의 Select Products 화면에서 아무리 스크롤해도 Connector/J가 안 보여요.

**원인**
일부 MySQL Installer 버전에서는 Connectors 항목이 기본 목록에 없어요.

**해결**
지금은 **MySQL Server**만 설치하고 넘어가도 괜찮아요.
Connector/J(Java 연결 드라이버)는 나중에 백엔드 개발할 때 Maven `pom.xml`로 자동 설치돼요.

---

## 3. DB 초기화 — init.sql은 어디서 실행해요?

**증상**
`database/init.sql` 파일이 있는데 어떻게 실행해야 할지 모르겠어요.

**해결**

1. Windows 검색에서 **MySQL 8.0 Command Line Client** 열기
2. 비밀번호 `123456` 입력 후 Enter
3. 아래 명령어 실행:

```sql
source C:/Users/사용자이름/Documents/GitHub/OpenSourceIce/database/init.sql
```

> 본인 Windows 사용자 이름으로 경로를 바꿔서 입력하세요.

**성공하면 이렇게 나와요**

```
Query OK, 1 row affected (0.00 sec)
Database changed
Query OK, 0 rows affected (0.01 sec)
```

**확인 방법**

```sql
USE odbo;
SHOW TABLES;
```

`members` 테이블이 보이면 완료!

---

## 4. Maven 빌드 오류 — JAVA_HOME not found

**증상**
Maven 빌드 시 아래 오류가 나타나요.

```
Error: JAVA_HOME not found in your environment.
Please set the JAVA_HOME variable in your environment to match the
location of your Java installation.
```

**원인**
Maven이 Java 설치 경로를 못 찾아요. `JAVA_HOME` 환경변수가 설정되지 않았어요.

**해결**

1. Windows 검색 → **"시스템 환경 변수 편집"** 열기
2. 하단 **환경 변수** 버튼 클릭
3. **시스템 변수** 섹션에서 **새로 만들기** 클릭
4. 아래 값 입력:
   - 변수 이름: `JAVA_HOME`
   - 변수 값: `C:\Program Files\Java\jdk-25`
5. 확인 → 확인
6. **VS Code 완전히 종료 후 재시작**

> Java 버전이 다르면 경로도 달라요. `C:\Program Files\Java\` 폴더에서 본인 JDK 폴더명을 확인하세요.

---

## 5. Maven wrapper 오류 — MavenWrapperMain 클래스 없음

**증상**
VS Code에서 Maven 빌드 시 아래 오류가 나타나요.

```
Error: Could not find or load main class org.apache.maven.wrapper.MavenWrapperMain
Caused by: java.lang.ClassNotFoundException: org.apache.maven.wrapper.MavenWrapperMain
```

또는

```
Maven executable not found in PATH. Please specify "maven.executable.path".
```

**원인**
Maven이 설치되지 않았거나, VS Code가 Maven 실행 파일 경로를 모르고 있어요.

**해결**

**Step 1**: PowerShell에서 Maven 다운로드 및 설치

```powershell
Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile "$env:TEMP\maven.zip"
Expand-Archive -Path "$env:TEMP\maven.zip" -DestinationPath "$env:LOCALAPPDATA\Programs\" -Force
```

**Step 2**: `.vscode/settings.json`에 경로 추가

```json
"maven.executable.path": "C:\\Users\\사용자이름\\AppData\\Local\\Programs\\apache-maven-3.9.6\\bin\\mvn.cmd"
```

**Step 3**: VS Code 재시작 (`Ctrl+Shift+P` → `Developer: Reload Window`)

---

## 6. 로그인이 안 돼요 — 404 오류 (Tomcat 포트 문제)

**증상**
로그인 버튼을 눌렀는데 "아이디와 비밀번호를 확인하세요" 오류가 나오고,
브라우저 개발자 도구(F12)에서 `api/login 404 Not Found` 오류가 보여요.

**원인**
Vite 프록시가 Tomcat 포트 `8080`으로 연결을 시도하는데,
실제 Tomcat이 `8081`(또는 다른 포트)에서 실행 중일 수 있어요.

**Tomcat 실제 포트 확인 방법**

VS Code 하단 출력 패널에서 `Community Server Connector` 선택 후 로그 확인:

```
프로토콜 핸들러 ["http-nio-8081"]을(를) 초기화합니다.
```

여기서 포트 번호를 확인하세요.

**해결**

`frontend/vite.config.js` 파일에서 포트를 실제 Tomcat 포트로 수정:

```js
server: {
  proxy: {
    '/api': 'http://localhost:8081',  // ← Tomcat 실제 포트로 변경
  },
},
```

수정 후 Vue dev server 재시작 (`Ctrl+C` → `npm run dev`)

---

## 7. ROOT.war 배포했는데 API가 404예요

**증상**
`ROOT.war`를 `webapps/` 폴더에 복사했는데 API 호출 시 404 오류가 계속 나와요.

**원인**
Tomcat의 `webapps/ROOT/` 폴더가 이미 존재하면, `ROOT.war`를 복사해도 기존 폴더를 그대로 사용해서 WAR가 배포되지 않아요.

**해결**

1. VS Code **SERVERS** 패널에서 Tomcat **Stop**
2. PowerShell 또는 파일 탐색기에서 아래 폴더 삭제:
   ```
   C:\Program Files\Apache Software Foundation\Tomcat 11.0\webapps\ROOT\
   ```
3. Tomcat **Start** → Tomcat이 자동으로 `ROOT.war`를 새 `ROOT/` 폴더로 압축 해제

**확인 방법**
```powershell
curl http://localhost:8081/api/login
```
404가 아닌 JSON 응답이 오면 성공!

---

## 아직도 안 되면?

팀 단톡방에 오류 메시지 스크린샷과 함께 올려주세요.
또는 이 저장소에 [Issue](../../issues)를 등록해 주세요.
