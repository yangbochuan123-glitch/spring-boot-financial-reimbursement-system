<template>
  <div class="type-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>报销类型管理</span>
        <el-button
          style="float: right"
          type="primary"
          size="small"
          @click="handleAdd"
        >添加类型</el-button>
      </div>

      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="typeName" label="类型名称"></el-table-column>
        <el-table-column prop="budgetLimit" label="预算限额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.budgetLimit }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
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
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="dialog.form.typeName" placeholder="请输入类型名称"></el-input>
        </el-form-item>
        <el-form-item label="预算限额" prop="budgetLimit">
          <el-input-number
            v-model="dialog.form.budgetLimit"
            :precision="2"
            :step="100"
            :min="0"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="说明" prop="description">
          <el-input
            type="textarea"
            v-model="dialog.form.description"
            :rows="3"
            placeholder="请输入说明"
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
  name: 'ReimbursementType',
  data() {
    return {
      list: [],
      dialog: {
        visible: false,
        type: 'add', // add or edit
        form: {
          id: null,
          typeName: '',
          budgetLimit: 0,
          description: ''
        }
      },
      rules: {
        typeName: [
          { required: true, message: '请输入类型名称', trigger: 'blur' }
        ],
        budgetLimit: [
          { required: true, message: '请输入预算限额', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialog.type === 'add' ? '添加报销类型' : '编辑报销类型'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.$http.get('/api/reimbursement/type/list').then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    handleAdd() {
      this.dialog.type = 'add'
      this.dialog.form = {
        id: null,
        typeName: '',
        budgetLimit: 0,
        description: ''
      }
      this.dialog.visible = true
    },
    handleEdit(row) {
      this.dialog.type = 'edit'
      this.dialog.form = { ...row }
      this.dialog.visible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该报销类型?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/reimbursement/type/delete/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.getList()
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
          this.$http.post(`/api/reimbursement/type/${action}`, this.dialog.form)
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
.type-container {
  padding: 20px;
}

.el-input-number {
  width: 100%;
}
</style> 