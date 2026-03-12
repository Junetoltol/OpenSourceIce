<!-- 회원 목록 화면: 관리자 전용, /api/members로 DB 전체 회원을 불러와 테이블로 표시 -->
<template>
  <div class="min-h-screen bg-gray-100 p-8">
    <div class="max-w-2xl mx-auto">
      <!-- 헤더 -->
      <div class="flex items-center justify-between mb-6">
        <div class="flex items-center gap-3">
          <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-10 h-10 object-contain" />
          <h1 class="text-xl font-bold text-gray-800">회원 목록</h1>
        </div>
        <button
          @click="handleLogout"
          class="text-sm text-gray-500 hover:text-gray-700 border border-gray-300 rounded-lg px-3 py-1 transition-colors"
        >
          로그아웃
        </button>
      </div>

      <!-- 로그인 성공 flash 메시지 (sessionStorage에서 읽음, 5초 후 사라짐) -->
      <div v-if="flashMsg" class="bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg px-4 py-2 mb-4">
        {{ flashMsg }}
      </div>

      <!-- 오류 메시지 -->
      <div v-if="errorMsg" class="bg-red-50 border border-red-200 text-red-600 text-sm rounded-lg px-4 py-2 mb-4">
        {{ errorMsg }}
      </div>

      <!-- 회원 테이블 -->
      <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
        <table class="w-full text-sm">
          <thead class="bg-gray-50 text-gray-600 font-semibold">
            <tr>
              <th class="px-6 py-3 text-left">아이디</th>
              <th class="px-6 py-3 text-left">이메일</th>
              <th class="px-6 py-3 text-left">가입일시</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="members.length === 0">
              <td colspan="3" class="px-6 py-6 text-center text-gray-400">등록된 회원이 없습니다.</td>
            </tr>
            <tr
              v-for="member in members"
              :key="member.id"
              class="border-t border-gray-100 hover:bg-gray-50"
            >
              <td class="px-6 py-3 text-gray-800">{{ member.id }}</td>
              <td class="px-6 py-3 text-gray-600">{{ member.email }}</td>
              <td class="px-6 py-3 text-gray-400">{{ member.signupTime }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const members = ref([])
const errorMsg = ref('')
const flashMsg = ref('')

onMounted(async () => {
  // 로그인 성공 flash 메시지 읽기
  const raw = sessionStorage.getItem('flashMsg')
  if (raw) {
    const { msg, time } = JSON.parse(raw)
    sessionStorage.removeItem('flashMsg')
    if (Date.now() - time < 300_000) {
      flashMsg.value = msg
      setTimeout(() => { flashMsg.value = '' }, 5000)
    }
  }
  try {
    const response = await fetch('/api/members')
    if (response.status === 401) {
      router.push('/login')
      return
    }
    const data = await response.json()
    if (data.success) {
      members.value = data.members
    }
  } catch {
    errorMsg.value = '서버에 연결할 수 없습니다.'
  }
})

async function handleLogout() {
  await fetch('/api/logout', { method: 'POST' })
  sessionStorage.removeItem('role')
  router.push('/')
}
</script>
