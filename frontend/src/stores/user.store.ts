// src/stores/user.store.ts
import { defineStore } from "pinia"
import { ref, computed } from "vue"
import type { CurrentUser } from "../types/CurrentUser"
import { fetchCurrentUser } from '../api/AuthApi'

export const useUserStore = defineStore("user", () => {
  const currentUser = ref<CurrentUser | null>(null)
  const isAuthenticated = computed(() => !!currentUser.value)
  const isAdmin = computed(() => currentUser.value?.roles?.includes("ROLE_ADMIN"))
  const isHr = computed(() => currentUser.value?.roles?.includes("ROLE_HR"))

  async function loadCurrentUser() {
    if (!currentUser.value) {
      currentUser.value = await fetchCurrentUser()
    }
  }

  function clearUser() {
    currentUser.value = null
  }

  return {
    currentUser,
    isAuthenticated,
    loadCurrentUser,
    clearUser,
    isAdmin,
    isHr
  }
})
