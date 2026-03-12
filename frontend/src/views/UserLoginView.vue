<!-- 사용자 로그인 화면: /api/login 호출 후 성공하면 마이페이지(/user/mypage)로 이동 -->
<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-lg w-full max-w-sm px-10 py-10">
      <!-- 로고 -->
      <div class="flex flex-col items-center mb-8">
        <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-20 h-20 object-contain mb-3" />
        <h1 class="text-xl font-bold text-gray-800">사용자 로그인</h1>
        <p class="text-sm text-gray-500 mt-1">사용자 전용 공간입니다</p>
      </div>

      <!-- 에러 메시지 -->
      <div v-if="errorMsg" class="bg-red-50 border border-red-200 text-red-600 text-sm rounded-lg px-4 py-2 mb-5">
        {{ errorMsg }}
      </div>

      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">아이디</label>
          <input
            ref="uIDRef"
            v-model="uID"
            type="text"
            placeholder="아이디를 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호</label>
          <input
            ref="uPWRef"
            v-model="uPW"
            type="password"
            placeholder="비밀번호를 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <button
          type="submit"
          :disabled="isLoading"
          class="w-full bg-green-700 hover:bg-green-800 text-white font-semibold py-2 rounded-lg transition-colors disabled:opacity-50"
        >
          {{ isLoading ? '로그인 중...' : '◀ 로그인 ▶' }}
        </button>
      </form>

      <!-- 회원가입 링크 -->
      <div class="text-center mt-5 space-y-2">
        <p class="text-sm text-gray-500">
          처음 방문이신가요?
          <RouterLink to="/signup" class="text-green-700 font-semibold hover:underline">회원가입</RouterLink>
        </p>
        <RouterLink to="/" class="block text-sm text-gray-400 hover:text-gray-600">← 메인으로 돌아가기</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const uID = ref('')
const uPW = ref('')
const errorMsg = ref('')
const isLoading = ref(false)
const uIDRef = ref(null)
const uPWRef = ref(null)

async function handleLogin() {
  errorMsg.value = ''

  if (uID.value === '') {
    errorMsg.value = '아이디를 입력해 주세요.'
    uIDRef.value.focus()
    return
  }
  if (uPW.value === '') {
    errorMsg.value = '비밀번호를 입력해 주세요.'
    uPWRef.value.focus()
    return
  }

  isLoading.value = true

  try {
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ uID: uID.value, uPW: uPW.value }),
    })

    const data = await response.json()

    if (response.ok && data.role === 'user') {
      // 사용자 정보와 role을 sessionStorage에 저장 (라우터 가드에서 사용)
      sessionStorage.setItem('role', 'user')
      sessionStorage.setItem('user', JSON.stringify({
        id: data.id,
        email: data.email,
        signupTime: data.signupTime,
      }))
      // 로그인 성공 flash 메시지 저장
      sessionStorage.setItem('flashMsg', JSON.stringify({
        msg: `환영합니다, ${data.id}님!`,
        time: Date.now()
      }))
      router.push('/user/mypage')
    } else if (data.role === 'admin') {
      errorMsg.value = '관리자 계정은 관리자 로그인을 이용해 주세요.'
    } else {
      errorMsg.value = data.message || '아이디와 비밀번호를 확인하세요.'
    }
  } catch {
    errorMsg.value = '서버에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    isLoading.value = false
  }
}
</script>
