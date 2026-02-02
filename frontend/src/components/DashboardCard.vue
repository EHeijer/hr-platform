<template>
  <div class="card" :class="size">
    <div class="card-header">
      <div
        v-if="icon"
        class="icon-wrapper"
      >
        <component :is="icon" />
      </div>

      <span class="title">{{ title }}</span>
    </div>

    <div class="content">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Component } from 'vue'

defineProps<{
  title: string
  size?: 'sm' | 'md' | 'lg'
  icon?: Component
  iconBg?: string
  iconColor?: string
}>()
</script>

<style scoped>
.card {
  background: var(--bg-secondary);
  border-radius: 12px;
  padding: 1.4rem;
  border: 1px solid var(--border);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.icon-wrapper {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--primary-soft);
  color: var(--primary);
}

.icon-wrapper svg {
  width: 18px;
  height: 18px;
}

.title {
  font-size: 0.85rem;
  opacity: 0.7;
}

/* Sizes */
.sm { grid-column: span 3; }
.md { grid-column: span 6; }
.lg { grid-column: span 9; }

@media (max-width: 768px) {
  .sm, .md, .lg {
    grid-column: span 1;
  }
}
</style>
