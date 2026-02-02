<template>
  <div v-if="profile && department" class="profile-card">
    <div class="header">
      <img class="avatar-lg" src="../assets/avatar-default.png" />
      <div>
        <h2>{{ profile.name }}</h2>
        <p>{{ formatRoles(profile.roles) }} | {{ department.name }}</p>
      </div>
    </div>

    <div class="grid">
      <div><label>Email:</label><span>{{ profile.email }}</span></div>
      <div><label>Phone:</label><span>{{ profile.phone }}</span></div>
      <div><label>Address:</label><span>{{ profile.address }}</span></div>
      <div><label>Start Date:</label><span>{{ profile.startDate }}</span></div>
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { UserProfile } from '../types/UserProfile'
import { fetchUserProfile } from '../api/UsersApi'
import { fetchDepartmentById } from '../api/DepartmentsApi'
import { useUserStore } from "../stores/user.store"
import { useRoute } from 'vue-router'
import { Role } from '../types/Role'
import { Department } from '../types/Department'
  
const route = useRoute()
const profile = ref<UserProfile | null>(null)
const department = ref<Department | null>(null)

onMounted(async () => {
  const userId = route.params.userId as string
  if (!userId) return
  profile.value = await fetchUserProfile(userId)
  department.value = await fetchDepartmentById(profile.value.departmentId)
})

function formatRoles(roles: Role[]) {
  return roles.map(r => r.name).join(", ");
}
  
async function save() {
    await fetch('/api/users/profile/' + profile.value.id, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(profile.value)
    })
}
</script>

<style scoped>
.profile-card {
  background: var(--bg-secondary);
  padding: 32px;
  border-radius: 20px;
  max-width: 900px;
  border: 1px solid var(--border);
}

.header {
  display: flex;
  gap: 20px;
  align-items: center;
  margin-bottom: 30px;
}
.header div h2 {
  margin-bottom: 0px;
}
.header div p {
  color: var(--text-muted);
  margin-top: 5px;
}

.avatar-lg {
  width: 96px;
  height: 96px;
  border-radius: 50%;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

label {
  font-size: 12px;
  color: var(--text-muted);
  margin-right: 3px;
}
</style>
  