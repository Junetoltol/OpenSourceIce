<!-- 마이페이지: 내 정보(아이디·이메일·가입일) 조회 및 이메일·비밀번호 수정 -->
<template>
  <div class="max-w-lg">

    <!-- flash 메시지 (로그인 성공 환영 메시지 등) -->
    <div v-if="flashMsg" class="bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg px-4 py-2 mb-6">
      {{ flashMsg }}
    </div>

    <!-- 내 정보 카드 -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
      <h2 class="text-base font-bold text-gray-700 mb-4">내 정보</h2>
      <div class="space-y-3 text-sm">
        <div class="flex">
          <span class="w-24 text-gray-400">아이디</span>
          <span class="text-gray-800 font-medium">{{ user.id }}</span>
        </div>
        <div class="flex">
          <span class="w-24 text-gray-400">이메일</span>
          <span class="text-gray-800">{{ user.email }}</span>
        </div>
        <div class="flex">
          <span class="w-24 text-gray-400">가입일</span>
          <span class="text-gray-500">{{ user.signupTime }}</span>
        </div>
      </div>
    </div>

    <!-- 정보 수정 폼 -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
      <h2 class="text-base font-bold text-gray-700 mb-4">정보 수정</h2>

      <!-- 성공 메시지 -->
      <div v-if="successMsg" class="bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg px-4 py-2 mb-4">
        {{ successMsg }}
      </div>
      <!-- 오류 메시지 -->
      <div v-if="errorMsg" class="bg-red-50 border border-red-200 text-red-600 text-sm rounded-lg px-4 py-2 mb-4">
        {{ errorMsg }}
      </div>

      <form @submit.prevent="handleUpdate" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">새 이메일 <span class="text-gray-400 font-normal">(선택)</span></label>
          <input
            v-model="newEmail"
            type="email"
            placeholder="변경할 이메일을 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">새 비밀번호 <span class="text-gray-400 font-normal">(선택)</span></label>
          <input
            v-model="newPassword"
            type="password"
            placeholder="8~16자, 영문·숫자·특수문자 포함"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">현재 비밀번호 <span class="text-red-400">*</span></label>
          <input
            v-model="currentPassword"
            type="password"
            placeholder="본인 확인을 위해 현재 비밀번호 입력"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <p class="text-xs text-gray-400 mt-1">수정하려면 현재 비밀번호를 반드시 입력해야 합니다.</p>
        </div>

        <button
          type="submit"
          :disabled="isLoading"
          class="w-full bg-green-700 hover:bg-green-800 text-white font-semibold py-2 rounded-lg transition-colors disabled:opacity-50"
        >
          {{ isLoading ? '저장 중...' : '변경사항 저장' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// sessionStorage에서 사용자 정보 읽기
const userRaw = sessionStorage.getItem('user')
const user = ref(userRaw ? JSON.parse(userRaw) : { id: '', email: '', signupTime: '' })

const newEmail = ref('')
const newPassword = ref('')
const currentPassword = ref('')
const errorMsg = ref('')
const successMsg = ref('')
const isLoading = ref(false)
const flashMsg = ref('')

onMounted(() => {
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
})

async function handleUpdate() {
  errorMsg.value = ''
  successMsg.value = ''

  if (!currentPassword.value) {
    errorMsg.value = '현재 비밀번호를 입력해 주세요.'
    return
  }
  if (!newEmail.value && !newPassword.value) {
    errorMsg.value = '변경할 이메일 또는 비밀번호를 입력해 주세요.'
    return
  }

  isLoading.value = true

  try {
    const params = new URLSearchParams({ currentPassword: currentPassword.value })
    if (newEmail.value) params.append('newEmail', newEmail.value)
    if (newPassword.value) params.append('newPassword', newPassword.value)

    const response = await fetch('/api/profile', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params,
    })

    const data = await response.json()

    if (response.ok) {
      successMsg.value = '정보가 성공적으로 변경되었습니다.'
      // sessionStorage의 이메일 업데이트
      if (newEmail.value) {
        user.value.email = newEmail.value
        sessionStorage.setItem('user', JSON.stringify(user.value))
      }
      newEmail.value = ''
      newPassword.value = ''
      currentPassword.value = ''
    } else {
      errorMsg.value = data.message || '수정 중 오류가 발생했습니다.'
    }
  } catch {
    errorMsg.value = '서버에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    isLoading.value = false
  }
}
</script>
