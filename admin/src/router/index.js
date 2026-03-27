import { createRouter, createWebHistory } from 'vue-router'
import Login from '../pages/Login.vue'
import Layout from '../pages/Layout.vue'
import Dashboard from '../pages/Dashboard.vue'
import QuestionManage from '../pages/QuestionManage.vue'
import UserManage from '../pages/UserManage.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login },
  {
    path: '/',
    component: Layout,
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', name: 'Dashboard', component: Dashboard },
      { path: 'questions', name: 'QuestionManage', component: QuestionManage },
      { path: 'users', name: 'UserManage', component: UserManage }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router