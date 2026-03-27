<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409eff">📚</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalQuestions }}</div>
              <div class="stat-label">总题目数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67c23a">👥</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalUsers }}</div>
              <div class="stat-label">用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e6a23c">📝</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalSubmissions }}</div>
              <div class="stat-label">答题记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #f56c6c">📊</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.todaySubmissions }}</div>
              <div class="stat-label">今日答题</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>排行榜 (Top 10)</span>
          </template>
          <el-table :data="ranking" stripe>
            <el-table-column prop="rank" label="排名" width="60" />
            <el-table-column prop="nickname" label="用户" />
            <el-table-column prop="correctCount" label="正确数" width="80" />
            <el-table-column prop="correctRate" label="正确率" width="80">
              <template #default="{ row }">
                {{ row.correctRate }}%
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近答题记录</span>
          </template>
          <el-table :data="recentSubmissions" stripe>
            <el-table-column prop="user.nickname" label="用户" />
            <el-table-column prop="question.title" label="题目" :show-overflow-tooltip="true" />
            <el-table-column prop="isCorrect" label="结果" width="80">
              <template #default="{ row }">
                <el-tag :type="row.isCorrect ? 'success' : 'danger'">
                  {{ row.isCorrect ? '正确' : '错误' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="submitTime" label="时间" width="160" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { statApi } from '../api/index'

const stats = ref({
  totalQuestions: 0,
  totalUsers: 0,
  totalSubmissions: 0,
  todaySubmissions: 0
})

const ranking = ref([])
const recentSubmissions = ref([])

const loadData = async () => {
  try {
    const overview = await statApi.overview()
    stats.value = overview.data

    const rankRes = await statApi.ranking({ limit: 10 })
    ranking.value = rankRes.data
  } catch (e) {
    console.error(e)
  }
}

onMounted(loadData)
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
}
.stat-label {
  color: #999;
  font-size: 14px;
}
</style>