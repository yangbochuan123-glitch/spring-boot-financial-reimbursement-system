<template>
  <div class="user-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>用户管理</span>
        <el-button
          style="float: right"
          type="primary"
          size="small"
          @click="handleAdd"
        >添加用户</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item>
          <el-input
            v-model="queryForm.username"
            placeholder="用户名"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryForm.roleId" placeholder="角色" clearable>
            <el-option label="管理员" :value="1"></el-option>
            <el-option label="财务" :value="2"></el-option>
            <el-option label="员工" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="realName" label="姓名"></el-table-column>
        <el-table-column prop="roleName" label="角色"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleResetPassword(scope.row)"
            >重置密码</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialog.visible" width="500px">
      <el-form :model="dialog.form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="用户名" prop="username" v-if="dialog.type === 'add'">
          <el-input v-model="dialog.form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="dialog.form.realName" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="dialog.form.roleId" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" :value="1"></el-option>
            <el-option label="财务" :value="2"></el-option>
            <el-option label="员工" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="dialog.form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="dialog.form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="dialog.form.status"
            :active-value="1"
            :inactive-value="0"
          ></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog title="修改密码" :visible.sync="passwordDialog.visible" width="400px">
      <el-form :model="passwordDialog.form" :rules="passwordRules" ref="passwordForm" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordDialog.form.oldPassword"
            type="password"
            placeholder="请输入原密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordDialog.form.newPassword"
            type="password"
            placeholder="请输入新密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordDialog.form.confirmPassword"
            type="password"
            placeholder="请确认新密码"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="passwordDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdatePassword">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserList',
  data() {
    // 验证确认密码
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordDialog.form.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      queryForm: {
        username: '',
        roleId: ''
      },
      list: [],
      dialog: {
        visible: false,
        type: 'add', // add or edit
        form: {
          id: null,
          username: '',
          realName: '',
          roleId: '',
          email: '',
          phone: '',
          status: 1
        }
      },
      passwordDialog: {
        visible: false,
        form: {
          userId: null,
          oldPassword: '',
          newPassword: '',
          confirmPassword: ''
        }
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        roleId: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ]
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialog.type === 'add' ? '添加用户' : '编辑用户'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.$http.get('/api/user/list', { params: this.queryForm }).then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    resetQuery() {
      this.queryForm = {
        username: '',
        roleId: ''
      }
      this.getList()
    },
    handleAdd() {
      this.dialog.type = 'add'
      this.dialog.form = {
        id: null,
        username: '',
        realName: '',
        roleId: '',
        email: '',
        phone: '',
        status: 1
      }
      this.dialog.visible = true
    },
    handleEdit(row) {
      this.dialog.type = 'edit'
      this.dialog.form = { ...row }
      this.dialog.visible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该用户?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/user/delete/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {})
    },
    handleResetPassword(row) {
      this.$confirm('确认重置该用户的密码?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.post(`/api/user/reset-password/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('密码已重置为默认密码：123456')
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const action = this.dialog.type === 'add' ? 'add' : 'update'
          this.$http.post(`/api/user/${action}`, this.dialog.form)
            .then(response => {
              if (response.data.code === 200) {
                this.$message.success(this.dialog.type === 'add' ? '添加成功' : '更新成功')
                this.dialog.visible = false
                this.getList()
              } else {
                this.$message.error(response.data.message)
              }
            })
        }
      })
    }
  }
}
</script>

<style scoped>
.user-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}
</style> 