<template>
  <div class="user-menu">
    <img class="avatar" src="../assets/avatar-default.png" />
    <span class="name">{{ user.username }}</span>
    
    <button class="dropdown-button" @click="open = !open">
      <Ellipsis/>
    </button>
    <div class="dropdown-wrapper" ref="dropdown-ref">
      <UserDropdown v-if="open" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, useTemplateRef } from 'vue'
import { Ellipsis } from 'lucide-vue-next';
import { useUserStore } from '../stores/user.store'
import { CurrentUser } from '../types/CurrentUser'
import UserDropdown from './UserDropdown.vue'
import { onClickOutside } from '@vueuse/core'

const userStore = useUserStore()
const user: CurrentUser = userStore.currentUser!
const open = ref(false)
const dropdownRef = useTemplateRef('dropdown-ref')

const avatarUrl = computed(() =>
  user.image || '../assets/avatar-default.png'
)

function close() {
  open.value = false
}
onClickOutside(dropdownRef, close)

function logout() {
  // 1. Rensa frontend state
  userStore.clearUser() 

  // 2. Gör browser-redirect (inte fetch)
  window.location.href = "/logout"
}
</script>

<style scoped>
.user-menu {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
 
  height: 100%;
}
.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}
.name {
  font-weight: 500;
}
.dropdown-button {
  color: inherit;
  background: none;
  border: none;
  cursor: pointer;
  border-radius: 20px;
  padding:3px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.dropdown-button:hover {
  background: var(--primary-soft)
}
.dropdown-button svg {
  width: 22px;
}
.dropdown-wrapper {
  position: relative;
}
</style>
