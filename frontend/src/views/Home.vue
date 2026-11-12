<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="welcome-card">
          <div slot="header">
            <span>欢迎使用财务管理系统</span>
          </div>
          <div class="welcome-content">
            <p>您的角色是：{{ roleNames[currentUser.roleId] }}</p>
            <p>上次登录时间：{{ new Date().toLocaleString() }}</p>
          </div>
        </el-card>

        <el-card class="announcement-card">
          <div slot="header">
            <span>系统公告</span>
          </div>
          <div v-if="announcements.length > 0">
            <div v-for="item in announcements" :key="item.id" class="announcement-item">
              <h4>{{ item.title }}</h4>
              <p>{{ item.content }}</p>
              <div class="announcement-footer">
                <span>发布时间：{{ item.createTime }}</span>
              </div>
            </div>
          </div>
          <div v-else class="empty-text">
            暂无公告
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="quick-actions">
          <div slot="header">
            <span>快捷操作</span>
          </div>
          <div class="action-list">
            <el-button 
              v-if="currentUser.roleId === 3"
              type="primary" 
              icon="el-icon-plus"
              @click="$router.push('/reimbursement/apply')"
            >申请报销</el-button>
            
            <el-button 
              v-if="currentUser.roleId === 2"
              type="primary" 
              icon="el-icon-document-checked"
              @click="$router.push('/reimbursement')"
            >报销审核</el-button>
            
            <el-button 
              v-if="currentUser.roleId === 1"
              type="primary" 
              icon="el-icon-setting"
              @click="$router.push('/user')"
            >用户管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  data() {
    return {
      roleNames: {
        1: '管理员',
        2: '财务',
        3: '员工'
      },
      announcements: []
    }
  },
  computed: {
    ...mapGetters(['currentUser'])
  },
  created() {
    this.getAnnouncements()
  },
  methods: {
    getAnnouncements() {
      this.$http.get('/api/announcement/published')
        .then(response => {
          if (response.data.code === 200) {
            this.announcements = response.data.data
          }
        })
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.welcome-card,
.announcement-card {
  margin-bottom: 20px;
}

.welcome-content {
  font-size: 16px;
  line-height: 1.8;
}

.announcement-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-footer {
  font-size: 12px;
  color: #999;
  margin-top: 10px;
}

.empty-text {
  text-align: center;
  color: #999;
  padding: 20px;
}

.quick-actions .action-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.quick-actions .el-button {
  width: 100%;
}
</style> 