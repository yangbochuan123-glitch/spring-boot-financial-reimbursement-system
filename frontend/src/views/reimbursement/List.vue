<template>
  <div class="list-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>报销{{ currentUser.roleId === 2 ? '审核' : '列表' }}</span>
        <el-button
          v-if="currentUser.roleId === 3"
          style="float: right"
          type="primary"
          size="small"
          @click="$router.push('/reimbursement/apply')"
        >申请报销</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item>
          <el-select v-model="queryForm.status" placeholder="审核状态" clearable>
            <el-option label="待审核" :value="0"></el-option>
            <el-option label="已通过" :value="1"></el-option>
            <el-option label="已驳回" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="applicantName" label="申请人"></el-table-column>
        <el-table-column prop="typeName" label="报销类型"></el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="(currentUser.roleId === 2 || currentUser.roleId === 1) && scope.row.status === 0"
              type="text"
              size="small"
              @click="handleApprove(scope.row)"
            >审核</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDetail(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog title="报销审核" :visible.sync="approveDialog.visible" width="500px">
      <el-form :model="approveDialog.form" :rules="approveRules" ref="approveForm" label-width="80px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="approveDialog.form.status">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="comment">
          <el-input
            type="textarea"
            v-model="approveDialog.form.comment"
            :rows="3"
            placeholder="请输入审核意见"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approveDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitApprove">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="报销详情" :visible.sync="detailDialog.visible" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请人">{{ detailDialog.data.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="报销类型">{{ detailDialog.data.typeName }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ detailDialog.data.amount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detailDialog.data.status)">
            {{ getStatusText(detailDialog.data.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间" :span="2">{{ detailDialog.data.createTime }}</el-descriptions-item>
        <el-descriptions-item label="报销说明" :span="2">{{ detailDialog.data.description }}</el-descriptions-item>
        <el-descriptions-item label="凭证图片" :span="2">
          <div v-if="detailDialog.data.imageUrls">
            <el-image
              v-for="(url, index) in detailDialog.data.imageUrls.split(',')"
              :key="index"
              style="width: 100px; height: 100px; margin-right: 10px"
              :src="url"
              :preview-src-list="detailDialog.data.imageUrls.split(',')"
            ></el-image>
          </div>
          <span v-else>无</span>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 审批记录 -->
      <div v-if="detailDialog.approvals && detailDialog.approvals.length > 0" style="margin-top: 20px">
        <div style="font-weight: bold; margin-bottom: 10px">审批记录</div>
        <el-timeline>
          <el-timeline-item
            v-for="item in detailDialog.approvals"
            :key="item.id"
            :type="item.status === 1 ? 'success' : 'danger'"
            :timestamp="item.createTime"
          >
            <p>{{ item.approverName }} {{ item.status === 1 ? '通过' : '驳回' }}</p>
            <p v-if="item.comment" style="color: #666">{{ item.comment }}</p>
          </el-timeline-item>
        </el-timeline>
      </div>

      <div slot="footer" v-if="currentUser.roleId === 3 && detailDialog.data.status === 2">
        <el-button type="primary" @click="handleResubmit(detailDialog.data)">重新提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'ReimbursementList',
  data() {
    return {
      queryForm: {
        status: ''
      },
      list: [],
      approveDialog: {
        visible: false,
        form: {
          id: null,
          status: 1,
          comment: ''
        }
      },
      detailDialog: {
        visible: false,
        data: {},
        approvals: []
      },
      approveRules: {
        status: [
          { required: true, message: '请选择审核结果', trigger: 'change' }
        ],
        comment: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['currentUser'])
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      const params = {
        status: this.queryForm.status,
        applicantId: this.currentUser.roleId === 3 ? this.currentUser.id : null
      }
      this.$http.get('/api/reimbursement/list', { params }).then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    resetQuery() {
      this.queryForm.status = ''
      this.getList()
    },
    getStatusType(status) {
      const types = {
        0: 'warning',
        1: 'success',
        2: 'danger'
      }
      return types[status]
    },
    getStatusText(status) {
      const texts = {
        0: '待审核',
        1: '已通过',
        2: '已驳回'
      }
      return texts[status]
    },
    handleApprove(row) {
      this.approveDialog.form = {
        id: row.id,
        status: 1,
        comment: ''
      }
      this.approveDialog.visible = true
    },
    submitApprove() {
      this.$refs.approveForm.validate(valid => {
        if (valid) {
          const { id, status, comment } = this.approveDialog.form
          this.$http.put(`/api/reimbursement/approve/${id}`, null, {
            params: { 
              status,
              comment,
              userId: this.currentUser.id
            }
          }).then(response => {
            if (response.data.code === 200) {
              this.$message.success('审核成功')
              this.approveDialog.visible = false
              this.getList()
            } else {
              this.$message.error(response.data.message)
            }
          })
        }
      })
    },
    handleDetail(row) {
      this.$http.get(`/api/reimbursement/detail/${row.id}`).then(response => {
        if (response.data.code === 200) {
          this.detailDialog.data = response.data.data
          this.$http.get(`/api/reimbursement/approval-comments/${row.id}`).then(response => {
            if (response.data.code === 200) {
              this.detailDialog.approvals = response.data.data
            }
          })
          this.detailDialog.visible = true
        }
      })
    },
    handleResubmit(row) {
      this.$confirm('确认重新提交该报销申请?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.put(`/api/reimbursement/resubmit/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('重新提交成功')
            this.detailDialog.visible = false
            this.getList()
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.list-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.el-image {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.el-timeline {
  margin: 0 20px;
}
</style> 