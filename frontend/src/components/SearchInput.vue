<template>
  <div class="search">
    <MagnifyingGlassIcon />
    <input placeholder="Search employees, teams..." />
  </div>
</template>

<script setup lang="ts">
import { MagnifyingGlassIcon } from '@heroicons/vue/24/outline'
</script>

<style scoped>
.search {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  background: #ffffff;
  padding: 0.6rem 0.9rem;
  border-radius: 20px;
  width: 320px;
  border: 1px solid var(--border);

  transition:
    background 0.2s ease,
    box-shadow 0.2s ease,
    border-color 0.2s ease;
}
.search:focus-within {
  background: var(--surface-elevated);
  border-color: var(--primary);
  box-shadow: 0 0 0 4px color-mix(in srgb, var(--primary) 20%, transparent);
}
.search svg {
  width: 20px;
  height: 20px;
  color: black;
  transition: color 0.2s ease;
}
.search:focus-within svg {
  color: var(--primary);
}
input {
  border: none;
  background: transparent;
  outline: none;
  width: 100%;
  font-size: 1rem;
  color: var(--text);
}
.search input::placeholder {
  color: var(--text-muted);
}

@media (max-width: 768px) {
  .search {
    width: 100%;
  }
}
</style>
