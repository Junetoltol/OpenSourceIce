<!-- 사용자 영역 공통 레이아웃: Sidebar(좌) + Header(상) + Main Content(중앙 <RouterView>) -->
<template>
  <div class="min-h-screen bg-gray-100 flex flex-col">

    <!-- Header: 현재 메뉴명 + 유저 ID + 로그아웃 -->
    <header class="bg-white border-b border-gray-200 px-6 py-3 flex items-center justify-between">
      <div class="flex items-center gap-3">
        <img src="@/assets/marker.png" alt="로고" class="w-8 h-8 object-contain" />
        <span class="font-bold text-gray-800 text-base">{{ currentMenuTitle }}</span>
      </div>
      <div class="flex items-center gap-3">
        <span class="text-sm text-gray-500">{{ userId }}</span>
        <button
          @click="handleLogout"
          class="text-sm text-gray-500 hover:text-gray-700 border border-gray-300 rounded-lg px-3 py-1 transition-colors"
        >
          로그아웃
        </button>
      </div>
    </header>

    <div class="flex flex-1">

      <!-- Sidebar: 메뉴 목록 -->
      <aside class="w-48 bg-white border-r border-gray-200 p-4">
        <nav class="space-y-1">
          <RouterLink
            v-for="menu in menus"
            :key="menu.path"
            :to="menu.path"
            class="flex items-center gap-2 px-3 py-2 rounded-lg text-sm text-gray-600 hover:bg-gray-100 transition-colors"
            active-class="bg-green-50 text-green-700 font-semibold"
          >
            <span>{{ menu.icon }}</span>
            <span>{{ menu.label }}</span>
          </RouterLink>
        </nav>
      </aside>

      <!-- Main Content: 팀원들이 각자 기능을 채울 영역 -->
      <main class="flex-1 p-8">
        <RouterView />
      </main>

    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// sessionStorage에서 로그인된 사용자 ID 읽기
const userRaw = sessionStorage.getItem('user')
const userId = userRaw ? JSON.parse(userRaw).id : '사용자'

// 사이드바 메뉴 목록 — 팀원이 새 메뉴 추가 시 이 배열에 항목 추가
const menus = [
  { path: '/user/mypage',  icon: '👤', label: '마이페이지' },
  { path: '/user/board',   icon: '📋', label: '자유게시판' },
  { path: '/user/courses', icon: '📚', label: '강의 추가' },
  { path: '/user/grades',  icon: '📊', label: '성적/학점 확인' },
]

// 현재 경로에 맞는 메뉴명을 헤더에 표시
const currentMenuTitle = computed(() => {
  const current = menus.find(m => route.path.startsWith(m.path))
  return current ? current.label : '사용자 페이지'
})

async function handleLogout() {
  await fetch('/api/logout', { method: 'POST' })
  sessionStorage.removeItem('user')
  sessionStorage.removeItem('role')
  router.push('/')
}
</script>
