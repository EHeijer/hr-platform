<template>
  <div class="dashboard">
    <div class="grid">

      <DashboardCard
        title="Total Employees"
        size="sm"
        :icon="UsersIcon"
      >
        <strong>128</strong>
      </DashboardCard>

      <DashboardCard
        title="Open Positions"
        size="sm"
        :icon="BriefcaseIcon"
      >
        <strong>12</strong>
      </DashboardCard>

      <DashboardCard
        title="Onboarding"
        size="md"
        :icon="CalendarIcon"
      >
        <strong>5 employees starting this week</strong>
      </DashboardCard>

      <DashboardCard
        title="Performance"
        size="md"
        :icon="ChartBarSquareIcon"
      >
        <strong>Avg score: 4.3 / 5</strong>
      </DashboardCard>

    </div>
  </div>
</template>

<script setup lang="ts">
import DashboardCard from '../components/DashboardCard.vue'
import { ChartBarSquareIcon, CalendarIcon, BriefcaseIcon, UsersIcon } from '@heroicons/vue/24/outline'
</script>
  
<style scoped>
.dashboard {
  padding: 2rem;
}

.grid {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  gap: 1.5rem;
}

@media (max-width: 1200px) {
  .grid {
    grid-template-columns: repeat(6, 1fr);
  }
}

@media (max-width: 768px) {
  .grid {
    grid-template-columns: repeat(1, 1fr);
  }
}
</style>
