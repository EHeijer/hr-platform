<template>
  <div class="page">
    <header class="page-header">
      <h1>Work Roles</h1>
      <button class="primary" @click="create">+ New Role</button>
    </header>

    <div class="layout">
      <div class="card">
        <h2>Existing roles</h2>

        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Description</th>
              <th></th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="role in roles" :key="role.id">
              <td>{{ role.name }}</td>
              <td>{{ role.description }}</td>
              <td>
                <button class="small" @click="viewUsersWithRole(role)">View users</button>
                <button class="small" @click="edit(role)">Edit</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="card" v-if="selectedRole">
        <h2>Users with {{ selectedRole.name }}</h2>

        <ul class="user-list">
          <li v-for="user in roleUsers" :key="user.id">
            <RouterLink :to="`/profile/${user.id}`">
              <img src='../assets/avatar-default.png' class="avatar-sm" />
              {{ user.name }} — {{ user.email }}
            </RouterLink>
          </li>
        </ul>
      </div>
    </div>

    <!-- modal -->
    <div class="modal" v-if="roleRequest">
      <div class="modal-content">
        <h3>{{ roleRequest.id ? 'Edit role' : 'Create role' }}</h3>

        <label>Name</label>
        <input v-model="roleRequest.name" />

        <label>Description</label>
        <textarea v-model="roleRequest.description" />

        <footer class="actions">
          <button @click="roleRequest=null">Cancel</button>
          <button class="primary" @click="saveRole">Save</button>
        </footer>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Role } from '../types/Role'
import { UserProfile } from '../types/UserProfile'
import { createRole, fetchAllRoles, updateRole } from '../api/RolesApi'

const roles = ref<Role[]>([])
const selectedRole = ref<any|null>(null)
const roleUsers = ref<UserProfile[]>([])
const roleRequest = ref<Role|null>(null)

onMounted(loadRoles)

async function loadRoles() {
  roles.value = await fetchAllRoles()
}

function viewUsersWithRole(role: Role) {
  roleUsers.value = role.users
  selectedRole.value = role
}

function create() {
  roleRequest.value = { name:'', description:'' }
}

function edit(role) {
  roleRequest.value = role
  console.log("Edit role", roleRequest.value)
}

async function saveRole() {
  const roleId = roleRequest.value.id
  const requestBody = JSON.stringify(roleRequest.value)

  if(roleId){
    await updateRole(roleId, requestBody)
  } else {
    await createRole(requestBody)
  }

  roleRequest.value = null
  await loadRoles()
}
</script>

<style scoped>
.page { 
  padding: 1.5rem; 
}
.page-header { 
  display:flex; justify-content:space-between; align-items:center; 
}
.layout { 
  display:grid; 
  gap:1.5rem; 
  grid-template-columns: 1fr 1fr; 
}

.card { 
  background: var(--card-bg); 
  border-radius: 16px; 
  padding: 1.5rem; 
  box-shadow: var(--shadow); 
}

.table { 
  width:100%; 
  border-collapse: collapse; 
}
.table th, .table td { 
  padding: .75rem; 
}
.table tbody tr:hover { 
  background: var(--hover); 
}

.avatar-sm { 
  width:32px; 
  height:32px; 
  border-radius:50%; 
  margin-right:.5rem; 
}
.user-list li { 
  padding:.25rem 0; 
}

.badge { 
  padding:.25rem .5rem; 
  border-radius:8px; 
}
.success { 
  background:#28c76f22; 
  color:#28c76f; 
}
.danger { 
  background:#e5393522; 
  color:#e53935; 
}

.modal { 
  position:fixed; 
  inset:0; 
  background:#00000055; 
  display:flex; 
  align-items:center; justify-content:center; 
}
.modal-content { 
  background:var(--card-bg); 
  padding:1.5rem; 
  border-radius:16px; 
  width:420px; 
}
.actions { 
  display:flex; 
  justify-content:flex-end; 
  gap:1rem; 
}
</style>
