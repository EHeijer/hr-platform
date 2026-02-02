<template>
  <aside class="sidebar">
    <div>
      <header class="header">
        <div class="logo">HR-Platform</div>
      </header>
      <nav>
        <RouterLink to="/">
          <Gauge /> 
          Dashboard
        </RouterLink>

        <RouterLink 
          v-if="userStore.currentUser"
          :to="`/profile/${userStore.currentUser.id}`"
        >
          <User />
          Profile
        </RouterLink>

        <RouterLink to="/users">
          <Users /> 
          Employees
        </RouterLink>

        <RouterLink to="/departments">
          <Building2 /> 
          Departments
        </RouterLink>

        <RouterLink to="/roles">
          <BadgeCheck /> 
          Roles
        </RouterLink>

        <RouterLink to="/time-tracking">
          <Clock /> 
          Time Tracking
        </RouterLink>

        <RouterLink to="/payroll">
          <Wallet /> 
          Payroll
        </RouterLink>
      </nav>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { Users, User, Building2, BadgeCheck, Gauge, Clock, Wallet } from 'lucide-vue-next'
import { useUserStore } from '../stores/user.store'

const userStore = useUserStore()

</script>

<style scoped>
.sidebar {
  width: 260px;
  background-color: var(--bg-secondary);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.sidebar .header {
  height: 85px;
  border-bottom: 1px solid var(--border);
  display: flex;
  justify-content: center;
  align-items: center;
}

.sidebar .logo {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  display: flex;
  justify-content: center;
}

.sidebar nav {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 35px 0;
}

.sidebar nav a {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 14px;
  font-size: 15px;
  font-weight: 500;
  text-decoration: none;

  color: var(--text-muted);

  transition:
    background-color 0.2s ease,
    color 0.2s ease,
    transform 0.15s ease;
}

.sidebar nav a svg {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
}

.sidebar nav a:hover {
  background-color: var(--primary-hover);
  color: var(--text-primary);
  transform: translateX(1px);
}

.sidebar nav a.router-link-active {
  background-color: var(--primary-soft);
  color: var(--primary);
}

.sidebar nav a.router-link-active svg {
  color: var(--primary);
}

@media (max-width: 900px) {
  .sidebar {
    display: none;
  }
}
</style>
