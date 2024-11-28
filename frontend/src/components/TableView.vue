<template>
  <div>
    <div class="top-cabin">
      <table>
        <tr>
          <th class="fix-name-column">Nazov</th>
          <th>Rating</th>
          <th>Hodnotenia</th>
          <th class="fix-region-column">Kraj</th>
          <th>Okres</th>
          <th>Lokalita</th>
          <th>Cena za noc</th>
          <th>Obsadenost</th>
          <th>Mesacny prijem</th>
        </tr>
        <tr v-for="cabin in data" :key="cabin.name">
          <td class="fix-name-column">
            <a :href="'https://megaubytovanie.sk/'+cabin.urlFragment" style="color: black;"
               target="_blank">{{ cabin.name }}</a>
          </td>
          <td>{{ cabin.rating }}</td>
          <td>{{ cabin.reviewsCount }}</td>
          <td>{{ cabin.region }}</td>
          <td>{{ cabin.district }}</td>
          <td>{{ cabin.locality }}</td>
          <td>{{ cabin.avgPricePerNight }}€</td>
          <td>{{ cabin.occupancy != null ? (cabin.occupancy * 100).toFixed(2) + '%' : '-' }}</td>
          <td>{{
              cabin.occupancy != null ? (cabin.avgPricePerNight * cabin.occupancy * 30).toFixed(2) + '€' : '-'
            }}
          </td>
        </tr>
      </table>
    </div>
    <hr style="clear: both;"/>
  </div>
</template>

<script>
export default {
  name: 'TableView',
  props: {
    data: Array,
  }
}
</script>

<style scoped>

.top-cabin {
  text-align: left;
  float: left;
  /*width: 100%;*/
}

.top-cabin table {
  /*width: 100%;*/
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
  width: 200px;
  max-width: 200px;
  min-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.fix-region-column {
  width: 125px;
  max-width: 125px;
  min-width: 125px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

</style>