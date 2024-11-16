<template>
  <div v-if="cabins.length > 0">
    <div>
      <ul>
        <li :class="activeComponent === 'topCabinsByRating' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByRating')">Top podla ratingu
        </li>
        <li :class="activeComponent === 'topCabinsByReviews' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByReviews')">Top podla poctu hodnoteni
        </li>
        <li :class="activeComponent === 'topCabinsByPriceAsc' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByPriceAsc')">Top podla ceny za noc vzostupne
        </li>
        <li :class="activeComponent === 'topCabinsByPriceDesc' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByPriceDesc')">Top podla ceny za noc zostupne
        </li>
        <li :class="activeComponent === 'topCabinsByMonthlyIncome' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByMonthlyIncome')">Top podla prijmu za mesiac
        </li>
        <li :class="activeComponent === 'topCabinsByOccupancy' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByOccupancy')">Top podla obsadenosti
        </li>
        <li :class="activeComponent === 'cabinsGroupedByRegion' ? 'active' : ''"
            @click="changeActiveComponent('cabinsGroupedByRegion')">Pocet chat podla kraju
        </li>
        <li :class="activeComponent === 'cabinsGroupedByDistrict' ? 'active' : ''"
            @click="changeActiveComponent('cabinsGroupedByDistrict')">Pocet chat podla okresu
        </li>
        <li :class="activeComponent === 'cabinsGroupedByLocality' ? 'active' : ''"
            @click="changeActiveComponent('cabinsGroupedByLocality')">Pocet chat podla lokality
        </li>
        <br style="clear: both;">
      </ul>
    </div>
    <div class="main-analysis-content">
      <select id="desiredResultSelect" v-model="desiredResultSize" @change="updateTopCabins(this.cabins)">
        <option v-for="option in desiredResultSizeOptions" :key="option" :value="option"
        >{{ option }}
        </option>
      </select>
      <TableView v-if="topCabinsByRating.length > 0 && activeComponent === 'topCabinsByRating'"
                 :data="topCabinsByRating"></TableView>
      <TableView v-if="topCabinsByReviews.length > 0 && activeComponent === 'topCabinsByReviews'"
                 :data="topCabinsByReviews"></TableView>
      <TableView v-if="topCabinsByPriceAsc.length > 0 && activeComponent === 'topCabinsByPriceAsc'"
                 :data="topCabinsByPriceAsc"></TableView>
      <TableView v-if="topCabinsByPriceDesc.length > 0 && activeComponent === 'topCabinsByPriceDesc'"
                 :data="topCabinsByPriceDesc"></TableView>
      <TableView v-if="topCabinsByMonthlyIncome.length > 0 && activeComponent === 'topCabinsByMonthlyIncome'"
                 :data="topCabinsByMonthlyIncome"></TableView>
      <TableView v-if="topCabinsByOccupancy.length > 0 && activeComponent === 'topCabinsByOccupancy'"
                 :data="topCabinsByOccupancy"></TableView>
      <SimpleTableView v-if="activeComponent === 'cabinsGroupedByRegion'"
                       :data="cabinsGroupedByRegion"
                       :keyName="'Region'"
                       :valueName="'Pocet'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'cabinsGroupedByDistrict'"
                       :data="cabinsGroupedByDistrict"
                       :keyName="'Okres'"
                       :valueName="'Pocet'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'cabinsGroupedByLocality'"
                       :data="cabinsGroupedByLocality"
                       :keyName="'Lokalita'"
                       :valueName="'Pocet'"></SimpleTableView>
    </div>
  </div>
</template>

<script>

import client from "@/services/client";
import TableView from "@/components/TableView.vue";
import SimpleTableView from "@/components/SimpleTableView.vue";

