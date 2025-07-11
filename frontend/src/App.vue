<template>
  <div id="left-bar">
    <h2>
      <a href="/" style="color: #FFF; text-decoration: none;">Realitkač</a>
    </h2>
    <div class="white-section"></div>
    <MainLeftBar @filters-changed="filtersChanged"></MainLeftBar>
  </div>
  <div id="right-rest-of-the-screen">
    <MapComponent ref="map" @on-marker-click="showCabin"></MapComponent>
    <CabinView ref="cabinView"></CabinView>
    <CabinForSaleView ref="cabinForSaleView"></CabinForSaleView>
    <div>
      <MainAnalysis ref="mainAnalysis" @on-cabin-click="showCabin" @on-property-click="showProperty"></MainAnalysis>
    </div>
  </div>
</template>

<script>
import MapComponent from './components/MapComponent.vue'
import MainLeftBar from './components/MainLeftBar.vue'
import MainAnalysis from './components/MainAnalysis.vue'
import CabinView from './components/CabinView.vue'
import CabinForSaleView from "./components/CabinForSaleView.vue";

export default {
  name: 'App',
  components: {
    MapComponent,
    MainLeftBar,
    MainAnalysis,
    CabinView,
    CabinForSaleView
  },
  methods: {
    showCabin(marker) {
      if (marker.cabin != null) {
        this.$refs.cabinView.showCabin(marker.cabin);
      } else {
        this.$refs.cabinForSaleView.showCabinForSale(marker.cabinForSale);
      }
    },
    showProperty(property) {
      this.$refs.cabinForSaleView.showCabinForSale(property);
    },
    filtersChanged(filters) {
      this.$refs.map.filtersChanged(
          filters.region,
          filters.district,
          filters.locality,
          filters.rating,
          filters.review,
          filters.price,
          filters.occupancy,
          filters.attributes,
          filters.star,
          filters.numberOfRegularBeds,
          filters.numberOfBedrooms,
          filters.showCabinsForRent,
          filters.showPropertiesForSale,
          filters.propertyForSale
      );
      this.$refs.mainAnalysis.filtersChanged(
          filters.region,
          filters.district,
          filters.locality,
          filters.rating,
          filters.review,
          filters.price,
          filters.occupancy,
          filters.attributes,
          filters.star,
          filters.numberOfRegularBeds,
          filters.numberOfBedrooms,
          filters.showCabinsForRent,
          filters.showPropertiesForSale,
          filters.propertyForSale
      );
      this.$router.push({query: filters});
    },
    createFiltersArray(filters) {
      let filtersArray = [];
      for (let key in filters) {
        if (filters[key] !== null && filters[key] !== undefined) {
          if (Array.isArray(filters[key])) {
            filters[key].forEach(value => {
              filtersArray.push(`${key}=${value}`);
            });
          } else {
            filtersArray.push(`${key}=${filters[key]}`);
          }
        }
      }
      return {
        "region": filters.region,
      };
    }
  }
}
</script>

<style>

#left-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 250px;
  height: 100vh;
  overflow: scroll;
  background-color: darkcyan;
  color: #fff;
}

#right-rest-of-the-screen {
  margin-left: 250px;
}

.white-section {
  height: 2px;
  margin: 5px 17px;
  background-color: white;
}

#app {

}

body {
  padding: 0;
  margin: 0;
  font-family: Avenir, Helvetica, Arial, sans-serif!important;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
