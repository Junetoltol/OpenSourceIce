<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-lg w-full max-w-sm px-10 py-10 text-center">
      <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-16 h-16 object-contain mx-auto mb-5" />
      <h1 class="text-xl font-bold text-gray-800 mb-2">가입이 완료되었습니다!</h1>
      <p class="text-sm text-gray-500 mb-8">회원 가입이 정상적으로 처리되었습니다.</p>
      <RouterLink
        to="/"
        class="inline-block bg-green-700 hover:bg-green-800 text-white font-semibold px-6 py-2 rounded-lg transition-colors"
      >
        메인으로 돌아가기
      </RouterLink>
    </div>
  </div>
</template>
