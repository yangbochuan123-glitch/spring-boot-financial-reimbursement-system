<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/home">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
        
        <el-submenu index="1" v-if="currentUser.roleId === 3">
          <template slot="title">
            <i class="el-icon-document"></i>
            <span>报销管理</span>
          </template>
          <el-menu-item index="/reimbursement">报销列表</el-menu-item>
          <el-menu-item index="/reimbursement/apply">报销申请</el-menu-item>
        </el-submenu>

        <el-submenu index="2" v-if="currentUser.roleId === 2 || currentUser.roleId === 1">
          <template slot="title">
            <i class="el-icon-document-checked"></i>
            <span>财务管理</span>
          </template>
          <el-menu-item index="/reimbursement">报销审核</el-menu-item>
          <el-menu-item index="/voucher">凭证管理</el-menu-item>
          <el-menu-item index="/report">财务报表</el-menu-item>
        </el-submenu>

        <el-submenu index="3" v-if="currentUser.roleId === 1">
          <template slot="title">
            <i class="el-icon-setting"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/user">用户管理</el-menu-item>
          <el-menu-item index="/role">角色管理</el-menu-item>
          <el-menu-item index="/reimbursement/type">报销类型</el-menu-item>
          <el-menu-item index="/announcement">系统公告</el-menu-item>
          <el-menu-item index="/log">登录日志</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-right">
          <span>欢迎，{{ currentUser.realName }}</span>
          <el-button type="text" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Layout',
  computed: {
    ...mapGetters(['currentUser'])
  },
  methods: {
    handleLogout() {
      this.$confirm('确认退出系统?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/api/logout').then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
          this.$message.success('退出成功')
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
}

.el-header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #e6e6e6;
}

.header-right {
  float: right;
  margin-right: 20px;
}

.header-right span {
  margin-right: 20px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style> 