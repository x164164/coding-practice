import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截
request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request

// API 函数
export const authApi = {
  login: (data) => request.post('/auth/login/username', data),
  getInfo: () => request.get('/auth/info'),
  updateInfo: (data) => request.put('/auth/info', data)
}

export const questionApi = {
  list: (params) => request.get('/question/list', { params }),
  get: (id) => request.get(`/question/${id}`),
  add: (data) => request.post('/question', data),
  update: (id, data) => request.put(`/question/${id}`, data),
  delete: (id) => request.delete(`/question/${id}`)
}

export const userApi = {
  list: (params) => request.get('/admin/user/list', { params }),
  update: (id, data) => request.put(`/admin/user/${id}`, data),
  delete: (id) => request.delete(`/admin/user/${id}`)
}

export const statApi = {
  overview: () => request.get('/stat/overview'),
  ranking: (params) => request.get('/stat/ranking', { params })
}