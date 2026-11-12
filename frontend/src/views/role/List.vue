<template>
  <div class="role-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>角色管理</span>
      </div>

      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="roleName" label="角色名称"></el-table-column>
        <el-table-column prop="roleCode" label="角色编码"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑角色" :visible.sync="dialog.visible" width="500px">
      <el-form :model="dialog.form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="dialog.form.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="dialog.form.roleCode" disabled></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            type="textarea"
            v-model="dialog.form.description"
            :rows="3"
            placeholder="请输入角色描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'RoleList',
  data() {
    return {
      list: [],
      dialog: {
        visible: false,
        form: {
          id: null,
          roleName: '',
          roleCode: '',
          description: ''
        }
      },
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.$http.get('/api/role/list').then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    handleEdit(row) {
      this.dialog.form = { ...row }
      this.dialog.visible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$http.post('/api/role/update', this.dialog.form)
            .then(response => {
              if (response.data.code === 200) {
                this.$message.success('更新成功')
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
.role-container {
  padding: 20px;
}
</style> 