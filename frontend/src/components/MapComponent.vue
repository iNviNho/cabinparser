<template>
  <div class="map-container">
    <GMapMap
        :center="{ lat: 48.669, lng: 19.699 }"
        :zoom="8"
        style="width: 100%; height: 500px"
    >
      <GMapMarker
          v-for="(marker, index) in markers"
          :key="index"
          :clickable="true"
          :icon='getIcon(marker.cabin.rating)'
          :position="marker.position"
          @click="onMarkerClick(marker)"
      />
    </GMapMap>
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
  <div v-if="openCabinSection" id="opened-cabin">
    <div class="left-section">
      <vue-carousel :data="selectedCabin.processedImages"></vue-carousel>
    </div>
    <div class="right-section">
      <h2 id="close" @click="openCabinSection = false">X</h2>
      <h3 style="margin-bottom: 0; margin-top: 0px;">
        <a :href="'https://megaubytovanie.sk/' + selectedCabin.urlFragment" style="color: black;"
           target="_blank">{{ selectedCabin.name }}</a>
      </h3>
      <p style="margin-top: 0;">
        <small>
          {{ selectedCabin.region }} > {{ selectedCabin.district }} > {{ selectedCabin.locality }}
        </small>
      </p>
      <p><strong>Rating:</strong> {{ selectedCabin.rating }} <strong>Hodnotenia:</strong> {{
          selectedCabin.reviewsCount
        }} <strong>Cena za noc:</strong> {{ selectedCabin.avgPricePerNight }}€
        <strong>Obsadenost:</strong> {{ (selectedCabin.occupancy * 100).toFixed(2) }}%</p>
      <p>{{ selectedCabin.description }}</p>
      <p>
        Pocet izieb: {{ selectedCabin.bedroomsCount }}<br>
        Pocet posteli: {{ selectedCabin.regularSleepingBeds }} |
        Pocet extra posteli: {{ selectedCabin.extraSleepingBeds }}
      </p>
    </div>
    <br style="clear: both;">
  </div>
</template>

<script>

import client from "@/services/client";

export default {
  name: 'MapComponent',
  data() {
    return {
      markers: [],
      selectedRegion: null,
      selectedDistrict: null,
      selectedLocality: null,
      openCabinSection: false,
      selectedCabin: null,
      selectedRating: null,
      selectedReview: null,
      selectedPrice: null
    }
  },
  methods: {
    onMarkerClick(marker) {
      this.openCabinSection = true;
      this.selectedCabin = marker.cabin;
      this.selectedCabin.processedImages = this.selectedCabin.images.map(image => {
        return "<div class='slide' style='background-image: url(" + image + ")'></div>";
      });
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
        occupancy
    ) {
      client.get(`/cabins?region=${region}&district=${district}&locality=${locality}&rating=${rating}&reviews=${review}&averagePricePerNight=${price}&occupancy=${occupancy}`)
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

#opened-cabin {
  margin-bottom: 20px;
  padding: 0 20px;
  border-bottom: 1px solid darkcyan;
}

#opened-cabin .left-section {
  float: left;
  width: 35%;
  overflow: hidden;
}

#opened-cabin .right-section {
  position: relative;
  float: right;
  width: calc(65% - 30px);
  text-align: left;
  padding-left: 15px;
  padding-right: 15px;
}

#opened-cabin .right-section h2 {
  position: absolute;
  top: 0px;
  right: 0px;
  cursor: pointer;
  text-align: right;
  margin-top: 0px;
}

#opened-cabin .left-section .slide {
  width: 100%;
  background-size: cover;
  background-position: center;
  height: 300px;
}

</style>
