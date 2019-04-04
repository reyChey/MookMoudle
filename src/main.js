import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'

Vue.config.productionTip = false

Vue.axios.interceptors.request.use(config => {
  // eslint-disable-next-line no-debugger
  // 在发送请求时带上token
  if (localStorage.JWT) {
    config.headers.authorization = `Bearer ${localStorage.JWT}`
  } else {
    config.headers.authorization = 'dummy' // 为了防止spring-security在响应头当中假如www-authenticate头，导致浏览器强制弹窗
  }
  return config
}, error => {
  // 处理error
  return Promise.reject(error.request)
})

/* 响应拦截器 */
Vue.axios.interceptors.response.use(res => {
  // 存储token
  localStorage.JWT = res.headers.authorization
  return res
}, error => {
  // eslint-disable-next-line no-debugger
  // 当请求时登录请求时，将错误转交给登录的组件自行处理
  if (error.request.responseURL.indexOf('/api/signIn') > 0) {
    return Promise.reject(error)
  } else if (error.response.status === 401 || error.response.status === 403) {
    // 状态码 401 ，403
    alert('你没有权限，请重新登录')
    localStorage.JWT = undefined
    app.$router.push({ path: '/' }) // 在前台将页面路由至登录页面
  } else {
    alert('服务器内部错误')
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
