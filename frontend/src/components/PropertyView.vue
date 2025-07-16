<template>
  <div>
    <div class="top-cabin">
      <table>
        <tr>
          <th class="fix-name-column">Nazov</th>
          <th class="fix-region-column">Typ</th>
          <th class="fix-region-column">Kraj</th>
          <th class="fix-region-column">Okres</th>
          <th class="fix-region-column">Lokalita</th>
          <th class="fix-region-column">Pozemok</th>
          <th class="fix-region-column">Zastavana plocha</th>
          <th class="fix-region-column">Uzitkova plocha</th>
          <th class="fix-region-column">Cena</th>
        </tr>
        <tr v-for="cabin in data" :key="cabin.title">
          <td class="fix-name-column">
            <a :href="cabin.link"
               style="color: black;"
               target="_blank"><img src="/external.png"
                                    width="15px"></a> |
            <span class="showcabinp" @click="showProperty(cabin)">
              {{ cabin.title }}
            </span>
          </td>
          <td>{{ cabin.category }}</td>
          <td class="fix-name-column-must">{{ cabin.region }} <span v-if="cabin.regionPriceToAverageDifference != null" :class="getClass(cabin.regionPriceToAverageDifference)">{{ cabin.regionPriceToAverageDifference }}%</span></td>
          <td class="fix-name-column-must">{{ cabin.district }} <span v-if="cabin.districtPriceToAverageDifference != null" :class="getClass(cabin.districtPriceToAverageDifference)">{{ cabin.districtPriceToAverageDifference }}%</span></td>
          <td class="fix-name-column-must">{{ cabin.locality }} <span v-if="cabin.localityPriceToAverageDifference != null" :class="getClass(cabin.localityPriceToAverageDifference)">{{ cabin.localityPriceToAverageDifference }}%</span></td>
          <td>{{ cabin.estate }}m²</td>
          <td>{{ cabin.houseEstate }}m²</td>
          <td>{{ cabin.floorEstate }}m²</td>
          <td style="text-align: right">{{ cabin.price.toLocaleString('fr-FR') }}€</td>
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
  },
  methods: {
    getClass(value) {
      let number = Number(value);
      if (number > 0) {
        return 'red-span';
      } else if (number < 0) {
        return 'green-span';
      }
      return 'neutral-span';
    },
    showProperty(property) {
      this.$emit('on-property-click', property);
    }
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
  width: calc(100% - 10px);
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

.fix-name-column-must {
  width: 250px;
  max-width: 250px;
  min-width: 250px;
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

.showcabinp {
  cursor: pointer;
}

.showcabinp:hover {
  text-decoration: underline;
}

.red-span {
  background-color: red;
  color: #fff;
  font-size: 15px;
  padding: 2px 5px;
  border-radius: 5px;
}

.green-span {
  background-color: green;
  color: #fff;
  font-size: 15px;
  padding: 2px 5px;
  border-radius: 5px;
}

.neutral-span {
  background-color: #808080;
  color: #fff;
  font-size: 15px;
  padding: 2px 5px;
  border-radius: 5px;
}

</style>