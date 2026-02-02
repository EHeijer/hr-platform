<template>
  <div class="dropdown">
    <RouterLink to="/profile">
      <User />
      Profile
    </RouterLink>
    <button class="logout" @click="logout">
      <LogOut />
      Logout
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '../stores/user.store'
import { useRouter } from 'vue-router'
import { LogOut, User } from 'lucide-vue-next'


const open = ref(false)
const userStore = useUserStore()
const router = useRouter()

function logout() {
  // 1. Rensa frontend state
  userStore.clearUser() 

  // 2. Gör browser-redirect (inte fetch)
  window.location.href = "/logout"
}
</script>

<style scoped>
.dropdown {
  position: absolute;
  right: 10px;
  top: calc(100% + 30px);
  background: var(--bg-secondary);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
  min-width: 180px;
  padding: 0.5rem;
}

.dropdown a,
.dropdown button {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0.6rem 0.8rem;
  text-align: left;
  border-radius: 8px;
  color: inherit;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 15px;
  font-weight: 500;
  text-decoration: none;
}

.dropdown a:hover,
.dropdown button:hover {
  background: var(--primary-soft);
}

.dropdown button svg,
.dropdown a svg {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
  margin-right: 7px;
}
.logout {
  color: #e5484d;
}
</style>
