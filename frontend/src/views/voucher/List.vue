<template>
  <div class="voucher-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>凭证管理</span>
        <el-button
          style="float: right"
          type="primary"
          size="small"
          @click="handleAdd"
        >添加凭证</el-button>
      </div>

      <!-- 统计信息 -->
      <el-row :gutter="20" class="statistics">
        <el-col :span="8">
          <el-card shadow="hover">
            <div class="statistic-item">
              <div class="label">总收入</div>
              <div class="value income">¥{{ statistics.income || '0.00' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div class="statistic-item">
              <div class="label">总支出</div>
              <div class="value expense">¥{{ statistics.expense || '0.00' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div class="statistic-item">
              <div class="label">结余</div>
              <div class="value" :class="balance >= 0 ? 'income' : 'expense'">
                ¥{{ balance }}
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item>
          <el-select v-model="queryForm.type" placeholder="凭证类型" clearable>
            <el-option label="收入" :value="1"></el-option>
            <el-option label="支出" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.voucherNo"
            placeholder="凭证编号"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 凭证列表 -->
      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="voucherNo" label="凭证编号" width="150"></el-table-column>
        <el-table-column prop="typeText" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.type === 1 ? 'success' : 'danger'">
              {{ scope.row.typeText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">
            <span :class="scope.row.type === 1 ? 'income' : 'expense'">
              ¥{{ scope.row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" show-overflow-tooltip></el-table-column>
        <el-table-column prop="operatorName" label="操作人"></el-table-column>
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
        <el-form-item label="凭证编号" prop="voucherNo" v-if="dialog.type === 'add'">
          <el-input v-model="dialog.form.voucherNo" placeholder="请输入凭证编号"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="dialog.form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="收入" :value="1"></el-option>
            <el-option label="支出" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number
            v-model="dialog.form.amount"
            :precision="2"
            :step="0.01"
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
import { mapGetters } from 'vuex'

export default {
  name: 'VoucherList',
  data() {
    return {
      queryForm: {
        type: '',
        voucherNo: ''
      },
      list: [],
      statistics: {
        income: 0,
        expense: 0
      },
      dialog: {
        visible: false,
        type: 'add', // add or edit
        form: {
          id: null,
          voucherNo: '',
          type: 1,
          amount: 0,
          description: '',
          operatorId: null
        }
      },
      rules: {
        voucherNo: [
          { required: true, message: '请输入凭证编号', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        amount: [
          { required: true, message: '请输入金额', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入说明', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['currentUser']),
    dialogTitle() {
      return this.dialog.type === 'add' ? '添加凭证' : '编辑凭证'
    },
    balance() {
      const income = parseFloat(this.statistics.income || 0)
      const expense = parseFloat(this.statistics.expense || 0)
      return (income - expense).toFixed(2)
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    getList() {
      this.$http.get('/api/voucher/list', { params: this.queryForm }).then(response => {
        if (response.data.code === 200) {
          this.list = response.data.data
        }
      })
    },
    getStatistics() {
      this.$http.get('/api/voucher/statistics').then(response => {
        if (response.data.code === 200) {
          this.statistics = response.data.data
        }
      })
    },
    resetQuery() {
      this.queryForm = {
        type: '',
        voucherNo: ''
      }
      this.getList()
    },
    handleAdd() {
      this.dialog.type = 'add'
      this.dialog.form = {
        id: null,
        voucherNo: new Date().getTime().toString(), // 使用时间戳作为默认凭证编号
        type: 1,
        amount: 0,
        description: '',
        operatorId: this.currentUser.id
      }
      this.dialog.visible = true
    },
    handleEdit(row) {
      this.dialog.type = 'edit'
      this.dialog.form = { ...row }
      this.dialog.visible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该凭证?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/voucher/delete/${row.id}`).then(response => {
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
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
          this.$http.post(`/api/voucher/${action}`, this.dialog.form)
            .then(response => {
              if (response.data.code === 200) {
                this.$message.success(this.dialog.type === 'add' ? '添加成功' : '更新成功')
                this.dialog.visible = false
                this.getList()
                this.getStatistics()
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
.voucher-container {
  padding: 20px;
}

.statistics {
  margin-bottom: 20px;
}

.statistic-item {
  text-align: center;
}

.statistic-item .label {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.statistic-item .value {
  font-size: 24px;
  font-weight: bold;
}

.income {
  color: #67C23A;
}

.expense {
  color: #F56C6C;
}

.search-form {
  margin-bottom: 20px;
}

.el-input-number {
  width: 100%;
}
</style> 