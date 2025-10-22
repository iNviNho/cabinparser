<template>
  <div>
    <div class="main-analysis-stats">
      <ul v-if="showCabinForRent">
        <h4 style="margin-bottom: 17px; margin-top: 40px;">Statistika podla chat</h4>
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
      </ul>
      <ul v-if="showCabinForRent">
        <h4 style="margin-bottom: 0;">Statistika podla krajov/okresov a miest</h4>
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
      </ul>
      <ul v-if="showCabinForRent">
        <h4 style="margin-bottom: 0;">Jednotliva statistika</h4>
        <li :class="activeComponent === 'occupancyPerCabinSize' ? 'active' : ''"
            @click="changeActiveComponent('occupancyPerCabinSize')">Obsadenost podla poctu izieb
        </li>
        <li :class="activeComponent === 'occupancyPerCabinRegularSleepingBeds' ? 'active' : ''"
            @click="changeActiveComponent('occupancyPerCabinRegularSleepingBeds')">Obsadenost podla poctu posteli
        </li>
      </ul>
      <ul v-if="showPropertiesForSale">
        <h4 style="margin-bottom: 17px; margin-top: 40px;">Statistika nehnutelnosti</h4>
        <li :class="activeComponent === 'topPropertyByPriceDesc' ? 'active' : ''"
            @click="changeActiveComponent('topPropertyByPriceDesc')">Top podla ceny zostupne
        </li>
        <li :class="activeComponent === 'topPropertyByPriceAsc' ? 'active' : ''"
            @click="changeActiveComponent('topPropertyByPriceAsc')">Top podla ceny vzostupne
        </li>

        <li :class="activeComponent === 'topPropertiesByRegionPriceDiffDesc' ? 'active' : ''"
            @click="changeActiveComponent('topPropertiesByRegionPriceDiffDesc')">Top podla ceny za m2 podla kraju
        </li>
        <li :class="activeComponent === 'topPropertiesByDistrictPriceDiffDesc' ? 'active' : ''"
            @click="changeActiveComponent('topPropertiesByDistrictPriceDiffDesc')">Top podla ceny za m2 podla okresu
        </li>
        <li :class="activeComponent === 'topPropertiesByLocalityPriceDiffDesc' ? 'active' : ''"
            @click="changeActiveComponent('topPropertiesByLocalityPriceDiffDesc')">Top podla ceny za m2 podla lokality
        </li>

        <br style="clear: both;">
      </ul>
    </div>
    <div class="main-analysis-content">
      <select id="desiredResultSelect" v-model="desiredResultSize" @change="updateTopCabins(this.cabins); updatePropertiesForSale(this.propertiesForSale)">
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
      <select v-if="componentsThatNeedGroupByMinCount.includes(activeComponent)" id="desiredMinCountOfCabinsSelect" v-model="desiredMinCount" @change="updateTopCabins(this.cabins); updatePropertiesForSale(this.propertiesForSale)">
        <option v-for="option in desiredMinCountOfCabinsOptions" :key="option" :value="option"
        >{{ option }}
        </option>
      </select>
      <br style="clear: both;">
      <PropertyView
        v-if="topPropertiesByPriceDesc.length > 0 && activeComponent === 'topPropertyByPriceDesc'"
        :data="topPropertiesByPriceDesc" @on-property-click="onPropertyClick"
      ></PropertyView>
      <PropertyView
        v-if="topPropertiesByPriceAsc.length > 0 && activeComponent === 'topPropertyByPriceAsc'"
        :data="topPropertiesByPriceAsc" @on-property-click="onPropertyClick"
      ></PropertyView>

      <PropertyView
        v-if="topPropertiesByRegionPriceDiffDesc.length > 0 && activeComponent === 'topPropertiesByRegionPriceDiffDesc'"
        :data="topPropertiesByRegionPriceDiffDesc" @on-property-click="onPropertyClick"
      ></PropertyView>
      <PropertyView
        v-if="topPropertiesByDistrictPriceDiffDesc.length > 0 && activeComponent === 'topPropertiesByDistrictPriceDiffDesc'"
        :data="topPropertiesByDistrictPriceDiffDesc" @on-property-click="onPropertyClick"
      ></PropertyView>
      <PropertyView
        v-if="topPropertiesByLocalityPriceDiffDesc.length > 0 && activeComponent === 'topPropertiesByLocalityPriceDiffDesc'"
        :data="topPropertiesByLocalityPriceDiffDesc" @on-property-click="onPropertyClick"
      ></PropertyView>

      <TableView v-if="topCabinsByRating.length > 0 && activeComponent === 'topCabinsByRating'"
                 :data="topCabinsByRating" @on-cabin-click="onCabinClick"></TableView>
      <TableView v-if="topCabinsByReviews.length > 0 && activeComponent === 'topCabinsByReviews'"
                 :data="topCabinsByReviews" @on-cabin-click="onCabinClick"></TableView>
      <TableView v-if="topCabinsByPriceAsc.length > 0 && activeComponent === 'topCabinsByPriceAsc'"
                 :data="topCabinsByPriceAsc" @on-cabin-click="onCabinClick"></TableView>
      <TableView v-if="topCabinsByPriceDesc.length > 0 && activeComponent === 'topCabinsByPriceDesc'"
                 :data="topCabinsByPriceDesc" @on-cabin-click="onCabinClick"></TableView>
      <TableView v-if="topCabinsByMonthlyIncome.length > 0 && activeComponent === 'topCabinsByMonthlyIncome'"
                 :data="topCabinsByMonthlyIncome" @on-cabin-click="onCabinClick"></TableView>
      <TableView v-if="topCabinsByOccupancy.length > 0 && activeComponent === 'topCabinsByOccupancy'"
                 :data="topCabinsByOccupancy" @on-cabin-click="onCabinClick"></TableView>

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
import PropertyView from "@/components/PropertyView.vue";
import SimpleTableView from "@/components/SimpleTableView.vue";

