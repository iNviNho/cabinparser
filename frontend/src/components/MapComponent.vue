<template>
  <div class="map-container">
    <GoogleMap
        :api-key="getApiKey()"
        :center="{ lat: 48.669, lng: 19.699 }"
        :zoom="8"
        style="width: 100%; height: 500px"
    >
      <Marker v-for="(marker, index) in markers" :key="index" :options="{
        position: {
          lat: marker.cabin.gpsPositionLatitude,
          lng: marker.cabin.gpsPositionLongitude
        },
        icon: getIcon(marker.cabin.rating),
        title: marker.cabin.name
      }" @click="onMarkerClick(marker)"/>
    </GoogleMap>
  </div>
  <div id="marker-legend">
    <ul>
      <li>
        <img src="https://maps.gstatic.com/mapfiles/ms2/micons/green.png"/>
        <span> > 9.5 </span>
      </li>
      <li>
        <img src="https://maps.gstatic.com/mapfiles/ms2/micons/lightblue.png"/>
        <span> 9 - 9.5 </span>
      </li>
      <li>
        <img src="https://maps.gstatic.com/mapfiles/ms2/micons/yellow.png"/>
        <span> 8 - 8.9 </span>
      </li>
      <li>
        <img src="https://maps.gstatic.com/mapfiles/ms2/micons/red.png"/>
        <span v-text="' < 8 '"></span>
      </li>
    </ul>
  </div>
</template>

<script>

import client from "@/services/client";
import {GoogleMap, Marker} from 'vue3-google-map';
import {googleMapsConfig} from "@/config/config";

export default {
  name: 'MapComponent',
  components: {
    GoogleMap,
    Marker
  },
  data() {
    return {
      markers: [],
      selectedRegion: null,
      selectedDistrict: null,
      selectedLocality: null,
      selectedRating: null,
      selectedReview: null,
      selectedPrice: null
    }
  },
  methods: {
    getApiKey() {
      return googleMapsConfig.apiKey;
    },
    onMarkerClick(marker) {
      this.$emit('on-marker-click', marker.cabin);
    },
    getIcon(rating) {
      let icon;
      if (rating < 8) {
        icon = 'https://maps.gstatic.com/mapfiles/ms2/micons/red.png';
      } else if (rating < 9) {
        icon = 'https://maps.gstatic.com/mapfiles/ms2/micons/yellow.png';
      } else if (rating <= 9.5) {
        icon = 'https://maps.gstatic.com/mapfiles/ms2/micons/lightblue.png';
      } else if (rating > 9.5) {
        icon = 'https://maps.gstatic.com/mapfiles/ms2/micons/green.png';
      }

      return icon;
    },
    filtersChanged(
        region,
        district,
        locality,
        rating,
        review,
        price,
        occupancy,
        attributes
    ) {
      client.get(`/cabins?region=${region}&district=${district}&locality=${locality}&rating=${rating}&reviews=${review}&averagePricePerNight=${price}&occupancy=${occupancy}&attributes=${attributes}`)
          .then(response => {
            this.markers = response.data.map(cabin => {
              return {
                position: {
                  lat: cabin.gpsPositionLatitude,
                  lng: cabin.gpsPositionLongitude
                },
                cabin: cabin
              }
            });
          })
          .catch(error => {
            console.log(error);
          });
    }
  }
}
</script>

<style>

.map-container {
  width: 100%;
}

#marker-legend ul {
  padding: 0;
  text-align: end;
  margin-top: 0px;
}

#marker-legend ul li {
  list-style: none;
  display: inline-block;
  align-items: center;
  margin: 5px;
  border-right: 1px solid #2c3e50;
  padding: 2px 5px;
}

#marker-legend ul li img {
  height: 22px;
}

#marker-legend ul li span {
  vertical-align: super;
}

</style>
