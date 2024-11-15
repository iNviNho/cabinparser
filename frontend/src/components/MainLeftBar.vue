<template>
  <div>
    <div class="inline-block">
      <h3>Kraj</h3>
      <select v-model="selectedRegion" @change="onRegionChange">
        <option v-for="region in regions" :key="region" :value="region">{{ region }}</option>
      </select>
    </div>
    <div class="inline-block">
      <h3>Okres</h3>
      <select v-model="selectedDistrict" :disabled="selectedRegion == null"
              @change="onDistrictChange">
        <option v-for="district in districts" :key="district" :value="district">{{ district }}</option>
      </select>
    </div>
    <div class="inline-block">
      <h3>Lokalita</h3>
      <select v-model="selectedLocality" :disabled="selectedDistrict == null"
              @change="filtersChanged">
        <option v-for="locality in localities" :key="locality" :value="locality">{{ locality }}</option>
      </select>
    </div>
    <div class="inline-block">
      <h3>Rating nad</h3>
      <select v-model="selectedRating" @change="filtersChanged">
        <option v-for="rating in ratings" :key="rating" :value="rating">{{ rating }}</option>
      </select>
    </div>
    <div class="inline-block">
      <h3>Hodnotenia nad</h3>
      <select v-model="selectedReview" @change="filtersChanged">
        <option v-for="review in reviews" :key="review" :value="review">{{ review }}</option>
      </select>
    </div>
    <div class="inline-block">
      <h3>Cena za noc nad</h3>
      <select v-model="selectedPrice" @change="filtersChanged">
        <option v-for="price in prices" :key="price" :value="price">{{ price }}</option>
      </select>
    </div>
    <div class="inline-block">
      <h3>Obsadenost nad</h3>
      <select v-model="selectedOccupancy" @change="filtersChanged">
        <option v-for="occupancy in occupancies" :key="occupancy" :value="occupancy">{{ occupancy }}%</option>
      </select>
    </div>
  </div>
</template>

<script>

import client from "@/services/client";

export default {
  name: 'MainLeftBar',
  data() {
    return {
      regions: ["All"],
      districts: ["All"],
      localities: ["All"],
      ratings: ["All", 8, 8.5, 9, 9.5, 10],
      reviews: ["All", 5, 20, 50, 75, 100],
      prices: ["All", 50, 100, 150, 200, 300, 400, 500, 750, 1000],
      occupancies: ["All", 10, 20, 30, 40, 50, 60, 70, 80, 90],
      selectedRegion: null,
      selectedDistrict: null,
      selectedLocality: null,
      selectedRating: null,
      selectedReview: null,
      selectedPrice: null,
      selectedOccupancy: null
    }
  },
  methods: {
    onRegionChange() {
      // if region is not 'All', make another call to get districts
      if (this.selectedRegion !== 'All') {
        client.get(`/locations/districts?region=${this.selectedRegion}`)
            .then(response => {
              this.districts = response.data;
              // add 'All' option
              this.districts.unshift('All');
              this.selectedDistrict = null;
              this.selectedLocality = null;
              this.filtersChanged();
            })
            .catch(error => {
              console.log(error);
            });
      } else {
        this.districts = [];
        this.selectedDistrict = null;
        this.filtersChanged();
      }
    },
    onDistrictChange() {
      if (this.selectedDistrict !== 'All') {
        client.get(`/locations/localities?region=${this.selectedRegion}&district=${this.selectedDistrict}`)
            .then(response => {
              this.localities = response.data;
              // add 'All' option
              this.localities.unshift('All');
              this.selectedLocality = null;
              this.filtersChanged();
            })
            .catch(error => {
              console.log(error);
            });
      } else {
        3
        this.selectedLocality = null;
        this.filtersChanged();
      }
    },
    filtersChanged() {
      this.$emit('filters-changed', {
        region: this.selectedRegion,
        district: this.selectedDistrict,
        locality: this.selectedLocality,
        rating: this.selectedRating,
        review: this.selectedReview,
        price: this.selectedPrice,
        occupancy: this.selectedOccupancy
      });
    }
  },
  created() {
    client.get('/locations/regions')
        .then(response => {
          this.regions = response.data;
          // add 'All' option
          this.regions.unshift('All');
        })
        .catch(error => {
          console.log(error);
        });
  }
}

</script>

<style scoped>

.inline-block {
  display: block;
  margin: 10px;
  padding: 0 6px;
}

.inline-block h3 {
  margin: 0;
  margin-bottom: 10px;
  padding: 0;
  font-size: 16px;
  color: #ffffff;
  text-align: left;
}


.inline-block select {
  padding: 7px;
  width: 100%;
  font-size: 16px;
  /*border: 1px solid black;*/
  cursor: pointer;
  border-radius: 5px;
  border: 1px solid #ccc;
  background-color: #fff;
  color: #000;
  transition: border-color 0.2s;
  margin-bottom: 7px;
}
</style>