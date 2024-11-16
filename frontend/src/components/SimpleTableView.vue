<template>
  <div>
    <div class="top-cabin">
      <table>
        <tr>
          <th class="fix-name-column">{{ keyName }}</th>
          <th>{{ valueName }}</th>
        </tr>
        <tr v-for="datapoint in data" :key="datapoint.key">
          <td>{{ datapoint.key }}</td>
          <td>{{ datapoint.value }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>

export default {
  name: 'SimpleTableView',
  props: {
    data: Array,
    keyName: String,
    valueName: String
  }
}
</script>

<style scoped>

.top-cabin {
  text-align: left;
  float: left;
  width: 100%;
}

.top-cabin table {
  width: 100%;
  border-collapse: collapse;
}

.top-cabin table tr:hover td {
  background-color: #f2f2f2;
}

.top-cabin table th {
  background-color: #f2f2f2;
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.top-cabin table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.fix-name-column {
  width: 300px;
  max-width: 300px;
  min-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

</style>