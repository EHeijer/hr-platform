<template>
    <AppLayout v-if="currentUser"/>
</template>

<script setup lang="ts">
import AppLayout from './layout/AppLayout.vue'
import { onMounted, ref, watch } from 'vue'
import {CurrentUser} from './types/CurrentUser'
import { useUserStore } from "./stores/user.store"

const userStore = useUserStore()
const currentUser = ref<CurrentUser | null>(null)

onMounted(async () => {
  await userStore.loadCurrentUser()
  currentUser.value = userStore.currentUser
})
</script>
  