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
    <div class="white-section" style="margin-top: 30px;"></div>
    <div>
      <h5 class="subtitle"><input type="checkbox" v-model="showCabinsForRent" @change="filtersChanged"> Chaty na prenajom</h5>
    </div>
    <div v-if="showCabinsForRent">
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
    <div class="white-section" style="margin-top: 30px;"></div>
    <div>
      <h5 class="subtitle"><input type="checkbox" v-model="showPropertiesForSale" @change="filtersChanged"> Nehnutelnosti na predaj</h5>
      <div v-if="showPropertiesForSale">
        <div class="inline-block">
        <h3>Typ</h3>
        <multiselect v-model="selectedPropertyForSale"
                     :hideSelected="true" :options="propertyForSaleTypes"
                     :placeholder="'Vyber'"
                     :selectLabel="''" multiple @remove="onPropertyForSaleChange" @select="onPropertyForSaleChange"></multiselect>
      </div>
      </div>
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
      showCabinsForRent: true,
      showPropertiesForSale: true,
      selectedPropertyForSale: null,
      propertyForSaleTypes: [
        "CHATA_NA_PREDAJ",
        "DOM_NA_PREDAJ",
        "REKREACNY_POZEMOK",
        "STAVEBNY_POZEMOK",
      ],
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
    async onPropertyForSaleChange() {
      this.filtersChanged();
    },
    async onRegionChange(updateFilters = true) {
      return client.get(`/locations/districts?region=${this.selectedRegion}`)
          .then(response => {
            this.districts = response.data;
            this.selectedDistrict = null;
            this.selectedLocality = null;
            if (updateFilters) {
              this.filtersChanged();
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    async onDistrictChange(updateFilters = true) {
      return client.get(`/locations/localities?region=${this.selectedRegion}&district=${this.selectedDistrict}`)
          .then(response => {
            this.localities = response.data;
            this.selectedLocality = null;
            if (updateFilters) {
              this.filtersChanged();
            }
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
        numberOfBedrooms: this.selectedNumberOfBedrooms,
        showCabinsForRent: this.showCabinsForRent,
        showPropertiesForSale: this.showPropertiesForSale,
        propertyForSale: this.selectedPropertyForSale,
      });
    },
    async resolveDefaultFilters() {
      const urlParams = new URLSearchParams(window.location.search);
      this.selectedRegion = urlParams.getAll('region').toString() === '' ? null : urlParams.getAll('region');
      await this.onRegionChange(false);
      this.selectedDistrict = urlParams.getAll('district').toString() === '' ? null : urlParams.getAll('district');
      await this.onDistrictChange(false);
      this.selectedLocality = urlParams.getAll('locality').toString() === '' ? null : urlParams.getAll('locality');
      if (urlParams.get('rating') !== null) {
        this.selectedRating = urlParams.getAll('rating');
      }
      if (urlParams.get('review') !== null) {
        this.selectedReview = urlParams.getAll('review');
      }
      if (urlParams.get('price') !== null) {
        this.selectedPrice = urlParams.getAll('price');
      }
      if (urlParams.get('occupancy') !== null) {
        this.selectedOccupancy = urlParams.getAll('occupancy');
      }
      this.selectedAttributes = urlParams.getAll('attributes');
      this.star = urlParams.get('star') === 'true';
      if (urlParams.get('numberOfRegularBeds') !== null) {
        this.selectedNumberOfRegularBeds = urlParams.getAll('numberOfRegularBeds');
      }
      if (urlParams.get('numberOfBedrooms') !== null) {
        this.selectedNumberOfBedrooms = urlParams.getAll('numberOfBedrooms');
      }
      this.showCabinsForRent = urlParams.get('showCabinsForRent') === 'true' || urlParams.get('showCabinsForRent') === null;
      this.showPropertiesForSale = urlParams.get('showPropertiesForSale') === 'true' || urlParams.get('showPropertiesForSale') === null;
      this.selectedPropertyForSale = urlParams.getAll('propertyForSale').toString() === '' ? null : urlParams.getAll('propertyForSale');
      this.filtersChanged();
    }
  },
  created() {
    Promise.all([
      client.get('/locations/regions'),
      client.get('/cabin-attributes')
    ])
    .then(([regionsResponse, attributesResponse]) => {
      this.regions = regionsResponse.data;
      this.availableAttributes = attributesResponse.data.map(feature => {
        return feature.translation;
      });
      this.resolveDefaultFilters();
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
  padding: 0 15px;
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

.subtitle {
  text-align: left;
  padding-left: 15px;
}
</style>