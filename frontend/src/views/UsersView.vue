<template>
  <div class="users-page">
    <header class="page-header">
      <h1>Employees</h1>
    </header>

    <div class="table-wrapper">
      <table class="users-table">
        <thead>
          <tr>
            <th>User</th>
            <th>Email</th>
            <th>Roles</th>
            <th>Department</th>
            <th>Enabled</th>
            <th v-if="isAllowedToEdit">Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="user in users"
            :key="user.id"
            @click="goToProfile(user.id)"
          >
            <td class="user-cell">
              <img
                class="avatar"
                src="../assets/avatar-default.png"
                alt="avatar"
              />
              <span>{{ user.name }}</span>
            </td>

            <td>{{ user.email }}</td>

            <!-- ROLES -->
            <td>
              <template v-if="user.roles?.length">
                <span
                  class="badge role"
                  v-for="role in user.roles"
                  :key="role.id"
                >
                  {{ role.name }}
                </span>
              </template>
              <template v-else>
                <span class="text-muted">—</span>
              </template>
            </td>

            <!-- DEPARTMENT -->
            <td>
              <span
                v-if="user.department"
                class="badge department"
              >
                {{ user.department.name }}
              </span>
              <span v-else class="text-muted">—</span>
            </td>

            <!-- ACTIVE -->
            <td>
              <span class="enabled">
                <CircleCheck class="circle-check" v-if="user.active" />
                <CircleX class="circle-x" v-else />
              </span>
            </td>

            <!-- EDIT BUTTON -->
            <td v-if="isAllowedToEdit">
              <button
                class="edit-btn"
                @click.stop="openEdit(user)"
              >
                Edit
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from "vue"
import { useRouter } from "vue-router"
import { fetchAllUsers } from "../api/UsersApi"
import type { UserProfile } from "../types/UserProfile"
import { CircleCheck, CircleX } from "lucide-vue-next"
import { useUserStore } from "../stores/user.store"

const users = ref<UserProfile[]>([])
const router = useRouter()
const userStore = useUserStore()
const isAllowedToEdit = userStore.isAdmin || userStore.isHr

onMounted(async () => {
  users.value = await fetchAllUsers()
})

function goToProfile(userId: string) {
  router.push(`/profile/${userId}`)
}

// 👉 editing dialog trigger (you plug in your modal later)
function openEdit(user: UserProfile) {
  console.log("Open edit dialog for", user)
}
</script>
  
<style scoped>
.users-page {
  padding: 5px;
}

h1 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 16px;
}

.table-wrapper {
  overflow-x: auto;
  background: var(--card-bg);
  border-radius: 16px;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

th {
  text-align: left;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  padding: 14px 16px;
}

td {
  padding: 16px;
  font-size: 14px;
}

tbody tr {
  cursor: pointer;
  transition: background 0.2s ease;
}

tbody tr:hover {
  background: var(--hover-bg);
}

/* USER CELL */
.user-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.badge {
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 500;
}

.badge.role {
  background: rgba(99, 102, 241, 0.15);
  color: #6366f1;
}

.badge.department {
  background: rgba(16, 185, 129, 0.15);
  color: #10b981;
}
.enabled {
  display: flex;
  justify-content: center;
  align-items: center;
}
.circle-check {
  color: rgb(6, 213, 6);
}
.circle-x {
  color: red;
}
.text-muted {
  color: var(--text-muted);
}
.edit-btn {
  padding: 6px 10px;
  border-radius: 8px;
  font-size: 12px;
  background: var(--primary);
  color: white;
}

/* MOBILE */
@media (max-width: 768px) {
  th:nth-child(2),
  td:nth-child(2),
  th:nth-child(4),
  td:nth-child(4) {
    display: none;
  }
}
</style>
  