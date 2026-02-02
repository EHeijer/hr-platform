<template>
  <div class="page">

    <header class="page-header">
      <h1>Departments</h1>
      <button class="primary" @click="openCreate">+ New Department</button>
    </header>

    <div class="layout">
      <!-- Department list -->
      <div class="card">
        <h2>All Departments</h2>

        <input
          v-model="search"
          placeholder="Search departments..."
          class="search"
        />

        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Code</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="department in filteredDepartments" :key="department.id">
              <td>{{ department.name }}</td>
              <td>{{ department.code }}</td>
              <td>
                <span :class="department.active ? 'badge success' : 'badge danger'">
                  {{ department.active ? 'Active' : 'Inactive' }}
                </span>
              </td>
              <td>
                <button class="small" @click="viewDepartmentUsers(department)">
                  View users
                </button>
                <button class="small" @click="editDepartment(department)">
                  Edit
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Users in department -->
      <div class="card" v-if="selectedDepartment">
        <h2>Users in {{ selectedDepartment.name }}</h2>

        <ul class="user-list">
          <li v-for="user in departmentUsers" :key="user.id">
            <RouterLink :to="`/profile/${user.id}`">
              <img src='../assets/avatar-default.png' class="avatar-sm" />
              {{ user.name }} — {{ user.email }}
            </RouterLink>
          </li>
        </ul>
      </div>
    </div>

    <!-- Dialog create/edit -->
    <div class="modal" v-if="departmentRequest">
      <div class="modal-content">
        <h3>{{ departmentRequest.id ? 'Edit Department' : 'Create Department' }}</h3>

        <label>Name</label>
        <input v-model="departmentRequest.name" />

        <label>Code</label>
        <input v-model="departmentRequest.code" />

        <label>Description</label>
        <textarea v-model="departmentRequest.description" />

        <label>
          <input type="checkbox" v-model="departmentRequest.active" />
          Active
        </label>

        <footer class="actions">
          <button @click="departmentRequest = null">Cancel</button>
          <button class="primary" @click="saveDepartment">Save</button>
        </footer>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { Department } from '../types/Department'
import { UserProfile } from '../types/UserProfile'
import { fetchAllDepartments, updateDepartment, createDepartment } from '../api/DepartmentsApi'
import { fetchUsersByDepartmentId } from '../api/UsersApi'

const departments = ref<Department[]>([])
const selectedDepartment = ref<any | null>(null)
const departmentUsers = ref<UserProfile[]>([])
const search = ref('')
const departmentRequest = ref<Department | null>(null)

onMounted(loadDepartments)

async function loadDepartments() {
  departments.value = await fetchAllDepartments()
}

const filteredDepartments = computed(() =>
  departments.value.filter(department =>
    department.name.toLowerCase().includes(search.value.toLowerCase())
  )
)

async function viewDepartmentUsers(department) {
  selectedDepartment.value = department
  departmentUsers.value = await fetchUsersByDepartmentId(department.id)
}

function openCreate() {
  departmentRequest.value = {
    name: '',
    code: '',
    description: '',
    active: true
  }
}

function editDepartment(department) {
  departmentRequest.value = department
}

async function saveDepartment() {
  const departmentId = departmentRequest.value.id
  const requestBody = JSON.stringify(departmentRequest.value)

  if (departmentId) {
    await updateDepartment(departmentId, requestBody)
  } else {
    await createDepartment(requestBody)
  }

  departmentRequest.value = null
  await loadDepartments()
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
