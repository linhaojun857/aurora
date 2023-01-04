<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- <div class="operation-container">
      <el-input clearable v-model="keywords" prefix-icon="el-icon-search" size="small" placeholder="请输入预约人"
        style="width: 200px;margin-left: auto;" @keyup.enter.native="searchOrder" />
      <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchOrder">
        搜索
      </el-button>
    </div> -->
    <el-table border v-loading="loading" :data="data">
      <el-table-column v-for="col of cols" :key="col.prop" :prop="col.prop" :label="col.label"></el-table-column>
    </el-table>
    <el-pagination class="pagination-container" background @size-change="sizeChange" @current-change="currentChange"
      :current-page="current" :page-size="size" :total="count" :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper" />
  </el-card>
</template>

<script>
export default {
  name: "fronted-order",
  data() {
    return {
      loading: false,
      keywords: '',
      data: [],
      count: 0,
      current: 1,
      size: 10,
      cols: [
        {
          prop: 'id',
          label: '预约编号'
        },
        {
          prop: 'reservationName',
          label: '预约人'
        },
        {
          prop: 'number',
          label: '预约人数'
        },
        {
          prop: 'reservationTime',
          label: '预约时间'
        },
        {
          prop: 'note',
          label: '备注'
        }
      ]
    }
  },
  methods: {
    searchOrder() { },
    async syncOrderList() {
      this.loading = true
      try {
        const res = await this.axios.post('/api/admin/reservation/search', {
          current: this.current,
          size: this.size
        })
        // console.log(res)
        this.data = res.data.data.records
        this.count = res.data.data.count
      } finally {
        this.loading = false
      }
    },
    async sizeChange(size) {
      this.size = size
      await this.syncOrderList()
    },
    async currentChange(current) {
      this.current = current
      await this.syncOrderList()
    },
  },
  async mounted() {
    await this.syncOrderList()
  }
}
</script>

<style scoped>
.operation-container {
  margin-top: 1.5rem;
}
</style>
