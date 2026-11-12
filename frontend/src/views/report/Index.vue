<template>
  <div class="report-container">
    <!-- 总览数据 -->
    <el-row :gutter="20" class="overview">
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

    <!-- 报销统计 -->
    <el-row :gutter="20" class="detail-cards">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header">
            <span>报销申请统计</span>
          </div>
          <el-table :data="reimbursementStats" border style="width: 100%">
            <el-table-column prop="status" label="状态">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="count" label="数量"></el-table-column>
            <el-table-column prop="amount" label="金额">
              <template slot-scope="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header">
            <span>报销类型统计</span>
          </div>
          <el-table :data="typeStats" border style="width: 100%">
            <el-table-column prop="typeName" label="类型"></el-table-column>
            <el-table-column prop="count" label="数量"></el-table-column>
            <el-table-column prop="amount" label="金额">
              <template slot-scope="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 月度统计 -->
    <el-row class="detail-cards">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header">
            <span>月度收支统计</span>
            <el-select 
              v-model="monthlyQuery.year" 
              placeholder="选择年份" 
              style="width: 120px; float: right; margin-left: 10px"
              @change="getMonthlyStats"
            >
              <el-option
                v-for="year in years"
                :key="year"
                :label="year"
                :value="year"
              ></el-option>
            </el-select>
          </div>
          <el-table :data="monthlyStats" border style="width: 100%">
            <el-table-column prop="month" label="月份">
              <template slot-scope="scope">
                {{ scope.row.month }}月
              </template>
            </el-table-column>
            <el-table-column prop="income" label="收入">
              <template slot-scope="scope">
                <span class="income">¥{{ scope.row.income }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="expense" label="支出">
              <template slot-scope="scope">
                <span class="expense">¥{{ scope.row.expense }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="balance" label="结余">
              <template slot-scope="scope">
                <span :class="scope.row.balance >= 0 ? 'income' : 'expense'">
                  ¥{{ scope.row.balance }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Report',
  data() {
    return {
      statistics: {
        income: 0,
        expense: 0
      },
      reimbursementStats: [],
      typeStats: [],
      monthlyStats: [],
      monthlyQuery: {
        year: new Date().getFullYear()
      },
      years: []
    }
  },
  computed: {
    balance() {
      const income = parseFloat(this.statistics.income || 0)
      const expense = parseFloat(this.statistics.expense || 0)
      return (income - expense).toFixed(2)
    }
  },
  created() {
    this.initYears()
    this.getStatistics()
    this.getReimbursementStats()
    this.getTypeStats()
    this.getMonthlyStats()
  },
  methods: {
    initYears() {
      const currentYear = new Date().getFullYear()
      this.years = [currentYear - 1, currentYear, currentYear + 1]
    },
    getStatistics() {
      this.$http.get('/api/voucher/statistics').then(response => {
        if (response.data.code === 200) {
          this.statistics = response.data.data
        }
      })
    },
    getReimbursementStats() {
      this.$http.get('/api/report/reimbursement-stats').then(response => {
        if (response.data.code === 200) {
          this.reimbursementStats = response.data.data
        }
      })
    },
    getTypeStats() {
      this.$http.get('/api/report/type-stats').then(response => {
        if (response.data.code === 200) {
          this.typeStats = response.data.data
        }
      })
    },
    getMonthlyStats() {
      this.$http.get('/api/report/monthly-stats', {
        params: this.monthlyQuery
      }).then(response => {
        if (response.data.code === 200) {
          this.monthlyStats = response.data.data
        }
      })
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
    }
  }
}
</script>

<style scoped>
.report-container {
  padding: 20px;
}

.overview {
  margin-bottom: 20px;
}

.detail-cards {
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

.box-card {
  margin-bottom: 20px;
}
</style> 