<template>
    <div class="layout">
        <Navbar />
        <main class="content">
            <router-view />
        </main>
    </div>
</template>
  
  <script setup lang="ts">
  import Navbar from '../components/Navbar.vue'
  </script>
  
  <style scoped>
  .layout {
    display: flex;
    flex-direction: column;
    height: 100vh;
  }
  
  .content {
    padding: 32px;
  }
  </style>
  