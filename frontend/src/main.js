// Vue 앱 시작점: Pinia(상태 관리)와 Router를 연결한 뒤 #app 요소에 마운트
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
