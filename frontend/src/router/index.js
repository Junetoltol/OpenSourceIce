import { createRouter, createWebHistory } from 'vue-router'

// ──────────────────────────────────────────
//  페이지 목록
//  path   : 브라우저 주소창에 표시되는 경로
//  name   : 코드에서 이 경로를 부를 때 쓰는 이름
// ──────────────────────────────────────────
const routes = [

  // 메인 화면 (관리자 / 사용자 선택)
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue'),
  },

  // 관리자 로그인 (아이디: space / 비밀번호: 123456)
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue'),
  },

  // 사용자 회원 가입
  {
    path: '/signup',
    name: 'signup',
    component: () => import('@/views/SignupView.vue'),
  },

  // 회원 가입 완료
  {
    path: '/signup-success',
    name: 'signup-success',
    component: () => import('@/views/SignupSuccessView.vue'),
  },

  // 회원 목록 (관리자 로그인 후)
  {
    path: '/members',
    name: 'members',
    component: () => import('@/views/MembersView.vue'),
  },

  // 사용자 로그인
  {
    path: '/user-login',
    name: 'user-login',
    component: () => import('@/views/UserLoginView.vue'),
  },

  // 사용자 영역 (Sidebar + Header 레이아웃, 로그인 후 접근)
  // 팀원이 새 페이지를 추가할 때: children 배열에 항목 추가
  {
    path: '/user',
    component: () => import('@/views/UserLayout.vue'),
    children: [
      // 마이페이지 (내 정보 조회 및 수정)
      {
        path: 'mypage',
        name: 'mypage',
        component: () => import('@/views/MyPageView.vue'),
      },
      // 자유게시판 — 팀원이 BoardView.vue에 기능 구현
      {
        path: 'board',
        name: 'board',
        component: () => import('@/views/BoardView.vue'),
      },
      // 강의 추가 — 팀원이 CoursesView.vue에 기능 구현
      {
        path: 'courses',
        name: 'courses',
        component: () => import('@/views/CoursesView.vue'),
      },
      // 성적/학점 확인 — 팀원이 GradesView.vue에 기능 구현
      {
        path: 'grades',
        name: 'grades',
        component: () => import('@/views/GradesView.vue'),
      },
    ],
  },

]

// ──────────────────────────────────────────
//  라우터 생성 (history 모드: URL에 # 없이 깔끔하게 표시)
// ──────────────────────────────────────────
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// ──────────────────────────────────────────
//  네비게이션 가드: 권한 없는 접근 차단
//  - /members      → role=admin 필요, 없으면 /login으로
//  - /user/*       → 로그인(user) 필요, 없으면 /user-login으로
// ──────────────────────────────────────────
router.beforeEach((to) => {
  const role = sessionStorage.getItem('role')
  const user = sessionStorage.getItem('user')

  // 관리자 전용 경로
  if (to.name === 'members' && role !== 'admin') {
    return { name: 'login' }
  }

  // 사용자 전용 경로
  if (to.path.startsWith('/user/') && !user) {
    return { name: 'user-login' }
  }
})

export default router
