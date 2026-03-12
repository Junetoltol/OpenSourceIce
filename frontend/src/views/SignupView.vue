<!-- 사용자 회원가입 화면: ID/PW/이메일 입력 후 /api/signup 호출, 성공 시 완료 페이지로 이동 -->
<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-lg w-full max-w-sm px-10 py-10">
      <!-- 로고 -->
      <div class="flex flex-col items-center mb-8">
        <img src="@/assets/marker.png" alt="한국외국어대학교" class="w-20 h-20 object-contain mb-3" />
        <h1 class="text-xl font-bold text-gray-800">회원 가입</h1>
        <p class="text-sm text-gray-500 mt-1">사용자 전용 공간입니다</p>
      </div>

      <!-- 에러 메시지 -->
      <div v-if="errorMsg" class="bg-red-50 border border-red-200 text-red-600 text-sm rounded-lg px-4 py-2 mb-5">
        {{ errorMsg }}
      </div>

      <!-- 교재 signup.jsp 폼 구조 -->
      <form @submit.prevent="handleSignup" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">아이디</label>
          <input
            ref="userIDRef"
            v-model="userID"
            type="text"
            placeholder="2~16자 이내로 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호</label>
          <input
            ref="userPWRef"
            v-model="userPW"
            type="password"
            placeholder="8~16자, 영문·숫자·특수문자 포함"
            :class="[
              'w-full border rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2',
              userPW.length === 0
                ? 'border-gray-300 focus:ring-green-500'
                : pwValid
                ? 'border-green-500 focus:ring-green-500'
                : 'border-red-400 focus:ring-red-400'
            ]"
          />
          <!-- 비밀번호 조건 안내 -->
          <p :class="[
            'text-xs mt-1',
            userPW.length === 0 ? 'text-gray-400' : pwValid ? 'text-green-600' : 'text-red-500'
          ]">
            8~16자, 영문·숫자·특수문자(!@#$ 등) 각 1개 이상 포함
          </p>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">이메일</label>
          <input
            ref="userMAILRef"
            v-model="userMAIL"
            type="email"
            placeholder="이메일 주소를 입력하세요"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm text-gray-900 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div class="flex gap-2 pt-1">
          <button
            type="reset"
            @click="resetForm"
            class="w-1/2 bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold py-2 rounded-lg transition-colors"
          >
            ◀ 다시입력
          </button>
          <button
            type="submit"
            :disabled="isLoading"
            class="w-1/2 bg-green-700 hover:bg-green-800 text-white font-semibold py-2 rounded-lg transition-colors disabled:opacity-50"
          >
            {{ isLoading ? '처리 중...' : '가입하기 ▶' }}
          </button>
        </div>
      </form>

      <!-- 메인으로 돌아가기 -->
      <div class="text-center mt-5">
        <RouterLink to="/" class="text-sm text-gray-400 hover:text-gray-600">← 메인으로 돌아가기</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const userID = ref('')
const userPW = ref('')
const userMAIL = ref('')
const errorMsg = ref('')
const isLoading = ref(false)
const userIDRef = ref(null)
const userPWRef = ref(null)
const userMAILRef = ref(null)

// 비밀번호 유효성: 8~16자, 영문·숫자·특수문자 각 1개 이상
const pwRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]).{8,16}$/
const pwValid = computed(() => pwRegex.test(userPW.value))

function resetForm() {
  userID.value = ''
  userPW.value = ''
  userMAIL.value = ''
  errorMsg.value = ''
}

// 교재 checkFun() 검증 로직 기반 + 비밀번호 강도 검사 추가
async function handleSignup() {
  errorMsg.value = ''

  if (userID.value.length < 2 || userID.value.length > 16) {
    errorMsg.value = '아이디는 2~16자 이내로 입력해야 합니다.'
    userIDRef.value.focus()
    return
  }
  if (!pwValid.value) {
    errorMsg.value = '비밀번호는 8~16자이며 영문, 숫자, 특수문자를 각 1개 이상 포함해야 합니다.'
    userPWRef.value.focus()
    return
  }
  if (userMAIL.value === '') {
    errorMsg.value = '이메일 주소는 반드시 입력해야 합니다.'
    userMAILRef.value.focus()
    return
  }

  isLoading.value = true

  try {
    const response = await fetch('/api/signup', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({
        userID: userID.value,
        userPW: userPW.value,
        userMAIL: userMAIL.value,
      }),
    })

    if (response.ok) {
      // 성공 메시지를 sessionStorage에 저장 (300초 유효)
      sessionStorage.setItem('flashMsg', JSON.stringify({
        msg: `환영합니다, ${userID.value}님! 회원가입이 완료되었습니다.`,
        time: Date.now()
      }))
      router.push('/signup-success')
    } else if (response.status === 409) {
      errorMsg.value = '이미 사용 중인 아이디입니다. 다른 아이디를 입력해 주세요.'
    } else {
      errorMsg.value = '가입 중 오류가 발생했습니다. 다시 시도해 주세요.'
    }
  } catch {
    errorMsg.value = '서버에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    isLoading.value = false
  }
}
</script>
