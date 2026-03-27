<template>
  <div class="question-manage">
    <!-- 搜索栏 -->
    <el-card>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="题目标题" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.category" placeholder="请选择" clearable>
            <el-option v-for="cat in categories" :key="cat" :label="cat" :value="cat" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="searchForm.difficulty" placeholder="请选择" clearable>
            <el-option label="简单" :value="1" />
            <el-option label="中等" :value="2" />
            <el-option label="困难" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">添加题目</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card style="margin-top: 20px">
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="题目标题" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column prop="type" label="类型" width="80">
          <template #default="{ row }">
            <el-tag>{{ typeMap[row.type] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="80">
          <template #default="{ row }">
            <el-tag :type="difficultyType[row.difficulty]">{{ difficultyMap[row.difficulty] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="submitCount" label="提交次数" width="90" />
        <el-table-column prop="correctRate" label="正确率" width="80">
          <template #default="{ row }">
            {{ row.correctRate ? row.correctRate + '%' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: right"
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑题目' : '添加题目'" width="700px">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="题目标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入题目标题" />
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入题目内容" />
        </el-form-item>
        <el-form-item label="题目类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="choice">选择题</el-radio>
            <el-radio label="code">编程题</el-radio>
            <el-radio label="answer">问答题</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择题选项" v-if="form.type === 'choice'">
          <div v-for="(opt, idx) in form.options" :key="idx" style="display: flex; margin-bottom: 10px">
            <el-input v-model="opt.label" placeholder="标签" style="width: 60px" />
            <el-input v-model="opt.content" placeholder="选项内容" style="flex: 1; margin-left: 10px" />
            <el-button type="danger" link @click="removeOption(idx)" v-if="form.options.length > 2">删除</el-button>
          </div>
          <el-button type="primary" link @click="addOption">+ 添加选项</el-button>
        </el-form-item>
        <el-form-item label="正确答案" prop="answer">
          <el-input v-model="form.answer" placeholder="请输入正确答案" />
        </el-form-item>
        <el-form-item label="答案解析">
          <el-input v-model="form.analysis" type="textarea" :rows="2" placeholder="请输入答案解析" />
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-radio-group v-model="form.difficulty">
            <el-radio :label="1">简单</el-radio>
            <el-radio :label="2">中等</el-radio>
            <el-radio :label="3">困难</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择">
            <el-option v-for="cat in categories" :key="cat" :label="cat" :value="cat" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="form.tags" placeholder="多个标签用逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { questionApi } from '../api/index'

const categories = ['数组', '字符串', '链表', '树', '动态规划', '贪心算法', '回溯算法', '分治算法']
const typeMap = { choice: '选择题', code: '编程题', answer: '问答题' }
const difficultyMap = { 1: '简单', 2: '中等', 3: '困难' }
const difficultyType = { 1: 'success', 2: 'warning', 3: 'danger' }

const loading = ref(false)
const tableData = ref([])
const searchForm = reactive({ keyword: '', category: '', difficulty: '' })
const pagination = reactive({ page: 1, size: 20, total: 0 })

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const form = reactive({
  id: null,
  title: '',
  content: '',
  type: 'choice',
  options: [{ label: 'A', content: '' }, { label: 'B', content: '' }],
  answer: '',
  analysis: '',
  difficulty: 1,
  category: '',
  tags: ''
})

const formRules = {
  title: [{ required: true, message: '请输入题目标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
  answer: [{ required: true, message: '请输入正确答案', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await questionApi.list({ ...searchForm, page: pagination.page, size: pagination.size })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, { keyword: '', category: '', difficulty: '' })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null, title: '', content: '', type: 'choice',
    options: [{ label: 'A', content: '' }, { label: 'B', content: '' }],
    answer: '', analysis: '', difficulty: 1, category: '', tags: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, {
    id: row.id, title: row.title, content: row.content, type: row.type,
    options: row.options ? JSON.parse(row.options) : [{ label: 'A', content: '' }, { label: 'B', content: '' }],
    answer: row.answer, analysis: row.analysis, difficulty: row.difficulty,
    category: row.category, tags: row.tags
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该题目吗?', '提示', { type: 'warning' }).then(async () => {
    await questionApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const addOption = () => {
  const labels = ['A', 'B', 'C', 'D', 'E', 'F']
  if (form.options.length < labels.length) {
    form.options.push({ label: labels[form.options.length], content: '' })
  }
}

const removeOption = (idx) => {
  form.options.splice(idx, 1)
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await questionApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await questionApi.add(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

onMounted(loadData)
</script>