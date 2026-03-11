<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-lg w-full max-w-sm px-10 py-10">
      <!-- 로고 -->
      <div class="flex flex-col items-center mb-8">
        <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-20 h-20 object-contain mb-3" />
        <h1 class="text-xl font-bold text-gray-800">관리자 로그인</h1>
        <p class="text-sm text-gray-500 mt-1">관리자 전용 공간입니다</p>
      </div>

      <!-- 에러 메시지 -->
      <div v-if="errorMsg" class="bg-red-50 border border-red-200 text-red-600 text-sm rounded-lg px-4 py-2 mb-5">
        {{ errorMsg }}
      </div>

      <!-- 교재 login.jsp 폼 구조 -->
      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">아이디</label>
          <input
            ref="uIDRef"
            v-model="uID"
            type="text"
            placeholder="아이디를 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호</label>
          <input
            ref="uPWRef"
            v-model="uPW"
            type="password"
            placeholder="비밀번호를 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <button
          type="submit"
          :disabled="isLoading"
          class="w-full bg-blue-800 hover:bg-blue-900 text-white font-semibold py-2 rounded-lg transition-colors disabled:opacity-50"
        >
          {{ isLoading ? '로그인 중...' : '◀ 로그인 ▶' }}
        </button>
      </form>

      <!-- 메인으로 돌아가기 -->
      <div class="text-center mt-5">
        <RouterLink to="/" class="text-sm text-gray-400 hover:text-gray-600">← 메인으로 돌아가기</RouterLink>
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

// 교재 checkFun() 검증 로직과 동일
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

    if (response.ok) {
      router.push('/members')
    } else {
      errorMsg.value = '아이디와 비밀번호를 확인하세요.'
    }
  } catch {
    errorMsg.value = '서버에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    isLoading.value = false
  }
}
</script>
