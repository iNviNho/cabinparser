<template>
  <div id="left-bar">
    <h2>Chatkovač</h2>
    <div class="white-section"></div>
    <MainLeftBar @filters-changed="filtersChanged"></MainLeftBar>
  </div>
  <div id="right-rest-of-the-screen">
    <MapComponent ref="map" @on-marker-click="showCabin"></MapComponent>
    <CabinView ref="cabinView"></CabinView>
    <div>
      <MainAnalysis ref="mainAnalysis" @on-cabin-click="showCabin"></MainAnalysis>
    </div>
  </div>
</template>

<script>
import MapComponent from './components/MapComponent.vue'
import MainLeftBar from './components/MainLeftBar.vue'
import MainAnalysis from './components/MainAnalysis.vue'
import CabinView from './components/CabinView.vue'

export default {
  name: 'App',
  components: {
    MapComponent,
    MainLeftBar,
    MainAnalysis,
    CabinView
  },
  methods: {
    showCabin(cabin) {
      this.$refs.cabinView.showCabin(cabin);
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
      )
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
