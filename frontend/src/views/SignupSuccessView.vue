<!-- 회원가입 완료 화면: sessionStorage의 flash 메시지를 읽어 성공 안내 표시 -->
<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-lg w-full max-w-sm px-10 py-10 text-center">
      <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-16 h-16 object-contain mx-auto mb-5" />
      <h1 class="text-xl font-bold text-gray-800 mb-2">가입이 완료되었습니다!</h1>

      <!-- flash 메시지 (sessionStorage에서 읽음, 5초 후 자동 사라짐) -->
      <div
        v-if="flashMsg"
        class="bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg px-4 py-3 mb-6"
      >
        {{ flashMsg }}
      </div>
      <p v-else class="text-sm text-gray-500 mb-8">회원 가입이 정상적으로 처리되었습니다.</p>

      <RouterLink
        to="/"
        class="inline-block bg-green-700 hover:bg-green-800 text-white font-semibold px-6 py-2 rounded-lg transition-colors"
      >
        메인으로 돌아가기
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const flashMsg = ref('')

onMounted(() => {
  const raw = sessionStorage.getItem('flashMsg')
  if (!raw) return

  const { msg, time } = JSON.parse(raw)
  sessionStorage.removeItem('flashMsg')

  // 300초(300_000ms) 이내인 경우에만 표시
  if (Date.now() - time < 300_000) {
    flashMsg.value = msg
    // 5초 후 자동 숨김
    setTimeout(() => { flashMsg.value = '' }, 5000)
  }
})
</script>