export default {
  name: 'MainAnalysis',
  components: {TableView, SimpleTableView, PropertyView},
  data() {
    return {
      activeComponent: 'topCabinsByRating',
      cabins: [],
      desiredResultSizeOptions: [5, 10, 25, 50, 100, 250],
      desiredMinCountOfCabinsOptions: [1, 2 , 4, 6, 8, 10, 20],
      componentsThatNeedGroupBy: ['cabinsCount', 'dailyVisits', 'dailyCapacity', 'occupancy', 'monthlyIncome', 'pricePerNight'],
      componentsThatNeedGroupByMinCount: ['occupancy', 'monthlyIncome', 'pricePerNight', 'occupancyPerCabinSize' , 'occupancyPerCabinSize', 'occupancyPerCabinRegularSleepingBeds'],
      desiredGroupByOptions: ['Region', 'Okres', 'Lokalita'],
      desiredMinCount: 10,
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
      showCabinForRent: true,
      showPropertiesForSale: true,
      propertiesForSale: [],
      topPropertiesByPriceDesc: [],
      topPropertiesByPriceAsc: [],
      topPropertiesByRegionPriceDiffDesc: [],
      topPropertiesByDistrictPriceDiffDesc: [],
      topPropertiesByLocalityPriceDiffDesc: [],
    }
  },
  methods: {
    changeActiveComponent(name) {
      this.activeComponent = name;
    },
    onCabinClick(cabin) {
      this.$emit('on-cabin-click', {cabin: cabin, cabinForRent: null});
    },
    onPropertyClick(property) {
      this.$emit('on-property-click', {cabin: null, cabinForRent: property});
    },
    updatePropertiesForSale(properties) {
      this.propertiesForSale = properties;
      this.topPropertiesByPriceDesc = properties.sort((a, b) => b.price - a.price).slice(0, this.desiredResultSize);
      this.topPropertiesByPriceAsc = properties.sort((a, b) => a.price - b.price).slice(0, this.desiredResultSize);
      this.topPropertiesByRegionPriceDiffDesc = properties.sort((a, b) => a.regionPriceToAverageDifference - b.regionPriceToAverageDifference).slice(0, this.desiredResultSize);
      this.topPropertiesByDistrictPriceDiffDesc = properties.sort((a, b) => a.districtPriceToAverageDifference - b.districtPriceToAverageDifference).slice(0, this.desiredResultSize);
      this.topPropertiesByLocalityPriceDiffDesc = properties.sort((a, b) => a.localityPriceToAverageDifference - b.localityPriceToAverageDifference).slice(0, this.desiredResultSize);
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
        .filter((value => {
          return value[1].count >= this.desiredMinCount;
        }))
        .map(([key, value]) => ({key, value: {value: (value.totalOccupancy / value.count * 100).toFixed(2), count: value.count}}))
        .sort((a, b) => b.value.value - a.value.value)
        .map(({key, value}) => ({key: key, value: value.value + '%' + " - [" + value.count + "chat]"}))
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
        .filter((value => {
          return value[1].count >= this.desiredMinCount;
        }))
        .map(([key, value]) => ({key, value: {value: (value.monthlyIncome / value.count).toFixed(2), count: value.count}}))
        .sort((a, b) => b.value.value - a.value.value)
        .map(({key, value}) => ({key: key, value: value.value + '€' + " - [" + value.count + "chat]"}))
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
        .filter((value => {
          return value[1].count >= this.desiredMinCount;
        }))
        .map(([key, value]) => ({key, value: {value: (value.pricePerNight / value.count).toFixed(2), count: value.count}}))
        .sort((a, b) => b.value.value - a.value.value)
        .map(({key, value}) => ({key: key, value: value.value + '€' + " - [" + value.count + "chat]"}))
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
        .filter((value => {
          return value[1].count >= this.desiredMinCount;
        }))
        .map(([key, value]) => ({key, value: {value: (value.occupancy / value.count * 100).toFixed(2), count: value.count}}))
        .sort((a, b) => b.value.value - a.value.value)
        .map(({key, value}) => ({key: key, value: value.value + '%' + " - [" + value.count + "chat]"}))
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
        .filter((value => {
          return value[1].count >= this.desiredMinCount;
        }))
        .map(([key, value]) => ({key, value: {value: (value.occupancy / value.count * 100).toFixed(2), count: value.count}}))
        .sort((a, b) => b.value.value - a.value.value)
        .map(({key, value}) => ({key: key, value: value.value + '%' + " - [" + value.count + "chat]"}))
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
        occupancy,
        attributes,
        star,
        numberOfRegularBeds,
        numberOfBedrooms,
        showCabinsForRent,
        showPropertiesForSale,
        propertyForSale,
    ) {
      this.showCabinForRent = showCabinsForRent;
      this.showPropertiesForSale = showPropertiesForSale;
      if (showCabinsForRent) {
        client.get(`/cabins?region=${region}&district=${district}&locality=${locality}&rating=${rating}&reviews=${review}&averagePricePerNight=${price}&occupancy=${occupancy}&attributes=${attributes}&star=${star}&numberOfRegularBeds=${numberOfRegularBeds}&numberOfBedrooms=${numberOfBedrooms}`)
            .then(response => {
              this.updateTopCabins(response.data);
            })
            .catch(error => {
              console.log(error);
            });
      } else {
        this.updateTopCabins([]);
      }
      if (showPropertiesForSale) {
        client.get(`/properties-for-sale?region=${region}&district=${district}&locality=${locality}&propertyForSale=${propertyForSale}`)
          .then(response => {
            this.updatePropertiesForSale(response.data);
          })
          .catch(error => {
            console.log(error);
          });
      } else {
        this.updatePropertiesForSale([]);
      }
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
  cursor: pointer;
  border-bottom: 1px solid black;
  padding: 5px 10px;
  border-left: 0;
  margin-top: 10px;
}

ul li:first-child {
  /*border-left: 1px solid black;*/
}

ul li.active {
  background-color: darkcyan;
  color: white;
}

#desiredResultSelect, #desiredGroupedBySelect, #desiredMinCountOfCabinsSelect {
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
  float: left;
  width: calc(100% - 220px);
  margin-bottom: 30px;
}

.main-analysis-stats {
  float: left;
  width: 200px;
  padding: 0 10px;
}

</style>