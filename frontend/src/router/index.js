import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../layout/Layout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: 'reimbursement',
        name: 'Reimbursement',
        component: () => import('../views/reimbursement/List.vue')
      },
      {
        path: 'reimbursement/apply',
        name: 'ReimbursementApply',
        component: () => import('../views/reimbursement/Apply.vue')
      },
      {
        path: 'reimbursement/type',
        name: 'ReimbursementType',
        component: () => import('../views/reimbursement/Type.vue')
      },
      {
        path: 'announcement',
        name: 'Announcement',
        component: () => import('../views/announcement/List.vue')
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/user/List.vue')
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('../views/role/List.vue')
      },
      {
        path: 'voucher',
        name: 'Voucher',
        component: () => import('../views/voucher/List.vue')
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('../views/log/List.vue')
      },
      {
        path: 'report',
        name: 'Report',
        component: () => import('../views/report/Index.vue')
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user'))
  if (to.path === '/login') {
    next()
  } else {
    if (!user) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
