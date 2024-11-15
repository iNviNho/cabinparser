<template>
  <div v-if="cabins.length > 0">
    <div>
      <ul>
        <li :class="activeComponent === 'topCabinsByRating' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByRating')">Top 25
          chat podla ratingu
        </li>
        <li :class="activeComponent === 'topCabinsByReviews' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByReviews')">Top 25
          chat podla poctu hodnoteni
        </li>
        <li :class="activeComponent === 'topCabinsByPriceAsc' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByPriceAsc')">Top 25 chat podla ceny za noc vzostupne
        </li>
        <li :class="activeComponent === 'topCabinsByPriceDesc' ? 'active' : ''"
            @click="changeActiveComponent('topCabinsByPriceDesc')">Top 25 chat podla ceny za noc zostupne
        </li>
        <br style="clear: both;">
      </ul>
    </div>
    <div class="main-analysis-content">
      <TableView v-if="topCabinsByRating.length > 0 && activeComponent === 'topCabinsByRating'"
                 :data="topCabinsByRating"></TableView>
      <TableView v-if="topCabinsByReviews.length > 0 && activeComponent === 'topCabinsByReviews'"
                 :data="topCabinsByReviews"></TableView>
      <TableView v-if="topCabinsByPriceAsc.length > 0 && activeComponent === 'topCabinsByPriceAsc'"
                 :data="topCabinsByPriceAsc"></TableView>
      <TableView v-if="topCabinsByPriceDesc.length > 0 && activeComponent === 'topCabinsByPriceDesc'"
                 :data="topCabinsByPriceDesc"></TableView>
    </div>
  </div>
</template>

<script>

import client from "@/services/client";
import TableView from "@/components/TableView.vue";

export default {
  name: 'MainAnalysis',
  components: {TableView},
  data() {
    return {
      activeComponent: 'topCabinsByRating',
      cabins: [],
      topCabinsByRating: [],
      topCabinsByReviews: [],
      topCabinsByPriceAsc: [],
      topCabinsByPriceDesc: [],
    }
  },
  methods: {
    changeActiveComponent(name) {
      this.activeComponent = name;
    },
    updateTopCabins(cabins) {
      this.cabins = cabins;
      this.topCabinsByRating = cabins.sort((a, b) => b.rating - a.rating).slice(0, 25);
      this.topCabinsByReviews = cabins.sort((a, b) => b.reviewsCount - a.reviewsCount).slice(0, 25);
      this.topCabinsByPriceAsc = cabins.sort((a, b) => a.avgPricePerNight - b.avgPricePerNight).slice(0, 25);
      this.topCabinsByPriceDesc = cabins.sort((a, b) => b.avgPricePerNight - a.avgPricePerNight).slice(0, 25);
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
  display: inline-block;
  cursor: pointer;
  border: 1px solid black;
  padding: 7px;
  border-left: 0;
}

ul li:first-child {
  border-left: 1px solid black;
}

ul li.active {
  background-color: darkcyan;
  color: white;
}

.main-analysis-content {
  margin-top: 30px;
}

</style>