import { defineStore } from 'pinia'

export const useThemeStore = defineStore('theme', {
  state: () => ({
    mode: localStorage.getItem('theme') || 'light'
  }),
  actions: {
    toggle() {
      this.mode = this.mode === 'light' ? 'dark' : 'light'
      localStorage.setItem('theme', this.mode)
      document.documentElement.setAttribute('data-theme', this.mode)
    },
    init() {
      document.documentElement.setAttribute('data-theme', this.mode)
    }
  }
})
