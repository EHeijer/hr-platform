<template>
  <header class="topbar">
    <SearchInput />
    <div class="switch-menu-wrapper">
      <div class="theme-switch-wrapper">
        <ThemeSwitch class="theme-switch" />
      </div>
      <UserMenu class="menu"/>
    </div>
  </header>
</template>

<script setup>
import { useUserStore } from '../stores/user.store'
import { useThemeStore } from '../stores/theme.store'
import ThemeSwitch from './ThemeSwitch.vue'
import UserMenu from './UserMenu.vue'
import SearchInput from './SearchInput.vue'

const userStore = useUserStore()
const theme = useThemeStore()

</script>

<style scoped>
.topbar {
  height: 85px;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
}
.switch-menu-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}
.theme-switch-wrapper {
  display: flex;
  justify-content: center;
  align-items: center; 
  width: 90px;
  height: 38px;
  border-right: 1.5px solid var(--border);
  border-left: 1.5px solid var(--border);
  margin-right: 20px;
}
</style>
