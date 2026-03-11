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

]

// ──────────────────────────────────────────
//  라우터 생성 (history 모드: URL에 # 없이 깔끔하게 표시)
// ──────────────────────────────────────────
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
