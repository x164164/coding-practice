<template>
  <div class="layout">
    <div class="sidebar">
      <div class="logo">编程刷题系统</div>
      <div class="menu">
        <router-link to="/dashboard" class="menu-item" :class="{ active: route.path === '/dashboard' }">
          首页
        </router-link>
        <router-link to="/questions" class="menu-item" :class="{ active: route.path === '/questions' }">
          题目管理
        </router-link>
        <router-link to="/users" class="menu-item" :class="{ active: route.path === '/users' }">
          用户管理
        </router-link>
      </div>
    </div>
    <div class="main">
      <div class="header">
        <span>{{ route.meta.title }}</span>
        <div>
          <span>{{ userInfo?.nickname }}</span>
          <el-button size="small" style="margin-left: 10px" @click="handleLogout">退出</el-button>
        </div>
      </div>
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const userInfo = computed(() => {
  const info = localStorage.getItem('userInfo')
  return info ? JSON.parse(info) : null
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
}
.content {
  padding: 0;
}
</style>