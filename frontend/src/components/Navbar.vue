<template>
    <header class="header">
      <div class="left">
        <span class="logo">HR Platform</span>
  
        <nav>
            <RouterLink to="/">Dashboard</RouterLink>
            <RouterLink to="/profile">Profile</RouterLink>
            <RouterLink to="/users">Users</RouterLink>
            <RouterLink to="/time-tracking">Time tracking</RouterLink>
            <RouterLink to="/payroll">Payroll</RouterLink>
        </nav>
      </div>
  
      <div class="right">
        <a class="logout" href="#" @click.prevent="handleLogout">Logout</a>
      </div>
    </header>
</template>
  
<script setup lang="ts">
import { useUserStore } from "../stores/user.store"

const userStore = useUserStore()

async function handleLogout() {
  // 1. Rensa frontend state
  userStore.clearUser() 

  // 2. Gör browser-redirect (inte fetch)
  window.location.href = "/logout"
}
</script>
  
<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 32px;
    background: var(--card);
    box-shadow: 0 1px 6px rgba(0,0,0,0.08);
}

.left {
    display: flex;
    align-items: center;
    gap: 32px;
}

.logo {
    font-weight: 600;
    font-size: 18px;
}

nav {
    display: flex;
    gap: 20px;
}

nav a {
    color: #374151;
}

nav a.router-link-active {
    color: var(--primary);
    font-weight: 500;
}

.right {
    display: flex;
    align-items: center;
    gap: 16px;
}

.logout {
    color: var(--primary);
    font-weight: 500;
}
</style>
  