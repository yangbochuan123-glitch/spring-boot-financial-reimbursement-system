<template>
  <div class="announcement-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>系统公告</span>
        <el-button
          v-if="currentUser.roleId === 1"
          style="float: right"
          type="primary"
          size="small"
          @click="handleAdd"
        >发布公告</el-button>
      </div>

      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="publisherName" label="发布人"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '已发布' : '未发布' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="currentUser.roleId === 1"
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="currentUser.roleId === 1"
              type="text"
              size="small"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleView(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialog.visible" width="600px">
      <el-form :model="dialog.form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="dialog.form.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            v-model="dialog.form.content"
            :rows="6"
            placeholder="请输入公告内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dialog.form.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">暂存</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="公告详情" :visible.sync="viewDialog.visible" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标题" :span="2">{{ viewDialog.data.title }}</el-descriptions-item>
        <el-descriptions-item label="发布人">{{ viewDialog.data.publisherName }}</el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ viewDialog.data.createTime }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">
          <div style="white-space: pre-wrap;">{{ viewDialog.data.content }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'AnnouncementList',
  data() {
    return {
      list: [],
      dialog: {
        visible: false,
        type: 'add', // add or edit
        form: {
          id: null,
          title: '',
          content: '',
          status: 1,
          publisherId: null
        }
      },
      viewDialog: {
        visible: false,
        data: {}
      },
      rules: {
        title: [
          { required: true, message: '请输入公告标题', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入公告内容', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['currentUser']),
    dialogTitle() {
      return this.dialog.type === 'add' ? '发布公告' : '编辑公告'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.$http.get('/api/announcement/list').then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    handleAdd() {
      this.dialog.type = 'add'
      this.dialog.form = {
        id: null,
        title: '',
        content: '',
        status: 1,
        publisherId: this.currentUser.id
      }
      this.dialog.visible = true
    },
    handleEdit(row) {
      this.dialog.type = 'edit'
      this.dialog.form = { ...row }
      this.dialog.visible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该公告?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/announcement/delete/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {})
    },
    handleView(row) {
      this.$http.get(`/api/announcement/detail/${row.id}`).then(response => {
        if (response.data.code === 200) {
          this.viewDialog.data = response.data.data
          this.viewDialog.visible = true
        }
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const action = this.dialog.type === 'add' ? 'add' : 'update'
          this.$http.post(`/api/announcement/${action}`, this.dialog.form)
            .then(response => {
              if (response.data.code === 200) {
                this.$message.success(this.dialog.type === 'add' ? '发布成功' : '更新成功')
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
.announcement-container {
  padding: 20px;
}
</style> 