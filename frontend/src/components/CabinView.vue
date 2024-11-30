<template>
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
      <ul class="attributes">
        <li v-for="attribute in selectedCabin.attributes" :key="attribute" v-text="attribute"></li>
      </ul>
    </div>
    <br style="clear: both;">
  </div>
</template>

<script>

export default {
  name: 'CabinView',
  data() {
    return {
      openCabinSection: false,
      selectedCabin: null
    }
  },
  methods: {
    showCabin(cabin) {
      this.openCabinSection = true;
      this.selectedCabin = cabin;
      this.selectedCabin.processedImages = this.selectedCabin.images.map(image => {
        return "<div class='slide' style='background-image: url(" + image + ")'></div>";
      });
    }
  }
}
</script>

<style>

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

.slide {
  width: 100%;
  background-size: cover;
  background-position: center;
  height: 300px;
}

.attributes {
  list-style: none;
  padding: 0;
}

.attributes li {
  display: inline-block;
  margin-right: 5px;
  margin-top: 5px;
  background-color: darkcyan;
  color: white;
  padding: 5px;
  border-radius: 5px;
}

</style>