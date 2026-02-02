import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useThemeStore } from './stores/theme.store'

import './assets/base.css'
import './assets/theme.css'
//import './styles.css'

createApp(App)
  .use(router)
  .use(createPinia())
  .mount('#app')

useThemeStore().init()
