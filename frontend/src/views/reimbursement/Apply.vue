<template>
  <div class="apply-container">
    <el-card>
      <div slot="header">
        <span>报销申请</span>
      </div>
      
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="报销类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择报销类型" style="width: 100%">
            <el-option
              v-for="item in typeOptions"
              :key="item.id"
              :label="item.typeName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="报销金额" prop="amount">
          <el-input-number
            v-model="form.amount"
            :precision="2"
            :step="0.01"
            :min="0"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="报销说明" prop="description">
          <el-input
            type="textarea"
            v-model="form.description"
            :rows="4"
            placeholder="请输入报销说明"
          ></el-input>
        </el-form-item>

        <el-form-item label="凭证图片" prop="imageUrls">
          <el-upload
            class="upload-demo"
            action="http://localhost:8080/api/file/upload"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :file-list="fileList"
            list-type="picture"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交申请</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'ReimbursementApply',
  data() {
    return {
      form: {
        typeId: '',
        amount: 0,
        description: '',
        imageUrls: ''
      },
      rules: {
        typeId: [
          { required: true, message: '请选择报销类型', trigger: 'change' }
        ],
        amount: [
          { required: true, message: '请输入报销金额', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入报销说明', trigger: 'blur' }
        ]
      },
      typeOptions: [],
      fileList: []
    }
  },
  computed: {
    ...mapGetters(['currentUser'])
  },
  created() {
    this.getTypeOptions()
  },
  methods: {
    getTypeOptions() {
      this.$http.get('/api/reimbursement/type/list').then(response => {
        if (response.data.code === 200) {
          this.typeOptions = response.data.data
        }
      })
    },
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.fileList = fileList
        const urls = fileList.map(file => file.response.data).join(',')
        this.form.imageUrls = urls
      }
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
      const urls = fileList.map(file => file.response.data).join(',')
      this.form.imageUrls = urls
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.form.applicantId = this.currentUser.id
          this.$http.post('/api/reimbursement/apply', this.form)
            .then(response => {
              if (response.data.code === 200) {
                this.$message.success('申请提交成功')
                this.$router.push('/reimbursement')
              } else {
                this.$message.error(response.data.message)
              }
            })
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.fileList = []
      this.form.imageUrls = ''
    }
  }
}
</script>

<style scoped>
.apply-container {
  padding: 20px;
}

.el-input-number {
  width: 100%;
}
</style> 