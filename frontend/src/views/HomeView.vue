<!-- 메인 화면: 관리자 접속(파란 버튼 → 로그인)과 사용자 접속(초록 버튼 → 사용자 로그인) 선택 -->
<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-lg w-full max-w-sm px-10 py-10 text-center">
      <!-- 로고 -->
      <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-20 h-20 object-contain mx-auto mb-4" />
      <h1 class="text-xl font-bold text-gray-800 mb-1">회원 관리 시스템</h1>
      <p class="text-sm text-gray-500 mb-8">한국외국어대학교 오픈소스 5조</p>

      <!-- 교재 main.jsp 구조와 동일: 두 버튼으로 분기 -->
      <div class="space-y-3">
        <RouterLink
          to="/login"
          class="block w-full bg-blue-800 hover:bg-blue-900 text-white font-semibold py-2 rounded-lg transition-colors"
        >
          관리자 접속하기 ▶
        </RouterLink>

        <RouterLink
          to="/user-login"
          class="block w-full bg-green-700 hover:bg-green-800 text-white font-semibold py-2 rounded-lg transition-colors"
        >
          사용자 접속하기 ▶
        </RouterLink>
      </div>
    </div>
  </div>
</template>