export default {
  name: 'MainAnalysis',
  components: {TableView, SimpleTableView},
  data() {
    return {
      activeComponent: 'topCabinsByRating',
      cabins: [],
      desiredResultSizeOptions: [5, 10, 25, 50, 100],
      desiredResultSize: 25,
      topCabinsByRating: [],
      topCabinsByReviews: [],
      topCabinsByPriceAsc: [],
      topCabinsByPriceDesc: [],
      topCabinsByMonthlyIncome: [],
      topCabinsByOccupancy: [],
      cabinsGroupedByRegion: [],
      cabinsGroupedByDistrict: [],
      cabinsGroupedByLocality: [],
    }
  },
  methods: {
    changeActiveComponent(name) {
      this.activeComponent = name;
    },
    updateTopCabins(cabins) {
      this.cabins = cabins;
      this.topCabinsByRating = cabins.sort((a, b) => b.rating - a.rating).slice(0, this.desiredResultSize);
      this.topCabinsByReviews = cabins.sort((a, b) => b.reviewsCount - a.reviewsCount).slice(0, this.desiredResultSize);
      this.topCabinsByPriceAsc = cabins.sort((a, b) => a.avgPricePerNight - b.avgPricePerNight).slice(0, this.desiredResultSize);
      this.topCabinsByPriceDesc = cabins.sort((a, b) => b.avgPricePerNight - a.avgPricePerNight).slice(0, this.desiredResultSize);
      this.topCabinsByMonthlyIncome = cabins.sort((a, b) => (b.avgPricePerNight * b.occupancy * 30) - (a.avgPricePerNight * a.occupancy * 30)).slice(0, this.desiredResultSize);
      this.topCabinsByOccupancy = cabins.sort((a, b) => b.occupancy - a.occupancy).slice(0, this.desiredResultSize);
      this.cabinsGroupedByRegion = Object.entries(cabins.reduce((acc, cabin) => {
        if (acc[cabin.region]) {
          acc[cabin.region] += 1;
        } else {
          acc[cabin.region] = 1;
        }
        return acc;
      }, {})).map(([key, value]) => ({key, value})).sort((a, b) => b.value - a.value).slice(0, this.desiredResultSize);
      this.cabinsGroupedByDistrict = Object.entries(cabins.reduce((acc, cabin) => {
        if (acc[cabin.district]) {
          acc[cabin.district] += 1;
        } else {
          acc[cabin.district] = 1;
        }
        return acc;
      }, {})).map(([key, value]) => ({key, value})).sort((a, b) => b.value - a.value).slice(0, this.desiredResultSize);
      this.cabinsGroupedByLocality = Object.entries(cabins.reduce((acc, cabin) => {
        if (acc[cabin.locality]) {
          acc[cabin.locality] += 1;
        } else {
          acc[cabin.locality] = 1;
        }
        return acc;
      }, {})).map(([key, value]) => ({key, value})).sort((a, b) => b.value - a.value).slice(0, this.desiredResultSize);
    },
    filtersChanged(
        region,
        district,
        locality,
        rating,
        review,
        price,
        occupancy
    ) {
      client.get(`/cabins?region=${region}&district=${district}&locality=${locality}&rating=${rating}&reviews=${review}&averagePricePerNight=${price}&occupancy=${occupancy}`)
          .then(response => {
            this.updateTopCabins(response.data);
          })
          .catch(error => {
            console.log(error);
          });
    }
  }
}

</script>

<style scoped>

ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  text-align: left;
}

ul li {
  display: block;
  float: left;
  cursor: pointer;
  border: 1px solid black;
  padding: 7px;
  border-left: 0;
  width: calc(25% - 24px);
  margin-right: 8px;
  border-radius: 5px;
  height: 35px;
  border-left: 1px solid black;
  margin-top: 10px;
}

ul li:first-child {
  /*border-left: 1px solid black;*/
}

ul li.active {
  background-color: darkcyan;
  color: white;
}

#desiredResultSelect {
  margin-bottom: 10px;
  padding: 5px;
  border-radius: 5px;
  border: 1px solid black;
  width: 100px;
  margin-left: 10px;
  margin-right: 10px;
  float: right;
}

</style>