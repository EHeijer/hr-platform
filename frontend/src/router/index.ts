import { createRouter, createWebHistory } from 'vue-router'

import DashboardView from '../views/DashboardView.vue'
import ProfileView from '../views/ProfileView.vue'
import UsersView from '../views/UsersView.vue'
import TimeTrackingView from '../views/TimeTrackingView.vue'
import PayrollView from '../views/PayrollView.vue'
import DepartmentsView from '../views/DepartmentsView.vue'
import RolesView from '../views/RolesView.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: DashboardView },

    // Users
    { path: '/users', component: UsersView },
    { path: "/profile/:userId", component: ProfileView },

    // Departments
    { path: '/departments', component: DepartmentsView },
    { path: '/departments/:departmentId/users', component: UsersView },

    // Roles
    { path: '/roles', component: RolesView },
    { path: '/roles/:roleId/users', component: UsersView },

    // Other existing
    { path: '/time-tracking', component: TimeTrackingView },
    { path: '/payroll', component: PayrollView }
  ]
})
