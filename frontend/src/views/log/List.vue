<template>
  <div class="log-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>登录日志</span>
        <el-button
          style="float: right; margin-left: 10px"
          type="danger"
          size="small"
          @click="handleClear"
        >清空日志</el-button>
      </div>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item>
          <el-input
            v-model="queryForm.username"
            placeholder="用户名"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryForm.operation" placeholder="操作类型" clearable>
            <el-option label="登录成功" value="登录成功"></el-option>
            <el-option label="登录失败" value="登录失败"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 日志列表 -->
      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="operation" label="操作类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getOperationType(scope.row.operation)">
              {{ scope.row.operation }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP地址" width="130"></el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180"></el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'LogList',
  data() {
    return {
      queryForm: {
        username: '',
        operation: ''
      },
      list: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.$http.get('/api/log/list', { params: this.queryForm }).then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    resetQuery() {
      this.queryForm = {
        username: '',
        operation: ''
      }
      this.getList()
    },
    handleDelete(row) {
      this.$confirm('确认删除该日志记录?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/log/delete/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {})
    },
    handleClear() {
      this.$confirm('确认清空所有日志记录?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.delete('/api/log/clear').then(response => {
          if (response.data.code === 200) {
            this.$message.success('清空成功')
            this.getList()
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {})
    },
    getOperationType(operation) {
      const types = {
        '登录成功': 'success',
        '登录失败': 'danger',
        '退出登录': 'info'
      }
      return types[operation] || ''
    }
  }
}
</script>

<style scoped>
.log-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}
</style> 