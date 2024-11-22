<template>
  <div>
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
        <br style="clear: both;">
        <li :class="activeComponent === 'cabinsCount' ? 'active' : ''"
            @click="changeActiveComponent('cabinsCount')">Pocet chat
        </li>
        <li :class="activeComponent === 'dailyVisits' ? 'active' : ''"
            @click="changeActiveComponent('dailyVisits')">Denna navstevnost
        </li>
        <li :class="activeComponent === 'dailyCapacity' ? 'active' : ''"
            @click="changeActiveComponent('dailyCapacity')">Denna kapacita
        </li>
        <li :class="activeComponent === 'occupancy' ? 'active' : ''"
            @click="changeActiveComponent('occupancy')">Obsadenost
        </li>
        <li :class="activeComponent === 'monthlyIncome' ? 'active' : ''"
            @click="changeActiveComponent('monthlyIncome')">Mesacny vynos
        </li>
        <li :class="activeComponent === 'pricePerNight' ? 'active' : ''"
            @click="changeActiveComponent('pricePerNight')">Cena za osobu/noc
        </li>
        <br style="clear: both;">
        <li :class="activeComponent === 'occupancyPerCabinSize' ? 'active' : ''"
            @click="changeActiveComponent('occupancyPerCabinSize')">Obsadenost podla poctu izieb
        </li>
        <li :class="activeComponent === 'occupancyPerCabinRegularSleepingBeds' ? 'active' : ''"
            @click="changeActiveComponent('occupancyPerCabinRegularSleepingBeds')">Obsadenost podla poctu posteli
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
      <select v-if="componentsThatNeedGroupBy.includes(activeComponent)" id="desiredGroupedBySelect"
              v-model="desiredGroupBy" @change="updateTopCabins(this.cabins)">
        <option v-for="option in desiredGroupByOptions" :key="option" :value="option"
        >{{ option }}
        </option>
      </select>
      <br style="clear: both;">
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

      <SimpleTableView v-if="activeComponent === 'cabinsCount'"
                       :data="cabinsCount"
                       :keyName="desiredGroupBy"
                       :valueName="'Pocet'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'dailyVisits'"
                       :data="dailyVisits"
                       :keyName="desiredGroupBy"
                       :valueName="'Pocet navstevnikov'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'dailyCapacity'"
                       :data="dailyCapacity"
                       :keyName="desiredGroupBy"
                       :valueName="'Kapacita'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'occupancy'"
                       :data="occupancy"
                       :keyName="desiredGroupBy"
                       :valueName="'Obsadenost'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'monthlyIncome'"
                       :data="monthlyIncome"
                       :keyName="desiredGroupBy"
                       :valueName="'Suma'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'pricePerNight'"
                       :data="pricePerNight"
                       :keyName="desiredGroupBy"
                       :valueName="'Cena'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'occupancyPerCabinSize'"
                       :data="occupancyPerCabinSize"
                       :keyName="'Pocet izieb'"
                       :valueName="'Obsadenost'"></SimpleTableView>
      <SimpleTableView v-if="activeComponent === 'occupancyPerCabinRegularSleepingBeds'"
                       :data="occupancyPerCabinRegularSleepingBeds"
                       :keyName="'Pocet posteli'"
                       :valueName="'Obsadenost'"></SimpleTableView>
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
      componentsThatNeedGroupBy: ['cabinsCount', 'dailyVisits', 'dailyCapacity', 'occupancy', 'monthlyIncome', 'pricePerNight'],
      desiredGroupByOptions: ['Region', 'Okres', 'Lokalita'],
      desiredResultSize: 25,
      desiredGroupBy: 'Region',
      topCabinsByRating: [],
      topCabinsByReviews: [],
      topCabinsByPriceAsc: [],
      topCabinsByPriceDesc: [],
      topCabinsByMonthlyIncome: [],
      topCabinsByOccupancy: [],
      cabinsCount: [],
      dailyVisits: [],
      dailyCapacity: [],
      occupancy: [],
      monthlyIncome: [],
      pricePerNight: [],
      occupancyPerCabinSize: [],
      occupancyPerCabinRegularSleepingBeds: [],
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
      this.cabinsCount = Object.entries(cabins.reduce((acc, cabin) => {
        const key = this.getKey(this.desiredGroupBy, cabin);

        if (acc[key]) {
          acc[key] += 1;
        } else {
          acc[key] = 1;
        }
        return acc;
      }, {})).map(([key, value]) => ({key, value})).sort((a, b) => b.value - a.value).slice(0, this.desiredResultSize);
      this.dailyVisits = Object.entries(cabins.reduce((acc, cabin) => {
        if (cabin.occupancy === 0 || cabin.occupancy === undefined) {
          return acc;
        }
        const key = this.getKey(this.desiredGroupBy, cabin);

        if (acc[key]) {
          acc[key] += cabin.maxPerson * cabin.occupancy;
        } else {
          acc[key] = cabin.maxPerson * cabin.occupancy;
        }
        acc[key] = parseFloat(acc[key].toFixed(0));
        return acc;
      }, {})).map(([key, value]) => ({key, value})).sort((a, b) => b.value - a.value).slice(0, this.desiredResultSize);
      this.dailyCapacity = Object.entries(cabins.reduce((acc, cabin) => {
        const key = this.getKey(this.desiredGroupBy, cabin);

        if (acc[key]) {
          acc[key] += cabin.maxPerson;
        } else {
          acc[key] = cabin.maxPerson;
        }
        return acc;
      }, {})).map(([key, value]) => ({key, value})).sort((a, b) => b.value - a.value).slice(0, this.desiredResultSize);
      this.occupancy = Object.entries(cabins.reduce((acc, cabin) => {
        const key = this.getKey(this.desiredGroupBy, cabin);

        if (cabin.occupancy === 0 || cabin.occupancy === undefined) {
          return acc;
        }
        acc[key] = acc[key] ? {
          totalOccupancy: acc[key].totalOccupancy + cabin.occupancy,
          count: acc[key].count + 1
        } : {totalOccupancy: cabin.occupancy, count: 1};
        return acc;
      }, {}))
          .map(([key, value]) => ({key, value: (value.totalOccupancy / value.count * 100).toFixed(2)}))
          .sort((a, b) => b.value - a.value)
          .map(({key, value}) => ({key, value: value + '%'}))
          .slice(0, this.desiredResultSize);
      this.monthlyIncome = Object.entries(cabins.reduce((acc, cabin) => {
        const key = this.getKey(this.desiredGroupBy, cabin);
        if (cabin.occupancy === 0 || cabin.occupancy === undefined || cabin.avgPricePerNight === undefined ||
            cabin.avgPricePerNight === 0) {
          return acc;
        }

        acc[key] = acc[key] ? {
          monthlyIncome: acc[key].monthlyIncome + (cabin.avgPricePerNight * cabin.occupancy * 30),
          count: acc[key].count + 1
        } : {monthlyIncome: (cabin.avgPricePerNight * cabin.occupancy * 30), count: 1};
        return acc;
      }, {}))
          .map(([key, value]) => ({key, value: (value.monthlyIncome / value.count).toFixed(2)}))
          .sort((a, b) => b.value - a.value)
          .map(({key, value}) => ({key, value: value + '€'}))
          .slice(0, this.desiredResultSize);
      this.pricePerNight = Object.entries(cabins.reduce((acc, cabin) => {
        const key = this.getKey(this.desiredGroupBy, cabin);
        if (cabin.avgPricePerNight === undefined || cabin.avgPricePerNight === 0) {
          return acc;
        }

        acc[key] = acc[key] ? {
          pricePerNight: acc[key].pricePerNight + (cabin.avgPricePerNight / cabin.maxPerson),
          count: acc[key].count + 1
        } : {pricePerNight: cabin.avgPricePerNight / cabin.maxPerson, count: 1};
        return acc;
      }, {}))
          .map(([key, value]) => ({key, value: (value.pricePerNight / value.count).toFixed(2)}))
          .sort((a, b) => b.value - a.value)
          .map(({key, value}) => ({key, value: value + '€'}))
          .slice(0, this.desiredResultSize);
      this.occupancyPerCabinSize = Object.entries(cabins.reduce((acc, cabin) => {
        const key = cabin.bedroomsCount;

        if (cabin.occupancy === 0 || cabin.occupancy === undefined) {
          return acc;
        }

        acc[key] = acc[key] ? {
          occupancy: acc[key].occupancy + cabin.occupancy,
          count: acc[key].count + 1
        } : {occupancy: cabin.occupancy, count: 1};
        return acc;
      }, {}))
          .map(([key, value]) => ({key, value: (value.occupancy / value.count * 100).toFixed(2)}))
          .sort((a, b) => b.value - a.value)
          .map(({key, value}) => ({key, value: value + '%'}))
          .slice(0, this.desiredResultSize);
      this.occupancyPerCabinRegularSleepingBeds = Object.entries(cabins.reduce((acc, cabin) => {
        const key = cabin.regularSleepingBeds;

        if (cabin.occupancy === 0 || cabin.occupancy === undefined) {
          return acc;
        }

        acc[key] = acc[key] ? {
          occupancy: acc[key].occupancy + cabin.occupancy,
          count: acc[key].count + 1
        } : {occupancy: cabin.occupancy, count: 1};
        return acc;
      }, {}))
          .map(([key, value]) => ({key, value: (value.occupancy / value.count * 100).toFixed(2)}))
          .sort((a, b) => b.value - a.value)
          .map(({key, value}) => ({key, value: value + '%'}))
          .slice(0, this.desiredResultSize);
    },
    getKey(desiredGroupBy, cabin) {
      switch (desiredGroupBy) {
        case 'Region':
          return cabin.region;
        case 'Okres':
          return cabin.district;
        case 'Lokalita':
          return cabin.locality;
        default:
          return null;
      }
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
  padding: 5px 10px;
  border-left: 0;
  width: calc(25% - 30px);
  margin-right: 8px;
  border-radius: 5px;
  border-left: 1px solid black;
  margin-top: 10px;
  border-top-left-radius: 0;
  border-bottom-right-radius: 0;
}

ul li:first-child {
  /*border-left: 1px solid black;*/
}

ul li.active {
  background-color: darkcyan;
  color: white;
}

#desiredResultSelect, #desiredGroupedBySelect {
  margin-bottom: 10px;
  padding: 5px;
  border-radius: 5px;
  border: 1px solid black;
  width: 100px;
  margin-left: 10px;
  margin-right: 10px;
  float: right;
}

.main-analysis-content {
  margin-top: 15px;
  margin-bottom: 30px;
}

</style>