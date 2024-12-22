<template>
  <div>
    <div class="inline-block">
      <h3>Kraj</h3>
      <multiselect v-model="selectedRegion" :hideSelected="true" :options="regions" :placeholder="'Vyber'"
                   :selectLabel="''" multiple @remove="onRegionChange" @select="onRegionChange"></multiselect>
    </div>
    <div class="inline-block">
      <h3>Okres</h3>
      <multiselect v-model="selectedDistrict" :disabled="selectedRegion == null || selectedRegion.length < 1"
                   :hideSelected="true" :options="districts"
                   :placeholder="'Vyber'"
                   :selectLabel="''" multiple @remove="onDistrictChange" @select="onDistrictChange"></multiselect>
    </div>
    <div class="inline-block">
      <h3>Lokalita</h3>
      <multiselect v-model="selectedLocality" :disabled="selectedDistrict == null || selectedDistrict.length < 1"
                   :hideSelected="true" :options="localities"
                   :placeholder="'Vyber'"
                   :selectLabel="''" multiple @remove="filtersChanged" @select="filtersChanged"></multiselect>
    </div>
    <div class="inline-block">
      <h3 style="margin-bottom: 50px;">Rating</h3>
      <Slider v-model="selectedRating" :format="'decimals'" :max="10" :min="0" :step="0.5" @change="ratingChanged"/>
    </div>
    <div class="inline-block">
      <h3 style="margin-top: 20px; margin-bottom: 50px;">Hodnotenia</h3>
      <Slider v-model="selectedReview" :format="'decimals'" :max="200" :min="0" :step="5" @change="reviewChanged"/>
    </div>
    <div class="inline-block">
      <h3 style="margin-top: 20px; margin-bottom: 50px;">Cena za noc</h3>
      <Slider v-model="selectedPrice" :format="formatPrice" :max="2500" :min="0" :step="50" @change="priceChanged"/>
    </div>
    <div class="inline-block">
      <h3 style="margin-top: 20px; margin-bottom: 50px;">Obsadenost</h3>
      <Slider v-model="selectedOccupancy" :format="formatPercentage" :max="100" :min="0"
              :step="5" @change="occupancyChanged"/>
    </div>
    <div class="inline-block" style="margin-bottom: 20px;">
      <h3 style="margin-top: 20px; margin-bottom: 50px;">Pocet posteli</h3>
      <Slider v-model="selectedNumberOfRegularBeds" :max="50" :min="0"
              :step="1" @change="numberOfRegularBedsChanged"/>
    </div>
    <div class="inline-block" style="margin-bottom: 20px;">
      <h3 style="margin-top: 20px; margin-bottom: 50px;">Pocet izieb</h3>
      <Slider v-model="selectedNumberOfBedrooms" :max="15" :min="0"
              :step="1" @change="numberOfBedroomsChanged"/>
    </div>
    <div class="inline-block" style="margin-bottom: 20px;">
      <h3>Vybavenost</h3>
      <multiselect v-model="selectedAttributes"
                   :options="availableAttributes"
                   :placeholder="'Vyber'"
                   :selectLabel="''" multiple @remove="filtersChanged" @select="filtersChanged"></multiselect>
    </div>
    <div class="inline-block" style="margin-bottom: 40px;">
      <h3><input type="checkbox" v-model="star" @change="filtersChanged"> Oblubene</h3>
    </div>
  </div>
</template>

<script>

import client from "@/services/client";
import Multiselect from 'vue-multiselect'
import Slider from '@vueform/slider'

export default {
  name: 'MainLeftBar',
  components: {
    Multiselect,
    Slider
  },
  data() {
    return {
      regions: [],
      districts: [],
      localities: [],
      ratings: ["All", 8, 8.5, 9, 9.5, 10],
      reviews: ["All", 5, 20, 50, 75, 100],
      prices: ["All", 50, 100, 150, 200, 300, 400, 500, 750, 1000],
      occupancies: ["All", 10, 20, 30, 40, 50, 60, 70, 80, 90],
      selectedRegion: null,
      selectedDistrict: null,
      selectedLocality: null,
      selectedRating: [0, 10],
      selectedReview: [0, 200],
      selectedPrice: [0, 2500],
      selectedOccupancy: [0, 100],
      selectedAttributes: [],
      availableAttributes: [],
      star: false,
      selectedNumberOfRegularBeds: [0, 50],
      selectedNumberOfBedrooms: [0, 15],
    }
  },
  methods: {
    numberOfRegularBedsChanged(selectedNumberOfRegularBeds) {
      this.selectedNumberOfRegularBeds = selectedNumberOfRegularBeds;
      this.filtersChanged();
    },
    numberOfBedroomsChanged(selectedNumberOfBedrooms) {
      this.selectedNumberOfBedrooms = selectedNumberOfBedrooms;
      this.filtersChanged();
    },
    ratingChanged(selectedRating) {
      this.selectedRating = selectedRating;
      this.filtersChanged();
    },
    reviewChanged(selectedReview) {
      this.selectedReview = selectedReview;
      this.filtersChanged();
    },
    priceChanged(selectedPrice) {
      this.selectedPrice = selectedPrice;
      this.filtersChanged();
    },
    occupancyChanged(selectedOccupancy) {
      this.selectedOccupancy = selectedOccupancy;
      this.filtersChanged();
    },
    formatPercentage(value) {
      return value + '%';
    },
    formatPrice(value) {
      return value.toFixed(0) + '€';
    },
    onRegionChange() {
      client.get(`/locations/districts?region=${this.selectedRegion}`)
          .then(response => {
            this.districts = response.data;
            this.selectedDistrict = null;
            this.selectedLocality = null;
            this.filtersChanged();
          })
          .catch(error => {
            console.log(error);
          });
    },
    onDistrictChange() {
      client.get(`/locations/localities?region=${this.selectedRegion}&district=${this.selectedDistrict}`)
          .then(response => {
            this.localities = response.data;
            this.selectedLocality = null;
            this.filtersChanged();
          })
          .catch(error => {
            console.log(error);
          });
    },
    filtersChanged() {
      this.$emit('filters-changed', {
        region: this.selectedRegion,
        district: this.selectedDistrict,
        locality: this.selectedLocality,
        rating: this.selectedRating,
        review: this.selectedReview,
        price: this.selectedPrice,
        occupancy: this.selectedOccupancy,
        attributes: this.selectedAttributes,
        star: this.star,
        numberOfRegularBeds: this.selectedNumberOfRegularBeds,
        numberOfBedrooms: this.selectedNumberOfBedrooms
      });
    }
  },
  created() {
    client.get('/locations/regions')
        .then(response => {
          this.regions = response.data;
          this.filtersChanged();
        })
        .catch(error => {
          console.log(error);
        });
    client.get('/cabin-attributes')
        .then(response => {
          this.availableAttributes = response.data.map(feature => {
            return feature.translation;
          });
          this.filtersChanged();
        })
        .catch(error => {
          console.log(error);
        });
  }
}

</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<style>
@import "~@vueform/slider/themes/default.css";
</style>
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