<template>
  <div class="map-container">
    <GoogleMap
        :api-key="getApiKey()"
        :center="{ lat: 48.669, lng: 19.699 }"
        :zoom="8"
        :style="getStyle()"
    >
      <Marker v-for="(marker, index) in markers" :key="index" :options="{
        position: marker.position,
        icon: getIcon(marker),
        title: getTitle(marker),
      }" @click="onMarkerClick(marker)"/>
    </GoogleMap>
    <div id="scroller"
      @mousedown="startDrag"
      @mousemove="onDrag"
      @mouseup="stopDrag"
    ><div class="box"></div></div>
  </div>
  <div id="marker-legend">
    <ul>
      <li>
        <img src="http://localhost:8080/lightgreen.png"/>
        <span> Stavebny pozemok </span>
      </li>
      <li>
        <img src="http://localhost:8080/darkgreen.png"/>
        <span> Rekreacny pozemok </span>
      </li>
      <li>
        <img src="http://localhost:8080/blue.png"/>
        <span> Dom na predaj </span>
      </li>
      <li>
        <img src="http://localhost:8080/brown.png"/>
        <span> Chata na predaj </span>
      </li>
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
      selectedPrice: null,
      height: 700,
      isDragging: false,
      startY: 0,
      currentY: 0,
    }
  },
  methods: {
    startDrag(event) {
      this.isDragging = true;
      this.startY = event.clientY;
    },
    onDrag(event) {
      if (this.isDragging) {
        let diff = event.clientY - this.startY;
        this.startY = event.clientY;
        this.height = this.height + diff;
      }
    },
    stopDrag() {
      this.isDragging = false;
    },
    getApiKey() {
      return googleMapsConfig.apiKey;
    },
    onMarkerClick(marker) {
      this.$emit('on-marker-click', marker);
    },
    getTitle(marker) {
      return marker.cabin != null ? marker.cabin.name : marker.cabinForSale.name;
    },
    getStyle() {
      return 'width: 100%; height: ' + this.height+ 'px;';
    },
    getIcon(marker) {
      if (marker.cabin != null) {
        const rating = marker.cabin.rating;
        const isStar = marker.cabin.star;

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

        if (isStar) {
          icon = '/placeholder.png';
        }

        return icon;
      } else {
        if (marker.cabinForSale.category == 'STAVEBNY_POZEMOK') {
          return 'http://localhost:8080/lightgreen.png';
        }
        if (marker.cabinForSale.category == 'REKREACNY_POZEMOK') {
          return 'http://localhost:8080/darkgreen.png';
        }
        if (marker.cabinForSale.category == 'CHATA_NA_PREDAJ') {
          return 'http://localhost:8080/brown.png';
        }
        if (marker.cabinForSale.category == 'DOM_NA_PREDAJ') {
          return 'http://localhost:8080/blue.png';
        }
        return 'http://localhost:8080/cabin.png';
      }

    },
    async filtersChanged(
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
        propertyForSale
    ) {
      const [cabinsResponse, cabinsForSaleResponse] = await Promise.all(
          [
            showCabinsForRent ? getCabins(
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
                numberOfBedrooms
            ) : {data: []},
            showPropertiesForSale ? getPropertiesForSale(
                region,
                district,
                locality,
                propertyForSale
            ) : {data: []}
          ]
      );
      this.markers = [];
      cabinsResponse.data.forEach(cabin => {
        this.markers.push({
          position: {
            lat: cabin.gpsPositionLatitude,
            lng: cabin.gpsPositionLongitude
          },
          cabin: cabin,
          cabinForSale: null,
        });
      });
      cabinsForSaleResponse.data.forEach(cabinForSale => {
        this.markers.push({
          position: {
            lat: cabinForSale.gpsPositionLatitude,
            lng: cabinForSale.gpsPositionLongitude
          },
          cabin: null,
          cabinForSale: cabinForSale,
        });
      });
    }
  }
}

function getCabins(
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
    numberOfBedrooms
) {
  return client.get(`/cabins?region=${region}&district=${district}&locality=${locality}&rating=${rating}&reviews=${review}&averagePricePerNight=${price}&occupancy=${occupancy}&attributes=${attributes}&star=${star}&numberOfRegularBeds=${numberOfRegularBeds}&numberOfBedrooms=${numberOfBedrooms}`)
    .catch(error => {
      console.log(error);
    });
}

function getPropertiesForSale(
    region,
    district,
    locality,
    propertyForSale
) {
  return client.get(`/properties-for-sale?region=${region}&district=${district}&locality=${locality}&propertyForSale=${propertyForSale}`)
    .catch(error => {
      console.log(error);
    });
}
</script>

<style>

.map-container {
  position: relative;
  z-index: 2;
  width: 100%;
}

#marker-legend {
  margin-top: -15px;
  z-index: 1;
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

#scroller {
  height: 20px;
  cursor: ns-resize;
  z-index: 2;

  .box {
    width: 100%;
    height: 2px;
    background-color: #000;
  }
}


</style>
