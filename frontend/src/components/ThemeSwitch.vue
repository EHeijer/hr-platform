<template>
  <div class="theme-switch">
    <button class="theme-switch-button" @click="toggle" :class="{ dark: isDark }">
      <span class="track">
        <span class="thumb" :class="{ dark: isDark }">
          <Moon v-if="isDark" />
          <SunMedium v-else />
        </span>
      </span>
    </button>
    <!-- <p :class="{ dark: isDark }">{{ isDark ? 'Dark Mode' : 'Light Mode' }}</p> -->
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useThemeStore } from '../stores/theme.store'
import { SunMedium, Moon } from 'lucide-vue-next';

const themeStore = useThemeStore()
const mode = themeStore.mode

const isDark = computed(() => themeStore.mode === 'dark')

function toggle() {
  themeStore.toggle()
}
</script>

<style scoped>
.theme-switch {
  display: flex;
  align-items: center;
  justify-content: center;
}
.theme-switch-button {
  position: relative;
  width: 56px;
  height: 28px;
  border-radius: 999px;
  background-color: var(--primary-soft);
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: background-color 0.3s ease;
}
.theme-switch-button:hover {
  box-shadow: var(--shadow-sm);
}
.theme-switch-button.dark {
  background-color: var(--primary);
}
.theme-switch p {
  margin-left: 12px;
  color: var(--text-muted);
}
.theme-switch p.dark {
  color: white;
}

.track {
  height: 24px;
  background: var(--color-muted);
  position: relative;
}

.thumb {
  width: 22px;
  height: 22px;
  background: white;
  border-radius: 50%;
  position: absolute;
  top: 0.8px;
  left: 0px;
  transition: transform 0.25s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}
.thumb.dark {
  transform: translateX(27px);
}
.thumb svg {
  width: 15px;
  color: var(--primary)
}
.thumb.dark svg {
  color: var(--primary)
}

</style>
